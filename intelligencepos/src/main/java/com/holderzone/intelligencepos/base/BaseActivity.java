package com.holderzone.intelligencepos.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.DialogFactory;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.StatusBarUtil;
import com.holderzone.intelligencepos.utils.keyboard.SoftKeyboardUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tcw on 2017/5/25.
 */

public abstract class BaseActivity<P extends IPresenter> extends RxAppCompatActivity implements IView {
    private static final String TAG_FAKE_STATUS_BAR_VIEW = "TAG_FAKE_STATUS_BAR_VIEW";
    private static final String TAG_MARGIN_ADDED = "TAG_MARGIN_ADDED";
    protected P mPresenter;

    private Unbinder mUnbinder;

    protected DialogFactory mDialogFactory;

    private Bundle mSavedInstanceState;

//     activity启动建议采用静态newIntent()方法传递context和extra data

    @CallSuper
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 加入activity管理栈
        AppManager.getInstance().addActivity(this);
        StatusBarUtil.setStatusBarColorM(this, R.color.layout_bg_white_ffffff);
//        // 解决键盘内存泄露
//        IMMLeaks.fixFocusedViewLeak(BaseApplication.getApplication());
        // 隐藏状态栏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 处理Extra数据
        Bundle extras = getIntent().getExtras();
        if (null != extras) {
            handleBundleExtras(extras);
        }
        if (null != savedInstanceState) {
            handleSavedInstanceState(savedInstanceState);
        }
        // 设置content视图
        setContentView(getContentViewLayoutId());
        // 注册事件 实现点击软键盘区域外，隐藏软键盘的效果
        SoftKeyboardUtil.registerTouchEvent(this);
        // ButterKnife绑定
        mUnbinder = ButterKnife.bind(this);
        // 初始化DialogFactory
        mDialogFactory = new DialogFactory(getSupportFragmentManager());
        // 初始化presenter
        mPresenter = initPresenter();
        // 初始化视图，注册事件
        initView(savedInstanceState);
        // 加载数据
        initData(savedInstanceState);
        // 状态赋值，供onSubscribe使用
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // onResume后才初始化的数据
        onSubscribe(mSavedInstanceState);
    }

    protected abstract void handleBundleExtras(@NonNull Bundle extras);

    protected abstract void handleSavedInstanceState(@NonNull Bundle savedInstanceState);

    @LayoutRes
    protected abstract int getContentViewLayoutId();

    @Nullable
    protected abstract P initPresenter();

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract void onSubscribe(@Nullable Bundle savedInstanceState);

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 从activity管理栈移除
        AppManager.getInstance().removeActivity(this);

        // ButterKnife解除视图绑定
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        mUnbinder = null;

        // 控制presenter层释放资源，并清除对presenter层的引用
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
        }
        mPresenter = null;

        // 解决键盘内存泄露
//        IMMLeaksSolution.fixInputMethod(this);
    }

    @Override
    public void showLoading() {
        mDialogFactory.showProgressDialog(this);
    }

    @Override
    public void hideLoading() {
        mDialogFactory.dismissProgressDialog();
    }

    @Override
    public void showMessage(String message) {
        BaseApplication.showMessage(message);
    }

    @Override
    public void showAuthorizationDialog(String msg) {
        DialogFactory.showForceLogoutDialogWithServiceShutDownResultDataClear(BaseActivity.this, msg);
    }

    @Override
    public void launchActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void finishActivity() {
        finish();
    }

    /**
     * 得到dialogFactory以显示对话框
     *
     * @return
     */
    public DialogFactory getDialogFactory() {
        return mDialogFactory;
    }
}
