package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.MergeTableContract;
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

/**
 * Created by tcw on 2017/7/4.
 */
public class MergeTablePresenter extends BasePresenter<MergeTableContract.View> implements MergeTableContract.Presenter {

    public MergeTablePresenter(MergeTableContract.View view) {
        super(view);
    }

    @Override
    public void requestTableCouldBeMerge(String salesOrderGUID) {
        DinnerE dinnerE = new DinnerE();
        dinnerE.setSalesOrderGUID(salesOrderGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DinnerB.GetCanMergeTable)
                .setRequestBody(dinnerE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    protected void next(XmlData xmlData) {
                        DinnerE dinnerE = xmlData.getDinnerE();
                        Preconditions.checkNotNull(dinnerE, "dinnerE==null");
                        mView.refreshMainGui(
                                dinnerE.getArrayOfDiningTableAreaE(),
                                dinnerE.getArrayOfDiningTableE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onRequestTableCouldBeMergeFailed();
                        }
                    }
                });
    }

    @Override
    public void submitMergeTable(String salesOrderGUID, List<SubSalesOrder> arrayOfSubSalesOrder) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderE.setArrayOfSubSalesOrder(arrayOfSubSalesOrder);
                    salesOrderE.setStaffGUID(users.getUsersGUID());
                    salesOrderE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Merge)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onMergeTableSucceed();
                    }
                });
    }
}