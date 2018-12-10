package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentSunmiL3Contract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017-10-30.
 * L3支付
 */

public class PaymentSunmiL3Presenter extends BasePresenter<PaymentSunmiL3Contract.View> implements PaymentSunmiL3Contract.Presenter {
    public PaymentSunmiL3Presenter(PaymentSunmiL3Contract.View view) {
        super(view);
    }

    @Override
    public void sunmiL3Pay(String saleOrderGUID, String paymentItemCode, double payableAmount, double actuallyAmount, String reTraceNo, String refNumber) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderGUID(saleOrderGUID);
                    salesOrderPaymentE.setPaymentItemCode(paymentItemCode);
                    salesOrderPaymentE.setCreateStaffGUID(users.getUsersGUID());
                    salesOrderPaymentE.setPayableAmount(payableAmount);// 应收
                    salesOrderPaymentE.setActuallyAmount(actuallyAmount);// 实收
                    salesOrderPaymentE.setTransactionNumber(reTraceNo);//原始流水号
                    salesOrderPaymentE.setRemark(refNumber);//参考号
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.AddOrdinary)
                            .setRequestBody(salesOrderPaymentE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        SalesOrderE salesOrderE = new SalesOrderE();
                        salesOrderE.setSalesOrderGUID(saleOrderGUID);
                        salesOrderE.setReturnSalesOrderPayment(1);
                        return XmlData.Builder()
                                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                                .setRequestBody(salesOrderE).buildRESTful()
                                .flatMap(mRepository::getXmlData);
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .compose(RxTransformer.applyTransformer(mView, true, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        // hideLoading不能放在doAfterTerminate中，代码回调了PaymentActivity的adapter#notifyAll，
                        // 会销毁CashFragment，dialog也会被销毁(相当于terminal中代码未执行)，但是重建的时候又显示出来了，于是dialog就一直显示着
                        mView.hideLoading();
                        // 支付成功
                        SalesOrderE salesOrderE = null;
                        if (xmlData.getArrayOfSalesOrderE().size() == 1) {
                            salesOrderE = xmlData.getArrayOfSalesOrderE().get(0);
                        } else {
                            for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
                                if (bean.getUpperState() == 1) {
                                    salesOrderE = bean;
                                    break;
                                }
                            }
                        }
                        mView.onSunmiL3Succeed(salesOrderE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // hideLoading不能放在doAfterTerminate中，代码回调了PaymentActivity的adapter#notifyAll，
                        // 会销毁CashFragment，dialog也会被销毁(相当于terminal中代码未执行)，但是重建的时候又显示出来了，于是dialog就一直显示着
                        mView.hideLoading();
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            if (apiException.getResultCode() == -55) {
                                //账单已经结账
                                mView.onSunmiL3Failed(0);
                            } else if (apiException.getResultCode() == -56) {
                                //超额支付
                                mView.onSunmiL3Failed(1);
                            } else {
                                mView.onSunmiL3Failed(2);
                            }
                        } else {
                            mView.onSunmiL3Failed(2);
                        }
                    }
                });
    }
}
