package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcPayContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by terry on 17-11-6.
 */

public class PaymentMtcPayPresenter extends BasePresenter<PaymentMtcPayContract.View> implements PaymentMtcPayContract.Presenter {

    public PaymentMtcPayPresenter(PaymentMtcPayContract.View view) {
        super(view);
    }

    @Override
    public void MeituanPay(String salesOrderGUID, String transactionNumber, int useCount, String paymentItemCode) {
        mRepository.getUsers().flatMap(users -> {
            SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
            salesOrderPaymentE.setSalesOrderGUID(salesOrderGUID);
            salesOrderPaymentE.setTransactionNumber(transactionNumber);
            salesOrderPaymentE.setUseCount(useCount);
            salesOrderPaymentE.setPaymentItemCode(paymentItemCode);
            return XmlData.Builder().setRequestMethod(RequestMethod.SalesOrderPaymentB.AddMeituanCoupon)
                    .setRequestBody(salesOrderPaymentE).buildRESTful();
        }).flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.MeituanPaySuccess(xmlData.getArrayOfMeituanCoupon());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.MeituanPayFailed();
                    }
                });
    }
}
