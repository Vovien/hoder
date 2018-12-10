package com.holderzone.intelligencepos.printer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.RealmUtil;
import com.holderzone.intelligencepos.utils.print.AbsPrinter;
import com.holderzone.intelligencepos.utils.print.PrintException;
import com.holderzone.intelligencepos.utils.print.PrinterFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.holderzone.intelligencepos.base.Constants.PUSH_MESSAGE_SAVE_TIME_MILLIS;
import static com.holderzone.intelligencepos.base.Constants.SUNMI_LOCAL_PRINT_CHECK_EXCEPTION_TIME;
import static com.holderzone.intelligencepos.printer.PushPrintService.MESSAGE_TYPE_CHECK_LOCAL_PRINT;
import static com.holderzone.intelligencepos.printer.PushPrintService.MESSAGE_TYPE_LOCAL_PRINT_NOT_CONNECT;
import static com.holderzone.intelligencepos.printer.PushPrintService.MESSAGE_TYPE_LOCAL_PRINT_NOT_FIND;
import static com.holderzone.intelligencepos.printer.PushPrintService.MESSAGE_TYPE_SHOW_LOCT_PRINT_DIED_DIALOG;

/**
 * 图片加载类
 * http://blog.csdn.net/lmj623565791/article/details/41874561
 *
 * @author zhy
 */
public class PrintHandler {

    private DataEmptyListener dataEmptyListener;
    private static final int MESSAGE_TYPE_ADD_TASK = 0x110;
    private static PrintHandler mInstance;
    private static long last_clear_push_data_time;
    private static AtomicBoolean isFeedbacking = new AtomicBoolean(false);
    private static AtomicBoolean isNeedNextUpload = new AtomicBoolean(false);

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;
    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    /**
     * UI线程中的Handler
     */
    private Handler mUIHandler;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

    private static final String TAG = "PrintHandler";

    private PrinterFactory printerFactory = new PrinterFactory();
    private Long localPrintBeginTimeStamp = null;
    private String mCurrentPrintKey = null;
    public static final AtomicBoolean localPrintDied = new AtomicBoolean(false);

    public enum Type {
        FIFO, LIFO;
    }

    private PrintHandler(int threadCount, Type type) {
        init(threadCount, type);
    }

    /**
     * 初始化
     *
     * @param threadCount
     * @param type
     */
    private void init(int threadCount, Type type) {
        initBackThread();
        // 创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 初始化后台轮询线程
     */
    private void initBackThread() {
        // 后台轮询线程
        mPoolThread = new Thread("push_back_thread") {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        switch (msg.what) {
                            case MESSAGE_TYPE_ADD_TASK:
                                // 线程池去取出一个任务进行执行
                                mThreadPool.execute(getTask());
                                try {
                                    mSemaphoreThreadPool.acquire();
                                } catch (InterruptedException e) {
                                }
                                break;
                            default:
                                break;
                        }
                    }
                };
                // 释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();
    }

    public static PrintHandler getInstance() {
        if (mInstance == null) {
            synchronized (PrintHandler.class) {
                if (mInstance == null) {
                    mInstance = new PrintHandler(DEAFULT_THREAD_COUNT, Type.FIFO);
                }
            }
        }
        return mInstance;
    }

    public static PrintHandler getInstance(int threadCount, Type type) {
        if (mInstance == null) {
            synchronized (PrintHandler.class) {
                if (mInstance == null) {
                    mInstance = new PrintHandler(threadCount, type);
                }
            }
        }
        return mInstance;
    }

    /**
     * 根据path为imageview设置图片
     *
     * @param printerE
     */
    public void print(final PrinterE printerE, List<String> imgs) {
        if (mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    PrinterE printerE1 = (PrinterE) msg.obj;
                    switch (msg.what) {
                        case MESSAGE_TYPE_CHECK_LOCAL_PRINT:
                            if (localPrintBeginTimeStamp != null && System.currentTimeMillis() - localPrintBeginTimeStamp >= SUNMI_LOCAL_PRINT_CHECK_EXCEPTION_TIME) {
                                if (printerE1.getPrintKey().equalsIgnoreCase(mCurrentPrintKey)) {
                                    localPrintBeginTimeStamp = null;
                                    BaseApplication.showMessage("当前设备打印故障");
                                    localPrintDied.set(true);
                                }
                            }
                            return;
                        case MESSAGE_TYPE_LOCAL_PRINT_NOT_FIND:
                            BaseApplication.showMessage("当前设备打印故障");
                            break;
                        case MESSAGE_TYPE_LOCAL_PRINT_NOT_CONNECT:
                            BaseApplication.showMessage("当前设备打印故障");
                            break;
                        case MESSAGE_TYPE_SHOW_LOCT_PRINT_DIED_DIALOG:
                            BaseApplication.showMessage("当前设备打印故障");
                            break;
                    }
                    if (System.currentTimeMillis() - printerE1.getPullPrintDataTimeStamp() < 300000) {
                        mUIHandler.postDelayed(() -> {
                            PrintHandler.getInstance().print(printerE1, imgs);
                        }, 4000);
                    } else {
                        Realm realm = Realm.getDefaultInstance();
                        try {
                            PushPrintDataRecord bean = realm.where(PushPrintDataRecord.class).equalTo("key", printerE1.getPrintKey()).findFirst();
                            if (bean != null) {
                                realm.beginTransaction();
                                bean.setTimeout(true);
                                realm.commitTransaction();
                            }
                        } finally {
                            realm.close();
                            Realm.compactRealm(RealmUtil.getPushConfig());
                        }
                    }
                }
            };
        }
        addTask(buildTask(printerE, imgs));
    }

    /**
     * 根据传入的参数，新建一个任务
     *
     * @param printerE
     * @return
     */
    private Runnable buildTask(final PrinterE printerE, List<String> imgs) {
        return new Runnable() {
            @Override
            public void run() {
                mCurrentPrintKey = printerE.getPrintKey();
                AbsPrinter absPrinter = PrinterFactory.produce(printerE);
                Realm realm = Realm.getDefaultInstance();
                try {
                    int printTimes = printerE.getPrintTimes();
                    realm.beginTransaction();
                    for (int j = 0; j < printTimes; j++) {
                        if (printerE.getOnLocal() == 1 && !"50".equalsIgnoreCase(printerE.getPrinterTypeUID())) {
                            localPrintBeginTimeStamp = System.currentTimeMillis();
                            if (localPrintDied.get()) {
                                BaseApplication.showMessage("当前设备打印故障");
                                return;
                            } else {
                                Message message = new Message();
                                message.what = MESSAGE_TYPE_CHECK_LOCAL_PRINT;
                                message.obj = printerE;
                                mUIHandler.sendMessageDelayed(message, SUNMI_LOCAL_PRINT_CHECK_EXCEPTION_TIME);
                            }
                        }
                        absPrinter.print(printerE, imgs);
                        localPrintBeginTimeStamp = null;
                        PushPrintDataRecord record = realm.where(PushPrintDataRecord.class).equalTo("key", printerE.getPrintKey()).findFirst();
                        if (record != null) {
                            record.setPrintFinishTimes(record.getPrintFinishTimes() == null ? 1 : (record.getPrintFinishTimes() + 1));
                            if (record.getPrintTimes() <= record.getPrintFinishTimes()) {
                                record.setPrintUseTime(System.currentTimeMillis() - record.getObtainDataMillisecond());
                                record.setPrintFinish(true);
                            }
                        }
                    }
                    //该打印任务成功
                } catch (PrintException e) {
                    localPrintBeginTimeStamp = null;
                    Message message = Message.obtain();
                    message.obj = printerE;
                    message.what = e.getType();
                    mUIHandler.sendMessage(message);
                } finally {
                    realm.commitTransaction();
                    realm.close();
                    Realm.compactRealm(RealmUtil.getPushConfig());
                }
                mSemaphoreThreadPool.release();
                notifyDataIsEmpty();
            }
        };
    }

    /**
     * 从任务队列取出一个方法
     *
     * @return
     */
    private Runnable getTask() {
        if (mType == Type.FIFO) {
            Runnable runnable = mTaskQueue.removeFirst();
//            notifyDataIsEmpty();
            return runnable;
        } else if (mType == Type.LIFO) {
            Runnable runnable = mTaskQueue.removeLast();
//            notifyDataIsEmpty();
            return runnable;
        }
        return null;
    }

    private void notifyDataIsEmpty() {

        synchronized (PrintHandler.class) {
            if (isFeedbacking.get() == false) {
                isFeedbacking.set(true);
            } else {
                return;
            }
        }
        Observable.<List<PushFeedback>>create(e -> {
            Realm realm = Realm.getDefaultInstance();
            List<PushFeedback> pushFeedbackList = new ArrayList<>();
            try {
                RealmResults<PushPrintDataRecord> records = realm.where(PushPrintDataRecord.class).isNotNull("printFinishTimes").or().equalTo("isTimeout", true).findAll();
                long now = System.currentTimeMillis();
                for (int i = 0; i < records.size(); i++) {
                    PushPrintDataRecord record = records.get(i);
                    if (record.isTimeout() || record.getPrintFinishTimes() >= record.getPrintTimes()) {
                        PushFeedback pushFeedback = new PushFeedback();
                        pushFeedback.setPrintKey(record.getKey());
                        pushFeedback.setObtainTime(record.getPullDataElapsedTime());
                        pushFeedback.setPrintConsumeTime(record.getPrintUseTime());
                        pushFeedback.setActualPrintCount(record.getPrintFinishTimes());
                        pushFeedback.setShouldPrintCount(record.getPrintTimes());
                        pushFeedbackList.add(pushFeedback);
                    }
                    if (i == 150) {
                        break;
                    }
                }
            } finally {
                realm.close();
                Realm.compactRealm(RealmUtil.getPushConfig());
            }
            if (pushFeedbackList.size() > 0) {
                e.onNext(pushFeedbackList);
            }
            e.onComplete();
        }).flatMap(pushFeedbacks -> {
            return pushFeedback(pushFeedbacks).flatMap(pushPrintResponse -> {
                if (pushPrintResponse.getSuccess() == 1) {
                    Realm realm = Realm.getDefaultInstance();
                    try {
                        realm.beginTransaction();
                        for (PushFeedback pushFeedback : pushFeedbacks) {
                            PushPrintDataRecord record = realm.where(PushPrintDataRecord.class).equalTo("key", pushFeedback.getPrintKey()).findFirst();
                            if (record != null) {
                                record.deleteFromRealm();
                            }
                        }
                        realm.commitTransaction();
                    } finally {
                        realm.close();
                        Realm.compactRealm(RealmUtil.getPushConfig());
                    }
                }
                return Observable.just(pushPrintResponse);
            });
        }).subscribe(new Observer<PushPrintResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(PushPrintResponse response) {

            }

            @Override
            public void onError(Throwable e) {
                resetStatus();
            }

            @Override
            public void onComplete() {
                resetStatus();
            }
        });

        if (last_clear_push_data_time < System.currentTimeMillis() - PUSH_MESSAGE_SAVE_TIME_MILLIS) {
            last_clear_push_data_time = System.currentTimeMillis();
            Observable.<String>create(e -> {
                Realm realm = Realm.getDefaultInstance();
                try {
                    RealmResults<PushPrintBean> records = realm.where(PushPrintBean.class).lessThanOrEqualTo("insertTimestamp", System.currentTimeMillis() - PUSH_MESSAGE_SAVE_TIME_MILLIS).findAll();
                    realm.beginTransaction();
                    for (PushPrintBean record : records) {
                        record.deleteFromRealm();
                    }
                    realm.commitTransaction();
                } finally {
                    realm.close();
                    Realm.compactRealm(RealmUtil.getPushConfig());
                }
                e.onNext("");
                e.onComplete();
            }).subscribe(r -> {
            }, throwable -> {
            });
        }
    }

    private void resetStatus() {
        isFeedbacking.set(false);
        if (isNeedNextUpload.get()) {
            mPoolThreadHandler.postDelayed(this::notifyDataIsEmpty, 2000);
        }
    }

    private Observable<PushPrintResponse> pushFeedback(List<PushFeedback> list) {
        return RepositoryImpl.getInstance().pushFeedback(list);
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);
        // if(mPoolThreadHandler==null)wait();
        try {
            if (mPoolThreadHandler == null) {
                mSemaphorePoolThreadHandler.acquire();
            }
        } catch (InterruptedException e) {
        }
        mPoolThreadHandler.sendEmptyMessage(MESSAGE_TYPE_ADD_TASK);
    }

    public void setDataEmptyListener(DataEmptyListener dataEmptyListener) {
        if (dataEmptyListener == null) {
            this.dataEmptyListener = dataEmptyListener;
        }
    }

    public interface DataEmptyListener {
        void onDataEmpty();
    }
}
