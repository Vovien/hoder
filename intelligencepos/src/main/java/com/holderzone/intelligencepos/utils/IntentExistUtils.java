package com.holderzone.intelligencepos.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Administrator on 2017-10-30.
 */

public class IntentExistUtils {
    public static boolean isIntentExisting(Intent intent, Context context) {
        final PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> resolveInfo =
                packageManager.queryIntentActivities(intent,
                        PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo.size() > 0) {
            return true;
        }
        return false;
    }
}
