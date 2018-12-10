package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * 结算presenter
 * Created by zhaoping on 2017/3/31.
 */

public class BalanceAccountsPresenter extends BasePresenter<BalanceAccountsContract.View> implements BalanceAccountsContract.Presenter {
    public BalanceAccountsPresenter(BalanceAccountsContract.View view) {
        super(view);
    }

    @Override
    public void getHesMember() {
        mRepository.getSystemConfig().subscribe(config -> mView.onGetHesMember(config.isHesMember()));
    }

    @Override
    public void getOrderInfo(String salesOrderGuid) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setSalesOrderGUID(salesOrderGuid);
                    salesOrderE.setCheckStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.GetListBySalesOrderGuid_V1)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE newSalesOrderE = xmlData.getSalesOrderE();
                        boolean isAutoCalc = newSalesOrderE.getAutoCalc() != null && newSalesOrderE.getAutoCalc() == 1;
                        List<SalesOrderE> salesOrderEList = diningTableToSalesOrder(newSalesOrderE);
                        List<OrderDishesGroup> list = groupingOrderDishes(salesOrderEList);

                        mView.onGetOrderDisheSuccess(isAutoCalc, list, salesOrderEList);
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


    /**
     * 将SalesOrderDiningTableE 转换为所需的SalesOrderE
     */
    private List<SalesOrderE> diningTableToSalesOrder(SalesOrderE newSalesOrderE) {
        List<SalesOrderE> salesOrderEList = new ArrayList<>();
        List<SalesOrderDiningTableE> orderDiningTableES = newSalesOrderE.getArrayOfSalesOrderDiningTableE();

        if (orderDiningTableES != null && orderDiningTableES.size() != 0) {
            for (int i = 0; i < orderDiningTableES.size(); i++) {
                SalesOrderDiningTableE orderDiningTableE = orderDiningTableES.get(i);

                SalesOrderE salesOrderE = new SalesOrderE();
                DiningTableE diningTableE = new DiningTableE();
                diningTableE.setAreaName(orderDiningTableE.getAreaName());
                diningTableE.setName(orderDiningTableE.getDtName());
                salesOrderE.setDiningTableE(diningTableE);
                salesOrderE.setArrayOfSalesOrderAdditionalFeesE(orderDiningTableE.getArrayOfSalesOrderAdditionalFeesE());
                salesOrderE.setArrayOfSalesOrderDishesE(orderDiningTableE.getArrayOfSalesOrderDishesE());
                salesOrderE.setDtAdditionalFeesTotal(orderDiningTableE.getDtAdditionalFeesTotal());
                salesOrderE.setUpperState(orderDiningTableE.getUpperState());
                salesOrderE.setSalesOrderGUID(orderDiningTableE.getSalesOrderGUID());
                salesOrderE.setTradeMode(newSalesOrderE.getTradeMode());


                if (orderDiningTableE.getUpperState() == 0 || orderDiningTableE.getUpperState() == 1) {
                    salesOrderE.setSerialNumber(newSalesOrderE.getSerialNumber());
                    salesOrderE.setMemberInfoE(newSalesOrderE.getMemberInfoE());
                    salesOrderE.setDiscountTotal(newSalesOrderE.getDiscountTotal());
                    salesOrderE.setAdditionalFeesTotal(newSalesOrderE.getAdditionalFeesTotal());
                    salesOrderE.setDishesConsumeTotal(newSalesOrderE.getDishesConsumeTotal());
                    salesOrderE.setArrayOfSalesOrderDiscountE(newSalesOrderE.getArrayOfSalesOrderDiscountE());
                    salesOrderE.setCheckTotal(newSalesOrderE.getCheckTotal());
                    salesOrderE.setDiscountRatio(newSalesOrderE.getDiscountRatio());
                }
                salesOrderEList.add(salesOrderE);
            }
        }

        return salesOrderEList;
    }

    /**
     * 订单数据分组
     *
     * @param salesOrderEList
     * @return
     */
    private List<OrderDishesGroup> groupingOrderDishes(List<SalesOrderE> salesOrderEList) {
        List<OrderDishesGroup> orderDishesGroups = new ArrayList<>();
        for (SalesOrderE bean : salesOrderEList) {
            OrderDishesGroup group = new OrderDishesGroup();
            group.setTitleColorRes(R.color.text_order_dishes_group_ineffective);
            if (bean.getTradeMode() == 1) {
                group.setTitle("快餐(" + bean.getArrayOfSalesOrderDishesE().size() + ")");
            } else {
                group.setTitle(bean.getDiningTableE().getAreaName() + "-" + bean.getDiningTableE().getName() + "(" + bean.getArrayOfSalesOrderDishesE().size() + ")");
            }
            group.setSalesOrderDishesEList(bean.getArrayOfSalesOrderDishesE());
            orderDishesGroups.add(group);
        }
        return orderDishesGroups;
    }

    @Override
    public void memberLoginOut(final String salesOrderGuid) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGuid);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.MemberLoginOut)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        BalanceAccountsPresenter.this.getOrderInfo(salesOrderGuid);
                        mView.onMemberLoginOutSuccess();
                    }
                });
    }

    @Override
    public void requestInvalid(String salesOrderGuid) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setSalesOrderGUID(salesOrderGuid);
                    salesOrderE.setCancelStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Invalid)
                            .setRequestBody(salesOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (xmlData.getApiNote().getNoteCode() != 1) {
                            if (xmlData.getApiNote().getResultCode() == -40) {
                                mView.invalidFiled(0);
                            }
                        } else {
                            mView.invalidSuccess();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.invalidFiled(1);
                    }
                });
    }

    @Override
    public void checkPrint(String salesOrderGuid) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGuid);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.CheckPrint)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (ApiNoteHelper.checkBusiness(xmlData)) {
                            if (ApiNoteHelper.checkPrinterWhenBusinessSuccess(xmlData)) {
                                mView.showMessage(ApiNoteHelper.obtainPrinterMsg(xmlData));
                            } else {
                                mView.onCheckPrintSuccess();
                            }
                        } else {
                            mView.showMessage(ApiNoteHelper.obtainErrorMsg(xmlData));
                        }
                    }
                });
    }
}
