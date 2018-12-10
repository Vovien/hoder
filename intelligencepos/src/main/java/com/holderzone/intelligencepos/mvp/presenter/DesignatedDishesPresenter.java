package com.holderzone.intelligencepos.mvp.presenter;

import android.util.Pair;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.DesignatedDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderServingDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by chencao on 2018/1/8.
 */

public class DesignatedDishesPresenter extends BasePresenter<DesignatedDishesContract.View> implements DesignatedDishesContract.Presenter {
    public DesignatedDishesPresenter(DesignatedDishesContract.View view) {
        super(view);
    }

    @Override
    public void getSalesOrderBatchList(String diningTableGUID) {

        Observable.zip(mRepository.getEnterpriseInfo(), mRepository.getStore(), Pair::new)
                .flatMap(pair -> {
                    EnterpriseInfo first = pair.first;
                    Store second = pair.second;
                    SalesOrderServingDishesE salesOrderServingDishesE = new SalesOrderServingDishesE();
                    salesOrderServingDishesE.setDiningTableGUID(diningTableGUID);
                    salesOrderServingDishesE.setEnterpriseInfoGUID(first.getEnterpriseInfoGUID());
                    salesOrderServingDishesE.setStoreGUID(second.getStoreGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderBatchB.GetDTDishListByNoServing)
                            .buildRESTful()
                            .doOnNext(xmlData -> xmlData.setSalesOrderServingDishesE(salesOrderServingDishesE))
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.getSalesOrderBatchList(xmlData.getArrayOfDiningTableE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.getSalesOrderBatchListFail();
                        }
                    }
                });
    }

    @Override
    public void upDataAll(String diningTableGUID, String salesOrderGUID) {

        Observable.zip(mRepository.getEnterpriseInfo(), mRepository.getStore(),mRepository.getUsers(), (enterpriseInfo, store, users) -> {

            SalesOrderServingDishesE salesOrderServingDishesE = new SalesOrderServingDishesE();
            salesOrderServingDishesE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
            salesOrderServingDishesE.setStoreGUID(store.getStoreGUID());
            salesOrderServingDishesE.setOperaStaffGUID(users.getUsersGUID());
            salesOrderServingDishesE.setDiningTableGUID(diningTableGUID);
            salesOrderServingDishesE.setSalesOrderGUID(salesOrderGUID);
            return salesOrderServingDishesE;
        })
                .flatMap(salesOrderServingDishesE -> XmlData.Builder()
                        .setRequestMethod(RequestMethod.SalesOrderBatchB.ServingDishesWholeDT)
                        .buildRESTful()
                        .doOnNext(xmlData -> xmlData.setSalesOrderServingDishesE(salesOrderServingDishesE))
                        .flatMap(mRepository::getXmlData))
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.upDataAllSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if(ExceptionHelper.checkNetException(e)){
                            mView.upDataAllFail();
                        }
                    }
                });
    }
}
