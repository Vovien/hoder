package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
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
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-9-6.
 * 修改退菜数量
 */

public class ChangeRetreatNumberActivity extends BaseActivity implements TextWatcher {
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.order_dishes_name)
    TextView mOrderDishesName;
    @BindView(R.id.order_dishes_unit)
    TextView mOrderDishesUnit;
    @BindView(R.id.order_dishes_count)
    EditText mOrderDishesCount;
    @BindView(R.id.order_dishes_count_delete)
    ImageView mOrderDishesCountDelete;
    @BindView(R.id.order_dishes_sure)
    Button mOrderDishesSure;
    private static final String ARGUMENT_EXTRA_GIFT_KEY = "ARGUMENT_EXTRA_GIFT_KEY";
    private static final String ARGUMENT_MAX_VALUE_KEY = "ARGUMENT_MAX_VALUE_KEY";
    private static final String ARGUMENT_EXTRA_SALES_ORDER_BATCH_DISHES_GUID_KEY = "ARGUMENT_EXTRA_SALES_ORDER_BATCH_DISHES_GUID_KEY";
    private static final String ARGUMENT_EXTRA_TITLE_KEY = "ARGUMENT_EXTRA_TITLE_KEY";
    private static final String DISHES_ORDER_NUMBER = "DISHES_ITEM_UNIT";
    private static final String ARGUMENT_EXTRA_HANG_KEY = "ARGUMENT_EXTRA_HANG_KEY";
    public static final String RESULT_CODE_CHANGED_NUMBER = "RESULT_CODE_CHANGED_NUMBER";
    public static final String RESULT_CODE_STORED_GUID = "RESULT_CODE_STORED_GUID";
    public static final String RESULT_CODE_MINUS_STOCK = "RESULT_CODE_MINUS_STOCK";
    public static final String RESULT_CODE_RETREAT_NUMBER = "RESULT_CODE_RETREAT_NUMBER";

    @BindView(R.id.change_retreat_number_switch)
    SwitchCompat mChangeRetreatNumberSwitch;
    /**
     * 最大菜品数量(也是当前显示的菜品数量)
     */
    private Double mMaxDishesNumber;
    /**
     * 是否是挂起菜品
     */
    private int mIsHang;
    /**
     * 是否是赠送菜品
     */
    private int mIsGift;

    /**
     * 操作菜品GUID
     */
    private String mSalesOrderBatchDishesGUID;

    /**
     * 菜品名称
     */
    private String mDishesName;
    /**
     * 下单数量
     */
    private double mOrderNumber;
    /**
     * 退菜数量
     */
    private double mRetreatNumber;
    /**
     * 是否开启扣减库存 1：开启 0：关闭
     */
    private int mMinusStock;

    /**
     * 启动ChangeRetreatNumberActivity
     *
     * @param context       上下文
     * @param title         菜品名称
     * @param maxCount      最大数量
     * @param gift          标记是否是赠送菜品
     * @param isHang        标记是否是挂起菜品
     * @param orderNumber   下单数量
     * @param minusStock    是否开启扣减库存 0:关闭 1：开启
     * @param retreatNumber 退菜数量
     */
    public static Intent newIntent(Context context, String salesOrderBatchDishesGUID, String title, Double maxCount,
                                   int isHang, int gift, Double orderNumber, int minusStock, double retreatNumber) {
        Intent intent = new Intent(context, ChangeRetreatNumberActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_EXTRA_SALES_ORDER_BATCH_DISHES_GUID_KEY, salesOrderBatchDishesGUID);
        bundle.putString(ARGUMENT_EXTRA_TITLE_KEY, title);
        bundle.putDouble(ARGUMENT_MAX_VALUE_KEY, maxCount);
        bundle.putInt(ARGUMENT_EXTRA_GIFT_KEY, gift);
        bundle.putDouble(DISHES_ORDER_NUMBER, orderNumber);
        bundle.putInt(ARGUMENT_EXTRA_HANG_KEY, isHang);
        bundle.putInt(RESULT_CODE_MINUS_STOCK, minusStock);
        bundle.putDouble(RESULT_CODE_RETREAT_NUMBER, retreatNumber);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderBatchDishesGUID = extras.getString(ARGUMENT_EXTRA_SALES_ORDER_BATCH_DISHES_GUID_KEY);
        mDishesName = extras.getString(ARGUMENT_EXTRA_TITLE_KEY);
        mIsGift = extras.getInt(ARGUMENT_EXTRA_GIFT_KEY);
        mIsHang = extras.getInt(ARGUMENT_EXTRA_HANG_KEY);
        mMaxDishesNumber = extras.getDouble(ARGUMENT_MAX_VALUE_KEY, -1);
        mOrderNumber = extras.getDouble(DISHES_ORDER_NUMBER);
        mMinusStock = extras.getInt(RESULT_CODE_MINUS_STOCK);
        mRetreatNumber = extras.getDouble(RESULT_CODE_RETREAT_NUMBER);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_change_retreat_number;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化Title
        initTitle();
        //设置菜品名称
        if (mIsHang == 0) {
            //挂起
            String hang = "[挂]";
            SpannableString hangString = new SpannableString(hang);
            hangString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ChangeRetreatNumberActivity.this, R.color.layout_bg_orange_f4a902)),
                    0, hangString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mOrderDishesName.append(hangString);
        }
        if (mIsGift == 1) {
            //赠送菜品
            String one = "[赠]";
            SpannableString firstString = new SpannableString(one);
            firstString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ChangeRetreatNumberActivity.this, R.color.tv_text_green_01b6ad)), 0, firstString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            mOrderDishesName.append(firstString);
        }
        //是否开启扣减库存
        mChangeRetreatNumberSwitch.setChecked(mMinusStock == 1);
        SpannableString secondString = new SpannableString(mDishesName);
        secondString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ChangeRetreatNumberActivity.this, R.color.tv_text_gray_555555)), 0, secondString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        mOrderDishesName.append(secondString);
        //设置菜品数量
        mOrderDishesCount.setText(getString(R.string.change_number_count_str, ArithUtil.stripTrailingZeros(mRetreatNumber > 0 ? mRetreatNumber : mOrderNumber)));
        //设置菜品下单数量
        mOrderDishesUnit.setText("(" + "下单:" + ArithUtil.stripTrailingZeros(mOrderNumber) + ")");
        //EditText设置
        mOrderDishesCount.setInputType(InputType.TYPE_NULL);
        mOrderDishesCount.addTextChangedListener(this);
        mOrderDishesCount.setSelection(0, mOrderDishesCount.getText().length());
        mOrderDishesCount.setHighlightColor(ContextCompat.getColor(this,R.color.edit_text_9cd5d5));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mChangeRetreatNumberSwitch.setOnCheckedChangeListener((compoundButton, b) -> mMinusStock = b ? 1 : 0);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.order_dishes_count_delete, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3,
            R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8,
            R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete, R.id.order_dishes_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.order_dishes_count_delete:
                mOrderDishesCount.setText("");
                mOrderDishesSure.setEnabled(false);
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
                String value = mOrderDishesCount.getText().toString();
                Double number = Double.parseDouble(value);
                Intent intent = new Intent(this, RetreatDishesActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(RESULT_CODE_STORED_GUID, mSalesOrderBatchDishesGUID);
                bundle.putDouble(RESULT_CODE_CHANGED_NUMBER, number);
                bundle.putInt(RESULT_CODE_MINUS_STOCK, mMinusStock);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * title设置
     */
    private void initTitle() {
        title.setTitleText("退菜数量");
        title.setOnReturnClickListener(() -> finishActivity());
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        mOrderDishesCountDelete.setVisibility(editable.toString().length() == 0 ? View.GONE : View.VISIBLE);
        if (!"".equalsIgnoreCase(editable.toString())) {
            Double value = Double.parseDouble(editable.toString());
            mOrderDishesSure.setEnabled(value != 0);
        } else {
            mOrderDishesSure.setEnabled(false);
        }
    }

    /**
     * 键盘输入设置
     */
    private void keyBoardSetting(String inputNumber) {
        String value = mOrderDishesCount.getText().toString();
        if (mOrderDishesCount.getSelectionEnd() - mOrderDishesCount.getSelectionStart() == mOrderDishesCount.getText().length()) {
            value = "";
        }
        if ("-".equals(inputNumber)) {
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 1);
            }
        } else if (RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value + inputNumber)) {
            value = value + inputNumber;
        }
        if (RegexExUtils.isDecimal(value)) {
            Double cur = Double.valueOf(value);
            if (ArithUtil.sub(cur, mMaxDishesNumber) > 0) {
                value = value.substring(0, value.length() - 1);
            }
        }
        mOrderDishesCount.setText(value);
    }
}
