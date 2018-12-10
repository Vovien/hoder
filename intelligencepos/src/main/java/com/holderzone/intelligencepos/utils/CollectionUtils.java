package com.holderzone.intelligencepos.utils;

import java.util.List;

/**
 * Created by lyd10892 on 2016/8/23.
 */

public class CollectionUtils {
    public static <D> boolean isEmpty(List<D> list) {
        return list == null || list.isEmpty();
    }

    public static <D> boolean isNotEmpty(List<D> list) {
        return !isEmpty(list);
    }
}
