package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.DesignatedOneDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderServingDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by chencao on 2018/1/10.
 */

public class DesignatedOneDishesPresenter extends BasePresenter<DesignatedOneDishesContract.View> implements DesignatedOneDishesContract.Presenter {
    public DesignatedOneDishesPresenter(DesignatedOneDishesContract.View view) {
        super(view);
    }

    @Override
    public void updataSalesOrderBatchList(List<SalesOrderServingDishesE> salesOrderServingDishesEList) {
        Observable.zip(mRepository.getEnterpriseInfo(), mRepository.getStore(), mRepository.getUsers(), (enterpriseInfo, store, users) -> {
            SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
            salesOrderBatchE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
            salesOrderBatchE.setStoreGUID(store.getStoreGUID());
            salesOrderBatchE.setOperaStaffGUID(users.getUsersGUID());
            salesOrderBatchE.setArrayOfSalesOrderServingDishesE(salesOrderServingDishesEList);

            return salesOrderBatchE;
        })
                .flatMap(salesOrderBatchE -> XmlData.Builder()
                        .setRequestMethod(RequestMethod.SalesOrderBatchB.ServingDishes)
                        .buildRESTful()
                        .doOnNext(xmlData -> xmlData.setSalesOrderBatchE(salesOrderBatchE))
                        .flatMap(mRepository::getXmlData))
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.updataSalesOrderBatchSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)){
                            mView.updataSalesOrderBatchFail();
                        }
                    }
                });
    }
}
