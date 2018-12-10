package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SplitTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DinnerE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SubSalesOrder;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.jakewharton.rxbinding2.internal.Preconditions;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by tcw on 2017/7/4.
 */
public class SplitTablePresenter extends BasePresenter<SplitTableContract.View> implements SplitTableContract.Presenter {

    public SplitTablePresenter(SplitTableContract.View view) {
        super(view);
    }

    @Override
    public void requestTable() {
        DinnerE dinnerE = new DinnerE();
        dinnerE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
        dinnerE.setReturnDiningTable(1);
        dinnerE.setMd5Hash16(null);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DinnerB.RefreshInfo)
                .setRequestBody(dinnerE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        DinnerE dinnerE = xmlData.getDinnerE();
                        Preconditions.checkNotNull(dinnerE, "dinnerE==null");
                        mView.onRequestTableSucceed(dinnerE.getArrayOfDiningTableE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onRequestTableFailed();
                        }
                    }
                });
    }

    @Override
    public void submitSplitTable(List<SubSalesOrder> arrayOfSubSalesOrder) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setArrayOfSubSalesOrder(arrayOfSubSalesOrder);
                    salesOrderE.setStaffGUID(users.getUsersGUID());
                    salesOrderE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Split)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        // 拆单成功
                        mView.onSplitSucceed();
                    }
                });
    }
}