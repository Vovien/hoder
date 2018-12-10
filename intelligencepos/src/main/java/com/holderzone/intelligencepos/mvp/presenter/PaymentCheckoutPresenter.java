package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentCheckoutContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * 结账状态
 * Created by tcw on 2017/6/1.
 */

public class PaymentCheckoutPresenter extends BasePresenter<PaymentCheckoutContract.View> implements PaymentCheckoutContract.Presenter {

    public PaymentCheckoutPresenter(PaymentCheckoutContract.View view) {
        super(view);
    }

    @Override
    public void submitCheckOut(String salesOrderGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderE.setCheckOutStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.CheckOut)
                            .setRequestBody(salesOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onCheckOutSucceed(xmlData.getSalesOrderE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int noteCode = apiException.getNoteCode();
                            int resultCode = apiException.getResultCode();
                            if (-4 == noteCode && 0 == resultCode) {
                                // 应付金额与实收金额不一致
                                mView.onCheckOutFailedBecauseOfMoneyNotMatch();
                            } else if (-4 == noteCode && -1 == resultCode) {
                                // 账单已结账
                                mView.onCheckOutFailedBecauseOfCheckoutAlready();
                            } else if (-4 == noteCode && -200 == resultCode) {
                                // 会员积分变化
                                mView.onCheckOutFailedByIntegralChange();
                            } else {
                                mView.onCheckOutFailed();
                            }
                        } else {
                            mView.onCheckOutFailed();
                        }
                    }
                });
    }
}
