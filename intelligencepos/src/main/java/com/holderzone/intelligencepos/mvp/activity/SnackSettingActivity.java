package com.holderzone.intelligencepos.mvp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.widget.RelativeLayout;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LiTao on 2017-8-8.
 * 快餐收银配置（设置是否启用号牌）
 */

public class SnackSettingActivity extends BaseActivity {

    @BindView(R.id.title)
    Title title;
    @BindView(R.id.order_dishes_setting_switch)
    SwitchCompat mOrderDishesSettingSwitch;
    @BindView(R.id.snack_setting_root)
    RelativeLayout snackSettingRoot;

    @Override
    protected void handleBundleExtras(Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_snack_setting;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化title
        title.setOnReturnClickListener(this::finishActivity);
        // 初始化选中
        RepositoryImpl.getInstance().getIsStartFlapper()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindToLifecycle(this))
                .subscribe(RxCompoundButton.checked(mOrderDishesSettingSwitch));
        // 设置监听
        mOrderDishesSettingSwitch.setOnCheckedChangeListener((compoundButton, b) ->
                RepositoryImpl.getInstance().saveIsStartFlapper(b)
                        .subscribeOn(Schedulers.io())
                        .compose(RxTransformer.bindToLifecycle(this))
                        .subscribe());
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

    @OnClick(R.id.snack_setting_root)
    public void onClick() {
        if (mOrderDishesSettingSwitch.isChecked()) {
            mOrderDishesSettingSwitch.setChecked(false);
            RepositoryImpl.getInstance().saveIsStartFlapper(false)
                    .subscribeOn(Schedulers.io())
                    .compose(RxTransformer.bindToLifecycle(this))
                    .subscribe();
        } else {
            mOrderDishesSettingSwitch.setChecked(true);
            RepositoryImpl.getInstance().saveIsStartFlapper(true)
                    .subscribeOn(Schedulers.io())
                    .compose(RxTransformer.bindToLifecycle(this))
                    .subscribe();
        }
    }
}
