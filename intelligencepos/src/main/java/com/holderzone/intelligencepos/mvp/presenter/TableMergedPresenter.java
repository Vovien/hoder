package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.TableMergedContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by tcw on 2017/9/4.
 */
public class TableMergedPresenter extends BasePresenter<TableMergedContract.View> implements TableMergedContract.Presenter {

    public TableMergedPresenter(TableMergedContract.View view) {
        super(view);
    }

    @Override
    public void requestPrintPrepayment(String salesOrderGUID) {
        SalesOrderE salesOrderE = new SalesOrderE();
        salesOrderE.setSalesOrderGUID(salesOrderGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.CheckPrint)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (ApiNoteHelper.checkBusiness(xmlData)) {
                            if (ApiNoteHelper.checkPrinterWhenBusinessSuccess(xmlData)) {
                                mView.showMessage(ApiNoteHelper.obtainPrinterMsg(xmlData));
                            } else {
                                mView.onRequestPrintPrepaymentSucceed(ApiNoteHelper.obtainSuccessMsg(xmlData));
                            }
                        } else {
                            mView.showMessage(ApiNoteHelper.obtainErrorMsg(xmlData));
                        }
                    }
                });
    }
}