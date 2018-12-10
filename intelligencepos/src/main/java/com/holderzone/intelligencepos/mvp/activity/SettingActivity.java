package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置activity
 */
public class SettingActivity extends BaseActivity {

    @BindView(R.id.userName)
    TextView userName;
    @BindView(R.id.userCode)
    TextView userCode;
    @BindView(R.id.title)
    Title title;

    /**
     * 静态方法，对外暴露该activity需要的参数
     *
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
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
        return R.layout.activity_setting;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        title.setOnReturnClickListener(this::finishActivity);
        RepositoryImpl.getInstance().getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindToLifecycle(this))
                .subscribe(users -> {
                    userName.setText(users.getName());
                    userCode.setText("员工编号：" + users.getNumber());
                });
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

    @OnClick({R.id.abort, R.id.snack_setting, R.id.remark_setting, R.id.userInfo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.userInfo:
                //账号信息
                startActivity(LoginUserInfoActivity.newIntent(this));
                break;
            case R.id.abort:
                //关于
                startActivity(AboutActivity.newIntent(getApplicationContext()));
                break;
            case R.id.snack_setting:
                //快餐收银设置
                PermissionManager.checkPermission(PermissionManager.PERMISSION_SETTING,
                        () -> startActivity(new Intent(this, SnackSettingActivity.class)));
                break;
            case R.id.remark_setting:
                //备注信息
                PermissionManager.checkPermission(PermissionManager.PERMISSION_SETTING,
                        () -> startActivity(RemarkManagerActivity.newIntent(this)));
                break;
            default:
                break;
        }
    }
}
