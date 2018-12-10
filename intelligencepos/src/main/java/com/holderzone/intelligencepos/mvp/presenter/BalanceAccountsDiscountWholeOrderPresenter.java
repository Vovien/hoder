package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountWholeOrderContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by zhaoping on 2017/6/1.
 */

public class BalanceAccountsDiscountWholeOrderPresenter extends BasePresenter<BalanceAccountsDiscountWholeOrderContract.View> implements BalanceAccountsDiscountWholeOrderContract.Presenter {
    public BalanceAccountsDiscountWholeOrderPresenter(BalanceAccountsDiscountWholeOrderContract.View view) {
        super(view);
    }

    @Override
    public void setDiscountRatio(String salesOrderGuid, double discount) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setDiscountRatio(discount);
                    salesOrderE.setSalesOrderGUID(salesOrderGuid);
                    salesOrderE.setCheckOutStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.SetDiscountRatio)
                            .setRequestBody(salesOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onDiscountRatioSuccess();
                    }
                });
    }
}
