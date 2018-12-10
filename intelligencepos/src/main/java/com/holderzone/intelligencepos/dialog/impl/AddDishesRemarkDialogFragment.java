package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.utils.keyboard.SoftKeyboardUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 添加菜品备注
 * Created by LiTao on 2017-9-5.
 */

public class AddDishesRemarkDialogFragment extends BaseDialogFragment<AddDishesRemarkDialogFragment.AddRemarkClickListener> {
    @BindView(R.id.add_remark_et)
    EditText addRemarkEt;
    @BindView(R.id.add_remark_delete)
    ImageView addRemarkDelete;
    @BindView(R.id.add_remark_top)
    LinearLayout addRemarkTop;
    @BindView(R.id.btn_negative)
    Button btnNegative;
    @BindView(R.id.btn_positive)
    Button btnPositive;
    private AddRemarkClickListener addRemarkClickListener;
    private static final String REMARK_KEY = "REMARK_KEY";
    private static final String REMARK_HINT_KEY = "REMARK_HINT_KEY";
    /**
     * 备注
     */
    private String mRemark;
    /**
     * EditText提示
     */
    private String mHint;

    public static AddDishesRemarkDialogFragment getInstance(String remark, String hint) {
        AddDishesRemarkDialogFragment addDishesRemarkDialog = new AddDishesRemarkDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(REMARK_KEY, remark);
        bundle.putSerializable(REMARK_HINT_KEY, hint);
        addDishesRemarkDialog.setArguments(bundle);
        return addDishesRemarkDialog;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mRemark = args.getString(REMARK_KEY);
        mHint = args.getString(REMARK_HINT_KEY);
    }

    @Override
    protected void setAttributesOnceCreate() {
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_add_dishes_remark;
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
        wlp.height = getResources().getDimensionPixelSize(R.dimen.add_dishes_remark_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        //设置点击软键盘以外的区域 软键盘消失
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getDialog().getWindow().getDecorView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return SoftKeyboardUtil.dispatchTouchEvent(getDialog(), event);
            }
        });
        addRemarkEt.setHint(mHint);
        addRemarkEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (!RegexExUtils.isDishesRemarksInputLegal(editable)) {
                    editable.delete(length - 1, length);
                    return;
                }
                if (RegexExUtils.isDishesRemarks(editable)) {
                    btnPositive.setEnabled(true);
                    addRemarkDelete.setVisibility(View.VISIBLE);
                } else {
                    btnPositive.setEnabled(false);
                    addRemarkDelete.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void initData() {
        if (mRemark != null && !"".equalsIgnoreCase(mRemark)) {
            addRemarkEt.setText(mRemark);
            addRemarkEt.setSelection(addRemarkEt.getText().length());
        }
    }

    @Override
    public void setDialogListener(AddRemarkClickListener addRemarkClickListener) {
        this.addRemarkClickListener = addRemarkClickListener;
    }


    //回调接口
    public interface AddRemarkClickListener {
        void onRemarkAdd(String remark);
    }

    @OnClick({R.id.add_remark_delete, R.id.btn_negative, R.id.btn_positive})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_remark_delete:
                addRemarkEt.setText("");
                break;
            case R.id.btn_negative:
                SoftKeyboardUtil.hideSoftKeyboardBySpecifiedView(getActivity(), addRemarkEt);
                dismiss();
                break;
            case R.id.btn_positive:
                SoftKeyboardUtil.hideSoftKeyboardBySpecifiedView(getActivity(), addRemarkEt);
                addRemarkClickListener.onRemarkAdd(addRemarkEt.getText().toString());
                dismiss();
                break;
            default:
                break;
        }
    }
}
