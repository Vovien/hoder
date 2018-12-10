package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.ChooseStoreContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.StoreE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * 选择门店
 * Created by tcw on 2017/6/6.
 */

public class ChooseStorePresenter extends BasePresenter<ChooseStoreContract.View> implements ChooseStoreContract.Presenter {

    public ChooseStorePresenter(ChooseStoreContract.View view) {
        super(view);
    }

    @Override
    public void requestStoreEList() {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.StoreB.GetList)
                .setRequestBody(new StoreE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onStoreEListObtainSucceed(xmlData.getArrayOfStoreE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        }
                    }
                });
    }

    @Override
    public void saveSelectedStoreThenReturn(Store store) {
        mRepository.saveStore(store)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(mView::onSaveSelectedStoreSucceedThenReturn);
    }

    @Override
    public void saveSelectedStoreThenLaunch(Store store) {
        mRepository.saveStore(store)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(mView::onSaveSelectedStoreSucceedThenLaunch);
    }
}
