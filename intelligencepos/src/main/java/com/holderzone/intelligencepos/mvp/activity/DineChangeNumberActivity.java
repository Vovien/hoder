package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.hall.activity.OrderDetailActivity;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-9-5.
 * 修改下单菜品数量
 */

public class DineChangeNumberActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.order_dishes_name)
    TextView orderDishesName;
    @BindView(R.id.order_dishes_unit)
    TextView orderDishesUnit;
    @BindView(R.id.order_dishes_count)
    EditText orderDishesCount;
    @BindView(R.id.order_dishes_count_delete)
    ImageView orderDishesCountDelete;
    @BindView(R.id.order_dishes_sure)
    Button orderDishesSure;
    public static final String ARGUMENT_EXTRA_DISHES_COUNT_KEY = "ARGUMENT_EXTRA_DISHES_COUNT_KEY";
    private static final String ARGUMENT_EXTRA_GIFT_KEY = "ARGUMENT_EXTRA_GIFT_KEY";
    private static final String ARGUMENT_MAX_VALUE_KEY = "ARGUMENT_MAX_VALUE_KEY";
    private static final String ARGUMENT_EXTRA_TITLE_KEY = "ARGUMENT_EXTRA_TITLE_KEY";
    public static final String DISHES_POSITION = "DISHES_POSITION";
    public static final int DISHES_RESULT_CODE = 105;
    public static final String DISHES_ITEM_POSITION = "DISHES_ITEM_POSITION";
    public static final String DISHES_ITEM_UNIT = "DISHES_ITEM_UNIT";
    public static final String ARGUMENT_EXTRA_HANG_KEY = "ARGUMENT_EXTRA_HANG_KEY";
    /**
     * 最大菜品数量
     */
    private Double mMaxDishesNumber;
    /**
     * 当前菜品数量
     */
    private Double mCurrentDishesNumber;
    /**
     * 是否是赠送菜品
     */
    private int mIsGift;

    /**
     * 菜品名称
     */
    private String mDishesName;
    /**
     * item位置
     */
    private int mPosition;
    /**
     * 菜品单位
     */
    private String mDishesUnit;
    /**
     * 是否是挂起菜品
     */
    private int mIsHang;

    public static Intent newIntent(Context context, String title, Double currentCount, Double maxCount,
                                   int gift, int position, String unit, int isHang) {
        Intent intent = new Intent(context, DineChangeNumberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_EXTRA_TITLE_KEY, title);
        bundle.putDouble(ARGUMENT_EXTRA_DISHES_COUNT_KEY, currentCount);
        bundle.putDouble(ARGUMENT_MAX_VALUE_KEY, maxCount);
        bundle.putInt(ARGUMENT_EXTRA_GIFT_KEY, gift);
        bundle.putInt(DISHES_POSITION, position);
        bundle.putString(DISHES_ITEM_UNIT, unit);
        bundle.putInt(ARGUMENT_EXTRA_HANG_KEY, isHang);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDishesName = extras.getString(ARGUMENT_EXTRA_TITLE_KEY);
        mCurrentDishesNumber = extras.getDouble(ARGUMENT_EXTRA_DISHES_COUNT_KEY);
        mIsGift = extras.getInt(ARGUMENT_EXTRA_GIFT_KEY);
        mPosition = extras.getInt(DISHES_POSITION);
        mIsHang = extras.getInt(ARGUMENT_EXTRA_HANG_KEY);
        double max = extras.getDouble(ARGUMENT_MAX_VALUE_KEY, -1);
        if (max != -1) {
            mMaxDishesNumber = max;
        }
        mDishesUnit = extras.getString(DISHES_ITEM_UNIT);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_dine_change_number;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //设置菜品名称
        if (mIsHang == 0) {
            //挂起
            String hang = "[挂]";
            SpannableString hangString = new SpannableString(hang);
            hangString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(DineChangeNumberActivity.this, R.color.tv_text_green_01b6ad)),
                    0, hangString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            orderDishesName.append(hangString);
        }
        if (mIsGift == 1) {
            //赠送菜品
            String one = "[赠]";
            SpannableString firstString = new SpannableString(one);
            firstString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(DineChangeNumberActivity.this, R.color.tv_text_green_01b6ad)), 0, firstString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            orderDishesName.append(firstString);
        }
        SpannableString secondString = new SpannableString(mDishesName);
        secondString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(DineChangeNumberActivity.this, R.color.tv_text_gray_555555)), 0, secondString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        orderDishesName.append(secondString);
        //设置菜品数量
        orderDishesCount.setText(getString(R.string.change_number_count_str, ArithUtil.stripTrailingZeros(mCurrentDishesNumber)));
        orderDishesUnit.setText("(" + mDishesUnit + ")");
        //是否大于估清数量
        if (mMaxDishesNumber != null && Double.parseDouble(orderDishesCount.getText().toString()) > mMaxDishesNumber) {
            orderDishesCount.setTextColor(ContextCompat.getColor(this, R.color.tv_text_red_f56766));
            orderDishesSure.setEnabled(false);
        }
        //EditText设置
        orderDishesCount.setInputType(InputType.TYPE_NULL);
        orderDishesCount.addTextChangedListener(this);
        orderDishesCount.setSelection(0, orderDishesCount.getText().length());
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

    private void initTitle() {
        title.setTitleText(getString(R.string.change_number_title));
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        orderDishesCountDelete.setVisibility(editable.toString().length() == 0 ? View.GONE : View.VISIBLE);
    }

    @OnClick({R.id.order_dishes_count_delete, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete, R.id.order_dishes_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_dishes_count_delete:
                orderDishesCount.setText("");
                orderDishesSure.setEnabled(false);
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
                keyBoardSetting(".");
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("-");
                break;
            case R.id.order_dishes_sure:
                Intent intent = new Intent(this, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble(ARGUMENT_EXTRA_DISHES_COUNT_KEY, mCurrentDishesNumber);
                bundle.putInt(DISHES_ITEM_POSITION, mPosition);
                intent.putExtras(bundle);
                setResult(DISHES_RESULT_CODE, intent);
                finishActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 键盘输入设置
     */
    private void keyBoardSetting(String inputNumber) {
        String value = orderDishesCount.getText().toString();
        if (orderDishesCount.getSelectionEnd() - orderDishesCount.getSelectionStart() == orderDishesCount.getText().length()) {
            value = "";
        }
        if ("-".equals(inputNumber)) {
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 1);
            }
        } else if (RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value + inputNumber)) {
            value = value + inputNumber;
        }
        if (mMaxDishesNumber != null && RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value) && Double.parseDouble(value) > mMaxDishesNumber) {
            orderDishesCount.setTextColor(ContextCompat.getColor(this, R.color.tv_text_red_f56766));
            orderDishesSure.setEnabled(false);
        } else {
            orderDishesCount.setTextColor(ContextCompat.getColor(this, R.color.et_text));
            orderDishesSure.setEnabled(RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value) && Double.parseDouble(value) > 0);
        }
        orderDishesCount.setText(value);
        if (value.length() == 0) {
            mCurrentDishesNumber = null;
        } else {
            mCurrentDishesNumber = Double.valueOf(value);
        }
    }
}
