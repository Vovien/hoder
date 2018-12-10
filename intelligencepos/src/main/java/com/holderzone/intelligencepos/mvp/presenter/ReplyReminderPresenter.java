package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.ReplyReminderContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by LT on 2018-04-03.
 */

public class ReplyReminderPresenter extends BasePresenter<ReplyReminderContract.View> implements ReplyReminderContract.Presenter {
    public ReplyReminderPresenter(ReplyReminderContract.View view) {
        super(view);
    }

    @Override
    public void submitReplyReminder(String unOrderGUID, String unOrderReceiveMsgGUID, List<UnReminderReplyContentE> arrayOfUnReminderReplyContentE) {
        mRepository.getUsers()
                .flatMap(users -> {
                    UnOrderE unOrderE = new UnOrderE();
                    unOrderE.setUnOrderGUID(unOrderGUID);
                    unOrderE.setUnOrderReceiveMsgGUID(unOrderReceiveMsgGUID);
                    unOrderE.setUserGUID(users.getUsersGUID());
                    unOrderE.setArrayOfUnReminderReplyContentE(arrayOfUnReminderReplyContentE);
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.ReplyReminder)
                            .setRequestBody(unOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSubmitReplyReminderSucceed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSubmitReplyReminderFailed();
                    }
                });
    }

    @Override
    public void addNewReminderReplyContent(UnReminderReplyContentE unReminderReplyContentE) {
        XmlData.Builder().setRequestMethod(RequestMethod.UnReminderReplyContentB.Insert)
                .setRequestBody(unReminderReplyContentE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onAddNewReminderReplyContentSucceed(xmlData.getUnReminderReplyContentE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onAddNewReminderReplyContentFailed();
                    }
                });
    }

    @Override
    public void requestReminderReplyContent() {
        XmlData.Builder().setRequestMethod(RequestMethod.UnReminderReplyContentB.GetList)
                .setRequestBody(new UnReminderReplyContentE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onRequestReminderReplyContentSucceed(xmlData.getArrayOfUnReminderReplyContentE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onRequestReminderReplyContentFailed();
                    }
                });
    }
}
