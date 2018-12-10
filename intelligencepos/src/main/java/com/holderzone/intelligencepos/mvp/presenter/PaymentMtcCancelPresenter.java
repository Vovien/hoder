package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentMtcCancelContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by terry on 17-11-6.
 */

public class PaymentMtcCancelPresenter extends BasePresenter<PaymentMtcCancelContract.View> implements PaymentMtcCancelContract.Presenter {

    public PaymentMtcCancelPresenter(PaymentMtcCancelContract.View view) {
        super(view);
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

    @Override
    public void requestSalesOrder(String salesOrderGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        salesOrderE.setReturnSalesOrderPayment(0);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        List<SalesOrderE> arrayOfSalesOrderE = xmlData.getArrayOfSalesOrderE();
                        if (arrayOfSalesOrderE != null) {
                            SalesOrderE salesOrderE = null;
                            if (arrayOfSalesOrderE.size() == 1) {
                                salesOrderE = arrayOfSalesOrderE.get(0);
                            } else {
                                for (SalesOrderE bean : arrayOfSalesOrderE) {
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
                        mView.onSalesOrderObtainFailed();
                    }
                });
    }
}
