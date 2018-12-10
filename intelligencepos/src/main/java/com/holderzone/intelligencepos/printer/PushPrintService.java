package com.holderzone.intelligencepos.printer;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;

import com.blankj.utilcode.util.EmptyUtils;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by 赵平 on 2017/3/30.
 * <p>
 * -------------------------------请求数据-------------------------------------------
 * 启动打印服务（正常情况下每隔5秒获取打印数据）
 * 流程1:启动打印服务(startService)  分两种情况 非主动调用  和主动调用(即下单结账后需调用获取打印数据，会打断等待间隔)
 * 流程2:获取打印数据  成功--->流程3  失败--->流程14
 * 流程3:判断获取打印数据key在本地数据库是否存在  存在--->流程4  不存在--->流程7
 * 流程4:判断该打印数据是否完成(打印次数是否等于完成次数) 完成--->流程5  未完成 -->流程6
 * 流程5:忽略该数据
 * 流程6:修改打印数据中（PrintE）的打印次数 -->流程8
 * 流程7:将数据信息封装成(PrintBean)插入到数据库  -->流程8
 * -------------------------------开始打印-------------------------------------------
 * 流程8:所有数据处理完成后开始打印
 * 流程9:判断打印类型(本地和网络打印)进行打印  成功--> 流程10  失败-->流程11
 * 流程10:成功记录在list
 * 流程11:是否全部失败  (全部失败不记录,部分失败记录完成打印次数)
 * 流程12：将记录的数据更新到数据库中
 * -------------------------------提交打印完成数据-------------------------------------------
 * 流程13：提交打印完成数据  成功--->修改数据库提交状态  失败不处理
 * <p>
 * 流程14：开始轮询打印数据
 */

public class PushPrintService extends Service implements OptimizePrintContract.ServiceCallback, PrintHandler.DataEmptyListener {
    private static final String TAG = "PushPrintService";
    public static final String EXTRA_RELOAD_PRINT_IMAGE = "reload_print_image";
    public static final int MESSAGE_TYPE_REQUEST_PRINT_DATA = 1;
    public static final int MESSAGE_TYPE_LOCAL_PRINT_NOT_FIND = 2;
    public static final int MESSAGE_TYPE_LOCAL_PRINT_NOT_CONNECT = 3;
    public static final int MESSAGE_TYPE_CHECK_LOCAL_PRINT = 6;
    public static final int MESSAGE_TYPE_SHOW_LOCT_PRINT_DIED_DIALOG = 7;
    private OptimizePrintPresenter mPresenter;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private static final AtomicBoolean needNextRequest = new AtomicBoolean(false);
    private final AtomicBoolean isAlive = new AtomicBoolean(false);
    private final AtomicBoolean hasLoadLocalPrintData = new AtomicBoolean(false);
    public static List<String> imgs;
    public static ExecutorService PUSH_THREAD_POOL = Executors.newFixedThreadPool(1);
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_TYPE_REQUEST_PRINT_DATA:
                    requestData();
                    break;
                default:
                    break;
            }
        }
    };
    public static void setNeedNextRequest() {
        needNextRequest.set(true);
    }
    private synchronized void requestData() {
        if (isRunning.get()) {
            needNextRequest.set(true);
        } else {
            isRunning.set(true);
            mPresenter.getPrintData();
        }
    }

    @Override
    public void onCreate() {
        PrintHandler.getInstance().setDataEmptyListener(this);
        isAlive.set(true);
        mPresenter = new OptimizePrintPresenter(this);
        String externalStorageState = Environment.getExternalStorageState();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra(EXTRA_RELOAD_PRINT_IMAGE, false)) {
            RepositoryImpl.getInstance().queryAvailableLocalPath(ImageBean.Type.PrintLogo).subscribe(strings -> imgs = strings);
        }
        if (hasLoadLocalPrintData.get() == true) {
            if (!isRunning.get()) {
                mHandler.sendEmptyMessage(MESSAGE_TYPE_REQUEST_PRINT_DATA);
                needNextRequest.set(false);
            } else {
                needNextRequest.set(true);
            }
        } else {
            mPresenter.getLocalPrintData();
        }
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        isAlive.set(false);
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onGetPrintDataResponse(List<PrinterE> list) {
        if (EmptyUtils.isNotEmpty(list)) {
            for (PrinterE printerE : list) {
                PrintHandler.getInstance().print(printerE, imgs);
            }
        }
        if (needNextRequest.get() || hasLoadLocalPrintData.get() == false) {
            needNextRequest.set(false);
            mHandler.sendEmptyMessage(MESSAGE_TYPE_REQUEST_PRINT_DATA);
            setHasLoadLocalData();
        }
        isRunning.set(false);
    }

    private void setHasLoadLocalData() {
        if (hasLoadLocalPrintData.get() == false) {
            hasLoadLocalPrintData.set(true);
        }
    }

    @Override
    public void onGetPrintDataResponseError() {
        setHasLoadLocalData();
        isRunning.set(false);
        needNextRequest.set(false);
        mHandler.sendEmptyMessage(MESSAGE_TYPE_REQUEST_PRINT_DATA);
    }

    @Override
    public void onDataEmpty() {
    }
}
