package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.MatchSalesOrderDishes;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;

import java.util.List;

import io.reactivex.Observable;

/**
 * 菜品赠送接口定义
 * Created by www on 2018/7/11.
 */
public interface DishesAwayContract {

    interface View extends IView {

        /**
         * 获取订单信息
         */
        void onGetOrderInfoSuccess(List<MatchSalesOrderDishes> list, SalesOrderE salesOrderE, int orderSize);

        /**
         * 赠送菜品业务成功
         */
        void setDishesGiftSuccess();

        /**
         * 获取订单信息失败
         */
        void showNetworkError();

    }

    interface Presenter extends IPresenter {

        /**
         * 获取订单信息
         */
        void getOrderInfo(String salesOrderGuid);

        /**
         * 确认赠送菜品
         */
        void SetDishesGift(List<SalesOrderBatchDishesE> list);
    }

}
