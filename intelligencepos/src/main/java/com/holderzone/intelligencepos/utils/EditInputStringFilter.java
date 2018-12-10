package com.holderzone.intelligencepos.utils;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * 输入框控制字符串长度 过滤器
 */
public class EditInputStringFilter implements InputFilter {
    /**
     * 最大字符长度（中英文默认一样）
     */
    private int MAX_LENGTH = 8;

    public EditInputStringFilter() {

    }

    public EditInputStringFilter(int maxLength) {
        this();
        MAX_LENGTH = maxLength;
    }

    /**
     * source    新输入的字符串
     * start    新输入的字符串起始下标，一般为0
     * end    新输入的字符串终点下标，一般为source长度-1
     * dest    输入之前文本框内容
     * dstart    原内容起始坐标，一般为0
     * dend    原内容终点坐标，一般为dest长度-1
     */

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        int keep = MAX_LENGTH - (dest.length() - (dend - dstart));

        if (keep <= 0) {
            return "";
        } else if (keep >= end - start) {
            return null; // keep original
        } else {
            keep += start;
            if (Character.isHighSurrogate(source.charAt(keep - 1))) {
                --keep;
                if (keep == start) {
                    return "";
                }
            }
            return source.subSequence(start, start + keep);
        }
    }
}
