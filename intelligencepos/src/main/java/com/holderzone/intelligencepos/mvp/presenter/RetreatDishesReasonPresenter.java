package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.RetreatDishesReasonContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by LiTao on 2017-9-6.
 */

public class RetreatDishesReasonPresenter extends BasePresenter<RetreatDishesReasonContract.View> implements RetreatDishesReasonContract.Presenter {
    public RetreatDishesReasonPresenter(RetreatDishesReasonContract.View view) {
        super(view);
    }

    @Override
    public void requestReturnRemark() {
        XmlData.Builder().setRequestMethod(RequestMethod.DishesReturnReasonB.GetList)
                .setRequestBody(new DishesReturnReasonE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.getReturnReasonSuccess(xmlData.getArrayOfDishesReturnReasonE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onRequestRetreatReasonsFailed();
                    }
                });
    }

    @Override
    public void addNewRetreatReason(String nameOfRetreatReason) {
        DishesReturnReasonE dishesReturnReasonE = new DishesReturnReasonE();
        dishesReturnReasonE.setName(nameOfRetreatReason);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesReturnReasonB.Insert)
                .setRequestBody(dishesReturnReasonE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onAddNewRetreatReasonSucceed(ApiNoteHelper.obtainSuccessMsg(xmlData), xmlData.getDishesReturnReasonE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onAddNewRetreatReasonFailed();
                    }
                });
    }

    @Override
    public void submitRetreatDishes(String salesOrderGUID, String diningTableGUID,
                                    List<SalesOrderBatchDishesE> arrayOfSalesOrderBatchDishesE,
                                    List<DishesReturnReasonE> arrayOfDishesReturnReasonE) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
                    salesOrderBatchE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderBatchE.setDiningTableGUID(diningTableGUID);
                    salesOrderBatchE.setStaffGUID(users.getUsersGUID());
                    salesOrderBatchE.setOperationType(-1);
                    salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(arrayOfSalesOrderBatchDishesE);
                    salesOrderBatchE.setArrayOfDishesReturnReasonE(arrayOfDishesReturnReasonE);
                    salesOrderBatchE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderBatchB.ReturnDishes)
                            .setRequestBody(salesOrderBatchE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onRetreatDishesSucceed();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onRetreatDishesFailed();
                    }
                });
    }
}
