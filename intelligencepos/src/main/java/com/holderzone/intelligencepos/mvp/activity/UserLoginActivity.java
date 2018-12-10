package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.UserLoginContract;
import com.holderzone.intelligencepos.mvp.model.bean.StoreE;
import com.holderzone.intelligencepos.mvp.model.bean.UsersE;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.presenter.UserLoginPresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewAfterTextChangeEvent;
import com.kennyc.view.MultiStateView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * 用户登录
 * Created by tcw on 2017/6/6.
 */

public class UserLoginActivity extends BaseActivity<UserLoginContract.Presenter> implements UserLoginContract.View {

    private static final int REQUEST_CODE_CHOOSE_STORE = 0x00000001;

    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.tv_store)
    TextView mTvStore;
    @BindView(R.id.et_user_name)
    EditText mEtUserName;
    @BindView(R.id.iv_clear_user_name)
    ImageView mIvClearUserName;
    @BindView(R.id.ll_user_name)
    LinearLayout mLlUserName;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.iv_clear_password)
    ImageView mIvClearPassword;
    @BindView(R.id.ll_password)
    LinearLayout mLlPassword;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.tv_version_code)
    TextView mTvVersionCode;

    /**
     * 当前选择的门店
     */
    private Store mLocalStore;

    @OnClick({R.id.iv_clear_user_name, R.id.ll_user_name, R.id.iv_clear_password, R.id.ll_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_user_name:
                mEtUserName.setText("");
                break;
            case R.id.ll_user_name:
                mEtUserName.requestFocus();
                break;
            case R.id.iv_clear_password:
                mEtPassword.setText("");
                break;
            case R.id.ll_password:
                mEtPassword.requestFocus();
                break;
            default:
                break;
        }
    }

    /**
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, UserLoginActivity.class);
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
        return R.layout.activity_user_login;
    }

    @Override
    protected UserLoginContract.Presenter initPresenter() {
        return new UserLoginPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        StatusBarUtil.setStatusBarColorM(this,R.color.layout_bg_common);
        // 默认不弹出键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);

        // 员工账号
        Observable.combineLatest(RxView.focusChanges(mEtUserName), RxTextView.afterTextChangeEvents(mEtUserName), Pair::new)
                .subscribe(pair -> {
                    Boolean focused = pair.first;
                    TextViewAfterTextChangeEvent event = pair.second;
                    if (focused) {
                        mLlUserName.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
                        mIvClearUserName.setVisibility(event.editable().length() > 0 ? View.VISIBLE : View.GONE);
                    } else {
                        mLlUserName.setBackgroundResource(R.drawable.shape_edit_corner_bg);
                        mIvClearUserName.setVisibility(View.GONE);
                    }
                    mBtnLogin.setEnabled(event.editable().length() > 0 && mEtPassword.getText().length() > 0);
                });

        // 员工密码
        Observable.combineLatest(RxView.focusChanges(mEtPassword), RxTextView.afterTextChangeEvents(mEtPassword), Pair::new)
                .subscribe(pair -> {
                    Boolean focused = pair.first;
                    TextViewAfterTextChangeEvent event = pair.second;
                    if (focused) {
                        mLlPassword.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
                        mIvClearPassword.setVisibility(event.editable().length() > 0 ? View.VISIBLE : View.GONE);
                    } else {
                        mLlPassword.setBackgroundResource(R.drawable.shape_edit_corner_bg);
                        mIvClearPassword.setVisibility(View.GONE);
                    }
                    mBtnLogin.setEnabled(mEtUserName.getText().length() > 0 && event.editable().length() > 0);
                });

        // 默认密码
        if (AppUtils.isAppDebug()) {
            mEtUserName.setText("10000");
            mEtUserName.setSelection(mEtUserName.getText().length());
            mEtPassword.setText("000000");
            mEtPassword.setSelection(mEtPassword.getText().length());
        } else {
            mBtnLogin.setEnabled(false);
        }

        // 登录按钮 防抖、监听
        mBtnLogin.setOnClickListener(v -> {
            mBtnLogin.setEnabled(false);
            mPresenter.requestLogin(mEtUserName.getText().toString(), mEtPassword.getText().toString());
        });

        // 切换门店 防抖、监听
        RxView.clicks(mTvStore).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> startActivityForResult(ChooseStoreActivity.newIntent(UserLoginActivity.this, true, mLocalStore.getStoreGUID()), REQUEST_CODE_CHOOSE_STORE));

        // 设置版本号
        mTvVersionCode.setText(getString(R.string.version_name, AppUtils.getAppVersionName()));

        // MultiStateView
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> mPresenter.requestLocalAndRemoteStore());
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        // 验证门店是否可用
        mPresenter.requestLocalAndRemoteStore();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (REQUEST_CODE_CHOOSE_STORE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        updateStore(extras.getParcelable("test"));
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /************************view callback begin*************************/

    @Override
    public void onRequestLocalRemoteStoreSucceed(Store localStore, List<StoreE> storeEList) {
        // 切换到内容布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        // 门店名
        updateStore(localStore);
        // 校验已选择门店是否可用 不可用则跳转到选择门店activity
        String storeGUID = localStore.getStoreGUID();
        boolean storeExistForNow = false;
        for (StoreE storeE : storeEList) {
            if (!storeExistForNow && storeE.getStoreGUID().equalsIgnoreCase(storeGUID)) {
                storeExistForNow = true;
            }
        }
        if (!storeExistForNow) {// 服务器最新store列表不包含本地门店guid所对应的门店：可能门店不再使用、可能更换企业绑定信息。
            startActivityForResult(ChooseStoreActivity.newIntent(UserLoginActivity.this, true, null), REQUEST_CODE_CHOOSE_STORE);
        }
    }

    @Override
    public void onRequestLocalRemoteStoreFailed() {
        // 切换到错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onNetworkError() {
        // 切换到错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void loginSuccess(UsersE usersE) {
        // map权限
        PermissionManager.mapArrayOfUserGroupRightE2UserRight(usersE);
        // 往数据库中写users
        mPresenter.saveLoginUsers(usersE);
    }

    @Override
    public void loginFail() {
        mBtnLogin.setEnabled(true);
    }

    @Override
    public void loginFailOutOfAccountOrPasswordIncorrect() {
        // 清空密码输入框
        mEtPassword.setText("");
    }

    @Override
    public void onSaveLoginUsersSucceed() {
        // 启动Splash活动，缓存数据
        launchActivity(SplashActivity.newIntent(UserLoginActivity.this));
        // 关闭当前activity
        finishActivity();
    }

    @Override
    public void onDispose() {

    }

    /************************view callback end*************************/

    /**
     * 更新当前Store实体，设置Store名字
     *
     * @param store
     */
    private void updateStore(Store store) {
        mLocalStore = store;
        String storeName = mLocalStore.getName();
        String storeCode = mLocalStore.getCode();
        if (storeCode != null) {
            mTvStore.setText(storeName + "[" + storeCode + "]");
        } else if (storeName != null) {
            mTvStore.setText(storeName);
        }
    }
}
