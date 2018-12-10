package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BillRecordContract;
import com.holderzone.intelligencepos.mvp.model.bean.PageInfo;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chencao on 2017/6/5.
 */

public class BillRecordPresenter extends BasePresenter<BillRecordContract.View> implements BillRecordContract.Presenter {

    public BillRecordPresenter(BillRecordContract.View view) {
        super(view);
    }

    @Override
    public void requestLocalAccountRecord() {
        mRepository.getAccountRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(accountRecord -> mView.onRequestLocalAccountRecordSucceed(accountRecord));
    }

    @Override
    public void setRequestBillRecodeList(int currentPage) {
        mRepository.getAccountRecord()
                .flatMap(accountRecord -> {
                    SalesOrderE salesOrderE = new SalesOrderE();
                    salesOrderE.setBusinessDay(accountRecord.getBusinessDay());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.GetListByBusinessDay)
                            .setRequestBody(salesOrderE).buildRESTful()
                            .flatMap(xmlData -> {
                                PageInfo page = new PageInfo();
                                page.setCurrentPage(currentPage);
                                page.setPageRowCount(15);
                                xmlData.setPageInfo(page);
                                return mRepository.getXmlData(xmlData);
                            });
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView,false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (xmlData.getApiNote().getNoteCode() == -101) {
                            mView.showAuthorizationDialog("授权过期");
                        } else if (xmlData.getApiNote().getNoteCode() != 1) {
                            if ( xmlData.getApiNote().getResultMsg() != null){
                                mView.getError( xmlData.getApiNote().getResultMsg());
                            }else {
                                mView.getError(xmlData.getApiNote().getNoteMsg());
                            }
                        } else {
                            mView.getBillRecodeList(xmlData.getArrayOfSalesOrderE(),xmlData.getPageInfo().getTotalRowCount());
                        }
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
}
