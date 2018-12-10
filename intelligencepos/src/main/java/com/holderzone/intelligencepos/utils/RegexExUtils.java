package com.holderzone.intelligencepos.utils;

import java.util.regex.Pattern;

/**
 * 正则相关工具类：自定义功能
 */
public final class RegexExUtils {
    /**
     * 手机号
     */
    public static final String REGEX_MOBILE = "^\\d{11}$";

    /**
     * 手机号合法，允许继续输入
     */
//    public static final String REGEX_MOBILE_INPUT_LEGAL = "^(\\d{0})|([1]\\d{0,10})$";
    public static final String REGEX_MOBILE_INPUT_LEGAL = "^\\d{0,11}$";

    /**
     * 金额
     */
//    public static final String REGEX_MONEY = "^([1-9]\\d{0,5})|(0\\.[1-9]\\d{0,1})|(0\\.0[1-9])|([1-9]\\d{0,5}\\.[1-9]\\d{0,1})|([1-9]\\d{0,5}\\.0\\d{0,1})$";
    public static final String REGEX_MONEY = "^0(\\.|\\.\\d{1,2})?|[1-9]\\d{0,5}(\\.|\\.\\d{1,2})?$";

    /**
     * 金额合法，允许继续输入
     */
    public static final String REGEX_MONEY_INPUT_LEGAL = "^(\\d{0})|(0)|([1-9]\\d{0,5})|(0\\.\\d{0,2})|([1-9]\\d{0,5}\\.\\d{0,2})$";

    // 浮点数合法 && 浮点数输入合法
    public static final String REGEX_DECIMAL = "^0(\\.|\\.\\d{1,2})?|[1-9]\\d{0,5}(\\.|\\.\\d{1,2})?$";
    public static final String REGEX_DECIMAL_INPUT_LEGAL = "^(\\d{0})|(0)|([1-9]\\d{0,5})|(0\\.\\d{0,2})|([1-9]\\d{0,5}\\.\\d{0,2})$";

    // 整数合法 && 整数输入合法
    public static final String REGEX_INTEGER = "^(0)|([1-9]\\d{0,5})$";
    public static final String REGEX_INTEGER_INPUT_LEGAL = "^(\\d{0})|(0)|([1-9]\\d{0,5})$";

    /**
     * 流水号
     */
    public static final String REGEX_SERIAL_NUMBER = "^\\d{0,20}$";//非必须，所以0位也行

    /**
     * 退菜原因
     */
    public static final String REGEX_RETREAT_REASON = "[a-zA-Z0-9\\u4E00-\\u9FA5]{1,20}";

    /**
     * 退菜原因合法，允许继续输入
     */
    public static final String REGEX_RETREAT_REASON_INPUT_LEGAL = "[a-zA-Z0-9\\u4E00-\\u9FA5]{0,20}";

    /**
     * 菜品备注
     */
    public static final String REGEX_DISHES_REMARK = "[a-zA-Z0-9\\u4E00-\\u9FA5]{1,20}";

    /**
     * 菜品备注合法，允许继续输入
     */
    public static final String REGEX_DISHES_REMARK_INPUT_LEGAL = "[a-zA-Z0-9\\u4E00-\\u9FA5]{0,20}";


    private RegexExUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 验证手机号
     */
    public static boolean isMobile(CharSequence input) {
        return isMatch(REGEX_MOBILE, input);
    }

    /**
     * 验证手机号是否合法，以允许继续输入
     */
    public static boolean isMobileInputLegal(CharSequence input) {
        return isMatch(REGEX_MOBILE_INPUT_LEGAL, input);
    }

    // 验证浮点数
    public static boolean isDecimal(CharSequence input) {
        return isMatch(REGEX_DECIMAL, input);
    }

    // 验证浮点数是否合法，以允许继续输入
    public static boolean isDecimalInputLegal(CharSequence input) {
        return isMatch(REGEX_DECIMAL_INPUT_LEGAL, input);
    }

    // 验证整数
    public static boolean isInteger(CharSequence input) {
        return isMatch(REGEX_INTEGER, input);
    }

    // 验证整数
    public static boolean isIntegerInputLegal(CharSequence input) {
        return isMatch(REGEX_INTEGER_INPUT_LEGAL, input);
    }

    /**
     * 验证金额
     */
    public static boolean isMoney(CharSequence input) {
        return isMatch(REGEX_MONEY, input);
    }

    /**
     * 验证金额是否合法，以允许继续输入
     */
    public static boolean isMoneyInputLegal(CharSequence input) {
        return isMatch(REGEX_MONEY_INPUT_LEGAL, input);
    }

    /**
     * 验证流水号
     */
    public static boolean isSerialNumber(CharSequence input) {
        return isMatch(REGEX_SERIAL_NUMBER, input);
    }

    /**
     * 验证退菜原因是否合法
     *
     * @param input
     * @return
     */
    public static boolean isRetreatReason(CharSequence input) {
        return isMatch(REGEX_RETREAT_REASON, input);
    }

    /**
     * 验证退菜原因是否合法
     *
     * @param input
     * @return
     */
    public static boolean isRetreatReasonInputLegal(CharSequence input) {
        return isMatch(REGEX_RETREAT_REASON_INPUT_LEGAL, input);
    }

    /**
     * 验证菜品备注是否合法
     *
     * @param input
     * @return
     */
    public static boolean isDishesRemarks(CharSequence input) {
        return isMatch(REGEX_DISHES_REMARK, input);
    }

    /**
     * 验证菜品备注是否合法
     *
     * @param input
     * @return
     */
    public static boolean isDishesRemarksInputLegal(CharSequence input) {
        return isMatch(REGEX_DISHES_REMARK_INPUT_LEGAL, input);
    }


    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(String regex, CharSequence input) {
        return input != null && Pattern.matches(regex, input);
    }
}