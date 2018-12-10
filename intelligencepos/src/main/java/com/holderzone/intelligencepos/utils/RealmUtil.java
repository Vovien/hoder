package com.holderzone.intelligencepos.utils;

import com.holderzone.intelligencepos.base.Constants;
import com.holderzone.intelligencepos.printer.PushRealmMigration;

import io.realm.RealmConfiguration;

/**
 * Created by zhaoping on 2018/7/12.
 */
public class RealmUtil {

    public static RealmConfiguration getPushConfig() {
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(Constants.PUSH_REALM_NAME)
                .schemaVersion(Constants.PUSH_REALM_VERSION)
                // 开始迁移
                .migration(new PushRealmMigration()).build();
        return config;
    }
}
