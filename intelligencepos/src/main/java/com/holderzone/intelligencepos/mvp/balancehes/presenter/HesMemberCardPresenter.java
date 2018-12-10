package com.holderzone.intelligencepos.mvp.balancehes.presenter;

import android.annotation.SuppressLint;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.balancehes.contract.HesMemberCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberModel;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.RxUtils;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class HesMemberCardPresenter extends BasePresenter<HesMemberCardContract.View> implements HesMemberCardContract.Presenter {
    public HesMemberCardPresenter(HesMemberCardContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getMemberCardList(String salesOrderGUID, String memberInfoGUID) {
        mRepository.getEnterpriseInfo()
                .flatMap(enterpriseInfo -> XmlData.Builder()
                        .setRequestMethod(RequestMethod.HPMemberB.GetListPreferential)
                        .buildRESTful()
                        .flatMap(xmlData -> {
                            SalesOrderE salesOrderE = new SalesOrderE();
                            salesOrderE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
                            salesOrderE.setSalesOrderGUID(salesOrderGUID);
                            salesOrderE.setMemberInfoGUID(memberInfoGUID);
                            salesOrderE.setPreferentialType(1);
                            salesOrderE.setPreferentialSelect(true);
                            xmlData.setSalesOrderE(salesOrderE);
                            return mRepository.getXmlData(xmlData);
                        }))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        MemberModel memberModel = xmlData.getMemberModel();
                        if (memberModel != null && memberModel.getVipCardModels() != null) {
                            mView.getMemberCardListSuccess(memberModel.getSelected() == null ? false : memberModel.getSelected(), memberModel.getVipCardModels());
                        } else {
                            mView.getMemberCardListSuccess(false, null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (RxUtils.checkNetException(e)) {
                            mView.getMemberCardListFail();
                            return;
                        }
                        super.onError(e);
                    }
                });
    }

    @Override
    public void requestUseMemberCard(List<String> codes, String salesOrderGUID, String memberInfoGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        salesOrderE.setMemberInfoGUID(memberInfoGUID);
        salesOrderE.setCodes(codes);

        XmlData.Builder()
                .setRequestBody(salesOrderE)
                .setRequestMethod(RequestMethod.SalesOrderB.PlatCardsDiscount)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    protected void next(XmlData xmlData) {
                        if (xmlData.getApiNote().getResultCode() == 101) {
                            mView.responseUseMemberCardNotAll(xmlData.getApiNote().getResultMsg());
                            return;
                        }
                        mView.responseUseMemberCardSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (RxUtils.checkNetException(e)) {
                            mView.responseUseMemberCardFail();
                        } else {
                            super.onError(e);
                        }
                    }
                });


    }

}
