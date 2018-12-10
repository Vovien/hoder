package com.holderzone.intelligencepos.mvp.model;

import com.holderzone.intelligencepos.mvp.model.cache.CacheManager;
import com.holderzone.intelligencepos.mvp.model.db.DbManager;
import com.holderzone.intelligencepos.mvp.model.file.FileManager;
import com.holderzone.intelligencepos.mvp.model.network.NetworkManager;
import com.holderzone.intelligencepos.mvp.model.prefs.PrefsManager;

/**
 * Created by tcw on 2017/9/6.
 */

public interface Repository extends CacheManager,
        FileManager, PrefsManager, DbManager,
        NetworkManager, ReleaseResourceManager {
}
