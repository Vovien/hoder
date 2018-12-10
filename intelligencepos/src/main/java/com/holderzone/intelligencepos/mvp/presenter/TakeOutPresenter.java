package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.TakeOutContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrder;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by LT on 2018-04-02.
 */

public class TakeOutPresenter extends BasePresenter<TakeOutContract.View> implements TakeOutContract.Presenter {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public TakeOutPresenter(TakeOutContract.View view) {
        super(view);
    }

    @Override
    public void requestUnOrderAndPlatformStatistic() {
        UnOrderE untreatedMessageUnOrderE = new UnOrderE();
        untreatedMessageUnOrderE.setMsgType("-1");
        untreatedMessageUnOrderE.setOrderType(-1);
        untreatedMessageUnOrderE.setOrderSubType(-1);
        XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.UntreatedMessageList)
                .setRequestBody(untreatedMessageUnOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        List<UnOrderE> arrayOfUnOrderE = xmlData.getArrayOfUnOrderE();
                        mView.onRequestUnOrderAndPlatformStatisticSucceed(arrayOfUnOrderE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onRequestUnOrderAndPlatformStatisticFailed();
                    }
                });


    }

    @Override
    public void disposeUnOrderAndPlatformStatistic() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
            mView.onRequestUnOrderAndPlatformStatisticDisposed();
        }
    }

    @Override
    public void submitConfirmOrder(String unOrderGUID, String unOrderReceiveMsgGUID, Integer isSalesOrder) {
        mRepository.getUsers()
                .flatMap(users -> {
                    UnOrderE unOrderE = new UnOrderE();
                    unOrderE.setUnOrderGUID(unOrderGUID);
                    unOrderE.setUnOrderReceiveMsgGUID(unOrderReceiveMsgGUID);
                    unOrderE.setUserGUID(users.getUsersGUID());
                    unOrderE.setIsSalesOrder(isSalesOrder);
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.ConfirmOrder)
                            .setRequestBody(unOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSubmitConfirmOrderSucceed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSubmitConfirmOrderFailed();
                    }
                });
    }

    @Override
    public void submitConfirmOrderThenEnterDetails(String unOrderGUID, String unOrderReceiveMsgGUID, Integer isSalesOrder) {
        mRepository.getUsers()
                .flatMap(users -> {
                    UnOrderE unOrderE = new UnOrderE();
                    unOrderE.setUnOrderGUID(unOrderGUID);
                    unOrderE.setUnOrderReceiveMsgGUID(unOrderReceiveMsgGUID);
                    unOrderE.setUserGUID(users.getUsersGUID());
                    unOrderE.setIsSalesOrder(isSalesOrder);
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.ConfirmOrder)
                            .setRequestBody(unOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSubmitConfirmOrderThenEnterDetailsSucceed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSubmitConfirmOrderThenEnterDetailsFailed();
                    }
                });
    }

    @Override
    public void submitRejectOrder(String unOrderGUID, String unOrderReceiveMsgGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    UnOrderE unOrderE = new UnOrderE();
                    unOrderE.setUnOrderGUID(unOrderGUID);
                    unOrderE.setUnOrderReceiveMsgGUID(unOrderReceiveMsgGUID);
                    unOrderE.setUserGUID(users.getUsersGUID());
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.RefuseOrder)
                            .setRequestBody(unOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSubmitRejectOrderSucceed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSubmitRejectOrderFailed();
                    }
                });
    }

    @Override
    public void submitConfirmChargeBack(String unOrderGUID, String unOrderReceiveMsgGUID, boolean isCheck) {
        mRepository.getUsers()
                .flatMap(users -> {
                    UnOrderE unOrderE = new UnOrderE();
                    unOrderE.setUnOrderGUID(unOrderGUID);
                    unOrderE.setUnOrderReceiveMsgGUID(unOrderReceiveMsgGUID);
                    unOrderE.setUserGUID(users.getUsersGUID());
                    unOrderE.setCheckMake(isCheck);
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.ConfirmChargeback)
                            .setRequestBody(unOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        int noteCode = xmlData.getApiNote().getNoteCode();
                        if (noteCode == 1) {
                            mView.onSubmitConfirmChargeBackSucceed();
                        } else if (noteCode == -4 && xmlData.getApiNote().getResultCode() == -110) {
                            //菜品已经出堂
                            mView.onConfirmReturnDishes();
                        } else {
                            mView.onSubmitConfirmChargeBackFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSubmitConfirmChargeBackFailed();
                    }
                });
    }

    @Override
    public void submitRefuseChargeBack(String unOrderGUID, String unOrderReceiveMsgGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    UnOrderE unOrderE = new UnOrderE();
                    unOrderE.setUnOrderGUID(unOrderGUID);
                    unOrderE.setUnOrderReceiveMsgGUID(unOrderReceiveMsgGUID);
                    unOrderE.setUserGUID(users.getUsersGUID());
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.RefuseChargeback)
                            .setRequestBody(unOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSubmitRefuseChargeBackSucceed();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onSubmitRefuseChargeBackFailed();
                    }
                });
    }

    @Override
    public void confirmOrder(UnOrderE order) {
        mRepository.getUsers()
                .flatMap(users -> {
                    order.setUserGUID(users.getUsersGUID());
                    order.setIsSalesOrder(1);
                    return XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.ConfirmOrder)
                            .setRequestBody(order).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        int noteCode = xmlData.getApiNote().getNoteCode();
                        if (noteCode == 1) {
                            mView.onConfirmOrderSuccess();
                        } else if (noteCode == -4 && xmlData.getApiNote().getResultCode() == -50) {
                            //超过估清
                            mView.addDishesEstimateFailed();
                        } else if (noteCode == -4 && xmlData.getApiNote().getResultCode() == -60) {
                            //没有菜品
                            mView.onConfirmOrderFailedInnerNoDish();
                        } else if (noteCode == -4) {
                            mView.showMessage(xmlData.getApiNote().getResultMsg());
                            mView.onConfirmOrderFailed();
                        } else {
                            mView.showMessage(xmlData.getApiNote().getNoteMsg());
                            mView.onConfirmOrderFailed();
                        }
                    }
                });
    }
}
