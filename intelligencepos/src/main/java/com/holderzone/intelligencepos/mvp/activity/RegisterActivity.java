package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.fragment.RegisterActivedFragment;
import com.holderzone.intelligencepos.mvp.fragment.RegisterInactiveFragment;
import com.holderzone.intelligencepos.mvp.fragment.RegisterServiceContractFragment;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.holderzone.intelligencepos.utils.keyboard.IMMLeaks;


/**
 * 用户注册界面
 * Created by tcw on 2017/5/26.
 */
public class RegisterActivity extends BaseActivity implements RegisterServiceContractFragment.NextStepListener,
        RegisterInactiveFragment.InactiveListener, RegisterActivedFragment.ActivedListener {

    private static final String EXTRA_ACTIVED = "EXTRA_ACTIVED";

    private boolean mActived;

    public static Intent newIntent(Context context) {
        return newIntent(context, false);
    }

    public static Intent newIntent(Context context, boolean actived) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra(EXTRA_ACTIVED, actived);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mActived = extras.getBoolean(EXTRA_ACTIVED);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(Bundle extras) {
        StatusBarUtil.setStatusBarColorM(this,R.color.layout_bg_common_primary);

        Fragment fragment = RegisterServiceContractFragment.newInstance();
        launchFragment(fragment);
    }

    @Override
    protected void initData(Bundle extras) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onGoOnClick(boolean launchFromActived, boolean launchFromInactived) {
        if (launchFromActived || launchFromInactived) {
            removeFragmentByPopstack();
        } else {
            if (mActived) {
                launchFragment(RegisterActivedFragment.newInstance());
            } else {
                launchFragment(RegisterInactiveFragment.newInstance());
            }
        }
    }

    @Override
    public void onExitClick() {
        finishActivity();
    }

    @Override
    public void onUserAgreementClickFromInactive() {
        Fragment fragment = RegisterServiceContractFragment.newInstance(false, true);
        launchFragment(fragment);
    }

    @Override
    public void onStartUsingClick() {
        launchActivity(ChooseStoreActivity.newIntent(RegisterActivity.this));
    }

    @Override
    public void onUserAgreementClickFromActived() {
        Fragment fragment = RegisterServiceContractFragment.newInstance(true, false);
        launchFragment(fragment);
    }

    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                showMessage("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                AppManager.getInstance().AppExit(this);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void launchFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void removeFragmentByPopstack() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TODO: 2017/5/26 解决键盘内存泄露，不一定有效，如果有效，可以放到BaseActivity里面
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            IMMLeaks.fixFocusedViewLeak(BaseApplication.getApplication());
        }
    }
}
