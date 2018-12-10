package com.holderzone.intelligencepos.dialog.impl;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexExUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2018-04-17.
 * 修改点菜数量对话框
 */

public class ChangeOrderDishesCountDialogFragment extends BaseDialogFragment<ChangeOrderDishesCountDialogFragment.ChangeOrderDishesCountListener> {
    private static final String ARGS_CURRENT_COUNT = "ARGS_CURRENT_COUNT";
    private static final String ARGS_MAX_COUNT = "ARGS_MAX_COUNT";
    private static final String ARGS_SALES_DISHES = "ARGS_SALES_DISHES";
    private static final String ARGS_TYPE = "ARGS_TYPE";
    private static final String ARGS_SHOW_TYPE = "ARGS_SHOW_TYPE";
    private static final String TYPE_COMMON = "TYPE_COMMON";
    private static final String TYPE_PACKAGE = "TYPE_PACKAGE";
    private static final String SHOW_TYPE_PRACTICE = "SHOW_TYPE_PRACTICE";
    private static final String SHOW_TYPE_COMMON = "SHOW_TYPE_COMMON";
    @BindView(R.id.order_dishes_count)
    EditText mOrderDishesCountET;
    @BindView(R.id.change_count_confirm)
    TextView mChangeCountConfirm;
    @BindView(R.id.btn_number_dot)
    Button btnNumberDot;
    /**
     * 当前点击的菜品数量
     */
    private Double mCurrentCount;
    /**
     * 记录当前选中的菜品本身的数量
     */
    private Double mCurrentDishesCount;
    /**
     * 菜品能点击的最大数量
     */
    private Double mMaxCount;
    /**
     * 菜品数据
     */
    private SalesOrderBatchDishesE mSalesOrderBatchDishesE;
    /**
     * 接口
     */
    private ChangeOrderDishesCountListener listener;
    /**
     * 类型
     */
    private String mType;
    /**
     * 显示类型
     */
    private String mShowType;
    /**
     * 是否点击确定按钮
     */
    private boolean isConfirm = false;

    public static ChangeOrderDishesCountDialogFragment newInstance(SalesOrderBatchDishesE salesOrderBatchDishesE, double currentCount
            , double maxCount, String type, String showType) {
        Bundle args = new Bundle();
        args.putDouble(ARGS_CURRENT_COUNT, currentCount);
        args.putDouble(ARGS_MAX_COUNT, maxCount);
        args.putParcelable(ARGS_SALES_DISHES, salesOrderBatchDishesE);
        args.putString(ARGS_TYPE, type);
        args.putString(ARGS_SHOW_TYPE, showType);
        ChangeOrderDishesCountDialogFragment fragment = new ChangeOrderDishesCountDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mCurrentDishesCount = mCurrentCount = args.getDouble(ARGS_CURRENT_COUNT);
        mSalesOrderBatchDishesE = args.getParcelable(ARGS_SALES_DISHES);
        double max = args.getDouble(ARGS_MAX_COUNT, -1);
        if (max != -1) {
            mMaxCount = max;
        }
        mType = args.getString(ARGS_TYPE);
        mShowType = args.getString(ARGS_SHOW_TYPE);
    }

    @Override
    protected void setAttributesOnceCreate() {
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_change_order_dishes_count;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(true);
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.setWindowAnimations(R.style.take_photo_anim);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dialog_add_description_height);
        window.setAttributes(wlp);
        if (getActivity() != null) {
            setBackgroundAlpha(getActivity(), 0.5f);
        }
    }

    /**
     * 设置页面的透明度
     *
     * @param bgAlpha 1表示不透明
     */
    public static void setBackgroundAlpha(Activity activity, float bgAlpha) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        activity.getWindow().setAttributes(lp);
    }

    @Override
    protected void initView() {
        //取消按钮的监听
        getDialog().setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (getActivity() != null) {
                    callBackByCancel();
                    setBackgroundAlpha(getActivity(), 1f);
                }
            }
        });
        //初始化EditText的内容
        if (mCurrentCount > 0) {
            mOrderDishesCountET.setText(String.valueOf(ArithUtil.stripTrailingZeros(mCurrentCount)));
        }
        //根据Type判断字体显示
        if (mType.equals(TYPE_COMMON)) {
            btnNumberDot.setText(".");
        } else if (mType.equals(TYPE_PACKAGE)) {
            btnNumberDot.setText("清空");
            btnNumberDot.setTextSize(21f);
        }
        mOrderDishesCountET.setInputType(InputType.TYPE_NULL);
        mOrderDishesCountET.setSelection(0, mOrderDishesCountET.getText().length());
        //EditText的改变事件
        mOrderDishesCountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(ChangeOrderDishesCountListener changeOrderDishesCountListener) {
        listener = changeOrderDishesCountListener;
    }


    @OnClick({R.id.change_count_cancel, R.id.btn_number_1, R.id.btn_number_2
            , R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7
            , R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete
            , R.id.change_count_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.change_count_cancel:
                isConfirm = false;
                dismiss();
                break;
            case R.id.change_count_confirm:
                isConfirm = true;
                dismiss();
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
                switch (mType) {
                    case TYPE_COMMON:
                        keyBoardSetting(".");
                        break;
                    case TYPE_PACKAGE:
                        //清空
                        mOrderDishesCountET.setText("");
                        mChangeCountConfirm.setEnabled(false);
                        break;
                    default:
                }
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("-");
                break;
            default:
        }
    }

    /**
     * 点击取消时的回调  避免出现数量0 的项
     */
    private void callBackByCancel() {
        if (listener != null) {
            if (isConfirm){
                if (mShowType.equals(SHOW_TYPE_COMMON)) {
                    listener.onDishesCountChanged(mSalesOrderBatchDishesE, mCurrentCount);
                } else if (mShowType.equals(SHOW_TYPE_PRACTICE)) {
                    listener.onPracticeCountChanged(mCurrentCount);
                }
            }else {
                if (mShowType.equals(SHOW_TYPE_COMMON)) {
                    listener.onDishesCountChanged(mSalesOrderBatchDishesE, mCurrentDishesCount);
                } else if (mShowType.equals(SHOW_TYPE_PRACTICE)) {
                    listener.onPracticeCountChanged(mCurrentDishesCount);
                }
            }
            dismiss();
        }
    }

    /**
     * 键盘输入设置
     */
    private void keyBoardSetting(String inputNumber) {
        String value = mOrderDishesCountET.getText().toString();
        if (mOrderDishesCountET.getSelectionEnd() - mOrderDishesCountET.getSelectionStart() == mOrderDishesCountET.getText().length()) {
            value = "";
        }
        if ("-".equals(inputNumber)) {
            if (value.length() > 0) {
                value = value.substring(0, value.length() - 1);
            }
        } else if (RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value + inputNumber)) {
            value = value + inputNumber;
        }
        if (mMaxCount != null && RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value) && Double.parseDouble(value) > mMaxCount) {
            mOrderDishesCountET.setTextColor(ContextCompat.getColor(getActivity(), R.color.text_order_dishes_no_print));
            mChangeCountConfirm.setEnabled(false);
        } else {
            mOrderDishesCountET.setTextColor(ContextCompat.getColor(getActivity(), R.color.et_text));
            mChangeCountConfirm.setEnabled(RegexExUtils.isMatch(RegexExUtils.REGEX_MONEY, value) && Double.parseDouble(value) >= 0);
        }
        mOrderDishesCountET.setText(value);
        if (value.length() == 0) {
            mCurrentCount = null;
        } else {
            mCurrentCount = Double.valueOf(value);
        }
    }

    /**
     * 回掉接口
     */
    public interface ChangeOrderDishesCountListener {
        /**
         * @param count                  数量
         * @param salesOrderBatchDishesE 菜品数据
         */
        void onDishesCountChanged(SalesOrderBatchDishesE salesOrderBatchDishesE, double count);

        /**
         * @param count 数量
         */
        void onPracticeCountChanged(double count);
    }
}
