package com.holderzone.intelligencepos.utils.print.local;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;

import com.blankj.utilcode.util.LogUtils;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.bean.PrintRowE;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.CollectionUtils;
import com.holderzone.intelligencepos.utils.print.AbsPrinter;
import com.holderzone.intelligencepos.utils.print.PrintException;

import java.util.List;

import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by tcw on 2017/10/16.
 */

public class LocalPrinterDelegate implements AbsPrinter {

    private static final String TAG = "LocalPrinterDelegate";

    private volatile static LocalPrinterDelegate sInstance;//使用volatile变量以保证是在主内存上取值

    private IWoyouService mIWoyouService = null;

    private ICallback mICallback = null;

    public static LocalPrinterDelegate getInstance() {
        if (sInstance == null) {
            synchronized (LocalPrinterDelegate.class) {
                if (sInstance == null) {
                    sInstance = new LocalPrinterDelegate();
                }
            }
        }
        return sInstance;
    }

    private LocalPrinterDelegate() {
        // 启动服务
        Intent intent = new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        Context context = BaseApplication.getContext();
        context.startService(intent);
        // 绑定服务
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIWoyouService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIWoyouService = IWoyouService.Stub.asInterface(service);
            }
        };
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        // 回调监听
        mICallback = new ICallback.Stub() {
            @Override
            public void onRunResult(boolean isSuccess, int code, final String msg) throws RemoteException {
                LogUtils.d(TAG, "isSuccess:" + isSuccess + ",code:" + code + ",msg:" + msg);
            }
        };
    }

    @Override
    public void print(PrinterE printerE, List<String> imgs) throws PrintException {
        try {
            List<PrintRowE> arrayOfPrintRowE = printerE.getArrayOfPrintRowE();
            int size = arrayOfPrintRowE.size();
            for (int i = 0; i < size; i++) {
                PrintRowE printRowE = arrayOfPrintRowE.get(i);
                // 设置文字对齐
                switch (printRowE.getAlign()) {
                    case "L":
                        mIWoyouService.setAlignment(0, mICallback);
                        break;
                    case "R":
                        mIWoyouService.setAlignment(2, mICallback);
                        break;
                    default:
                        mIWoyouService.setAlignment(1, mICallback);
                        break;
                }
                // 设备打印类型
                switch (printRowE.getContentType()) {
                    case "Text":
                        mIWoyouService.printTextWithFont(printRowE.getContent() + "\n", "", Integer.valueOf(printRowE.getFontSize()), mICallback);
                        break;
                    case "IMG":
                        if (CollectionUtils.isNotEmpty(imgs)) {
                            String printLogoPath = null;
                            for (String s : imgs) {
                                if (s.endsWith(printRowE.getContent())) {
                                    printLogoPath = s;
                                    break;
                                }
                            }
                            if (printLogoPath != null) {
                                mIWoyouService.printBitmap(BitmapFactory.decodeFile(printLogoPath), mICallback);
                                mIWoyouService.printText("\n", mICallback);
                            }
                        }
                        break;
                    case "QRCode":
                        mIWoyouService.printQRCode(printRowE.getContent(), 8, 3, mICallback);
                        break;
                    default:
                        // do nothing for now
                        break;
                }
            }
            // 切纸
            mIWoyouService.cutPaper(mICallback);
        } catch (NullPointerException e) {
            throw new PrintException("未发现本机打印设备", PrintException.MESSAGE_TYPE_LOCAL_PRINT_NOT_FIND);
        } catch (RemoteException e) {
            throw new PrintException("本机打印设备未连接", PrintException.MESSAGE_TYPE_LOCAL_PRINT_NOT_CONNECT);
        }
    }

    /**
     * 本地测试
     */
    public void printLocalTest() throws PrintException {
        try {
            mIWoyouService.printerSelfChecking(mICallback);
        } catch (NullPointerException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> BaseApplication.showMessage("未发现本机打印设备"));
        } catch (RemoteException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> BaseApplication.showMessage("本机打印设备未连接"));
        }
    }

    /**
     * 开钱箱
     */
    public void openDrawer() {
        try {
            mIWoyouService.openDrawer(mICallback);
        } catch (NullPointerException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> BaseApplication.showMessage("未发现本机打印设备"));
        } catch (RemoteException e) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> BaseApplication.showMessage("本机打印设备未连接"));
        }
    }
}
