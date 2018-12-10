package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SnackOrderContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by LiTao on 2017-8-3.
 */

public class SnackOrderPresenter extends BasePresenter<SnackOrderContract.View> implements SnackOrderContract.Presenter {
    public SnackOrderPresenter(SnackOrderContract.View view) {
        super(view);
    }

    @Override
    public void getListNotCheckOut() {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setTradeMode(1);
        salesOrderE.setReturnEntityArray(1);
        XmlData.Builder().setRequestMethod(RequestMethod.SalesOrderB.GetListNotCheckOut)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onGetOrderListSuccess(xmlData.getArrayOfSalesOrderE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onGetOrderListFiled();
                    }
                });
    }
}
