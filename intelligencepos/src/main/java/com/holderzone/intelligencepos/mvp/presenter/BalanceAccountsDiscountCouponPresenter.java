package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountCouponContract;
import com.holderzone.intelligencepos.mvp.model.bean.CouponsE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 券业务
 * Created by zhaoping on 2017/4/24.
 */

public class BalanceAccountsDiscountCouponPresenter extends BasePresenter<BalanceAccountsDiscountCouponContract.View> implements BalanceAccountsDiscountCouponContract.Presenter {
    public BalanceAccountsDiscountCouponPresenter(BalanceAccountsDiscountCouponContract.View view) {
        super(view);
    }

    @Override
    public void validateCoupon(String couponsNumber, String salesOrderGuid) {
        CouponsE couponsE = new CouponsE();
        couponsE.setCouponsNumber(couponsNumber);
        couponsE.setUseCheck(1);
        couponsE.setSalesOrderGUID(salesOrderGuid);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.CouponsB.GetSingle)
                .setRequestBody(couponsE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onValidateCouponSuccess(xmlData.getCouponsE());
                    }
                });
    }

    @Override
    public void getListByMember(String memberInfoGUID, String salesOrderGuid) {
        CouponsE card = new CouponsE();
        card.setMemberInfoGUID(memberInfoGUID);
        card.setSalesOrderGUID(salesOrderGuid);
        card.setResultBeUse(1);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.CouponsB.GetListByMember)
                .setRequestBody(card).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onGetCouponListByMemberSuccess(xmlData.getArrayOfCouponsE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        }
                    }
                });
    }

    @Override
    public void useCoupons(List<CouponsE> list) {
        mRepository.getEnterpriseInfo()
                .flatMap(enterpriseInfo -> {
                    String enterpriseInfoGUID = enterpriseInfo.getEnterpriseInfoGUID();
                    for (CouponsE couponsE : list) {
                        couponsE.setEnterpriseInfoGUID(enterpriseInfoGUID);
                    }
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.CouponsB.UseCoupons).buildRESTful()
                            .flatMap(xmlData -> {
                                xmlData.setArrayOfCouponsE(list);
                                return mRepository.getXmlData(xmlData);
                            });
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onUseCouponsSuccess();
                    }
                });
    }
}
