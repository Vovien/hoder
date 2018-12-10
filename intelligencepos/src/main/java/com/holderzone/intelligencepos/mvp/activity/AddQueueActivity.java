package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.AddQueueContract;
import com.holderzone.intelligencepos.mvp.presenter.AddQueuePresenter;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 新增排队 Activity
 */
public class AddQueueActivity extends BaseActivity<AddQueueContract.Presenter> implements AddQueueContract.View {
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_return)
    LinearLayout llReturn;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @BindView(R.id.tv_dining_number)
    TextView tvDiningNumber;
    @BindView(R.id.btn_number_1)
    Button btnNumber1;
    @BindView(R.id.btn_number_2)
    Button btnNumber2;
    @BindView(R.id.btn_number_3)
    Button btnNumber3;
    @BindView(R.id.btn_number_4)
    Button btnNumber4;
    @BindView(R.id.btn_number_5)
    Button btnNumber5;
    @BindView(R.id.btn_number_6)
    Button btnNumber6;
    @BindView(R.id.btn_number_7)
    Button btnNumber7;
    @BindView(R.id.btn_number_8)
    Button btnNumber8;
    @BindView(R.id.btn_number_9)
    Button btnNumber9;
    @BindView(R.id.btn_number_dot)
    Button btnNumberDot;
    @BindView(R.id.btn_number_0)
    Button btnNumber0;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.btn_take_the_number)
    Button btnTakeTheNumber;

    private boolean mPhoneNumberSelected;

    private StringBuilder mSbPhoneNumber = new StringBuilder();
    private StringBuilder mSbDiningNumber = new StringBuilder();

    public static Intent newInstance(Context context) {
        return new Intent(context, AddQueueActivity.class);
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_add_queue;
    }

    @Nullable
    @Override
    protected AddQueueContract.Presenter initPresenter() {
        return new AddQueuePresenter(this);
    }


    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        llMenu.setVisibility(View.GONE);
        ivSearch.setVisibility(View.GONE);
        tvTitle.setText("取号");
        btnNumberDot.setText(getString(R.string.selected_org_number_clean));
        btnNumberDot.setTextSize(18.7f);
        btnTakeTheNumber.setEnabled(false);

        // 设置电话号码默认选中
        tvPhoneNumber.setText(mSbPhoneNumber);
        tvDiningNumber.setText(mSbDiningNumber);
        if (mPhoneNumberSelected) {
            tvPhoneNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
        } else {
            tvDiningNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
        }

        RxView.clicks(btnTakeTheNumber).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(o -> {
            // 取消轮询、dispose请求、手动操作标志置true
            if (mSbDiningNumber.length() > 0) {
                mPresenter.submitAdd(Integer.valueOf(mSbDiningNumber.toString()), mSbPhoneNumber.toString().replaceAll(" ", ""));
            }
        });


    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.tv_phone_number, R.id.tv_dining_number, R.id.iv_return,R.id.tv_title, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_phone_number:
                setSelectInput(true);
                break;
            case R.id.tv_dining_number:
                setSelectInput(false);
                break;
            case R.id.iv_return:
            case R.id.tv_title:
                finishActivityEvent();
                break;
            case R.id.btn_number_1:
                keyBoardSetting("1");
                break;
            case R.id.btn_number_2:
                keyBoardSetting("2");
                break;
            case R.id.btn_number_3:
                keyBoardSetting("3");
                break;
            case R.id.btn_number_4:
                keyBoardSetting("4");
                break;
            case R.id.btn_number_5:
                keyBoardSetting("5");
                break;
            case R.id.btn_number_6:
                keyBoardSetting("6");
                break;
            case R.id.btn_number_7:
                keyBoardSetting("7");
                break;
            case R.id.btn_number_8:
                keyBoardSetting("8");
                break;
            case R.id.btn_number_9:
                keyBoardSetting("9");
                break;
            case R.id.btn_number_dot:
                keyBoardSetting("clear");
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("del");
                break;
        }
    }

    /**
     * 数字键盘输入
     *
     * @param number
     */
    private void keyBoardSetting(String number) {
        if (mPhoneNumberSelected) {
            int length = mSbPhoneNumber.length();
            if ("clear".equalsIgnoreCase(number)) {
                mSbPhoneNumber.delete(0, length);
            } else if ("del".equalsIgnoreCase(number)) {
                if (length == 0) {
                    return;
                }
                mSbPhoneNumber.deleteCharAt(--length);
                if (length == 3 || length == 8) {
                    mSbPhoneNumber.deleteCharAt(--length);
                }
            } else {
                if (length > 12) {
                    return;
                }
                mSbPhoneNumber.append(number);
                if (length == 2 || length == 7) {
                    mSbPhoneNumber.append(" ");
                }
                if (mSbPhoneNumber.length() > 12 && TextUtils.isEmpty(tvDiningNumber.getText())) {
                    mPhoneNumberSelected = false;
                    tvPhoneNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg);
                    tvDiningNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
                }
            }
            tvPhoneNumber.setText(mSbPhoneNumber);
        } else {
            int length = mSbDiningNumber.length();
            if ("clear".equalsIgnoreCase(number)) {
                mSbDiningNumber.delete(0, length);
            } else if ("del".equalsIgnoreCase(number)) {
                if (length == 0) {
                    return;
                }
                mSbDiningNumber.deleteCharAt(length - 1);
            } else {
                if (mSbDiningNumber.length() > 2) {
                    return;
                }
                mSbDiningNumber.append(number);
            }
            tvDiningNumber.setText(mSbDiningNumber);
        }
        modifyTakeTheNumberBtnStatus();
    }

    /**
     * 实时修改取号按钮状态
     */
    private void modifyTakeTheNumberBtnStatus() {
        String phoneNumber = mSbPhoneNumber.toString().replaceAll(" ", "");
        String diningNumber = mSbDiningNumber.toString();

        if ((TextUtils.isEmpty(phoneNumber) || RegexExUtils.isMobile(phoneNumber))
                && !TextUtils.isEmpty(diningNumber) && Integer.valueOf(diningNumber) > 0) {
            btnTakeTheNumber.setEnabled(true);
        } else {
            btnTakeTheNumber.setEnabled(false);
        }
    }

    @Override
    public void onAddSuccess() {
        // 清空输入框
        mSbPhoneNumber.delete(0, mSbPhoneNumber.length());
        mSbDiningNumber.delete(0, mSbDiningNumber.length());
        tvPhoneNumber.setText(mSbPhoneNumber);
        tvDiningNumber.setText(mSbDiningNumber);
        finishActivityEvent();
    }

    private void finishActivityEvent() {
        setResult(RESULT_OK, getIntent());
        finishActivity();
    }

    @Override
    public void onAddFailed() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivityEvent();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setSelectInput(boolean phoneNumberSelected) {
        if (phoneNumberSelected) {
            mPhoneNumberSelected = true;
            tvPhoneNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
            tvDiningNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg);
        } else {
            mPhoneNumberSelected = false;
            tvPhoneNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg);
            tvDiningNumber.setBackgroundResource(R.drawable.shape_edit_corner_bg_focused);
        }

    }
}
