package com.holderzone.intelligencepos.mvp.presenter;

import android.annotation.SuppressLint;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentBankCardContract;
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
 * 银行卡支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentBankCardPresenter extends BasePresenter<PaymentBankCardContract.View> implements PaymentBankCardContract.Presenter {

    public PaymentBankCardPresenter(PaymentBankCardContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void requestIsHes() {
        mRepository.getSystemConfig()
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(parametersConfig -> mView.getIsHes(parametersConfig.isHesVersion()));
    }

    @Override
    public void submitBankCardPay(String saleOrderGUID, String paymentItemCode, double payableAmount, double actuallyAmount, String transactionNumber) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderGUID(saleOrderGUID);
                    salesOrderPaymentE.setPaymentItemCode(paymentItemCode);
                    salesOrderPaymentE.setPayableAmount(payableAmount);// 应收
                    salesOrderPaymentE.setActuallyAmount(actuallyAmount);// 实收
                    salesOrderPaymentE.setTransactionNumber(transactionNumber);
                    salesOrderPaymentE.setCreateStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.AddOrdinary)
                            .setRequestBody(salesOrderPaymentE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
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
                        // 会销毁BankCardFragment，dialog也会被销毁(相当于terminal中代码未执行)，但是重建的时候又显示出来了，于是dialog就一直显示着
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
                        mView.onBankCardPaySucceed(salesOrderE);
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);// hideLoading不能放在doAfterTerminate中，代码回调了PaymentActivity的adapter#notifyAll，
                        // 会销毁CashFragment，dialog也会被销毁(相当于terminal中代码未执行)，但是重建的时候又显示出来了，于是dialog就一直显示着
                        mView.hideLoading();
                        // 支付失败
                        mView.onBankCardPayFailed();
                    }
                });
    }
}
