package com.holderzone.intelligencepos.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : 正则相关工具类
 * </pre>
 */
public final class RegexUtils {
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

    /**
     * 流水号
     */
    public static final String REGEX_SERIAL_NUMBER = "^\\d{0,20}$";//非必须，所以0位也行
    /**
     * 流水号合法，允许继续输入
     */
    public static final String REGEX_SERIAL_NUMBER_INPUT_LEGAL = "^\\d{0,20}$";

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

    /**
     * 点菜数量
     */
    public static final String REGEX_DISH_COUNT = "^0(.|.\\d{1,2})?|[1-9]\\d{0,1}(.|.\\d{1,2})?$";
    public static final String REGEX_DISH_COUNT_INPUT_LEGAL = "^(\\d{0})|(0)|([1-9]\\d{0,2})|(0\\.\\d{0,2})|([1-9]\\d{0,2}\\.\\d{0,2})$";

    /**
     * 正则：手机号（精确）
     * <p>移动：134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188</p>
     * <p>联通：130、131、132、145、155、156、175、176、185、186</p>
     * <p>电信：133、153、173、177、180、181、189</p>
     * <p>全球星：1349</p>
     * <p>虚拟运营商：170</p>
     */
    public static final String REGEX_MOBILE_EXACT = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|(147))\\d{8}$";
    /**
     * 正则：电话号码
     */
    public static final String REGEX_TEL = "^0\\d{2,3}[- ]?\\d{7,8}";
    /**
     * 正则：身份证号码15位
     */
    public static final String REGEX_ID_CARD15 = "^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$";
    /**
     * 正则：身份证号码18位
     */
    public static final String REGEX_ID_CARD18 = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9Xx])$";
    /**
     * 正则：邮箱
     */
    public static final String REGEX_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
    /**
     * 正则：URL
     */
    public static final String REGEX_URL = "[a-zA-z]+://[^\\s]*";
    /**
     * 正则：汉字
     */
    public static final String REGEX_ZH = "^[\\u4e00-\\u9fa5]+$";
    /**
     * 正则：用户名，取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位
     */
    public static final String REGEX_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";
    /**
     * 正则：yyyy-MM-dd格式的日期校验，已考虑平闰年
     */
    public static final String REGEX_DATE = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";
    /**
     * 正则：IP地址
     */
    public static final String REGEX_IP = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";

    /**
     * 正则：双字节字符(包括汉字在内)
     */
    public static final String REGEX_DOUBLE_BYTE_CHAR = "[^\\x00-\\xff]";
    /**
     * 正则：空白行
     */
    public static final String REGEX_BLANK_LINE = "\\n\\s*\\r";
    /**
     * 正则：QQ号
     */
    public static final String REGEX_TENCENT_NUM = "[1-9][0-9]{4,}";
    /**
     * 正则：中国邮政编码
     */
    public static final String REGEX_ZIP_CODE = "[1-9]\\d{5}(?!\\d)";
    /**
     * 正则：正整数
     */
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 正则：负整数
     */
    public static final String REGEX_NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 正则：整数
     */
    public static final String REGEX_INTEGER = "^-?[1-9]\\d*$";
    /**
     * 正则：非负整数(正整数 + 0)
     */
    public static final String REGEX_NOT_NEGATIVE_INTEGER = "^[1-9]\\d*|0$";
    /**
     * 正则：非正整数（负整数 + 0）
     */
    public static final String REGEX_NOT_POSITIVE_INTEGER = "^-[1-9]\\d*|0$";
    /**
     * 正则：正浮点数
     */
    public static final String REGEX_POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 正则：负浮点数
     */
    public static final String REGEX_NEGATIVE_FLOAT = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

    private RegexUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }
    ///////////////////////////////////////////////////////////////////////////
    // If u want more please visit http://toutiao.com/i6231678548520731137
    ///////////////////////////////////////////////////////////////////////////

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
     * 验证流水号是否合法，以允许继续输入
     */
    public static boolean isSerialNumberInputLegal(CharSequence input) {
        return isMatch(REGEX_SERIAL_NUMBER_INPUT_LEGAL, input);
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
     * 验证点菜数量是否合法，以允许继续输入
     */
    public static boolean isDishCountInputLegal(CharSequence input) {
        return isMatch(REGEX_DISH_COUNT_INPUT_LEGAL, input);
    }

    /**
     * 验证手机号（精确）
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMobileExact(CharSequence input) {
        return isMatch(REGEX_MOBILE_EXACT, input);
    }

    /**
     * 验证电话号码
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isTel(CharSequence input) {
        return isMatch(REGEX_TEL, input);
    }

    /**
     * 验证身份证号码15位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard15(CharSequence input) {
        return isMatch(REGEX_ID_CARD15, input);
    }

    /**
     * 验证身份证号码18位
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIDCard18(CharSequence input) {
        return isMatch(REGEX_ID_CARD18, input);
    }

    /**
     * 验证邮箱
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isEmail(CharSequence input) {
        return isMatch(REGEX_EMAIL, input);
    }

    /**
     * 验证URL
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isURL(CharSequence input) {
        return isMatch(REGEX_URL, input);
    }

    /**
     * 验证汉字
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isZh(CharSequence input) {
        return isMatch(REGEX_ZH, input);
    }

    /**
     * 验证用户名
     * <p>取值范围为a-z,A-Z,0-9,"_",汉字，不能以"_"结尾,用户名必须是6-20位</p>
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isUsername(CharSequence input) {
        return isMatch(REGEX_USERNAME, input);
    }

    /**
     * 验证yyyy-MM-dd格式的日期校验，已考虑平闰年
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isDate(CharSequence input) {
        return isMatch(REGEX_DATE, input);
    }

    /**
     * 验证IP地址
     *
     * @param input 待验证文本
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isIP(CharSequence input) {
        return isMatch(REGEX_IP, input);
    }
//    /**
//     * 判断是否匹配正则
//     *
//     * @param regex 正则表达式
//     * @param input 要匹配的字符串
//     * @return {@code true}: 匹配<br>{@code false}: 不匹配
//     */
//    public static boolean isMatch(String regex, CharSequence input) {
//        return input != null && input.length() > 0 && Pattern.matches(regex, input);
//    }

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

    /**
     * 获取正则匹配的部分
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return 正则匹配的部分
     */
    public static List<String> getMatches(String regex, CharSequence input) {
        if (input == null) {
            return null;
        }
        List<String> matches = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }

    /**
     * 获取正则匹配分组
     *
     * @param input 要分组的字符串
     * @param regex 正则表达式
     * @return 正则匹配分组
     */
    public static String[] getSplits(String input, String regex) {
        if (input == null) {
            return null;
        }
        return input.split(regex);
    }

    /**
     * 替换正则匹配的第一部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换正则匹配的第一部分
     */
    public static String getReplaceFirst(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceFirst(replacement);
    }

    /**
     * 替换所有正则匹配的部分
     *
     * @param input       要替换的字符串
     * @param regex       正则表达式
     * @param replacement 代替者
     * @return 替换所有正则匹配的部分
     */
    public static String getReplaceAll(String input, String regex, String replacement) {
        if (input == null) {
            return null;
        }
        return Pattern.compile(regex).matcher(input).replaceAll(replacement);
    }
}