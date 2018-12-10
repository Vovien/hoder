package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsAllDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by zhaoping on 2017/6/3.
 */

public class BalanceAccountsAllDishesPresenter extends BasePresenter<BalanceAccountsAllDishesContract.View>
        implements BalanceAccountsAllDishesContract.Presenter {
    public BalanceAccountsAllDishesPresenter(BalanceAccountsAllDishesContract.View view) {
        super(view);
    }

    @Override
    public void checkPrint(String salesOrderGuid) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGuid);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.CheckPrint)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        int noteCode = xmlData.getApiNote().getNoteCode();
                        if (1 == noteCode) {
                            mView.onCheckPrintSuccess();
                            if (xmlData.getApiNote().getResultCode() == 10) {
                                mView.showMessage(xmlData.getApiNote().getResultMsg());
                            }
                        } else if (noteCode == -4) {
                            mView.showMessage(xmlData.getApiNote().getResultMsg());
                        } else {
                            mView.showMessage(xmlData.getApiNote().getNoteMsg());
                        }
                    }
                });
    }

    @Override
    public void getHasOpenMemberPrice() {
        mRepository.getSystemConfig().subscribe(parametersConfig -> mView.onGetHasOpenMemberPrice(parametersConfig.isMemberPrice()));
    }
}
