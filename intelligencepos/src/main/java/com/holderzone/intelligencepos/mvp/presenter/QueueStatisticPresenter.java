package com.holderzone.intelligencepos.mvp.presenter;

import android.annotation.SuppressLint;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.QueueStatisticContract;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 排队统计 Presenter
 */
public class QueueStatisticPresenter extends BasePresenter<QueueStatisticContract.View> implements QueueStatisticContract.Presenter {
    public QueueStatisticPresenter(QueueStatisticContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void requestLocalAccountRecord() {
        mRepository.getAccountRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindToLifecycle(mView))
                .subscribe(mView::onRequestLocalAccountRecordSucceed);
    }

    @Override
    public void getStatisticalReport(String businessDay) {
        QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
        queueUpRecordE.setBusinessDay(businessDay);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.QueueUpRecordB.GetStatisticalReport)
                .setRequestBody(queueUpRecordE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onGetStatisticalReportSuccess(xmlData.getArrayOfQueueUpReportE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onGetStatisticalReportFailed();
                        }
                    }
                });
    }
}
