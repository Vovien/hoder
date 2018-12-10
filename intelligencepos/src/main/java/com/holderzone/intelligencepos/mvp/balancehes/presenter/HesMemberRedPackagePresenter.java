package com.holderzone.intelligencepos.mvp.balancehes.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.balancehes.contract.HesMemberRedPackageContract;
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

public class HesMemberRedPackagePresenter extends BasePresenter<HesMemberRedPackageContract.View> implements HesMemberRedPackageContract.Presenter {
    public HesMemberRedPackagePresenter(HesMemberRedPackageContract.View view) {
        super(view);
    }

    @Override
    public void getMemberRedPackageList(String salesOrderGUID, String memberInfoGUID) {
        mRepository.getEnterpriseInfo()
                .flatMap(enterpriseInfo -> XmlData.Builder()
                        .setRequestMethod(RequestMethod.HPMemberB.GetListPreferential)
                        .buildRESTful()
                        .flatMap(xmlData -> {
                            SalesOrderE salesOrderE = new SalesOrderE();
                            salesOrderE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
                            salesOrderE.setSalesOrderGUID(salesOrderGUID);
                            salesOrderE.setMemberInfoGUID(memberInfoGUID);
                            salesOrderE.setPreferentialType(3);
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
                        if (memberModel != null && memberModel.getRedPackageModels() != null) {
                            mView.responseMemberRedEnvelopesListSuccess(memberModel.getRedPackageModels());
                        } else {
                            mView.responseMemberRedEnvelopesListSuccess(null);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (RxUtils.checkNetException(e)) {
                            mView.responseMemberRedEnvelopesListFail();
                            return;
                        }
                        super.onError(e);
                    }
                });
    }

    @Override
    public void requestUseRedPackage(List<String> codes, String salesOrderGUID, String memberInfoGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        salesOrderE.setMemberInfoGUID(memberInfoGUID);
        salesOrderE.setPreferentialSelect(true);
        salesOrderE.setCodes(codes);

        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.UseRedPacket)
                .setRequestBody(salesOrderE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    protected void next(XmlData xmlData) {
                        if (xmlData.getApiNote().getResultCode() == 101) {
                            mView.responseUseRedEnvelopesNotAll(xmlData.getApiNote().getResultMsg());
                            return;
                        }
                        mView.responseUseRedEnvelopesSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (RxUtils.checkNetException(e)) {
                            mView.responseUseRedEnvelopesFail();
                            return;
                        }
                        super.onError(e);
                    }
                });
    }
}
