package com.holderzone.intelligencepos.widget.popupwindow.impl;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.RegexUtils;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePricePopup extends RelativePopupWindow {
    private ChangePricePopup.ChangePricePopupListener mOnItemClickListener;
    private double price;
    private Context context;
    private boolean isChange;
    @BindView(R.id.changePriceText)
    EditText changePriceText;

    public interface ChangePricePopupListener {
        void onConfirmClick(double newPrice);
    }

    //设置价格并且改变背景颜色
    public void setPrice(double price) {
        this.price = price;
        setBackgroundAlpha(((Activity) context), 0.5f);
        changePriceText.requestFocus();
        changePriceText.setText(context.getString(R.string.change_price_pop, ArithUtil.stripTrailingZeros(price)));
        changePriceText.setSelection(0, changePriceText.getText().length());
        changePriceText.setHighlightColor(ContextCompat.getColor(context, R.color.edit_text_9cd5d5));
        isChange = true;
    }

    public ChangePricePopup(Context context) {
        this.context = context;
        View view;
        setContentView(view = LayoutInflater.from(context).inflate(R.layout.popwindow_change_price, null));
        ButterKnife.bind(this, view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb3000000);
        setBackgroundDrawable(dw);
        setAnimationStyle(R.style.take_photo_anim);
        changePriceText.setInputType(InputType.TYPE_NULL);
        if (context instanceof ChangePricePopup.ChangePricePopupListener) {
            mOnItemClickListener = (ChangePricePopup.ChangePricePopupListener) context;
        }
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (context != null) {
                    setBackgroundAlpha((Activity) context, 1f);
                }
            }
        });
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

    private void appendEstimateCount(String s) {
        String text = changePriceText.getText().toString();
        if (changePriceText.getSelectionEnd() - changePriceText.getSelectionStart() == text.length()) {
            text = "";
        }
        if ("-".equalsIgnoreCase(s)) {
            text = text.length() > 0 ? text.substring(0, text.length() - 1) : text;
        } else if (RegexUtils.isMatch(RegexUtils.REGEX_MONEY, text + s)) {
            text = text + s;
        }
        isChange = RegexUtils.isMatch(RegexUtils.REGEX_MONEY, text);
        changePriceText.setText(text);
    }

    @OnClick({R.id.cancel, R.id.ok, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_number_1:
                appendEstimateCount("1");
                break;
            case R.id.btn_number_2:
                appendEstimateCount("2");
                break;
            case R.id.btn_number_3:
                appendEstimateCount("3");
                break;
            case R.id.btn_number_4:
                appendEstimateCount("4");
                break;
            case R.id.btn_number_5:
                appendEstimateCount("5");
                break;
            case R.id.btn_number_6:
                appendEstimateCount("6");
                break;
            case R.id.btn_number_7:
                appendEstimateCount("7");
                break;
            case R.id.btn_number_8:
                appendEstimateCount("8");
                break;
            case R.id.btn_number_9:
                appendEstimateCount("9");
                break;
            case R.id.btn_number_0:
                appendEstimateCount("0");
                break;
            case R.id.btn_number_dot:
                appendEstimateCount(".");
                break;
            case R.id.iv_delete:
                appendEstimateCount("-");
                break;
            case R.id.cancel:
                dismiss();
                break;
            case R.id.ok:
                if (isChange) {
                    if (mOnItemClickListener != null) {
                        Double d = Double.valueOf(changePriceText.getText().toString());
                        if (d > price) {
                            Toast.makeText(context.getApplicationContext(), "折扣价必须小于等于单价！", Toast.LENGTH_SHORT).show();
                        } else {
                            dismiss();
                            mOnItemClickListener.onConfirmClick(Double.valueOf(changePriceText.getText().toString()));
                        }
                    }
                } else {
                    Toast.makeText(context.getApplicationContext(), "请输入金额！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
