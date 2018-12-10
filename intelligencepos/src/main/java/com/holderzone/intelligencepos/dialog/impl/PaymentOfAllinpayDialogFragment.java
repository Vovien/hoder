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
 * Created by LT on 2017-11-20.
 * 通联支付异常情况弹出框
 */

public class PaymentOfAllinpayDialogFragment extends BaseDialogFragment<PaymentOfAllinpayDialogFragment.SetPaymentNumberListener> {
    @BindView(R.id.allin_pay_trace_number)
    EditText mAllinPayTraceNumber;
    @BindView(R.id.trace_number_delete)
    ImageView mTraceNumberDelete;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;
    @BindView(R.id.allin_pay_ref_number)
    EditText mAllinPayRefNumber;
    @BindView(R.id.ref_number_delete)
    ImageView mRefNumberDelete;
    /**
     * 回调接口
     */
    private SetPaymentNumberListener mSetPaymentNumberListener;

    public static PaymentOfAllinpayDialogFragment getInstance() {
        return new PaymentOfAllinpayDialogFragment();
    }

    @OnClick({R.id.trace_number_delete, R.id.btn_negative, R.id.btn_positive, R.id.ref_number_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.trace_number_delete:
                mAllinPayTraceNumber.setText("");
                break;
            case R.id.ref_number_delete:
                mAllinPayRefNumber.setText("");
                break;
            case R.id.btn_negative:
                SoftKeyboardUtil.hideSoftKeyboardBySpecifiedView(getActivity(), mAllinPayTraceNumber);
                dismiss();
                break;
            case R.id.btn_positive:
                SoftKeyboardUtil.hideSoftKeyboardBySpecifiedView(getActivity(), mAllinPayTraceNumber);
                if (mSetPaymentNumberListener != null) {
                    mSetPaymentNumberListener.onPaymentNumberChecked(mAllinPayRefNumber.getText().toString(), mAllinPayTraceNumber.getText().toString());
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface SetPaymentNumberListener {
        void onPaymentNumberChecked(String refNumber, String traceNumber);
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
        return R.layout.dialog_fragment_allinpay;
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
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dishes_allin_pay_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        //设置点击软键盘以外的区域 软键盘消失
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getDecorView().setOnTouchListener((view, motionEvent) -> SoftKeyboardUtil.dispatchTouchEvent(getDialog(), motionEvent));
        mAllinPayTraceNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int traceLength = editable.length();
                int refLength = mAllinPayRefNumber.getText().toString().length();
                mTraceNumberDelete.setVisibility(traceLength == 0 ? View.GONE : View.VISIBLE);
                if (refLength > 0 && traceLength > 0) {
                    mBtnPositive.setEnabled(true);
                } else {
                    mBtnPositive.setEnabled(false);
                }
            }
        });
        mAllinPayRefNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int refLength = editable.length();
                int traceLength = mAllinPayTraceNumber.getText().toString().length();
                mRefNumberDelete.setVisibility(refLength == 0 ? View.GONE : View.VISIBLE);
                if (refLength > 0 && traceLength > 0) {
                    mBtnPositive.setEnabled(true);
                } else {
                    mBtnPositive.setEnabled(false);
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(SetPaymentNumberListener setPaymentNumberListener) {
        this.mSetPaymentNumberListener = setPaymentNumberListener;
    }
}
