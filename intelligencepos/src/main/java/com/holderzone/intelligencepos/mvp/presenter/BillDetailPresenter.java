package com.holderzone.intelligencepos.mvp.presenter;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.BillDetailContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

/**
 * Created by chencao on 2017/6/5.
 */

public class BillDetailPresenter extends BasePresenter<BillDetailContract.View> implements BillDetailContract.Presenter {
    public BillDetailPresenter(BillDetailContract.View view) {
        super(view);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void setRequestBillInfomation(SalesOrderE salesOrderE) {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (xmlData.getApiNote().getNoteCode() == -101) {
                            mView.showAuthorizationDialog("授权过期");
                            return;
                        } else {
                            if (xmlData.getApiNote().getNoteCode() != 1) {
                                if (xmlData.getApiNote().getResultMsg() != null) {
                                    mView.getError(xmlData.getApiNote().getResultMsg());
                                } else {
                                    mView.getError(xmlData.getApiNote().getNoteMsg());
                                }
                            } else {
                                mView.getResponsSalesOrderEInfomation(xmlData.getArrayOfSalesOrderE());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.showNetworkError();
                        }
                    }
                });
    }

    @Override
    public void setRequestCounterCheck(SalesOrderE salesOrderE) {
        mRepository.getUsers()
                .flatMap(users -> {
                    salesOrderE.setStaffGUID(users.getUsersGUID());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderB.Recovery)
                            .setRequestBody(salesOrderE).buildRESTful();
                })
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
//                        Log.e("msg", "---getHttpData--访问服务器成功----->" + new Gson().toJson(xmlData));
                        if (xmlData.getApiNote().getNoteCode() == -101) {
                            mView.showAuthorizationDialog("授权过期");
                            return;
                        } else {
                            if (xmlData.getApiNote().getResultCode() == 1) {
                                mView.getResponsByReturnCount("业务成功");
                            } else if (xmlData.getApiNote().getNoteCode() == -4 && xmlData.getApiNote().getResultCode() == -100) {
                                mView.getResponsByReturnCount(xmlData.getApiNote().getResultMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }

    @Override
    public void setRequestPrint(SalesOrderE salesOrderE) {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderB.CheckPrint)
                .setRequestBody(salesOrderE).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView, false) {
                    @Override
                    protected void next(XmlData xmlData) {
//                        Log.e("msg", "---getHttpData--访问服务器成功----->" + new Gson().toJson(xmlData));
                        if (xmlData.getApiNote().getNoteCode() == -101) {
                            mView.showAuthorizationDialog("授权过期");
                            return;
                        } else {
                            if (xmlData.getApiNote().getResultCode() == 1) {
                                mView.getResopnsPrint(xmlData.getApiNote().getResultMsg());
                            } else {
                                mView.getError(xmlData.getApiNote().getResultMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                    }
                });
    }
}
