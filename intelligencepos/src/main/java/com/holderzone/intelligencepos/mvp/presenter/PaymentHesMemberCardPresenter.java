package com.holderzone.intelligencepos.mvp.presenter;

import android.annotation.SuppressLint;
import android.util.Pair;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentHesMemberCardContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberModel;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.RxUtils;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.Observable;

public class PaymentHesMemberCardPresenter extends BasePresenter<PaymentHesMemberCardContract.View> implements PaymentHesMemberCardContract.Presenter {
    public PaymentHesMemberCardPresenter(PaymentHesMemberCardContract.View view) {
        super(view);
    }


    @SuppressLint("CheckResult")
    @Override
    public void requestMemberCardList(String salesOrderGUID, String memberInfoGUID) {
        Observable.zip(mRepository.getEnterpriseInfo(), mRepository.getStore(), Pair::new)
                .flatMap(pair -> {
                    EnterpriseInfo enterpriseInfo = pair.first;
                    Store store = pair.second;
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.HPMemberB.GetMemberPayCard)
                            .buildRESTful()
                            .flatMap(xmlData -> {
                                        MemberInfoE memberInfoE = new MemberInfoE();
                                        memberInfoE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
                                        memberInfoE.setType("4");
                                        memberInfoE.setStoreGUID(store.getStoreGUID());
                                        memberInfoE.setContent(memberInfoGUID);
                                        xmlData.setMemberInfoE(memberInfoE);
                                        return mRepository.getXmlData(xmlData);
                                    }
                            );
                })
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        MemberModel memberModel = xmlData.getMemberModel();
                        if (memberModel != null && memberModel.getVipCardModels() != null) {
                            mView.responseMemberCardListSuccess(memberModel.getVipCardModels());
                        } else {
                            mView.responseMemberCardListSuccess(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (RxUtils.checkNetException(e)) {
                            mView.responseMemberCardListFail();
                            return;
                        }
                        super.onError(e);
                    }
                });
    }
}
