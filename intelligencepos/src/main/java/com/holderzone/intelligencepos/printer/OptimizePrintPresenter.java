package com.holderzone.intelligencepos.printer;

import com.blankj.utilcode.util.EmptyUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.Repository;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.RealmUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.holderzone.intelligencepos.printer.PushPrintService.PUSH_THREAD_POOL;

/**
 * Created by zhaoping on 2017/4/7.
 */

public class OptimizePrintPresenter implements OptimizePrintContract.Presenter {

    private CompositeDisposable mDisposables;
    protected Repository mRepository;

    public OptimizePrintPresenter(OptimizePrintContract.ServiceCallback callback) {
        mRepository = RepositoryImpl.getInstance();
        mServiceCallback = callback;
        onCreate();
    }

    public void dispose(Disposable disposable) {
        if (mDisposables != null) {
            mDisposables.delete(disposable);
        }
    }

    //取消所有的订阅
    public void dispose() {
        if (mDisposables != null) {
            mDisposables.clear();
        }
    }

    protected void addSubscription(Disposable disposable) {
        if (disposable == null) {
            return;
        }
        if (mDisposables == null) {
            mDisposables = new CompositeDisposable();
        }
        mDisposables.add(disposable);
    }

    protected final String TAG = this.getClass().getSimpleName();

    protected OptimizePrintContract.ServiceCallback mServiceCallback;

    public OptimizePrintPresenter() {
    }

    @Override
    public void onDestroy() {
        dispose();
    }

    protected void onCreate() {
    }

    @Override
    public void getPrintData() {
        Observable.<String>create(e -> {
            Realm realm = Realm.getDefaultInstance();
            try {
                RealmResults<PushPrintBean> realmResults = realm.where(PushPrintBean.class).equalTo("MsgType", 0).equalTo("hasObtain", false).findAll();
                StringBuilder stringBuilder = new StringBuilder();
                realm.beginTransaction();
                for (int i = 0; i < realmResults.size(); i++) {
                    PushPrintBean pushPrintBean = realmResults.get(i);
                    if (pushPrintBean.isHasObtain() == false) {
                        stringBuilder.append(pushPrintBean.getKey()).append(",");
                    }

                    if (i == 10) {
                        break;
                    }
                }
                if (realmResults.size() > 11) {
                    PushPrintService.setNeedNextRequest();
                }
                String key = stringBuilder.length() == 0 ? "" : stringBuilder.substring(0, stringBuilder.length() - 1);
                realm.commitTransaction();
                e.onNext(key);
                e.onComplete();
            } finally {
                realm.close();
                Realm.compactRealm(RealmUtil.getPushConfig());
            }
        }).subscribeOn(Schedulers.from(PUSH_THREAD_POOL)).observeOn(Schedulers.io()).flatMap(new Function<String, ObservableSource<PushPrintResponse>>() {
            @Override
            public ObservableSource<PushPrintResponse> apply(String key) throws Exception {
                if (EmptyUtils.isEmpty(key)) {
                    PushPrintResponse optimizePrintResponse = new PushPrintResponse();
                    optimizePrintResponse.setSuccess(1);
                    return Observable.<PushPrintResponse>just(optimizePrintResponse);
                }
                return mRepository.getPrintData(key).doOnNext(response -> {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        if (response.getSuccess() == 1 && EmptyUtils.isNotEmpty(response.getData().getNot())) {
                            for (String s : response.getData().getNot()) {
                                PushPrintBean bean = realm.where(PushPrintBean.class).equalTo("Key", s).findFirst();
                                if (bean != null) {
                                    bean.setHasObtain(true);
                                }
                            }
                            BaseApplication.showMessage("有" + response.getData().getNot().size() + "条打印数据获取失败，请联系客户");
                        }
                        if (response.getSuccess() == 1 && EmptyUtils.isNotEmpty(response.getData().getList())) {
                            response.setPrintList(new Gson().fromJson(response.getData().getList().toString(), new TypeToken<List<PrinterE>>() {
                            }.getType()));
                            List<String> stringList = response.getData().getList();
                            List<PrinterE> printDataList = response.getPrintList();
                            long currentTimeMillis = System.currentTimeMillis();
                            for (int i = 0; i < stringList.size(); i++) {
                                PushPrintDataRecord record = new PushPrintDataRecord();
                                PrinterE printerE = printDataList.get(i);
                                printerE.setPullPrintDataTimeStamp(currentTimeMillis);
                                PushPrintBean bean = realm.where(PushPrintBean.class).equalTo("Key", printerE.getPrintKey()).findFirst();
                                if (bean != null) {
                                    record.setPullDataElapsedTime(currentTimeMillis - bean.getInsertTimestamp());
                                    bean.setHasObtain(true);
                                }
                                record.setKey(printerE.getPrintKey());
                                record.setObtainDataMillisecond(currentTimeMillis);
                                record.setPrintDataJson(stringList.get(i));
                                record.setPrintTimes(printerE.getPrintTimes());
                                realm.insertOrUpdate(record);
                            }
                        }
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                        Realm.compactRealm(RealmUtil.getPushConfig());
                    }
                });
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new PrintDataObserver(this));
    }

    @Override
    public void getLocalPrintData() {
        Observable.<List<PrinterE>>create(e -> {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<PushPrintDataRecord> records;
            try {
                records = realm.where(PushPrintDataRecord.class).equalTo("isTimeout", false)
                        .equalTo("isPrintFinish", false).findAll();
                List<PrinterE> list = new ArrayList<>();
                if (EmptyUtils.isNotEmpty(records)) {
                    Gson gson = new Gson();
                    for (PushPrintDataRecord record : records) {
                        PrinterE printerE = gson.fromJson(record.getPrintDataJson(), PrinterE.class);
                        printerE.setPrintTimes(record.getPrintTimes() - (record.getPrintFinishTimes() == null ? 0 : record.getPrintFinishTimes()));
                        printerE.setPullPrintDataTimeStamp(record.getObtainDataMillisecond());
                        list.add(printerE);
                    }
                }
                e.onNext(list);
                e.onComplete();
            } finally {
                realm.close();
                Realm.compactRealm(RealmUtil.getPushConfig());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new LocalPrintDataObserver(this));
    }

    static class PrintDataObserver implements Observer<PushPrintResponse> {

        private WeakReference<OptimizePrintPresenter> weakReference;

        protected PrintDataObserver(OptimizePrintPresenter printPresenter) {
            weakReference = new WeakReference<>(printPresenter);
        }

        @Override
        public void onSubscribe(Disposable d) {
//            weakReference.get().addSubscription(d);
        }

        @Override
        public void onNext(PushPrintResponse response) {
            if (response.getSuccess() == 1) {
                if (response.getData() == null || response.getData().getList() == null) {
                    weakReference.get().mServiceCallback.onGetPrintDataResponse(null);
                } else {
                    if (EmptyUtils.isNotEmpty(response.getPrintList())) {
                        weakReference.get().mServiceCallback.onGetPrintDataResponse(response.getPrintList());
                    } else {
                        weakReference.get().mServiceCallback.onGetPrintDataResponse(null);
                    }
                }
            } else {
                weakReference.get().mServiceCallback.onGetPrintDataResponseError();
            }
            weakReference.clear();
        }

        @Override
        public void onError(Throwable e) {
            weakReference.get().mServiceCallback.onGetPrintDataResponseError();
            weakReference.clear();
        }

        @Override
        public void onComplete() {
        }
    }

    static class LocalPrintDataObserver implements Observer<List<PrinterE>> {

        private WeakReference<OptimizePrintPresenter> weakReference;

        protected LocalPrintDataObserver(OptimizePrintPresenter printPresenter) {
            weakReference = new WeakReference<>(printPresenter);
        }

        @Override
        public void onSubscribe(Disposable d) {
//            weakReference.get().addSubscription(d);
        }

        @Override
        public void onNext(List<PrinterE> list) {
            weakReference.get().mServiceCallback.onGetPrintDataResponse(list);
            weakReference.clear();
        }

        @Override
        public void onError(Throwable e) {
            weakReference.get().mServiceCallback.onGetPrintDataResponseError();
            weakReference.clear();
        }

        @Override
        public void onComplete() {
        }
    }
}
