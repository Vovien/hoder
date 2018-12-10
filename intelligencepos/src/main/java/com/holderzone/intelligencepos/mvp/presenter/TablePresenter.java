package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.TableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DinnerE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.jakewharton.rxbinding2.internal.Preconditions;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by tcw on 2017/9/4.
 */
public class TablePresenter extends BasePresenter<TableContract.View> implements TableContract.Presenter {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public TablePresenter(TableContract.View view) {
        super(view);
    }

    @Override
    public void requestMainGUIRefreshInfo(String md5Hash16) {
        mCompositeDisposable.clear();
        DinnerE dinnerE = new DinnerE();
        dinnerE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
        dinnerE.setReturnTableArea(1);
        dinnerE.setReturnDiningTable(1);
        dinnerE.setReturnTableStatus(0);
        dinnerE.setReturnSalesMsg(0);
        dinnerE.setMd5Hash16(md5Hash16);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.DinnerB.RefreshInfo)
                .setRequestBody(dinnerE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        DinnerE dinnerE = xmlData.getDinnerE();
                        Preconditions.checkNotNull(dinnerE, "dinnerE==null");
                        mView.refreshMainGui(
                                dinnerE.getArrayOfDiningTableAreaE(),
                                dinnerE.getArrayOfDiningTableE(), dinnerE.getMd5Hash16());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onRequestMainGUIRefreshInfoFailed();
                        }
                    }
                });
    }

    @Override
    public void disposeMainGUIRefreshInfo() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }
}