package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.RetreatReasonSettingContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.jakewharton.rxbinding2.internal.Preconditions;

import java.util.List;

/**
 * Created by LiTao on 2017-9-7.
 */

public class RetreatReasonSettingPresenter extends BasePresenter<RetreatReasonSettingContract.View> implements RetreatReasonSettingContract.Presenter {
    public RetreatReasonSettingPresenter(RetreatReasonSettingContract.View view) {
        super(view);
    }

    @Override
    public void requestRetreatReasons() {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesReturnReasonB.GetList)
                .setRequestBody(new DishesReturnReasonE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        List<DishesReturnReasonE> arrayOfDishesReturnReasonE = xmlData.getArrayOfDishesReturnReasonE();
                        Preconditions.checkNotNull(arrayOfDishesReturnReasonE, "arrayOfDishesReturnReasonE==null");
                        mView.onRequestRetreatReasonsSucceed(arrayOfDishesReturnReasonE);
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onRequestRetreatReasonsFailed();
                        }
                    }
                });
    }

    @Override
    public void addNewRetreatReason(String nameOfRetreatReason) {
        DishesReturnReasonE dishesReturnReasonE = new DishesReturnReasonE();
        dishesReturnReasonE.setName(nameOfRetreatReason);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesReturnReasonB.Insert)
                .setRequestBody(dishesReturnReasonE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onAddNewRetreatReasonSucceed(ApiNoteHelper.obtainSuccessMsg(xmlData), xmlData.getDishesReturnReasonE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onAddNewRetreatReasonFailed(nameOfRetreatReason);
                    }
                });
    }

    @Override
    public void deleteRetreatReason(List<DishesReturnReasonE> arrayOfDishesReturnReasonE) {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesReturnReasonB.BatchDelete)
                .buildRESTful()
                .flatMap(xmlData -> {
                    xmlData.setArrayOfDishesReturnReasonE(arrayOfDishesReturnReasonE);
                    return mRepository.getXmlData(xmlData);
                })
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onDeleteRetreatReasonSucceed();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onDeleteRetreatReasonFailed();
                    }
                });
    }
}
