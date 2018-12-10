package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SnackDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by chencao on 2017/8/2.
 */

public class SnackDishesPresenter extends BasePresenter<SnackDishesContract.View> implements SnackDishesContract.Presenter {
    public SnackDishesPresenter(SnackDishesContract.View view) {
        super(view);
    }

    @Override
    public void getListNotCheckOut() {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setTradeMode(1);
        salesOrderE.setReturnEntityArray(0);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetListNotCheckOut)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView, false, false))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onGetListNotCheckOutSuccess(xmlData.getSalesOrderE().getArrayCount());
                    }
                });
    }

    @Override
    public void getSystemConfig() {
        mRepository.getSystemConfig().subscribe(mView::onGetSystemConfigSuccess);
    }
}
