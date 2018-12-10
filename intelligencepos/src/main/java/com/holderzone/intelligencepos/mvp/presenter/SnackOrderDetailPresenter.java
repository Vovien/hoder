package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SnackOrderDetailContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.HashMap;
import java.util.Map;

import static com.holderzone.intelligencepos.mvp.model.bean.XmlData.Builder;

/**
 * Created by LiTao on 2017-8-3.
 */

public class SnackOrderDetailPresenter extends BasePresenter<SnackOrderDetailContract.View> implements SnackOrderDetailContract.Presenter {
    public SnackOrderDetailPresenter(SnackOrderDetailContract.View view) {
        super(view);
    }

    @Override
    public void getDishesEstimate() {
        mRepository.getAccountRecord()
                .flatMap(accountRecord -> {
                    DishesEstimateRecordE dishesEstimateRecordE = new DishesEstimateRecordE();
                    dishesEstimateRecordE.setBusinessDay(accountRecord.getBusinessDay());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.DishesEstimateRecordB.GetCurrentInfo)
                            .setRequestBody(dishesEstimateRecordE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
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
    public void createOrder(SalesOrderBatchE salesOrderBatchE, String numberPlate, int mGuestCount, String memberGuid) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setOrgNumber(numberPlate);
                    salesOrderE.setGuestCount(mGuestCount);
                    salesOrderE.setMemberInfoGUID(memberGuid);
                    salesOrderE.setCreateStaffGUID(users.getUsersGUID());
                    salesOrderE.setReceptionStaffGUID(users.getUsersGUID());
                    salesOrderE.setTradeMode(1);
                    salesOrderE.setSalesOrderBatchE(salesOrderBatchE);
                    return Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Create)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, false) {
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

    @Override
    public void getFastSalesGuestCount() {
        mRepository.getSystemConfig()
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(new BaseObserver<ParametersConfig>(mView) {
                               @Override
                               protected void next(ParametersConfig parametersConfig) {
                                   mView.responseFastSalesGuestCountSuccess(parametersConfig.isFastSalesGuestCount());
                               }

                               @Override
                               public void onError(Throwable e) {
                                   mView.responseFastSalesGuestCountFail();
                                   super.onError(e);
                               }
                           }
                );
    }
}
