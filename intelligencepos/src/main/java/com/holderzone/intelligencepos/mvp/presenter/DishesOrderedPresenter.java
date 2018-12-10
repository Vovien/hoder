package com.holderzone.intelligencepos.mvp.presenter;

import com.blankj.utilcode.util.EmptyUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.DishesOrderedContract;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesN;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchN;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderServingDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.mvp.model.prefs.PrefsManagerImpl;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesRecordViewBean;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesViewBean;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.helper.ApiNoteHelper;
import com.holderzone.intelligencepos.utils.helper.ExceptionHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tcw on 2017/9/4.
 */
public class DishesOrderedPresenter extends BasePresenter<DishesOrderedContract.View> implements DishesOrderedContract.Presenter {

    /**
     * 是否开启划菜功能
     */
    private Boolean mBoolean;
    private boolean mHasOpenMemberPrice;

    public DishesOrderedPresenter(DishesOrderedContract.View view) {
        super(view);
    }

    @Override
    public void checkBill(String SalesOrderGUID) {
        SalesOrderServingDishesE salesOrderServingDishesE = new SalesOrderServingDishesE();
        salesOrderServingDishesE.setSalesOrderGUID(SalesOrderGUID);
        XmlData.Builder()
                .setRequestMethod(RequestMethod.SalesOrderServingDishesB.CheckOrderServing)
                .setRequestBody(salesOrderServingDishesE)
                .buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {

                    @Override
                    protected void next(XmlData xmlData) {
                        mView.getChickBillResult(xmlData.getSalesOrderServingDishesE().getCheckState() == 1);
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                        }
                    }
                });
    }

    @Override
    public void requestArrayOfSalesOrderBatchE(String salesOrderGUID, String diningTableGUID) {
        PrefsManagerImpl.getInstance().getIsDesignatedDishes()
                .flatMap(aBoolean -> {
                    mBoolean = aBoolean;
                    SalesOrderBatchE salesOrderBatchE = new SalesOrderBatchE();
                    salesOrderBatchE.setReturnMemberInfo(1);
                    salesOrderBatchE.setSalesOrderGUID(salesOrderGUID);
                    salesOrderBatchE.setDiningTableGUID(diningTableGUID);
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.SalesOrderBatchB.GetListByTable_V1)
                            .setRequestBody(salesOrderBatchE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        if (mBoolean == null) {
                            mBoolean = false;
                        }
                        List<SalesOrderBatchN> arrayOfSalesOrderBatchE = xmlData.getArrayOfSalesOrderBatchN();
                        List<OrderedDishesRecordViewBean> record = new ArrayList<>();
                        List<OrderedDishesViewBean> dishesHangList = new ArrayList<>();
                        List<OrderedDishesViewBean> dishesNormalList = new ArrayList<>();
                        if (EmptyUtils.isNotEmpty(arrayOfSalesOrderBatchE)) {
                            for (SalesOrderBatchN batch : arrayOfSalesOrderBatchE) {
                                List<SalesOrderBatchDishesN> dishesList = batch.getArrayOfSalesOrderBatchDishesN();
                                if (EmptyUtils.isNotEmpty(dishesList)) {
                                    int size = dishesList.size();
                                    for (int i = 0; i < size; i++) {
                                        SalesOrderBatchDishesN dishes = dishesList.get(i);
                                        //--------------------------操作记录--------------------------
                                        OrderedDishesRecordViewBean bean = new OrderedDishesRecordViewBean();
                                        OrderedDishesViewBean activeDishes = new OrderedDishesViewBean();
                                        bean.setCookMethod(dishes.getPracticeStr());
                                        bean.setShowTopSpace(i == 0);
                                        if (EmptyUtils.isNotEmpty(dishes.getPracticeStr())) {
                                            if (batch.getOperationType() == -1) {
                                                bean.setCookMethodPrice("-￥" + ArithUtil.stripTrailingZeros(ArithUtil.round(ArithUtil.mul(dishes.getBackCount(), dishes.getPracticePrice()), 2)));
                                            } else {
                                                bean.setCookMethodPrice("￥" + ArithUtil.stripTrailingZeros(ArithUtil.round(ArithUtil.mul(dishes.getOrderCount(), dishes.getPracticePrice()), 2)));
                                            }
                                        }
                                        if (batch.getOperationType() == -1) {
                                            bean.setPrice("-￥" + ArithUtil.stripTrailingZeros(ArithUtil.round(ArithUtil.mul(dishes.getBackCount(), dishes.getPrice()), 2)));
                                        } else {
                                            bean.setPrice("￥" + ArithUtil.stripTrailingZeros(ArithUtil.round(ArithUtil.mul(dishes.getOrderCount(), dishes.getPrice()), 2)));
                                        }
                                        if (i == 0) {
                                            bean.setTitleColorResId(batch.getOperationType() == -1 ? R.color.layout_bg_red_f56766 : R.color.layout_bg_green_01b6ad);
                                            bean.setShowTitle(true);
                                            bean.setOperateTime(batch.getBatchTime());
                                            bean.setOperatePrice((batch.getOperationType() == -1 ? "-" : "") + "￥" + ArithUtil.stripTrailingZeros(ArithUtil.round(batch.getTotal(), 2)));

                                        }

                                        bean.setSubDishesString(dishes.getSubDishesStr());
                                        if (dishes.getGift() == 1) {
                                            bean.setDishesTagName("[赠]");
                                            activeDishes.setDishesTagName("[赠]");
                                            bean.setDishesTagColorResId(R.color.common_text_color_01b6ad);
                                            activeDishes.setDishesTagColorResId(R.color.common_text_color_01b6ad);
                                        } else if (mHasOpenMemberPrice && dishes.getIsMemberPrice() == 1) {
                                            bean.setDishesTagName("[会]");
                                            activeDishes.setDishesTagName("[会]");
                                            bean.setDishesTagColorResId(R.color.common_text_color_f4a902);
                                            activeDishes.setDishesTagColorResId(R.color.common_text_color_f4a902);
                                        }
                                        bean.setDishesName(dishes.getSimpleName());
                                        bean.setCount("×" + ArithUtil.stripTrailingZeros(batch.getOperationType() == -1 ? dishes.getBackCount() : dishes.getOrderCount()));
                                        if (i == size - 1) {
                                            bean.setShowBottomSpace(true);
                                            if (EmptyUtils.isNotEmpty(batch.getReturnReasonStr())) {
                                                bean.setReturnReason("退因：" + batch.getReturnReasonStr());
                                            }
                                        }
                                        bean.setShowDivider(i < size - 1 || EmptyUtils.isNotEmpty(bean.getReturnReason()));
                                        bean.setReturnDishes(batch.getOperationType() == -1);
                                        record.add(bean);
                                        //--------------------------操作记录--------------------------
                                        if (batch.getOperationType() == -1 || dishes.getOrderCount().compareTo(dishes.getBackCount()) == 0) {
                                            continue;
                                        }
                                        activeDishes.setSubDishesString(dishes.getSubDishesStr());
                                        activeDishes.setDishesName(dishes.getSimpleName());
                                        activeDishes.setCookMethod(dishes.getPracticeStr());
                                        activeDishes.setCookMethodPrice(ArithUtil.round(ArithUtil.mul(dishes.getPracticePrice(), dishes.getCheckCount()), 2));
                                        activeDishes.setCount(dishes.getCheckCount());
                                        activeDishes.setPrice(ArithUtil.round(ArithUtil.mul(dishes.getPrice(), dishes.getCheckCount()), 2));
                                        activeDishes.setServingCount(dishes.getServingCount());
                                        //0 = 挂起 1 = 叫起 2=制作
                                        if (dishes.getDishesStatus() == 0) {
                                            dishesHangList.add(activeDishes);
                                        } else {
                                            dishesNormalList.add(activeDishes);
                                        }
                                    }
                                }
                            }
                        }
                        if (dishesHangList.size() > 0) {
                            dishesHangList.get(0).setGroupTitleColorResId(R.color.btn_text_orange_f4a902);
                            dishesHangList.get(0).setShowTopSpace(true);
                            dishesHangList.get(0).setGroupTitleName("挂起中（" + dishesHangList.size() + "）");
                            dishesHangList.get(dishesHangList.size() - 1).setGoneDivider(true);
                            dishesHangList.get(0).setGroupTitleImgResId(R.drawable.dishes_hanged_up_title);
                        }
                        if (dishesNormalList.size() > 0) {
                            dishesNormalList.get(0).setGroupTitleColorResId(R.color.btn_text_red_f56766);
                            dishesNormalList.get(0).setShowTopSpace(true);
                            dishesNormalList.get(0).setGroupTitleName("已叫起（" + dishesNormalList.size() + "）");
                            dishesNormalList.get(dishesNormalList.size() - 1).setGoneDivider(true);
                            dishesNormalList.get(0).setGroupTitleImgResId(R.drawable.dishes_called_up_title);
                        }
                        dishesHangList.addAll(dishesNormalList);
                        mView.onRequestArrayOfSalesOrderBatchSucceed(record, dishesHangList, mBoolean, xmlData.getMemberInfoE());
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        if (ExceptionHelper.checkNetException(e)) {
                            mView.onNetworkError();
                        } else {
                            mView.onRequestArrayOfSalesOrderBatchFailed();
                        }
                    }
                });
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

    @Override
    public void getHasOpenMemberPrice() {
        mRepository.getSystemConfig().subscribe(parametersConfig -> mHasOpenMemberPrice = parametersConfig.isMemberPrice());
    }
}