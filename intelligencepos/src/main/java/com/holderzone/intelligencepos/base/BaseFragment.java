package com.holderzone.intelligencepos.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.holderzone.intelligencepos.dialog.DialogFactory;
import com.holderzone.intelligencepos.utils.keyboard.SoftKeyboardUtil;
import com.trello.rxlifecycle2.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tcw on 2017/5/25.
 */

public abstract class BaseFragment<P extends IPresenter> extends RxFragment implements IView {
    protected final String TAG = this.getClass().getSimpleName();

    protected P mPresenter;

    protected BaseActivity mBaseActivity;

    protected DialogFactory mDialogFactory;

    private Unbinder mUnbinder;

    private Bundle mSavedInstanceState;

    @CallSuper
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof BaseActivity) {
            mBaseActivity = (BaseActivity) activity;
        }
    }

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 建议派生都使用Bundle来传输少量的数据
        Bundle extras = getArguments();
        if (null != extras) {
            handleBundleExtras(extras); //extras不为空，该虚函数才会执行
        }
        if (savedInstanceState != null) {
            handleSavedInstanceState(savedInstanceState);
        }
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        if (getContentViewLayoutId() != 0) {
            view = inflater.inflate(getContentViewLayoutId(), container, false);
        } else {
            view = super.onCreateView(inflater, container, savedInstanceState);
        }
        // ButterKnife绑定
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @CallSuper
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 注册事件 实现点击软键盘区域外，隐藏软键盘的效果
        SoftKeyboardUtil.registerTouchEvent(mBaseActivity);

        // 初始化presenter
        mPresenter = initPresenter();
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 初始化DialogFactory
        mDialogFactory = new DialogFactory(getChildFragmentManager());

        // 初始化视图，注册事件
        initView(savedInstanceState);

        // 初始化数据
        initData(savedInstanceState);

        // 状态赋值，供onSubscribe使用
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onResume() {
        super.onResume();
        // onResume后才初始化的数据
        onSubscribe(mSavedInstanceState);
    }

    protected abstract void handleBundleExtras(@NonNull Bundle extras);

    protected abstract void handleSavedInstanceState(@NonNull Bundle savedInstanceState);

    @LayoutRes
    protected abstract int getContentViewLayoutId();

    protected abstract P initPresenter();

    protected abstract void initView(@Nullable Bundle savedInstanceState);

    protected abstract void initData(@Nullable Bundle savedInstanceState);

    protected abstract void onSubscribe(@Nullable Bundle savedInstanceState);

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @CallSuper
    @Override
    public void onDestroy() {
        super.onDestroy();

        // ButterKnife解除视图绑定
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        mUnbinder = null;

        // 控制presenter层释放资源，并清除对presenter层的引用
        if (mPresenter != null) {
            mPresenter.onDestroy();//释放资源
            mPresenter = null;
        }
    }

    @CallSuper
    @Override
    public void onDetach() {
        super.onDetach();
        mBaseActivity = null;
    }

    @Override
    public void showLoading() {
        mDialogFactory.showProgressDialog(mBaseActivity);
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
        DialogFactory.showForceLogoutDialogWithServiceShutDownResultDataClear(mBaseActivity, msg);
    }

    @Override
    public void launchActivity(Intent intent) {
        if (mBaseActivity != null) {
            mBaseActivity.startActivity(intent);
        } else {
            throw new NullPointerException("mBaseActivity为null");
        }
    }

    @Override
    public void finishActivity() {
        if (mBaseActivity != null) {
            mBaseActivity.finish();
        } else {
            throw new NullPointerException("mBaseActivity为null");
        }
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
