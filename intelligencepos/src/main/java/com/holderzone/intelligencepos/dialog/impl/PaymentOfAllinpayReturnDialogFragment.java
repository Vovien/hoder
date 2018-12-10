package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.utils.keyboard.SoftKeyboardUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2017-11-21.
 * 通联支付撤销异常处理对话框
 */

public class PaymentOfAllinpayReturnDialogFragment extends BaseDialogFragment<PaymentOfAllinpayReturnDialogFragment.SetAllinPaymentReturnListener> {
    @BindView(R.id.allin_pay_number)
    EditText mAllinPayNumber;
    @BindView(R.id.number_delete)
    ImageView mNumberDelete;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;
    /**
     * 回掉接口
     */
    private SetAllinPaymentReturnListener mPaymentReturnListener;

    public static PaymentOfAllinpayReturnDialogFragment newInstance() {
        return new PaymentOfAllinpayReturnDialogFragment();
    }

    @OnClick({R.id.number_delete, R.id.btn_negative, R.id.btn_positive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.number_delete:
                mAllinPayNumber.setText("");
                break;
            case R.id.btn_negative:
                SoftKeyboardUtil.hideSoftKeyboardBySpecifiedView(getActivity(), mAllinPayNumber);
                dismiss();
                break;
            case R.id.btn_positive:
                SoftKeyboardUtil.hideSoftKeyboardBySpecifiedView(getActivity(), mAllinPayNumber);
                if (mPaymentReturnListener != null) {
                    mPaymentReturnListener.onReturnNumberChecked(mAllinPayNumber.getText().toString());
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface SetAllinPaymentReturnListener {
        void onReturnNumberChecked(String refNumber);
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {

    }

    @Override
    protected void setAttributesOnceCreate() {
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_allinpay_return;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        // 设置宽度为dialog_width*dialog_height，居中显示
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dishes_allin_pay_return_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        //设置点击软键盘以外的区域 软键盘消失
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getDecorView().setOnTouchListener((view, motionEvent) -> SoftKeyboardUtil.dispatchTouchEvent(getDialog(), motionEvent));
        mAllinPayNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                mNumberDelete.setVisibility(length == 0 ? View.GONE : View.VISIBLE);
                mBtnPositive.setEnabled(length != 0);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(SetAllinPaymentReturnListener setAllinPaymentReturnListener) {
        this.mPaymentReturnListener = setAllinPaymentReturnListener;
    }
}
