package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.CallUpDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by tcw on 2017/7/6.
 */
public class CallUpDishesPresenter extends BasePresenter<CallUpDishesContract.View> implements CallUpDishesContract.Presenter {

    public CallUpDishesPresenter(CallUpDishesContract.View view) {
        super(view);
    }

    @Override
    public void requestHangedUpDishes(String salesOrderGUID, Integer checkStatus) {
        SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
        salesOrderBatchE.setSalesOrderGUID(salesOrderGUID);
        salesOrderBatchE.setCheckStatus(checkStatus);
        salesOrderBatchE.setReturnBatchDishesPractice(1);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderBatchB.GetHangDishes)
                .setRequestBody(salesOrderBatchE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE salesOrderE = xmlData.getSalesOrderE();
                        if (salesOrderE != null) {
                            List<SalesOrderBatchE> arrayOfSalesOrderBatchE = salesOrderE.getArrayOfSalesOrderBatchE();
                            List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = new ArrayList<>();
                            for (SalesOrderBatchE orderBatchE : arrayOfSalesOrderBatchE) {
                                List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE = orderBatchE.getArrayOfSalesOrderBatchDishesE();
                                for (SalesOrderBatchDishesE salesOrderBatchDishesE : arrayOfSalesOrderBatchDishesE) {
                                    salesOrderBatchDishesEList.add(salesOrderBatchDishesE);
                                }
                            }
                            mView.onRequestHangedUpDishesSucceed(salesOrderBatchDishesEList);
                        }
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onRequestHangedUpDishesFailed();
                        }
                    }
                });
    }

    @Override
    public void submitCallUpDishes(List<SalesOrderBatchDishesE> salesOrderBatchDishesEList) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
                    salesOrderBatchE.setStaffGUID(users.getUsersGUID());
                    salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(salesOrderBatchDishesEList);
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderBatchB.WakeUp)
                            .setRequestBody(salesOrderBatchE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onCallUpSucceed();
                    }
                });
    }
}