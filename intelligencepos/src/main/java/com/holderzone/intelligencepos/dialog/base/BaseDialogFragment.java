package com.holderzone.intelligencepos.dialog.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tcw on 2017/5/25.
 */

public abstract class BaseDialogFragment<T> extends DialogFragment {

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 解析Argment参数
        parseArgumentExtra(getArguments());

        // 需在onCreate()中设置的属性
        setAttributesOnceCreate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getContentViewLayoutId(), container, false);

        // ButterKnife 绑定
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        // 需在setContentView()之前设置的属性
        setAttributesBefore();

        // 调用super，完成setContentView(视图)
        super.onActivityCreated(savedInstanceState);

        // 需在setContentView()之后设置的属性
        setAttributesAfter();

        // 初始化视图，注册事件
        initView();

        // 初始化数据，发起请求
        initData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // ButterKnife 解除视图绑定
        if (mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        mUnbinder = null;
    }

    /**
     * 解析argument
     *
     * @param args
     */
    protected abstract void parseArgumentExtra(Bundle args);

    /**
     * 需在onCreate()中设置的属性
     * style 等
     */
    protected abstract void setAttributesOnceCreate();

    /**
     * 提供布局id
     *
     * @return
     */
    @LayoutRes
    protected abstract int getContentViewLayoutId();

    /**
     * 需在setContentView()之前设置的属性
     * ... 等
     */
    protected abstract void setAttributesBefore();

    /**
     * 需在setContentView()之后设置的属性
     * 屏幕尺寸 等
     */
    protected abstract void setAttributesAfter();

    /**
     * 初始化视图，注册事件
     */
    protected abstract void initView();

    /**
     * 初始化数据，发起请求
     */
    protected abstract void initData();

    /**
     * 设置监听回调
     *
     * @param t
     */
    public abstract void setDialogListener(T t);
}
