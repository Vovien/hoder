package com.holderzone.intelligencepos.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 金额无丢失精度处理
 */
public class ArithUtil {
    //默认除法运算精度
    private static final int DEF_DIV_SCALE = 10;

    //这个类不能实例化
    private ArithUtil() {
    }

    /**
     * 提供精确的加法运算。
     *
     * @param d1 被加数
     * @param d2 加数
     * @return 两个参数的和
     */
    public static double add(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     *
     * @param d1 被减数
     * @param d2 减数
     * @return 两个参数的差
     */
    public static double sub(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     *
     * @param d1 被乘数
     * @param d2 乘数
     * @return 两个参数的积
     */
    public static double mul(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param d1 被除数
     * @param d2 除数
     * @return 两个参数的商
     */
    public static double div(double d1, double d2) {
        return div(d1, d2, DEF_DIV_SCALE);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param d1    被除数
     * @param d2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static double div(double d1, double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 提供精确的小数位四舍五入处理。
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /***
     * 获取快捷收款项目
     * @param d
     * @return
     */
    public static Set<Integer> getShortcutMoneyList(double d) {
        Set<Integer> list = new LinkedHashSet<Integer>();
        int money = (int) d;
        int a = money / 100 * 100;
        money = money % 100;
        int temp = money;
        list.add(a + temp);
        if (temp % 5 != 0 && money % 100 < 95) {
            temp = temp - (temp % 5) + 5;
            list.add(a + temp);
        }
        if ((temp > 50 || money < 40) && temp % 10 != 0) {
            temp = temp - (temp % 10) + 10;
            list.add(a + temp);
        } else if (money % 60 == 0) {
            temp = temp - (temp % 60) + 10;
            list.add(a + temp);
        }
        if (money % 100 < 50 && money % 20 > 0 && money % 40 < 40 && temp + 10 < 50 && money > 20) {
            list.add(a + temp + 10);
        }
        if (money % 80 == 0) {
            list.add(a + temp + 10);
        }
        if ((money > 50 || money < 20) && temp % 20 != 0 && temp % 50 != 20) {
            temp = temp - (temp % 20) + 20;
            list.add(a + temp);
        } else {
            int aaa = money - money % 10;
            if (temp > 50 && aaa % 20 != 0 && money % 70 != 0 && money % 100 < 90) {
                temp = aaa + 20;
                list.add(a + temp);
            }
        }
        if (temp % 50 != 0 && temp % 70 != 0) {
            temp = temp - (temp % 50) + 50;
            list.add(a + temp);
        }
        if (temp % 100 != 0) {
            temp = temp - (temp % 100) + 100;
            list.add(a + temp);
        }
        return list;
    }

    public static String test(double value) {
        int intValue = (int) value;
        if (BigDecimal.valueOf(intValue).compareTo(BigDecimal.valueOf(value)) == 0) {
//        if (Double.compare(intValue, value) == 0) {
            return String.valueOf(intValue);
        }

        double timesValues = value * 10;
        int timesIntValue = (int) timesValues;
        if (BigDecimal.valueOf(timesIntValue).compareTo(BigDecimal.valueOf(timesValues)) == 0) {
//        if (Double.compare(timesIntValue, timesValues) == 0) {
            return String.valueOf(timesIntValue / 10.0);
        }

        return String.valueOf((int)(value * 100) / 100.0);
    }

    /**
     * 获取最小位数的float
     *
     * @param value
     * @return
     */
    public static String stripTrailingZeros(Float value) {
        if (value == null) {
            return "0";
        }
        return stripTrailingZeros((float) value);
    }

    /**
     * 获取最小位数的float
     *
     * @param value
     * @return
     */
    public static String stripTrailingZeros(float value) {
        if (value == 0) {
            return "0";
        }
        double d = Double.parseDouble(String.valueOf(value)) ;
        return stripTrailingZeros(d);
    }

    /**
     * 获取最小位数的double
     *
     * @param value
     * @return
     */
    public static String stripTrailingZeros(Double value) {
        if (value == null) {
            return "0";
        }
        return stripTrailingZeros((double) value);
    }

    /**
     * 获取最小位数的double
     *
     * @param value
     * @return
     */

    public static String stripTrailingZeros(double value) {
         if (value == 0) {
            return "0";
        }
        return new BigDecimal(String.valueOf(value)).stripTrailingZeros().toPlainString();
    }


    /**
     * 保留小数点后几位，不足的用0补
     *
     * @param v     需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static String roundByScale(double v, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        if(scale == 0){
            return new DecimalFormat("0").format(v);
        }
        String formatStr = "0.";
        for(int i=0;i<scale;i++){
            formatStr = formatStr + "0";
        }
        return new DecimalFormat(formatStr).format(v);
    }
} 