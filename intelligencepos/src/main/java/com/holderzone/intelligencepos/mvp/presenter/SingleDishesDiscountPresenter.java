package com.holderzone.intelligencepos.mvp.presenter;


import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SingleDishesDiscountContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SingleDishesDiscount;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by www on 2018/8/8.
 */

public class SingleDishesDiscountPresenter extends BasePresenter<SingleDishesDiscountContract.View> implements SingleDishesDiscountContract.Presenter {
    public SingleDishesDiscountPresenter(SingleDishesDiscountContract.View view) {
        super(view);
    }

    @Override
    public void getIsMemberPrice() {
        mRepository.getSystemConfig().subscribe(config -> mView.onGetIsMemberPrice(config.isMemberPrice()));
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
                        final boolean[] enableMemberPrice = new boolean[1];
                        mRepository.getSystemConfig().subscribe(config -> enableMemberPrice[0] = config.isMemberPrice());
                        List<SalesOrderE> orderList = xmlData.getArrayOfSalesOrderE();
                        List<SingleDishesDiscount> list = new java.util.ArrayList<>();
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
                                    if (batchDishesE.getGift() == 1) {
                                        continue;
                                    }
                                    SingleDishesDiscount singleDishesDiscount = new SingleDishesDiscount();
                                    singleDishesDiscount.setTime(salesOrderBatch.getBatchTime());
                                    singleDishesDiscount.setGift(batchDishesE.getGift());
                                    singleDishesDiscount.setDishesName(batchDishesE.getDishesE().getSimpleName());
                                    singleDishesDiscount.setDiscountPrice(batchDishesE.getDiscountPrice());
                                    singleDishesDiscount.setCookMethodString(batchDishesE.getPracticeNames());
                                    singleDishesDiscount.setPackageDishesString(batchDishesE.getSubDishes());
                                    singleDishesDiscount.setCheckCount(batchDishesE.getReviewCheckCount() == -1 ? batchDishesE.getCheckCount() : batchDishesE.getReviewCheckCount());
                                    singleDishesDiscount.setPrice(batchDishesE.getPrice());
                                    singleDishesDiscount.setPracticeSubTotal(batchDishesE.getPracticeSubTotal());
                                    singleDishesDiscount.setMemberPrice(batchDishesE.getMemberPrice());
                                    if (batchDishesE.getDiscountPrice() == null || batchDishesE.getDiscountPrice() == -1) {
                                        if (enableMemberPrice[0] && mainOrder.getMemberInfoE() != null) {
                                            singleDishesDiscount.setNewPrice(batchDishesE.getMemberPrice());
                                        } else {
                                            singleDishesDiscount.setNewPrice(batchDishesE.getPrice());
                                        }
                                    } else {
                                        singleDishesDiscount.setNewPrice(batchDishesE.getDiscountPrice());
                                    }
                                    singleDishesDiscount.setSalesOrderBatchGUID(batchDishesE.getSalesOrderBatchGUID());
                                    singleDishesDiscount.setSalesOrderBatchDishesGUID(batchDishesE.getSalesOrderBatchDishesGUID());
                                    if (orderList.size() >= 1 && j == 0 && i == 0) {
                                        int count = 0;
                                        for (SalesOrderBatchE batchE : order.getArrayOfSalesOrderBatchE()) {
                                            count += batchE.getArrayOfSalesOrderBatchDishesE().size();
                                        }
                                        if (order.getDiningTableE().getAreaName() != null && order.getDiningTableE().getName() != null) {
                                            singleDishesDiscount.setTitle(order.getDiningTableE().getAreaName() + "-" + order.getDiningTableE().getName() + "（" + count + "）");
                                        } else {
                                            singleDishesDiscount.setTitle("快餐" + "（" + count + "）");
                                        }
                                    }
                                    list.add(singleDishesDiscount);
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
    public void setPrice(List<SalesOrderBatchDishesE> list) {
        SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
        salesOrderBatchE.setArrayOfSalesOrderBatchDishesE(list);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderBatchB.SetPrice)
                .setRequestBody(salesOrderBatchE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onReviewDishesSuccess();


                    }
                });

    }
}
