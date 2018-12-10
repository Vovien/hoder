package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.OrderRecordE;

import java.util.List;

/**
 * 预定列表contract
 * Created by tcw on 2017/6/6.
 */

public interface PredictedTableContract {

    interface View extends IView {

        /**
         * 获取营业日预订列表成功
         */
        void onOrderRecordObtainSuccess(List<OrderRecordE> orderRecordEList);

        /**
         * 获取营业日预订列表失败
         */
        void onOrderRecordObtainFailed();

        /**
         * 网络错误
         */
        void onNetworkError();
    }

    interface Presenter extends IPresenter {

        /**
         * 获取营业日预订列表
         */
        void requestOrderRecord(List<Integer> orderStatList);
    }
}