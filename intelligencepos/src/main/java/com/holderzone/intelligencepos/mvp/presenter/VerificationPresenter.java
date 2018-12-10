package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.VerificationContract;
import com.holderzone.intelligencepos.mvp.model.bean.EquipmentsE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;
import com.memoizrlabs.retrooptional.Optional;

import io.reactivex.Observable;

/**
 * 设备激活（非主动激活） VerificationContract.Presenter实现类
 * 判断本地是否存在商家信息
 * 判断一体机设备是否绑定到具体商家
 * 判断一体机设备授权验证是否过期
 * 判断本地是否存在门店信息
 * Created by tcw on 2017/4/14.
 */

public class VerificationPresenter extends BasePresenter<VerificationContract.View> implements VerificationContract.Presenter {

    public VerificationPresenter(VerificationContract.View view) {
        super(view);
    }

    @Override
    public void verify() {
        verifyEnterpriseInfo(DeviceHelper.getInstance().getDeviceID())
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<Boolean>(mView) {
                    @Override
                    protected void next(Boolean storeExist) {
                        mView.onVerifySuccess(storeExist);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // 此处不调用super，有界面显示"未激活"，不需要额外对话框;
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onVerifyFailed(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 校验本地是否存在EnterpriseInfo实体信息
     *
     * @param equipmentsUID
     * @return
     */
    private Observable<Boolean> verifyEnterpriseInfo(String equipmentsUID) {
        return mRepository.getOptionalEnterpriseInfo()
                .flatMap(enterpriseInfoOptional -> {
                    if (enterpriseInfoOptional.isPresent()) {
                        return verifyAuthorization(equipmentsUID);
                    } else {
                        return verifyBinding(equipmentsUID);
                    }
                });
    }

    /**
     * 校验一体机是否绑定到具体的企业
     *
     * @param equipmentsUID
     * @return
     */
    private Observable<Boolean> verifyBinding(String equipmentsUID) {
        return XmlData.Builder(false)
                .setRequestMethod(RequestMethod.EquipmentsB.GetBinding)
                .setRequestBody(new EquipmentsE(equipmentsUID)).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return verifyAuthorization(equipmentsUID);
                    }
                    return Observable.error(new ApiException(xmlData));
                });
    }

    /**
     * 校验一体机授权是否过期
     *
     * @param equipmentsUID
     * @return
     */
    private Observable<Boolean> verifyAuthorization(String equipmentsUID) {
        return XmlData.Builder(false)
                .setRequestMethod(RequestMethod.EquipmentsB.AuthorizeCheck)
                .setRequestBody(new EquipmentsE(equipmentsUID)).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        return mRepository.saveEnterpriseInfo(xmlData.getEnterpriseInfoE());
                    }
                    return Observable.error(new ApiException(xmlData));
                })
                .flatMap(enterpriseInfo -> verifyStore());
    }

    /**
     * 校验本地是否存在当前Store门店信息
     *
     * @return
     */
    private Observable<Boolean> verifyStore() {
        return mRepository.getOptionalStore()
                .map(Optional::isPresent);
    }
}
