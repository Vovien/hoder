package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2018-04-08.
 * 确认换菜对话框
 */

public class ConfirmChangeDishesDialogFragment extends BaseDialogFragment<ConfirmChangeDishesDialogFragment.ConfirmChangeDishesListener> {
    public static final String CHANGE_DISHES_TITLE = "CHANGE_DISHES_TITLE";
    @BindView(R.id.title)
    TextView mTitleTv;
    private ConfirmChangeDishesListener listener;
    /**
     * 标题
     */
    private String mTitle;

    public static ConfirmChangeDishesDialogFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(CHANGE_DISHES_TITLE, title);
        ConfirmChangeDishesDialogFragment fragment = new ConfirmChangeDishesDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @OnClick({R.id.close_iv, R.id.confirm_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                dismiss();
                break;
            case R.id.confirm_change:
                if (listener != null) {
                    dismiss();
                    listener.onConfirmChange();
                }
                break;
            default:
                break;
        }
    }

    public interface ConfirmChangeDishesListener {
        void onConfirmChange();
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mTitle = args.getString(CHANGE_DISHES_TITLE);
    }

    @Override
    protected void setAttributesOnceCreate() {
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_cofiem_change_dishes;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {
        //设置标题
        mTitleTv.setText(mTitle);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(ConfirmChangeDishesListener confirmChangeDishesListener) {
        listener = confirmChangeDishesListener;
    }
}
