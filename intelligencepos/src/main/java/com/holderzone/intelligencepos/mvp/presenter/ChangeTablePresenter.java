package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.ChangeTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DinnerE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.jakewharton.rxbinding2.internal.Preconditions;

import io.reactivex.disposables.Disposable;

/**
 * Created by tcw on 2017/7/4.
 */
public class ChangeTablePresenter extends BasePresenter<ChangeTableContract.View> implements ChangeTableContract.Presenter {

    public ChangeTablePresenter(ChangeTableContract.View view) {
        super(view);
    }

    @Override
    public void requestTableCouldBeChange() {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DinnerB.GetCanExchangeTable)
                .setRequestBody(new DinnerE()).buildRESTful()
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
                            mView.onRequestTableCouldBeChangeFailed();
                        }
                    }
                });
    }

    @Override
    public void submitChangeTable(String salesOrderGUID, String orgDiningTableGUID, String newDiningTableGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderE.setOrgDiningTableGUID(orgDiningTableGUID);
                    salesOrderE.setNewDiningTableGUID(newDiningTableGUID);
                    salesOrderE.setStaffGUID(users.getUsersGUID());
                    salesOrderE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.ChangeTable)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onChangeTableSucceed();
                    }
                });
    }
}