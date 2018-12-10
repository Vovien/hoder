package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsMemCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.CardsE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by zhaoping on 2017/3/31.
 */

public class BalanceAccountsMemCardPresenter extends BasePresenter<BalanceAccountsMemCardContract.View> implements BalanceAccountsMemCardContract.Presenter {
    public BalanceAccountsMemCardPresenter(BalanceAccountsMemCardContract.View view) {
        super(view);
    }

    @Override
    public void getCardListByMember(String memberInfoGUID) {
        CardsE cardsE = new CardsE();
        cardsE.setMemberInfoGUID(memberInfoGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.CardsB.GetListByMember)
                .setRequestBody(cardsE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onGetCardListSuccess(xmlData.getArrayOfCardsE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkErrorLayout();
                        }
                    }
                });
    }

    @Override
    public void vipCardDiscount(String salesOrderGUID, String cardsChipNo) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setCardsChipNo(cardsChipNo);
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.VipCardDiscount)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onVipCardDiscountSuccess();
                    }
                });
    }
}
