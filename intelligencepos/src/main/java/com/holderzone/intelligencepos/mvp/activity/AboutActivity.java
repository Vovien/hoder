package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.AboutContract;
import com.holderzone.intelligencepos.mvp.model.bean.EnterpriseInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.EquipmentsE;
import com.holderzone.intelligencepos.mvp.presenter.AboutPresenter;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;

/**
 * 设置-关于 界面
 * Created by zhaoping on 2017/6/7.
 */

public class AboutActivity extends BaseActivity<AboutContract.Presenter> implements AboutContract.View {

    @BindView(R.id.deviceCode)
    TextView deviceCode;
    @BindView(R.id.account)
    TextView account;
    @BindView(R.id.enterpriseName)
    TextView enterpriseName;
    @BindView(R.id.validateDate)
    TextView validateDate;
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.versionTextView)
    TextView versionTextView;

    public static Intent newIntent(Context context) {
        return new Intent(context, AboutActivity.class);
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_about;
    }

    @Override
    protected AboutContract.Presenter initPresenter() {
        return new AboutPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        title.setOnReturnClickListener(this::finishActivity);
        deviceCode.setText(DeviceHelper.getInstance().getDeviceID());
        versionTextView.setText(getString(R.string.version_name, AppUtils.getAppVersionName()));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestInfo();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void getInfoSuccess(EquipmentsE equipmentsE, EnterpriseInfoE enterpriseInfoE) {
        account.setText(enterpriseInfoE.getEnterpriseInfoUID());
        enterpriseName.setText(enterpriseInfoE.getName());
        validateDate.setText(equipmentsE.getExpiryDate());
    }

    @Override
    public void onDispose() {

    }
}
