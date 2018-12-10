package com.holderzone.intelligencepos.mvp.contract;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.base.IView;
import com.holderzone.intelligencepos.mvp.model.bean.EnterpriseInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.EquipmentsE;

/**
 * 关于商家信息contract
 * Created by LiTao on 2017-5-15.
 */

public interface AboutContract {

    interface View extends IView {

        /**
         * 获取注册商家基本信息成功
         */
        void getInfoSuccess(EquipmentsE equipmentsE, EnterpriseInfoE enterpriseInfoE);
    }

    interface Presenter extends IPresenter {

        /**
         * 请求注册商家基本信息
         */
        void requestInfo();
    }
}
