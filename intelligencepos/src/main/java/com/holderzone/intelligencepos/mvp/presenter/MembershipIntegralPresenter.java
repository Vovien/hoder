package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.MembershipIntegralContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by chencao on 2018/2/2.
 */

public class MembershipIntegralPresenter extends BasePresenter<MembershipIntegralContract.View> implements MembershipIntegralContract.Presenter {
    public MembershipIntegralPresenter(MembershipIntegralContract.View view) {
        super(view);
    }

    @Override
    public void requestCanUsePoints(String salesOrderGUID, String memberInfoGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        salesOrderE.setMemberInfoGUID(memberInfoGUID);

        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetCanUsePoints)
                .setRequestBody(salesOrderE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.responseCanUsePoints(xmlData.getSalesOrderE());
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkErrorLayout();
                        }
                    }
                });

    }

    @Override
    public void requestUsePoints(String salesOrderGUID, int usePoints, double pointsAmount) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        salesOrderE.setUsePoints(usePoints);
        salesOrderE.setPointsAmount(pointsAmount);

        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.UsePoints)
                .setRequestBody(salesOrderE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.resopnseUsePoints();
                    }
                });
    }

    @Override
    public void requestCancelUse(String salesOrderGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.CancelPoints)
                .setRequestBody(salesOrderE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.resopnseUsePoints();
                    }
                });
    }
}
