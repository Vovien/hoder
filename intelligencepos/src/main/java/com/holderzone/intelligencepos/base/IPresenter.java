package com.holderzone.intelligencepos.base;

/**
 * Created by tcw on 2017/3/17.
 */

public interface IPresenter {

    /**
     * view层调用以实现presenter的资源回收
     */
    void onDestroy();
}
