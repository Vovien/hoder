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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.DesignatedOneDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderServingDishesE;
import com.holderzone.intelligencepos.mvp.presenter.DesignatedOneDishesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 对单独菜品进行操作
 * Created by chencao on 2018/1/9.
 */

public class DesignatedOneDishesActivity extends BaseActivity<DesignatedOneDishesContract.Presenter> implements DesignatedOneDishesContract.View, TextWatcher {
    private static final String ARGUMENT_EXTRA_DISHES_COUNT_KEY = "ARGUMENT_EXTRA_DISHES_COUNT_KEY";
    private static final String ARGUMENT_EXTRA_GIFT_KEY = "ARGUMENT_EXTRA_GIFT_KEY";
    private static final String ARGUMENT_MAX_VALUE_KEY = "ARGUMENT_MAX_VALUE_KEY";
    private static final String ARGUMENT_EXTRA_TITLE_KEY = "ARGUMENT_EXTRA_TITLE_KEY";
    private static final String KEY_DISHES_GUIID = "KEY_DISHES_GUIID";
    public static final String RESULT_CODE_DATA_CHANGED = "RESULT_CODE_DATA_CHANGED";

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
    @BindView(R.id.order_front)
    LinearLayout orderFront;
    @BindView(R.id.order_dishes_sure)
    Button orderDishesSure;
    @BindView(R.id.order_button)
    LinearLayout orderButton;

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
     * 当前菜品guid
     */
    private String salesOrderBatchDishesGUID;
    /**
     * 当前选择的划菜数量
     */
    private double servingCount;

    /**
     * @param context                   上下文
     * @param title                     标题栏文字
     * @param currentCount              当前已选可划菜数量
     * @param maxCount                  当前可划菜总数量
     * @param gift                      是否赠送
     * @param salesOrderBatchDishesGUID 当前菜品guid
     * @return
     */
    public static Intent newIntent(Context context, String title, Double currentCount, Double maxCount, int gift, String salesOrderBatchDishesGUID) {
        Intent intent = new Intent(context, DesignatedOneDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_EXTRA_TITLE_KEY, title);
        bundle.putDouble(ARGUMENT_EXTRA_DISHES_COUNT_KEY, currentCount);
        bundle.putDouble(ARGUMENT_MAX_VALUE_KEY, maxCount);
        bundle.putInt(ARGUMENT_EXTRA_GIFT_KEY, gift);
        bundle.putString(KEY_DISHES_GUIID, salesOrderBatchDishesGUID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDishesName = extras.getString(ARGUMENT_EXTRA_TITLE_KEY);
        mCurrentDishesNumber = extras.getDouble(ARGUMENT_EXTRA_DISHES_COUNT_KEY);
        mIsGift = extras.getInt(ARGUMENT_EXTRA_GIFT_KEY);
        double max = extras.getDouble(ARGUMENT_MAX_VALUE_KEY, -1);
        if (max != -1) {
            mMaxDishesNumber = max;
        }
        servingCount = mMaxDishesNumber;
        salesOrderBatchDishesGUID = extras.getString(KEY_DISHES_GUIID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_change_number;
    }

    @Override
    protected DesignatedOneDishesContract.Presenter initPresenter() {
        return new DesignatedOneDishesPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //设置菜品名称
        if (mIsGift == 1) {
            //赠送菜品
            String one = "[赠]";
            SpannableString firstString = new SpannableString(one);
            firstString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(DesignatedOneDishesActivity.this, R.color.button_bg_green_normal)), 0, firstString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            orderDishesName.append(firstString);
            SpannableString secondString = new SpannableString(mDishesName);
            secondString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(DesignatedOneDishesActivity.this, R.color.common_text_color_555555)), 0, secondString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            orderDishesName.append(secondString);
        } else {
            orderDishesName.setText(mDishesName);
        }
        //设置菜品数量
        orderDishesCount.setText(getString(R.string.change_number_count_str, ArithUtil.stripTrailingZeros(mCurrentDishesNumber)));
        CharSequence charSequence = new SpanUtils().append("(下单：")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_707070))
                .append(ArithUtil.stripTrailingZeros(mCurrentDishesNumber))
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_2495ee))
                .append("，待上：")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_707070))
                .append(ArithUtil.stripTrailingZeros(servingCount))
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_f56766))
                .append(")")
                .setForegroundColor(ContextCompat.getColor(this, R.color.common_text_color_707070))
                .create();
        orderDishesUnit.setText(charSequence);
        //是否大于估清数量
        if (mMaxDishesNumber != null && Double.parseDouble(orderDishesCount.getText().toString()) > mMaxDishesNumber) {
            orderDishesCount.setTextColor(ContextCompat.getColor(this, R.color.text_order_dishes_no_print));
            orderDishesSure.setEnabled(false);
        }
        //EditText设置
        orderDishesCount.setInputType(InputType.TYPE_NULL);
        orderDishesCount.addTextChangedListener(this);
        orderDishesCount.setSelection(0, orderDishesCount.getText().length());
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    private void initTitle() {
        title.setTitleText(getString(R.string.designated_title));
        title.setOnReturnClickListener(() -> finishActivity());
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
                List<SalesOrderServingDishesE> salesOrderServingDishesEList = new ArrayList<>();
                SalesOrderServingDishesE salesOrderServingDishesE = new SalesOrderServingDishesE();
                salesOrderServingDishesE.setSalesOrderBatchDishesGUID(salesOrderBatchDishesGUID);
                salesOrderServingDishesE.setServingCount(servingCount);
                salesOrderServingDishesEList.add(salesOrderServingDishesE);
                mPresenter.updataSalesOrderBatchList(salesOrderServingDishesEList);
                break;
            default:
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
            orderDishesCount.setTextColor(ContextCompat.getColor(this, R.color.text_order_dishes_no_print));
            orderDishesSure.setEnabled(false);
        } else {
            orderDishesCount.setTextColor(ContextCompat.getColor(this, R.color.et_text));
            if (RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value)) {
                servingCount = Double.parseDouble(value);
                orderDishesSure.setEnabled(servingCount > 0);
            }

        }
        orderDishesCount.setText(value);
        if (value.length() == 0) {
            mCurrentDishesNumber = null;
        } else {
            mCurrentDishesNumber = Double.valueOf(value);
        }
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

    @Override
    public void onDispose() {

    }

    @Override
    public void updataSalesOrderBatchSuccess() {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(RESULT_CODE_DATA_CHANGED, true);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void updataSalesOrderBatchFail() {

    }
}
