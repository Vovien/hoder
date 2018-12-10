package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.RemarkSettingContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

/**
 * Created by LiTao on 2017-8-9.
 */

public class RemarkSettingPresenter extends BasePresenter<RemarkSettingContract.View> implements RemarkSettingContract.Presenter {

    public RemarkSettingPresenter(RemarkSettingContract.View view) {
        super(view);
    }

    @Override
    public void requestDishesRemark() {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesRemarkB.GetList)
                .setRequestBody(new DishesRemarkE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.getDishesRemarkSuccess(xmlData.getArrayOfDishesRemarkE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkError();
                        }
                    }
                });
    }

    @Override
    public void requestAddDishesRemark(String Name) {
        DishesRemarkE dishesRemarkE = new DishesRemarkE();
        dishesRemarkE.setName(Name);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesRemarkB.Insert)
                .setRequestBody(dishesRemarkE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.addRemarkSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.addDishesRemarkFiled("添加备注失败!", Name);
                    }
                });
    }

    @Override
    public void requestDeleteDishesRemark(List<DishesRemarkE> arrayOfDishesRemarkE) {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DishesRemarkB.BatchDelete).buildRESTful()
                .flatMap(xmlData -> {
                    xmlData.setArrayOfDishesRemarkE(arrayOfDishesRemarkE);
                    return mRepository.getXmlData(xmlData);
                })
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.deleteRemarkSuccess();
                    }
                });
    }
}
