package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsAdditionalFeesContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderAdditionalFeesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by Administrator on 2017/4/7.
 */

public class BalanceAccountsAdditionalFeesPresenter extends BasePresenter<BalanceAccountsAdditionalFeesContract.View> implements BalanceAccountsAdditionalFeesContract.Presenter {
    public BalanceAccountsAdditionalFeesPresenter(BalanceAccountsAdditionalFeesContract.View view) {
        super(view);
    }

    @Override
    public void getAdditionalFeesList() {
        mRepository.getAllAdditionalFee()
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<List<AdditionalFees>>(mView, true) {
                    @Override
                    protected void next(List<AdditionalFees> additionalFeesEs) {
                        mView.onGetAdditionalFeesSuccess(additionalFeesEs);
                    }
                });
    }

    @Override
    public void getOrderAdditionalFeesList(String salesOrderGUID) {
        SalesOrderAdditionalFeesE salesOrderAdditionalFeesE = new SalesOrderAdditionalFeesE();
        salesOrderAdditionalFeesE.setSalesOrderGUID(salesOrderGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderAdditionalFeesB.GetList)
                .setRequestBody(salesOrderAdditionalFeesE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onGetOrderAdditionalFeesSuccess(xmlData.getArrayOfSalesOrderAdditionalFeesE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkErrorLayout();
                        }
                    }
                });
    }

    @Override
    public void setFees(String salesOrderGUID, List<SalesOrderAdditionalFeesE> list) {
        mRepository.getUsers()
                .flatMap(users -> {
                    String usersGUID = users.getUsersGUID();
                    for (SalesOrderAdditionalFeesE salesOrderAdditionalFeesE : list) {
                        salesOrderAdditionalFeesE.setAddUsersGUID(usersGUID);
                    }
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setCheckOutStaffGUID(usersGUID);
                    salesOrderE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderE.setArrayOfSalesOrderAdditionalFeesE(list);
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.SetFees)
                            .setRequestBody(salesOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSetFeesSuccess();
                    }
                });
    }
}
