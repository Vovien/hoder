package com.holderzone.intelligencepos.mvp.hall.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.hall.contract.OrderDetailContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LiTao on 2017-9-5.
 */

public class OrderDetailPresenter extends BasePresenter<OrderDetailContract.View> implements OrderDetailContract.Presenter {
    public OrderDetailPresenter(OrderDetailContract.View view) {
        super(view);
    }

    @Override
    public void getDishesEstimate() {
        mRepository.getAccountRecord()
                .flatMap(accountRecord -> {
                    DishesEstimateRecordE dishesEstimateRecordE = new DishesEstimateRecordE();
                    String businessDay = accountRecord.getBusinessDay();
                    dishesEstimateRecordE.setBusinessDay(businessDay);
                    return XmlData.Builder().setRequestMethod(RequestMethod.DishesEstimateRecordB.GetCurrentInfo)
                            .setRequestBody(dishesEstimateRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        Map<String, DishesEstimateRecordDishes> estumateMap = new HashMap<String, DishesEstimateRecordDishes>();
                        Map<String, Double> map = new HashMap<String, Double>();
                        if (xmlData.getDishesEstimateRecordE() != null && xmlData.getDishesEstimateRecordE().getArrayOfDishesEstimateRecordDishes() != null
                                && xmlData.getDishesEstimateRecordE().getArrayOfDishesEstimateRecordDishes().size() > 0) {
                            if (xmlData.getArrayOfSalesOrderDishesE() != null) {
                                for (SalesOrderDishesE salesOrderDishesE : xmlData.getArrayOfSalesOrderDishesE()) {
                                    map.put(salesOrderDishesE.getDishesGUID().toLowerCase(), salesOrderDishesE.getCheckCount());
                                }
                            }
                            for (DishesEstimateRecordDishes dishesEstimateRecordDishes : xmlData.getDishesEstimateRecordE().getArrayOfDishesEstimateRecordDishes()) {
                                Double CheckCount = map.get(dishesEstimateRecordDishes.getDishesGUID().toLowerCase());
                                dishesEstimateRecordDishes.setSaleCount(CheckCount == null ? 0d : CheckCount);
                                dishesEstimateRecordDishes.setResidueCount(dishesEstimateRecordDishes.getEstimateCount() - dishesEstimateRecordDishes.getSaleCount());
                                dishesEstimateRecordDishes.setCurrentOrderCount(0d);
                                estumateMap.put(dishesEstimateRecordDishes.getDishesGUID().toLowerCase(), dishesEstimateRecordDishes);
                            }
                        }
                        mView.onGetDishesEstimateSuccess(estumateMap);
                    }
                });
    }

    @Override
    public void createOrder(SalesOrderBatchE salesOrderBatchE) {
        mRepository.getUsers()
                .flatMap(users -> {
                    salesOrderBatchE.setStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderBatchB.AddDishes)
                            .setRequestBody(salesOrderBatchE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView,false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        int noteCode = xmlData.getApiNote().getNoteCode();
                        if (1 == noteCode) {
                            mView.onCreateOrderSuccess(xmlData.getSalesOrderE());
                            if (xmlData.getApiNote().getResultCode() == 10) {
                                mView.showMessage(xmlData.getApiNote().getResultMsg());
                            }
                        } else if (noteCode == -4 && xmlData.getApiNote().getResultCode() == -50) {//菜品数量超出估清数量
                            mView.onCreateOrderFiled("所选菜品超出估清数量");
                        } else if (noteCode == -4) {
                            mView.showMessage(xmlData.getApiNote().getResultMsg());
                        } else {
                            mView.showMessage(xmlData.getApiNote().getNoteMsg());
                        }
                    }
                });


    }
}
