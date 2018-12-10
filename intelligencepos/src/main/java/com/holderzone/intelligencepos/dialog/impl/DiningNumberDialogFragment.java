package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;

import butterknife.BindView;

public class DiningNumberDialogFragment extends BaseDialogFragment<DiningNumberDialogFragment.DiningNumberDialogListener> {
    public static final String ARGUMENT_EXTRA_TITLE_KEY = "ARGUMENT_EXTRA_TITLE_KEY";
    public static final String ARGUMENT_EXTRA_DINING_NUMBER_KEY = "ARGUMENT_EXTRA_DINING_NUMBER_KEY";
    public static final String ARGUMENT_EXTRA_DINING_NUMBER_MIN_KEY = "ARGUMENT_EXTRA_DINING_NUMBER_MIN_KEY";
    public static final String ARGUMENT_EXTRA_DINING_NUMBER_MAX_KEY = "ARGUMENT_EXTRA_DINING_NUMBER_MAX_KEY";
    public static final String ARGUMENT_EXTRA_LEFT_BTN_TEXT_KEY = "ARGUMENT_EXTRA_LEFT_BTN_TEXT_KEY";
    public static final String ARGUMENT_EXTRA_RIGHT_BTN_TEXT_KEY = "ARGUMENT_EXTRA_RIGHT_BTN_TEXT_KEY";
    public static final String ARGUMENT_EXTRA_STEP = "ARGUMENT_EXTRA_STEP";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_msg)
    TextView mTvMsg;
    @BindView(R.id.iv_close)
    ImageView mIvClose;
    @BindView(R.id.iv_sub)
    ImageView mIvSub;
    @BindView(R.id.et_dining_number)
    EditText mEtDiningNumber;
    @BindView(R.id.iv_plus)
    ImageView mIvPlus;
    @BindView(R.id.btn_negative)
    Button mBtnNegative;
    @BindView(R.id.btn_positive)
    Button mBtnPositive;

    private int mCurDiningNumber = 1;
    private String mTitle = "标题";
    private int mDiningNumber = 999;
    private int mMinDiningNumber = 1;
    private int mMaxDiningNumber = 999;
    private int mStep = 1;
    private String mLeftBtnText;
    private String mRightBtnText;

    private DiningNumberDialogListener mDialogListener;


    public static DiningNumberDialogFragment newInstance(String title, int number, int minNumber, int maxNumber, int step, String leftBtnText, String rightBtnText){
        Bundle args = new Bundle();
        args.putString(ARGUMENT_EXTRA_TITLE_KEY, title);
        args.putInt(ARGUMENT_EXTRA_DINING_NUMBER_KEY, number);
        args.putInt(ARGUMENT_EXTRA_DINING_NUMBER_MIN_KEY, minNumber);
        args.putInt(ARGUMENT_EXTRA_DINING_NUMBER_MAX_KEY, maxNumber);
        if (step < 1) {
            step = 1;
        }
        args.putInt(ARGUMENT_EXTRA_STEP, step);
        args.putString(ARGUMENT_EXTRA_LEFT_BTN_TEXT_KEY, leftBtnText);
        args.putString(ARGUMENT_EXTRA_RIGHT_BTN_TEXT_KEY, rightBtnText);
        DiningNumberDialogFragment fragment = new DiningNumberDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected void parseArgumentExtra(Bundle args) {
        mTitle = args.getString(ARGUMENT_EXTRA_TITLE_KEY);
        mDiningNumber = args.getInt(ARGUMENT_EXTRA_DINING_NUMBER_KEY);
        mMinDiningNumber = args.getInt(ARGUMENT_EXTRA_DINING_NUMBER_MIN_KEY);
        mMaxDiningNumber = args.getInt(ARGUMENT_EXTRA_DINING_NUMBER_MAX_KEY);
        mLeftBtnText = args.getString(ARGUMENT_EXTRA_LEFT_BTN_TEXT_KEY);
        mRightBtnText = args.getString(ARGUMENT_EXTRA_RIGHT_BTN_TEXT_KEY);
        mStep = args.getInt(ARGUMENT_EXTRA_STEP);
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_dining_number;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 处理取消事件的回调
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {
        mTvTitle.setText(mTitle);
        mTvMsg.setText(mMinDiningNumber + "-" + mMaxDiningNumber + "人");
        mEtDiningNumber.setText(String.valueOf(mCurDiningNumber = mDiningNumber));
        mBtnNegative.setText(mLeftBtnText);
        mBtnPositive.setText(mRightBtnText);
        checkGuestNumber(mCurDiningNumber);

        mIvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mIvSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int guestNum = Integer.valueOf(mEtDiningNumber.getText().toString());
                if (guestNum > mMinDiningNumber) {
                    mCurDiningNumber = guestNum - mStep;
                    mEtDiningNumber.setText(String.valueOf(mCurDiningNumber));
                    mEtDiningNumber.setSelection(mEtDiningNumber.getText().length());
                    checkGuestNumber(mCurDiningNumber);
                } else {
                    BaseApplication.showMessage("最少" + mMinDiningNumber + "人");
                }
            }
        });
        mEtDiningNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    int guestNumber = Integer.valueOf(s.toString());
                    if (!checkGuestNumber(guestNumber)) {
                        BaseApplication.showMessage("最少1人，最多" + mMaxDiningNumber + "人");
                        mEtDiningNumber.setText(String.valueOf(mCurDiningNumber));
                        mEtDiningNumber.setSelection(mEtDiningNumber.getText().length());
                    } else {
                        mCurDiningNumber = guestNumber;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mIvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int guestNum = Integer.valueOf(mEtDiningNumber.getText().toString());
                if (guestNum < mMaxDiningNumber) {
                    mCurDiningNumber = guestNum + mStep;
                    mEtDiningNumber.setText(String.valueOf(mCurDiningNumber));
                    mEtDiningNumber.setSelection(mEtDiningNumber.getText().length());
                    checkGuestNumber(mCurDiningNumber);
                } else {
                    BaseApplication.showMessage("最多" + mMaxDiningNumber + "人");
                }
            }
        });
        mBtnNegative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onDiningNumberDialogLeftBtnClick(Integer.valueOf(mEtDiningNumber.getText().toString()));
                }
            }
        });
        mBtnPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mDialogListener != null) {
                    mDialogListener.onDiningNumberDialogRightBtnClick(Integer.valueOf(mEtDiningNumber.getText().toString()));
                }
            }
        });
    }

    @Override
    protected void initData() {

    }

    private boolean checkGuestNumber(int guestNumber) {
        if (guestNumber < mMinDiningNumber) {
            return false;
        } else if (guestNumber == mMinDiningNumber) {
            if (mIvSub.isEnabled()) {
                mIvSub.setEnabled(false);
            }
            if (!mIvPlus.isEnabled()) {
                mIvPlus.setEnabled(true);
            }
            return true;
        } else if (guestNumber < mMaxDiningNumber) {
            if (!mIvSub.isEnabled()) {
                mIvSub.setEnabled(true);
            }
            if (!mIvPlus.isEnabled()) {
                mIvPlus.setEnabled(true);
            }
            return true;
        } else if (guestNumber == mMaxDiningNumber) {
            if (!mIvSub.isEnabled()) {
                mIvSub.setEnabled(true);
            }
            if (mIvPlus.isEnabled()) {
                mIvPlus.setEnabled(false);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setDialogListener(DiningNumberDialogListener diningNumberDialogListener) {
        mDialogListener = diningNumberDialogListener;
    }

    public interface DiningNumberDialogListener {
        void onDiningNumberDialogLeftBtnClick(int number);

        void onDiningNumberDialogRightBtnClick(int number);
    }
}
