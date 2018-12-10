package com.holderzone.intelligencepos.utils;

import com.blankj.utilcode.util.EncryptUtils;

import java.util.Random;

/**
 * Created by zhaoping on 2017/11/22.
 */

public class Security {

    public static final String SALT = "aYmW2uTaoeKQj6upJyTTxkgGTrsaz8YQecFhMhp1J7eDEVBBxFbhpuDYSPcKbSJpo3MhAnLCi0kzgMb6Z0L36RBTIQx6eO0ZxXoKjtak2I8xjdSgNeTjhPsGB1ITYOxC";

    public static String getStr(int min, int max) {
        String[] s1 = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Random rand = new Random();
        String str = "";
        int rnum = rand.nextInt(max) % (max - min + 1) + min;
        for (int i = 0; i < rnum; ++i) {
            int rnum1 = rand.nextInt(s1.length) % (s1.length - 1);
            str = str + s1[rnum1];
        }
        return str;
    }

    public static String md5(String password) {
        return EncryptUtils.encryptMD5ToString((EncryptUtils.encryptMD5ToString(password) + Security.SALT).toLowerCase());
    }
}
