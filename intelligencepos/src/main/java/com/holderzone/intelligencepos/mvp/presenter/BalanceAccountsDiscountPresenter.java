package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountContract;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by zhaoping on 2017/7/20.
 */

public class BalanceAccountsDiscountPresenter extends BasePresenter<BalanceAccountsDiscountContract.View> implements BalanceAccountsDiscountContract.Presenter {
    public BalanceAccountsDiscountPresenter(BalanceAccountsDiscountContract.View view) {
        super(view);
    }

    @Override
    public void getOrderInfo(String salesOrderGuid) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGuid);
        salesOrderE.setReturnSalesOrderDishes(1);
        salesOrderE.setReturnSalesOrderPayment(1);
        salesOrderE.setReturnSalesOrderDiscount(1);
        salesOrderE.setReturnSalesOrderAdditionalFees(1);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        List<OrderDishesGroup> list = new java.util.ArrayList<>();
                        SalesOrderE mainOrder = null;
                        for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
                            OrderDishesGroup group = new OrderDishesGroup();
                            group.setTitleColorRes(R.color.text_order_dishes_group_ineffective);
                            if (bean.getTradeMode() == 1) {
                                group.setTitle("快餐(" + bean.getArrayOfSalesOrderDishesE().size() + ")");
                            } else {
                                group.setTitle(bean.getDiningTableE().getAreaName() + "-" + bean.getDiningTableE().getName() + "(" + bean.getArrayOfSalesOrderDishesE().size() + ")");
                            }
                            group.setSalesOrderDishesEList(bean.getArrayOfSalesOrderDishesE());
                            list.add(group);
                            if (bean.getUpperState() == 1) {
                                mainOrder = bean;
                            }
                        }
                        if (mainOrder == null) {
                            mainOrder = xmlData.getArrayOfSalesOrderE().get(0);
                        }
                        mView.onGetOrderDisheSuccess(list, mainOrder);
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
}
