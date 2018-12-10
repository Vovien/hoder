package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcCheckContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by terry on 17-11-6.
 */

public class PaymentMtcCheckPresenter extends BasePresenter<PaymentMtcCheckContract.View> implements PaymentMtcCheckContract.Presenter {

    public PaymentMtcCheckPresenter(PaymentMtcCheckContract.View view) {
        super(view);
    }

    @Override
    public void MeituanCheck(String transactionNumber) {
        SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
        salesOrderPaymentE.setTransactionNumber(transactionNumber);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderPaymentB.MeituanPrepare)
                .setRequestBody(salesOrderPaymentE).buildRESTful()
                .flatMap(xmlData -> mRepository.getXmlData(xmlData))
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.MeituanCheckSuccess(xmlData.getMeituanCoupon());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            String message = e.getMessage();
                            ApiException apiException = (ApiException) e;
                            if (apiException.getNoteCode() == -101) {
                                mView.showAuthorizationDialog(message);//授权过期 错误
                                return;
                            } else {
                                mView.MeituanCheckFailed();
                                return;
                            }
                        }
                        super.onError(e);
                    }
                });
    }
}
