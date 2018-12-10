package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountChangePaymentContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by zhaoping on 2017/6/1.
 */

public class BalanceAccountsDiscountChangePaymentPresenter extends BasePresenter<BalanceAccountsDiscountChangePaymentContract.View> implements BalanceAccountsDiscountChangePaymentContract.Presenter {
    public BalanceAccountsDiscountChangePaymentPresenter(BalanceAccountsDiscountChangePaymentContract.View view) {
        super(view);
    }

    @Override
    public void setActually(SalesOrderE salesOrderE) {
        mRepository.getUsers()
                .flatMap(users -> {
                    salesOrderE.setCheckOutStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.SetActually)
                            .setRequestBody(salesOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onChangeOrderPriceSuccess();
                    }
                });
    }
}
