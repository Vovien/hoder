package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.DiscountDetailsContract;

/**
 * Created by chencao on 2018/2/2.
 */

public class DiscountDetailsPresenter extends BasePresenter<DiscountDetailsContract.View> implements DiscountDetailsContract.Presenter{
    public DiscountDetailsPresenter(DiscountDetailsContract.View view) {
        super(view);
    }
}
