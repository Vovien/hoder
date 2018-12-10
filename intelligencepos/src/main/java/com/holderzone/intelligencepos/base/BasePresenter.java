package com.holderzone.intelligencepos.base;

import com.holderzone.intelligencepos.mvp.model.Repository;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;

/**
 * Created by tcw on 2017/3/17.
 */

public abstract class BasePresenter<V extends IView> implements IPresenter {

    protected V mView;

    protected Repository mRepository;

    public BasePresenter(V view) {
        mView = view;
        mRepository = RepositoryImpl.getInstance();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mRepository = null;
    }
}
