package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PredictedTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.OrderRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.List;

import io.reactivex.disposables.Disposable;

/**
 * Created by tcw on 2017/9/5.
 */
public class PredictedTablePresenter extends BasePresenter<PredictedTableContract.View> implements PredictedTableContract.Presenter {

    public PredictedTablePresenter(PredictedTableContract.View view) {
        super(view);
    }

    @Override
    public void requestOrderRecord(List<Integer> orderStatList) {
        mRepository.getAccountRecord()
                .flatMap(accountRecord -> {
                    OrderRecordE orderRecordE = new OrderRecordE();
                    orderRecordE.setBusinessDay(accountRecord.getBusinessDay());
                    orderRecordE.setOrderStatList(orderStatList);
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.OrderRecordB.GetListByCurrent)
                            .setRequestBody(orderRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onOrderRecordObtainSuccess(xmlData.getArrayOfOrderRecordE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onOrderRecordObtainFailed();
                        }
                    }
                });
    }
}