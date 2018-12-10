package com.holderzone.intelligencepos.mvp.presenter;

import android.net.ParseException;
import android.os.NetworkOnMainThreadException;
import android.util.Pair;

import com.google.gson.JsonParseException;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.QueueContract;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import org.json.JSONException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 排队 Presenter
 */
public class QueuePresenter extends BasePresenter<QueueContract.View> implements QueueContract.Presenter {

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public QueuePresenter(QueueContract.View view) {
        super(view);
    }

    /**
     * 获取排队类型数据
     *
     * @return 获取排队类型数据的Observable
     */
    private Observable<XmlData> getQueueUpType() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.QueueUpTypeB.GetList)
                .setRequestBody(new QueueUpTypeE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    /**
     * 获取排队记录数据
     *
     * @return 获取排队记录数据的Observable
     */
    private Observable<XmlData> getQueueUpRecord() {
        return XmlData.Builder()
                .setRequestMethod(RequestMethod.QueueUpRecordB.GetList)
                .setRequestBody(new QueueUpRecordE()).buildRESTful()
                .flatMap(mRepository::getXmlData);
    }

    @Override
    public void getQueueUpInfo() {
        Observable.zip(getQueueUpType(), getQueueUpRecord(), (xmlData, xmlData2) -> new Pair(xmlData, xmlData2))
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new Observer<Pair>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(Pair pair) {
                        XmlData queueUpType = (XmlData) pair.first;
                        XmlData queueUpRecord = (XmlData) pair.second;
                        if (ApiNoteHelper.checkBusiness(queueUpType) && ApiNoteHelper.checkBusiness(queueUpRecord)) {
                            mView.onGetQueueUpInfoSucceed(queueUpType.getArrayOfQueueUpTypeE(), queueUpRecord.getArrayOfQueueUpRecordE());
                        } else if (ApiNoteHelper.checkAuthorizationWhenBusinessFailed(queueUpType) || ApiNoteHelper.checkAuthorizationWhenBusinessFailed(queueUpRecord)) {
                            String msg = ApiNoteHelper.obtainErrorMsg(queueUpType);
                            mView.showAuthorizationDialog(msg != null ? msg : ApiNoteHelper.obtainErrorMsg(queueUpRecord));
                        } else {
                            String msg = ApiNoteHelper.obtainErrorMsg(queueUpType);
                            mView.showMessage(msg != null ? msg : ApiNoteHelper.obtainErrorMsg(queueUpRecord));
                            mView.onGetQueueUpInfoFailed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showMessage("网络异常");
                            mView.onNetWorkError();
                            return;
                        } else if (e instanceof JsonParseException
                                || e instanceof JSONException
                                || e instanceof ParseException) {
                            mView.showMessage("解析失败");
                        } else if (e instanceof NullPointerException) {
                            mView.showMessage("空指针异常");
                        } else if (e instanceof NetworkOnMainThreadException) {
                            mView.showMessage("主线程耗时操作");
                        } else {
                            mView.showMessage("未知错误");
                        }
                        mView.onGetQueueUpInfoFailed();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void disposeQueueUpInfo() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
            mView.onQueueUpInfoDisposed();
        }
    }

    @Override
    public void submitCall(String queueUpRecordGUID) {
        QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
        queueUpRecordE.setQueueUpRecordGUID(queueUpRecordGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.QueueUpRecordB.Call)
                .setRequestBody(queueUpRecordE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onCallSuccess();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onCallFailed();
                    }
                });
    }

    @Override
    public void submitUpdateCustomerCount(String queueUpRecordGUID, int customerCount) {
        QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
        queueUpRecordE.setQueueUpRecordGUID(queueUpRecordGUID);
        queueUpRecordE.setCustomerCount(customerCount);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.QueueUpRecordB.UpdateCustomerCount)
                .setRequestBody(queueUpRecordE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onUpdateCustomerCountSuccess();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onUpdateCustomerCountFailed();
                    }
                });
    }

    @Override
    public void submitConfirm(String queueUpRecordGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
                    queueUpRecordE.setQueueUpRecordGUID(queueUpRecordGUID);
                    queueUpRecordE.setFinishUsersGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.QueueUpRecordB.Confirm)
                            .setRequestBody(queueUpRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onConfirmSuccess();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onConfirmFailed();
                    }
                });
    }

    @Override
    public void submitSkip(String queueUpRecordGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
                    queueUpRecordE.setQueueUpRecordGUID(queueUpRecordGUID);
                    queueUpRecordE.setFinishUsersGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.QueueUpRecordB.Skip)
                            .setRequestBody(queueUpRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onSkipSuccess();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onSkipFailed();
                    }
                });
    }

    @Override
    public void submitRecover(String queueUpRecordGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
                    queueUpRecordE.setQueueUpRecordGUID(queueUpRecordGUID);
                    queueUpRecordE.setFinishUsersGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.QueueUpRecordB.Recover)
                            .setRequestBody(queueUpRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onRecoverSuccess();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onRecoverFailed();
                    }
                });
    }

    @Override
    public void submitDelete(String queueUpRecordGUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    QueueUpRecordE queueUpRecordE = new QueueUpRecordE();
                    queueUpRecordE.setQueueUpRecordGUID(queueUpRecordGUID);
                    queueUpRecordE.setFinishUsersGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.QueueUpRecordB.Delete)
                            .setRequestBody(queueUpRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onDeleteSuccess();
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onDeleteFailed();
                    }
                });
    }
}
