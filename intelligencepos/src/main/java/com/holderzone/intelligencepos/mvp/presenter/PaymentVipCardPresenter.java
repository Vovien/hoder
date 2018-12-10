package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentVipCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.ApiNote;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.disposables.Disposable;

/**
 * 会员卡支付
 * Created by tcw on 2017/5/31.
 */

public class PaymentVipCardPresenter extends BasePresenter<PaymentVipCardContract.View> implements PaymentVipCardContract.Presenter {
    public PaymentVipCardPresenter(PaymentVipCardContract.View view) {
        super(view);
    }

    @Override
    public void requestLoginAndGetCardsByMember(String salesOrderGUID, String regTel, String memberInfoGUID) {
        CardsE cardsE = new CardsE();
        cardsE.setRegTel(regTel);
        cardsE.setMemberInfoGUID(memberInfoGUID);
        cardsE.setSalesOrderGUID(salesOrderGUID);
        cardsE.setUseBusinessType(1);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.CardsB.GetListByMemberUse)
                .setRequestBody(cardsE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        // 获取会员信息成功
                        mView.onLoginAndGetCardsSuccess(xmlData.getArrayOfCardsE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            // 获取会员信息失败，除去toast外额外的操作可放在这里面执行
                            mView.onLoginAndGetCardsFailed();
                        }
                    }
                });
    }

    @Override
    public void requestCardDiscountDetails(CardsE cardsE) {

        XmlData.Builder()
                .setRequestMethod(RequestMethod.CardsB.CardsBCheckAmount)
                .setRequestBody(cardsE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        ApiNote apiNote = xmlData.getApiNote();
                        Integer resultCode = apiNote.getResultCode();
                        if (resultCode == 1) {
                            mView.readDiscountDetailsSuccess(xmlData.getCardsE());
                        } else {
                            mView.readDiscountDetailsFailed();
                        }
                    }

                });
    }

}
