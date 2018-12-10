package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

/**
 * 二维码扫描界面
 */
public class CaptureActivity extends Activity implements DecoratedBarcodeView.TorchListener {

    public static final String EXTRA_TITLE = "cn.holdzone.intelligencepos.title";
    public static final String EXTRA_MESSAGE = "cn.holdzone.intelligencepos.message";

    private CaptureManager captureManager;
    private DecoratedBarcodeView mDBV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarColorM(this,R.color.layout_bg_white_ffffff);
        setContentView(R.layout.activity_capture);
        mDBV = (DecoratedBarcodeView) findViewById(R.id.dbv_custom);
        mDBV.setTorchListener(this);

        Intent intent = getIntent();
        String titleText = intent.getStringExtra(EXTRA_TITLE);
        String messageText = intent.getStringExtra(EXTRA_MESSAGE);
        TextView tvTitle = (TextView) findViewById(R.id.tv_title);
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle.setText(titleText);
        }
        TextView tvMessage = (TextView) findViewById(R.id.tv_notice_msg);
        if (!TextUtils.isEmpty(messageText)) {
            tvMessage.setText(messageText);
        }
        LinearLayout lLReturn = (LinearLayout) findViewById(R.id.ll_return);
        lLReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        });

        //重要代码，初始化捕获
        captureManager = new CaptureManager(this, mDBV);
        captureManager.initializeFromIntent(getIntent(), savedInstanceState);
        captureManager.decode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        captureManager.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        captureManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        captureManager.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        captureManager.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mDBV.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onTorchOn() {

    }

    @Override
    public void onTorchOff() {

    }
}
