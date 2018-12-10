package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentManageContract;
import com.holderzone.intelligencepos.mvp.model.bean.ApiNote;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 付款项管理
 * Created by tcw on 2017/6/1.
 */

public class PaymentManagePresenter extends BasePresenter<PaymentManageContract.View> implements PaymentManageContract.Presenter {
    /**
     * 轮询disposable
     */
    private Disposable mIntervalDisposable;

    public PaymentManagePresenter(PaymentManageContract.View view) {
        super(view);
    }

    @Override
    public void requestSalesOrder(String salesOrderGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        salesOrderE.setReturnSalesOrderPayment(1);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE salesOrderE = null;
                        List<SalesOrderE> arrayOfSalesOrderE = xmlData.getArrayOfSalesOrderE();
                        if (arrayOfSalesOrderE != null && arrayOfSalesOrderE.size() > 0) {
                            if (arrayOfSalesOrderE.size() == 1) {
                                salesOrderE = xmlData.getArrayOfSalesOrderE().get(0);
                            } else {
                                for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
                                    if (bean.getUpperState() == 1) {
                                        salesOrderE = bean;
                                        break;
                                    }
                                }
                            }
                            mView.onSalesOrderObtainSucceed(salesOrderE);
                        }
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onSalesOrderObtainFailed(e.getMessage());
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkErrorLayout();
                        }
                    }
                });
    }

    //    @Override
//    public void submitRefund(String saleOrderGUID, String salesOrderPaymentGuid, String type, String traceNumber) {
//        mRepository.getUsers()
//                .flatMap(users -> {
//                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
//                    salesOrderPaymentE.setSalesOrderPaymentGUID(salesOrderPaymentGuid);
//                    salesOrderPaymentE.setRefundStaffGUID(users.getUsersGUID());
//                    if (type.equals("SYB01")) {
//                        salesOrderPaymentE.setTransactionNumber(traceNumber);
//                    }
//                    return XmlData.Builder()
//                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.Refund)
//                            .setRequestBody(salesOrderPaymentE).buildRESTful()
//                            .flatMap(mRepository::getXmlData);
//                })
//                .flatMap(xmlData -> {
//                    if (ApiNoteHelper.checkBusiness(xmlData)) {
//                        SalesOrderE salesOrderE = new SalesOrderE();
//                        salesOrderE.setSalesOrderGUID(saleOrderGUID);
//                        salesOrderE.setReturnSalesOrderPayment(1);
//                        return XmlData.Builder()
//                                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
//                                .setRequestBody(salesOrderE).buildRESTful()
//                                .flatMap(mRepository::getXmlData);
//                    } else {
//                        return Observable.error(new ApiException(xmlData.getApiNote()));
//                    }
//                })
//                .compose(RxTransformer.applyTransformer(mView))
//                .subscribe(new BaseObserver<XmlData>(mView) {
//                    @Override
//                    protected void next(XmlData xmlData) {
//                        SalesOrderE salesOrder = null;
//                        if (xmlData.getArrayOfSalesOrderE().size() == 1) {
//                            salesOrder = xmlData.getArrayOfSalesOrderE().get(0);
//                        } else {
//                            for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
//                                if (bean.getUpperState() == 1) {
//                                    salesOrder = bean;
//                                    break;
//                                }
//                            }
//                        }
//                        mView.onRefundSucceed(salesOrder);
//                    }
//
//                    @Override
//                    protected void error(Throwable e) {
//                        if (type.equals(PaymentActivity.PaymentItemCode_SYB01)) {
//                            mView.onRefundFailed(e.getMessage());
//                        } else {
//                            mView.onRefundFailed(e.getMessage());
//                        }
//                    }
//                });
//    }
    @Override
    public void submitRefund(String saleOrderGUID, String salesOrderPaymentGuid, String type, String traceNumber) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderPaymentGUID(salesOrderPaymentGuid);
                    salesOrderPaymentE.setRefundStaffGUID(users.getUsersGUID());
                    if ("SYB01".equals(type)) {
                        salesOrderPaymentE.setTransactionNumber(traceNumber);
                    }
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.Refund)
                            .setRequestBody(salesOrderPaymentE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return requestNewSalesOrder(saleOrderGUID);
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
                        //判断待付款状态
                        ApiNote apiNote = xmlData.getApiNote();
                        if (apiNote == null) {
                            return;//继续轮询
                        }
                        //隐藏对话框
                        mView.hideIntervalDialog();
                        //结果处理
                        if (ApiNoteHelper.checkBusiness(xmlData)) {
                            SalesOrderE salesOrder = null;
                            if (xmlData.getArrayOfSalesOrderE().size() == 1) {
                                salesOrder = xmlData.getArrayOfSalesOrderE().get(0);
                            } else {
                                for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
                                    if (bean.getUpperState() == 1) {
                                        salesOrder = bean;
                                        break;
                                    }
                                }
                            }
                            mView.onRefundSucceed(salesOrder);
                        } else {
                            if (ApiNoteHelper.checkAuthorizationWhenBusinessFailed(xmlData)) {
                                mView.showAuthorizationDialog(ApiNoteHelper.obtainAuthorizationMsg(xmlData));
                            } else {
                                mView.onRefundFailed(ApiNoteHelper.obtainErrorMsg(xmlData));
                            }
                        }
                        //停止轮询
                        if (mIntervalDisposable != null) {
                            mIntervalDisposable.dispose();
                            mIntervalDisposable = null;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        mView.hideIntervalDialog();
                        mView.onRefundFailed(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void disposeWxPay() {
        if (mIntervalDisposable != null) {
            mIntervalDisposable.dispose();
            mIntervalDisposable = null;
            mView.hideIntervalDialog();
        }
    }

    /**
     * 请求新的订单数据
     *
     * @param saleOrderGUID
     * @return
     */
    private Observable<XmlData> requestNewSalesOrder(String saleOrderGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(saleOrderGUID);
        salesOrderE.setReturnSalesOrderPayment(1);
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    /**
     * 检查轮询支付状态
     *
     * @param salesOrderGUID
     * @param salesOrderPaymentE
     * @return
     */
    private Observable<XmlData> checkPayStatus(String salesOrderGUID, SalesOrderPaymentE salesOrderPaymentE) {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderPaymentB.CheckRefundStatus)
                .setRequestBody(salesOrderPaymentE).buildRESTful()
                .flatMap(xmlData -> Observable.interval(0, 5, TimeUnit.SECONDS).map(aLong -> xmlData))
                .flatMap(mRepository::getXmlData)
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return requestNewSalesOrder(salesOrderGUID);
                    } else {
                        if (ApiNoteHelper.checkIntervalStatusWhenBusinessFailed(xmlData)) {
                            return Observable.just(new XmlData());
                        }
                        return Observable.error(new ApiException(xmlData));
                    }
                });
    }
}
