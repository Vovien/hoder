package com.holderzone.intelligencepos.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;

import butterknife.ButterKnife;

/**
 * Created by tcw on 2017/8/29.
 */

public abstract class BaseDialog extends Dialog {

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 需在setContentView()之前设置的属性
        setAttributesBefore();

        // 设置ContentView
        setContentView(LayoutInflater.from(getContext()).inflate(getContentViewLayoutId(), null));

        // ButterKnife 绑定
        ButterKnife.bind(this);

        // 需在setContentView()之后设置的属性
        setAttributesAfter();

        // 初始化视图，注册事件
        initView();

        // 初始化数据，发起请求
        initData();
    }

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
}
