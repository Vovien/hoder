package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.DiscountOperationContract;

/**
 * Created by chencao on 2018/2/2.
 */

public class DiscountOperationPresenter extends BasePresenter<DiscountOperationContract.View> implements DiscountOperationContract.Presenter {
    public DiscountOperationPresenter(DiscountOperationContract.View view) {
        super(view);
    }
}
