package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.AddQueueContract;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * 新增排队 Presenter
 */
public class AddQueuePresenter extends BasePresenter<AddQueueContract.View> implements AddQueueContract.Presenter {
    public AddQueuePresenter(AddQueueContract.View view) {
        super(view);
    }

    @Override
    public void submitAdd(int customerCount, String customerTel) {
        mRepository.getUsers()
                .flatMap(users -> {
                    QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
                    queueUpRecordE.setCustomerCount(customerCount);
                    queueUpRecordE.setCustomerTel(customerTel);
                    queueUpRecordE.setCreateUsersGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.QueueUpRecordB.Add)
                            .setRequestBody(queueUpRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(ApiNoteHelper.applyCheckWithPrinter(mView))
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (ApiNoteHelper.checkBusiness(xmlData)) {
                            mView.onAddSuccess();
                        } else if (ApiNoteHelper.checkPrinterWhenBusinessSuccess(xmlData)) {
                            mView.showMessage(ApiNoteHelper.obtainPrinterMsg(xmlData));
                            mView.onAddSuccess();
                        } else {
                            onError(new ApiException(xmlData));
                        }
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onAddFailed();
                    }
                });
    }
}
