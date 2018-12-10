package com.holderzone.intelligencepos.mvp.model;

import io.reactivex.Observable;

/**
 * Created by tcw on 2017/7/26.
 */

public interface ReleaseResourceManager {

    Observable<Boolean> releaseForReLogin();

    Observable<Boolean> releaseForAppExit();
}
