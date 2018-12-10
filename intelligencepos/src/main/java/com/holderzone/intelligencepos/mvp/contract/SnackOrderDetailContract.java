package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;

import java.util.Map;

/**
 * 快销订单详情contract
 * Created by LiTao on 2017-8-3.
 */

public interface SnackOrderDetailContract {

    interface View extends IView {

        /**
         * 获取菜品估清数据成功
         */
        void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map);

        /**
         * 下单成功
         */
        void onCreateOrderSuccess(SalesOrderE salesOrderE);

        /**
         * 下单失败
         */
        void onCreateOrderFiled(String msg);
        /**
         * 请求获取快餐是否启用输入人数功能成功
         *
         * @param isFastSalesGuestCount
         */
        void responseFastSalesGuestCountSuccess(boolean isFastSalesGuestCount);

        /**
         * 请求获取快餐是否启用输入人数功能失败
         */
        void responseFastSalesGuestCountFail();
    }

    interface Presenter extends IPresenter {

        /**
         * 请求当前营业日估清
         */
        void getDishesEstimate();

        /**
         * 下单请求
         */
        void createOrder(SalesOrderBatchE salesOrderBatchE, String numberPlate,int mGuestCount,String memberGuid);
        /**
         * 快餐是否启用输入人数功能
         */
        void getFastSalesGuestCount();
    }
}
