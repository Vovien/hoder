package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.OpenTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by tcw on 2017/9/5.
 */
public class OpenTablePresenter extends BasePresenter<OpenTableContract.View> implements OpenTableContract.Presenter {

    public OpenTablePresenter(OpenTableContract.View view) {
        super(view);
    }

    @Override
    public void submitOpenTable(String diningTableGUID, int guestCount, String orderRecordGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setGuestCount(guestCount);
                    salesOrderE.setCreateStaffGUID(users.getUsersGUID());
                    salesOrderE.setReceptionStaffGUID(users.getUsersGUID());
                    salesOrderE.setTradeMode(0);
                    salesOrderE.setDiningTableGUID(diningTableGUID);
                    salesOrderE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    if (orderRecordGUID != null) {
                        salesOrderE.setOrderRecordGUID(orderRecordGUID);
                    }
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Create)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE salesOrderE = xmlData.getSalesOrderE();
                        String salesOrderGUID = salesOrderE.getSalesOrderGUID();
                        mView.onSalesOrderCreated(salesOrderGUID);
                    }
                });
    }

    @Override
    public void submitOpenTableThenOrderDishes(String diningTableGUID, int guestCount, String orderRecordGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setGuestCount(guestCount);
                    salesOrderE.setCreateStaffGUID(users.getUsersGUID());
                    salesOrderE.setReceptionStaffGUID(users.getUsersGUID());
                    salesOrderE.setTradeMode(0);
                    salesOrderE.setDiningTableGUID(diningTableGUID);
                    salesOrderE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    if (orderRecordGUID != null) {
                        salesOrderE.setOrderRecordGUID(orderRecordGUID);
                    }
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Create)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE salesOrderE = xmlData.getSalesOrderE();
                        String salesOrderGUID = salesOrderE.getSalesOrderGUID();
                        mView.onSalesOrderCreatedThenOrderDishes(salesOrderGUID);
                    }
                });
    }
}