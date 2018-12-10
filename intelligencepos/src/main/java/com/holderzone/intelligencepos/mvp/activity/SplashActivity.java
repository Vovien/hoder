package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.SplashContract;
import com.holderzone.intelligencepos.mvp.presenter.SplashPresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.holderzone.intelligencepos.widget.CircleProgress;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * 启动页 数据缓存
 * Created by tcw on 2017/5/26.
 */
public class SplashActivity extends BaseActivity<SplashContract.Presenter> implements SplashContract.View {

    @BindView(R.id.pb_download)
    CircleProgress mCircleProgress;
    @BindView(R.id.iv_sync_error)
    ImageView mIvSyncError;
    @BindView(R.id.btn_exit)
    Button mBtnExit;
    @BindView(R.id.btn_re_login)
    Button mBtnReLogin;


    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected SplashContract.Presenter initPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColorM(this,R.color.layout_bg_common);
        mCircleProgress.setProgress(0);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestData();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void refreshUI(int progress) {
        mCircleProgress.setProgress(progress);
        if (progress == 100) {
            // 延迟100毫秒启动HomeActivity
            Observable.timer(100, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(@NonNull Long aLong) throws Exception {
                            launchHomeActivity();
                            finishActivity();
                        }
                    });
        }
    }

    @Override
    public void showErrorLayout() {
        mCircleProgress.setVisibility(View.GONE);
        mIvSyncError.setVisibility(View.VISIBLE);
        mBtnExit.setVisibility(View.VISIBLE);
        mBtnReLogin.setVisibility(View.VISIBLE);
    }

    @Override
    public void launchHomeActivity() {
        launchActivity(HomeActivity.newIntent(SplashActivity.this));
    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.btn_exit, R.id.btn_re_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_exit:
                AppManager.getInstance().AppExitWithDataClear(SplashActivity.this);
                break;
            case R.id.btn_re_login:
                launchActivity(UserLoginActivity.newIntent(SplashActivity.this));
                finishActivity();
                break;
            default:
                break;
        }
    }
}
