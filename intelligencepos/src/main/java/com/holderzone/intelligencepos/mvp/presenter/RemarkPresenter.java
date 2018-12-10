package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.RemarkContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by LiTao on 2017-8-2.
 */

public class RemarkPresenter extends BasePresenter<RemarkContract.View> implements RemarkContract.Presenter {
    public RemarkPresenter(RemarkContract.View view) {
        super(view);
    }

    @Override
    public void requestDishesRemark() {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesRemarkB.GetList)
                .setRequestBody(new DishesRemarkE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.getDishesRemarkSuccess(xmlData.getArrayOfDishesRemarkE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.getDishesRemarkFiled();
                    }
                });
    }

    @Override
    public void addDishesRemark(DishesRemarkE dishesRemarkE) {
        mRepository.getUsers()
                .flatMap(users -> {
                    dishesRemarkE.setStoreGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.DishesRemarkB.Insert)
                            .setRequestBody(dishesRemarkE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onAddDishesRemarkSucceed(xmlData.getApiNote().getNoteMsg()
                                , xmlData.getDishesRemarkE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onAddDishesRemarkFailed();
                    }
                });
    }
}
