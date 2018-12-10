package com.holderzone.intelligencepos.utils;

import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

/**
 * Created by zhaoping on 2017/5/27.
 */

public class TextUtils {
    /***
     * TextView 追加文字(可设置字体大小，字体颜色)
     * @param textView
     * @param text
     * @param resColor
     * @param resDimen
     */
    public static void appentTextViewText(TextView textView, String text, @ColorRes Integer resColor, @DimenRes Integer resDimen) {
        SpannableString spannableString = new SpannableString(text);
        if (resColor != null) {
            spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(textView.getContext(), resColor)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (resDimen != null) {
            spannableString.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(resDimen)), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.append(spannableString);
    }

    public static void decorateTextViewText(TextView textView, int start, int end, @ColorRes Integer resColor, @DimenRes Integer resDimen) {
        SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
        if (resColor != null) {
            builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(textView.getContext(), resColor)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        if (resDimen != null) {
            builder.setSpan(new AbsoluteSizeSpan((int) textView.getContext().getResources().getDimension(resDimen)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        textView.setText(builder);
    }
}
