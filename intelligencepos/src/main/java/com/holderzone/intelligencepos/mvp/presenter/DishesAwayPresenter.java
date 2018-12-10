package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.DishesAwayContract;
import com.holderzone.intelligencepos.mvp.model.bean.MatchSalesOrderDishes;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by Administrator on 2017/4/7.
 */

public class DishesAwayPresenter extends BasePresenter<DishesAwayContract.View> implements DishesAwayContract.Presenter {

    public DishesAwayPresenter(DishesAwayContract.View view) {
        super(view);
    }

    @Override
    public void getOrderInfo(String salesOrderGuid) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGuid);
        salesOrderE.setReturnBatch(1);
        salesOrderE.setShowScene(1);
        salesOrderE.setReturnBatchDishes(1);
        salesOrderE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        List<SalesOrderE> orderList = xmlData.getArrayOfSalesOrderE();
                        List<MatchSalesOrderDishes> list = new java.util.ArrayList<>();
                        SalesOrderE mainOrder = null;
                        for (SalesOrderE order : orderList) {
                            if (orderList.size() == 1) {
                                mainOrder = order;
                            } else if (order.getUpperState() == 1) {
                                mainOrder = order;
                            }
                            for (int i = 0; i < order.getArrayOfSalesOrderBatchE().size(); i++) {
                                SalesOrderBatchE salesOrderBatch = order.getArrayOfSalesOrderBatchE().get(i);
                                for (int j = 0; j < salesOrderBatch.getArrayOfSalesOrderBatchDishesE().size(); j++) {
                                    SalesOrderBatchDishesE batchDishesE = salesOrderBatch.getArrayOfSalesOrderBatchDishesE().get(j);
                                    MatchSalesOrderDishes matchSalesOrderDishes = new MatchSalesOrderDishes();
                                    matchSalesOrderDishes.setReviewCheckCount(batchDishesE.getReviewCheckCount());
                                    matchSalesOrderDishes.setTime(salesOrderBatch.getBatchTime());
                                    matchSalesOrderDishes.setCookMethodString(batchDishesE.getPracticeNames());
                                    matchSalesOrderDishes.setPackageDishesString(batchDishesE.getSubDishes());
                                    matchSalesOrderDishes.setGift(batchDishesE.getGift());
                                    matchSalesOrderDishes.setDishesName(batchDishesE.getDishesE().getSimpleName());
                                    if (batchDishesE.getReviewCheckCount() != -1) {
                                        matchSalesOrderDishes.setConfirmCount(batchDishesE.getReviewCheckCount());
                                    } else {
                                        matchSalesOrderDishes.setConfirmCount(batchDishesE.getCheckCount());
                                    }
                                    matchSalesOrderDishes.setCheckCount(batchDishesE.getCheckCount());
                                    matchSalesOrderDishes.setPrice(batchDishesE.getPrice());
                                    // TODO: 18-4-4 copy会员价
                                    matchSalesOrderDishes.setMemberPrice(batchDishesE.getMemberPrice());
                                    matchSalesOrderDishes.setSalesOrderBatchGUID(batchDishesE.getSalesOrderBatchGUID());
                                    matchSalesOrderDishes.setSalesOrderBatchDishesGUID(batchDishesE.getSalesOrderBatchDishesGUID());
                                    if (orderList.size() > 1 && j == 0 && i == 0) {
                                        int count = 0;
                                        for (SalesOrderBatchE batchE : order.getArrayOfSalesOrderBatchE()) {
                                            count += batchE.getArrayOfSalesOrderBatchDishesE().size();
                                        }
                                        if (order.getDiningTableE().getAreaName() != null && order.getDiningTableE().getName() != null) {
                                            matchSalesOrderDishes.setTitle(order.getDiningTableE().getAreaName() + "-" + order.getDiningTableE().getName() + "（" + count + "）");
                                        } else {
                                            matchSalesOrderDishes.setTitle("快餐" + "（" + count + "）");
                                        }
                                    }
                                    list.add(matchSalesOrderDishes);
                                }
                            }
                        }
                        mView.onGetOrderInfoSuccess(list, mainOrder, orderList.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkError();
                        }
                    }
                });
    }

    @Override
    public void SetDishesGift(List<SalesOrderBatchDishesE> list) {
        Observable.zip(mRepository.getEnterpriseInfo(), mRepository.getStore(), mRepository.getUsers(), (enterpriseInfo, store, users) -> {
            SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
            salesOrderBatchE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
            salesOrderBatchE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
            salesOrderBatchE.setStoreGUID(store.getStoreGUID());
            salesOrderBatchE.setStaffGUID(users.getUsersGUID());
            salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(list);

            return salesOrderBatchE;
        })
                .flatMap(salesOrderBatchE -> XmlData.Builder()
                        .setRequestMethod(RequestMethod.SalesOrderBatchB.SetDishesGift)
                        .buildRESTful()
                        .doOnNext(xmlData -> xmlData.setSalesOrderBatchE(salesOrderBatchE))
                        .flatMap(mRepository::getXmlData))
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.setDishesGiftSuccess();
                    }

                });

    }
}
