package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.SnackOpenTableContract;

/**
 * 快餐开台页面 Presenter
 * Created by Administrator on 2018/3/24/024.
 */

public class SnackOpenTablePresenter extends BasePresenter<SnackOpenTableContract.View> implements SnackOpenTableContract.Presenter {
    public SnackOpenTablePresenter(SnackOpenTableContract.View view) {
        super(view);
    }
}
