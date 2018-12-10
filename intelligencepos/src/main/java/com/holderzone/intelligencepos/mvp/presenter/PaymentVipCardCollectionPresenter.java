package com.holderzone.intelligencepos.mvp.presenter;

import android.annotation.SuppressLint;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.PaymentVipCardCollectionContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.ApiException;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.EncoderUtil;
import com.holderzone.intelligencepos.utils.RxUtils;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 会员卡支付 收款
 * Created by tcw on 2017/5/31.
 */

public class PaymentVipCardCollectionPresenter extends BasePresenter<PaymentVipCardCollectionContract.View> implements PaymentVipCardCollectionContract.Presenter {

    public PaymentVipCardCollectionPresenter(PaymentVipCardCollectionContract.View view) {
        super(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void hesMemberPay(String salesOrderGUID, String paymentItemCode, double payableAmount, int useMemberBlance, String content, String password) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderPaymentE.setCreateStaffGUID(users.getUsersGUID());
                    salesOrderPaymentE.setPaymentItemCode(paymentItemCode);
                    salesOrderPaymentE.setPayableAmount(payableAmount);
                    salesOrderPaymentE.setUseMemberBlance(useMemberBlance);
                    salesOrderPaymentE.setContent(content);
                    if (password != null) {
                        salesOrderPaymentE.setPayPassword(EncoderUtil.encode(password));
                    }
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.AddMemberPlat)
                            .setRequestBody(salesOrderPaymentE)
                            .buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .flatMap(xmlData -> {
                    if (RxUtils.checkBusiness(xmlData)) {
                        SalesOrderE salesOrderE = new SalesOrderE();
                        salesOrderE.setSalesOrderGUID(salesOrderGUID);
                        salesOrderE.setReturnSalesOrderPayment(1);
                        return XmlData.Builder()
                                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                                .setRequestBody(salesOrderE)
                                .buildRESTful()
                                .flatMap(mRepository::getXmlData);
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE salesOrder = null;
                        if (xmlData.getArrayOfSalesOrderE().size() == 1) {
                            salesOrder = xmlData.getArrayOfSalesOrderE().get(0);
                        } else {
                            for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
                                if (bean.getUpperState() == 1) {
                                    salesOrder = bean;
                                    break;
                                }
                            }
                        }
                        mView.onPaySuccess(salesOrder);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
//                        ApiException apiException = (ApiException) e;
//                        if (apiException.getResultCode() == -104) {// resultCode==-104密码错误
//                            mView.onPayFailedOutOfWrongPsd();
//                        }
                    }
                });

    }

    @Override
    public void submitVipCardPay(String saleOrderGUID, String paymentItemCode, double payableAmount, String cardsChipNo, String memberPassWord) {
        mRepository.getUsers()
                .flatMap(users -> {
                    SalesOrderPaymentE salesOrderPaymentE = new SalesOrderPaymentE();
                    salesOrderPaymentE.setSalesOrderGUID(saleOrderGUID);
                    salesOrderPaymentE.setPaymentItemCode(paymentItemCode);
                    salesOrderPaymentE.setPayableAmount(payableAmount);
                    salesOrderPaymentE.setCardsChipNo(cardsChipNo);
                    salesOrderPaymentE.setCreateStaffGUID(users.getUsersGUID());
                    salesOrderPaymentE.setMemberPassWord(memberPassWord);
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderPaymentB.AddVipCard)
                            .setRequestBody(salesOrderPaymentE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .flatMap(xmlData -> {
                    if (ApiNoteHelper.checkBusiness(xmlData)) {
                        SalesOrderE salesOrderE = new SalesOrderE();
                        salesOrderE.setSalesOrderGUID(saleOrderGUID);
                        salesOrderE.setReturnSalesOrderPayment(1);
                        return XmlData.Builder()
                                .setRequestMethod(RequestMethod.SalesOrderB.GetSingle)
                                .setRequestBody(salesOrderE).buildRESTful()
                                .flatMap(mRepository::getXmlData);
                    } else {
                        return Observable.error(new ApiException(xmlData));
                    }
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        SalesOrderE salesOrder = null;
                        if (xmlData.getArrayOfSalesOrderE().size() == 1) {
                            salesOrder = xmlData.getArrayOfSalesOrderE().get(0);
                        } else {
                            for (SalesOrderE bean : xmlData.getArrayOfSalesOrderE()) {
                                if (bean.getUpperState() == 1) {
                                    salesOrder = bean;
                                    break;
                                }
                            }
                        }
                        mView.onPaySuccess(salesOrder);
                    }

                    @Override
                    protected void error(Throwable e) {
                        if (e instanceof ApiException) {
                            ApiException apiException = (ApiException) e;
                            if (apiException.getResultCode() == -104) {// resultCode==-104密码错误
                                mView.onPayFailedOutOfWrongPsd();
                            }
                        }
                    }
                });
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
