package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LiTao on 2017-8-8.
 * 登录员工信息页面
 */

public class LoginUserInfoActivity extends BaseActivity {

    @BindView(R.id.title)
    Title title;
    @BindView(R.id.login_user_user)
    TextView loginUserUser;
    @BindView(R.id.login_user_business_day)
    TextView loginUserBusinessDay;
    @BindView(R.id.login_user_start_time)
    TextView loginUserStartTime;
    @BindView(R.id.login_user_login_time)
    TextView loginUserLoginTime;
    @BindView(R.id.exit)
    Button exit;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginUserInfoActivity.class);
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
        return R.layout.activity_login_user_info;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化Title
        initTitle();
        //登录员工以及编号
        RepositoryImpl.getInstance().getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindToLifecycle(this))
                .subscribe(users -> loginUserUser.setText(users.getName() + "(" + users.getNumber() + ")"));

        //营业日
        RepositoryImpl.getInstance()
                .getAccountRecord()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindToLifecycle(this))
                .subscribe(accountRecord -> {
                    loginUserBusinessDay.setText(accountRecord.getBusinessDay());
                });
        //登录时间
        loginUserStartTime.setText(DeviceHelper.getInstance().getLoginTime());
        //登录时长
        loginUserLoginTime.setText(DeviceHelper.getInstance().getLoginDuration());
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @OnClick(R.id.exit)
    public void onClick() {
        mDialogFactory.showConfirmDialog(getString(R.string.exit_content), true, getString(R.string.cancel), R.drawable.selector_button_green, true, "确定退出", R.drawable.selector_button_red, new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
            }

            @Override
            public void onPosClick() {
                BaseApplication.getApplication().stopMqttService();
                AppManager.getInstance().AppExitWithServiceShutDownAndDataClear(getApplicationContext());
            }
        });
    }

    private void initTitle() {
        title.setOnReturnClickListener(this::finishActivity);
    }
}
