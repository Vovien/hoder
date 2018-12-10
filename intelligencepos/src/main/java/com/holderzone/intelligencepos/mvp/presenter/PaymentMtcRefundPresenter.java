package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcRefundContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by terry on 17-11-6.
 */

public class PaymentMtcRefundPresenter extends BasePresenter<PaymentMtcRefundContract.View> implements PaymentMtcRefundContract.Presenter {

    public PaymentMtcRefundPresenter(PaymentMtcRefundContract.View view) {
        super(view);
    }

    @Override
    public void QueryMeituanCoupon(String salesOrderPaymentGUID) {
        SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
        salesOrderPaymentE.setSalesOrderPaymentGUID(salesOrderPaymentGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderPaymentB.QueryMeituanCoupon)
                .setRequestBody(salesOrderPaymentE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.QueryMeituanCouponSuccess(xmlData.getArrayOfMeituanCoupon());
                    }
                });
    }

    @Override
    public void MeituanRefund(String salesOrderGUID, String salesOrderPaymentGUID, String transactionNumber) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderPaymentE.setSalesOrderPaymentGUID(salesOrderPaymentGUID);
                    salesOrderPaymentE.setTransactionNumber(transactionNumber);
                    salesOrderPaymentE.setRefundStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.CancelMeituanCoupon)
                            .setRequestBody(salesOrderPaymentE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.MeituanRefundSuccess();
                    }
                });
    }
}
