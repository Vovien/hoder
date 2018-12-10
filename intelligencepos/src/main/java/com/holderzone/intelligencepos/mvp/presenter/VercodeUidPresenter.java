package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.VercodeUidContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

public class VercodeUidPresenter extends BasePresenter<VercodeUidContract.View> implements VercodeUidContract.Presenter {

    public VercodeUidPresenter(VercodeUidContract.View view) {
        super(view);
    }

    @Override
    public void requestVerCode(String regTel) {
        MemberInfoE memberInfoE = new MemberInfoE();
        memberInfoE.setRegTel(regTel);
        memberInfoE.setNeedReg(1);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.MemberInfoB.GetVerCode)
                .setRequestBody(memberInfoE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        MemberInfoE memberInfoE1 = xmlData.getMemberInfoE();
                        if (memberInfoE1 != null) {
                            mView.onGetVerCodeSuccess(memberInfoE1.getVerCodeUID());
                        } else {
                            mView.onGetVerCodeFailed("数据非法，MemberInfoE为null。");
                        }
                    }
                });
    }

    @Override
    public void submitResetPassword(String regTel, String verCode, String verCodeUID) {
        mRepository.getUsers()
                .flatMap(users -> {
                    MemberInfoE memberInfoE = new MemberInfoE();
                    memberInfoE.setRegTel(regTel);
                    memberInfoE.setCode(verCode);
                    memberInfoE.setVerCodeUID(verCodeUID);
                    memberInfoE.setStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.MemberInfoB.ResetPassWord)
                            .setRequestBody(memberInfoE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onResetPasswordSuccess();
                    }
                });
    }
}
