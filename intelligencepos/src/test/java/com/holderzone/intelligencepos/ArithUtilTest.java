package com.holderzone.intelligencepos;

import com.holderzone.intelligencepos.utils.ArithUtil;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.assertEquals;

/**
 * Created by chencao on 2018/1/10.
 */

public class ArithUtilTest {
    @Test

    public void getMinimumBit_isCorrect() {
        Assert.assertEquals("100", ArithUtil.getMinimumBit(100.00));
        Assert.assertEquals("100.1", ArithUtil.getMinimumBit(100.100));
        Assert.assertEquals("1.019", ArithUtil.getMinimumBit(1.019));
        Assert.assertEquals("1.999", ArithUtil.getMinimumBit(1.999));
        Assert.assertEquals("1.99", ArithUtil.getMinimumBit(1.99));
        Assert.assertEquals("1", ArithUtil.getMinimumBit(1.00));
        Assert.assertEquals("1.62", ArithUtil.getMinimumBit(1.62));
        Assert.assertEquals("1.6", ArithUtil.getMinimumBit(1.60));
        Assert.assertEquals("-1.6", ArithUtil.getMinimumBit(-1.60));
        Assert.assertEquals("-1.6", ArithUtil.getMinimumBit(-1.600));
        Assert.assertEquals("0", ArithUtil.getMinimumBit(0.00));
        Assert.assertEquals("0", ArithUtil.getMinimumBit(0.));
        Assert.assertEquals("0", ArithUtil.getMinimumBit(.0));
        Assert.assertEquals("0", ArithUtil.getMinimumBit(+0.00));
//        Assert.assertEquals("0", ArithUtil.getMinimumBit(-0.00));
        Assert.assertEquals("0.1", ArithUtil.getMinimumBit(0.10));
        Assert.assertEquals("0.11", ArithUtil.getMinimumBit(0.11));
        Assert.assertEquals("0.111", ArithUtil.getMinimumBit(0.111));
    }

    @Test
    @Ignore
    public void test_isCorrect() throws Exception {
        Assert.assertEquals("1", ArithUtil.test(1));
        Assert.assertEquals("1.01", ArithUtil.test(1.019));
        Assert.assertEquals("1.99", ArithUtil.test(1.999));
        Assert.assertEquals("1.99", ArithUtil.test(1.99));
        Assert.assertEquals("1", ArithUtil.test(1.00));
        Assert.assertEquals("1.62", ArithUtil.test(1.62));
        Assert.assertEquals("1.6", ArithUtil.test(1.60));
        Assert.assertEquals("-1.6", ArithUtil.test(-1.60));
        Assert.assertEquals("-1.6", ArithUtil.test(-1.600));
        Assert.assertEquals("0", ArithUtil.test(0.00));
        Assert.assertEquals("0", ArithUtil.test(+0.00));
        Assert.assertEquals("0", ArithUtil.test(-0.00));
        Assert.assertEquals("0.1", ArithUtil.test(0.10));
        Assert.assertEquals("0.11", ArithUtil.test(0.11));
        Assert.assertEquals("0.11", ArithUtil.test(0.111));
    }
}
