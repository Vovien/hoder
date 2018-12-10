package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.AboutContract;
import com.holderzone.intelligencepos.mvp.model.bean.ApiBase;
import com.holderzone.intelligencepos.mvp.model.bean.EnterpriseInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.EquipmentsE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.trello.rxlifecycle2.internal.Preconditions;

/**
 * Created by LiTao on 2017-5-15.
 */

public class AboutPresenter extends BasePresenter<AboutContract.View> implements AboutContract.Presenter {

    public AboutPresenter(AboutContract.View view) {
        super(view);
    }

    @Override
    public void requestInfo() {
        EquipmentsE equipmentsE = new EquipmentsE();
        equipmentsE.setEquipmentsUID(DeviceHelper.getInstance().getDeviceID());
        XmlData.Builder()
                .setRequestMethod(RequestMethod.EquipmentsB.GetInfo)
                .setRequestBody(equipmentsE).buildRESTful()
                .flatMap(xmlData -> {
                    ApiBase apiBase = xmlData.getApiBase();
                    apiBase.setModel("FrontendB_Public");
                    apiBase.setMerID(null);
                    return mRepository.getXmlData(xmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        EquipmentsE equipmentsE1 = xmlData.getEquipmentsE();
                        Preconditions.checkNotNull(equipmentsE1, "equipmentsE1==null");
                        EnterpriseInfoE enterpriseInfoE = xmlData.getEnterpriseInfoE();
                        Preconditions.checkNotNull(enterpriseInfoE, "enterpriseInfoE==null");
                        mView.getInfoSuccess(equipmentsE1, enterpriseInfoE);
                    }
                });
    }
}
