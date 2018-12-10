package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentAliPayContract;
import com.holderzone.intelligencepos.mvp.model.bean.ApiNote;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.holderzone.intelligencepos.mvp.model.bean.XmlData.Builder;

/**
 * 支付宝支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentAliPayPresenter extends BasePresenter<PaymentAliPayContract.View> implements PaymentAliPayContract.Presenter {

    /**
     * 轮询disposable
     */
    private Disposable mIntervalDisposable;

    public PaymentAliPayPresenter(PaymentAliPayContract.View view) {
        super(view);
    }

    @Override
    public void submitAliPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String transactionNumber) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderGUID(saleOrderGUID);
                    salesOrderPaymentE.setPaymentItemCode(paymentItemCode);
                    salesOrderPaymentE.setPayableAmount(payableAmount);
                    salesOrderPaymentE.setTransactionNumber(transactionNumber);
                    salesOrderPaymentE.setCreateStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.AddAlipay)
                            .setRequestBody(salesOrderPaymentE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return requestSalesOrder(saleOrderGUID);
                    } else {
                        if (ApiNoteHelper.checkNeedIntervalWhenBusinessFailed(xmlData)) {
                            SalesOrderPaymentE salesOrderPaymentE1 = xmlData.getSalesOrderPaymentE();
                            if (salesOrderPaymentE1 != null) {
                                return checkPayStatus(saleOrderGUID, salesOrderPaymentE1);
                            }
                        }
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> mView.showIntervalDialog())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(new Observer<XmlData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mIntervalDisposable = d;
                    }

                    @Override
                    public void onNext(XmlData xmlData) {
                        // 判断待付款状态
                        ApiNote apiNote = xmlData.getApiNote();
                        if (apiNote == null) {
                            return;//继续轮询
                        }
                        // 隐藏对话框
                        mView.hideIntervalDialog();
                        // 结果处理
                        if (ApiNoteHelper.checkBusiness(xmlData)) {
                            SalesOrderE mainSalesOrderE = null;
                            List<SalesOrderE> arrayOfSalesOrderE = xmlData.getArrayOfSalesOrderE();
                            if (arrayOfSalesOrderE != null) {
                                if (arrayOfSalesOrderE.size() == 1) {
                                    mainSalesOrderE = xmlData.getArrayOfSalesOrderE().get(0);
                                } else {
                                    for (SalesOrderE salesOrderE : arrayOfSalesOrderE) {
                                        if (salesOrderE.getUpperState() == 1) {
                                            mainSalesOrderE = salesOrderE;
                                            break;
                                        }
                                    }
                                }
                            }
                            if (mainSalesOrderE != null) {
                                mView.onAliPaySucceed(mainSalesOrderE);
                            } else {
                                mView.onAliPayFailed("数据非法，SalesOrderE不得为null。");
                            }
                        } else {
                            if (ApiNoteHelper.checkAuthorizationWhenBusinessFailed(xmlData)) {
                                mView.showAuthorizationDialog(ApiNoteHelper.obtainAuthorizationMsg(xmlData));
                            } else {
                                mView.onAliPayFailed(ApiNoteHelper.obtainErrorMsg(xmlData));
                            }
                        }
                        // 停止轮询
                        if (mIntervalDisposable != null) {
                            mIntervalDisposable.dispose();
                            mIntervalDisposable = null;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideIntervalDialog();
                        mView.onAliPayFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void disposeAliPay() {
        if (mIntervalDisposable != null) {
            mIntervalDisposable.dispose();
            mIntervalDisposable = null;
        }
    }

    /**
     * 检查轮询支付状态
     *
     * @param salesOrderGUID
     * @param salesOrderPaymentE
     * @return
     */
    private Observable<XmlData> checkPayStatus(String salesOrderGUID, SalesOrderPaymentE salesOrderPaymentE) {
        return Builder()
                .setRequestMethod(RequestMethod.SalesOrderPaymentB.CheckPayStatus)
                .setRequestBody(salesOrderPaymentE).buildRESTful()
                .flatMap(xmlData -> Observable.interval(0, 5, TimeUnit.SECONDS).map(aLong -> xmlData))
                .flatMap(mRepository::getXmlData)
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return requestSalesOrder(salesOrderGUID);// 完成
                    } else {
                        if (ApiNoteHelper.checkIntervalStatusWhenBusinessFailed(xmlData)) {
                            return Observable.just(new XmlData());
                        }
                        return Observable.error(new ApiException(xmlData));
                    }
                });
    }

    /**
     * 请求新的订单数据
     *
     * @param saleOrderGUID
     * @return
     */
    private Observable<XmlData> requestSalesOrder(String saleOrderGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(saleOrderGUID);
        salesOrderE.setReturnSalesOrderPayment(1);
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }
}
