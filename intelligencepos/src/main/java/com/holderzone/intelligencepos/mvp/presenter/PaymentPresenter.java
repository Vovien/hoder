package com.holderzone.intelligencepos.mvp.presenter;

import android.annotation.SuppressLint;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentPresenter extends BasePresenter<PaymentContract.View> implements PaymentContract.Presenter {
    public PaymentPresenter(PaymentContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void requestPaymentItem() {
        mRepository.getAllPaymentItem()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(paymentItems -> mView.onPaymentItemObtainSucceed(paymentItems));
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

    @SuppressLint("CheckResult")
    @Override
    public void requestIsHesMember() {
        mRepository.getSystemConfig()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(parametersConfig -> mView.responseIsHesMember(parametersConfig.isHesMember()));
    }


}
