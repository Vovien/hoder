package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsMemberLoginContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by zhaoping on 2017/6/1.
 */

public class BalanceAccountsMemberLoginPresenter extends BasePresenter<BalanceAccountsMemberLoginContract.View> implements BalanceAccountsMemberLoginContract.Presenter {
    public BalanceAccountsMemberLoginPresenter(BalanceAccountsMemberLoginContract.View view) {
        super(view);
    }

    @Override
    public void memberLogin(String salesOrderGuid, String tel) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGuid);
        salesOrderE.setRegTel(tel);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.MemberLogin)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, true) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onMemberLoginSuccess(xmlData.getSalesOrderE() == null ? null : xmlData.getSalesOrderE().getMemberInfoE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            int noteCode = apiException.getNoteCode();
                            int resultCode = apiException.getResultCode();
                            /**该会员已经登录其他账单**/
                            if (-4 == noteCode && -200 == resultCode) {
                                mView.onMemberLoginFailByLoginOther();
                            } else if (apiException.getNoteCode() == -4 && apiException.getResultCode() == 10039){
                                mView.onMemberLoginFailByNoRegister();
                            }else {
                                super.onError(e);

                            }
                        } else {
                            super.onError(e);
                        }
                    }
                });
    }

    @Override
    public void requestMemberInfo(String tel) {
        MemberInfoE memberInfoE = new MemberInfoE();
        memberInfoE.setRegTel(tel);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.MemberInfoB.GetSingle)
                .setRequestBody(memberInfoE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (xmlData.getApiNote().getNoteCode() == 1 && xmlData.getApiNote().getResultCode() == 1) {
                            mView.onQueryMemberSuccess(xmlData.getMemberInfoE());
                        } else {
                            mView.showMessage("未查询到改会员！");
                        }
                    }
                });
    }
}
