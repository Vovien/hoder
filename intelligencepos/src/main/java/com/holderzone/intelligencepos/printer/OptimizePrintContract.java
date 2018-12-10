package com.holderzone.intelligencepos.printer;

import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;

import java.util.List;

/**
 * 打印相关接口定义
 * Created by zhaoping on 2017/4/20.
 */

public interface OptimizePrintContract {
    interface ServiceCallback {
        /**
         * @param list
         */
        void onGetPrintDataResponse(List<PrinterE> list);

        /**
         * 获取打印数据失败
         */
        void onGetPrintDataResponseError();
    }

    interface Presenter extends IPresenter {
        /**
         * 获取打印数据
         */
        void getPrintData();

        /**
         * 获取本地未打印成功数据
         */
        void getLocalPrintData();
    }

}
