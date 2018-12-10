package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.TakeawayBillDetailContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by LT on 2018-04-04.
 */

public class TakeawayBillDetailPresenter extends BasePresenter<TakeawayBillDetailContract.View> implements TakeawayBillDetailContract.Presenter {
    public TakeawayBillDetailPresenter(TakeawayBillDetailContract.View view) {
        super(view);
    }

    @Override
    public void getOrderDetail(String orderGuid) {
        UnOrderE unOrderE = new UnOrderE();
        unOrderE.setUnOrderGUID(orderGuid);
        unOrderE.setReturnDishes(1);
        XmlData.Builder().setRequestMethod(RequestMethod.UnOrderB.GetSingle)
                .setRequestBody(unOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        int noteCode = xmlData.getApiNote().getNoteCode();
                        if (1 == noteCode) {
                            mView.onGetOrderSuccess(xmlData.getUnOrderE());
                        } else if (noteCode == -4) {
                            mView.showMessage(xmlData.getApiNote().getResultMsg());
                        } else {
                            mView.showMessage(xmlData.getApiNote().getNoteMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.showNetworkError();
                    }
                });

    }

    @Override
    public void confirmOrder(UnOrderE order) {
        mRepository.getUsers()
                .flatMap(users -> {
                    order.setUserGUID(users.getUsersGUID());
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
}
