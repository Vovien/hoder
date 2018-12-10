package com.holderzone.intelligencepos.mvp.presenter;

import android.annotation.SuppressLint;
import android.util.Pair;

import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.HomeContract;
import com.holderzone.intelligencepos.mvp.model.bean.AccountRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.HomeItemBean;
import com.holderzone.intelligencepos.mvp.model.bean.OrderCountE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 主页
 * Created by tcw on 2017/4/17.
 */

public class HomePresenter extends BasePresenter<HomeContract.View> implements HomeContract.Presenter {
    public static final String ITEM_TABLE_GUID = "ITEM_TABLE_GUID";
    public static final String ITEM_SNACK_DISHES_GUID = "ITEM_SNACK_DISHES_GUID";
    public static final String ITEM_TAKE_OUT_GUID = "ITEM_TAKE_OUT_GUID";
    public static final String ITEM_QUEUE_GUID = "ITEM_QUEUE_GUID";
    public static final String ITEM_CASH_REGISTER_GUID = "ITEM_CASH_REGISTER_GUID";
    public static final String ITEM_SOFTWARE_SETTING_GUID = "ITEM_SOFTWARE_SETTING_GUID";

    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public HomePresenter(HomeContract.View view) {
        super(view);
    }


    @Override
    public void getAccountRecord() {
        XmlData.Builder()
                .setRequestMethod(RequestMethod.AccountRecordB.GetCurrent)
                .setRequestBody(new AccountRecordE()).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView, false, false))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        AccountRecordE accountRecordE1 = xmlData.getAccountRecordE();
                        if (accountRecordE1 != null) {
                            mView.onAccountRecordGetSuccess(accountRecordE1);
                        } else {
                            mView.onAccountRecordGetFailed();
                        }
                    }

                    @Override
                    protected void error(Throwable e) {
                        super.error(e);
                        mView.onAccountRecordGetFailed();
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void saveAccountRecord(AccountRecord accountRecord) {
        mRepository.saveAccountRecord(accountRecord)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(mView::onAccountRecordSaveSuccess);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getLocalStore() {
        mRepository.getStore()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))
                .subscribe(mView::onLocalStoreGetSucceed);
    }

    @Override
    public void requestMainGUIRefreshInfo() {
        Observable.zip(mRepository.getStore(), mRepository.getEnterpriseInfo(), Pair::new)
                .flatMap(pair -> {
                    Store store = pair.first;
                    EnterpriseInfo enterpriseInfo = pair.second;
                    AccountRecordE accountRecordE = new AccountRecordE();
                    accountRecordE.setStoreGUID(store.getStoreGUID());
                    accountRecordE.setEnterpriseInfoGUID(enterpriseInfo.getEnterpriseInfoGUID());
                    return XmlData.Builder().setRequestMethod(RequestMethod.AccountRecordB.GetOrderCount)
                            .setRequestBody(accountRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mCompositeDisposable.add(d);
                    }

                    @Override
                    protected void next(XmlData xmlData) {
                        OrderCountE orderCountE = xmlData.getOrderCountE();
                        mView.refreshMainGui(initVersionDate(orderCountE));
                    }
                });
    }

    @Override
    public void disposeMainGUIRefreshInfo() {
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
            mView.onMainGUIRefreshInfoDisposed();
        }
    }

    @Override
    public void getHesVersion() {
        mRepository.getSystemConfig().subscribe(config -> mView.onGetHesVersion(config.isHesVersion()));
    }

    /**
     * pager数据
     */
    private List<HomeItemBean> initVersionDate(OrderCountE orderCountE) {
        List<HomeItemBean> itemBeans = new ArrayList<>();
        HomeItemBean table = new HomeItemBean();
        table.setItemGUID(ITEM_TABLE_GUID);
//        table.setImageId(R.drawable.table_icon);
        table.setOrderCount(orderCountE.getTSOrderCount());
        table.setName("堂食收银");
        HomeItemBean snack = new HomeItemBean();
        snack.setItemGUID(ITEM_SNACK_DISHES_GUID);
//        snack.setImageId(R.drawable.snack_icon);
        snack.setOrderCount(orderCountE.getSnackOrderCount());
        snack.setName("快餐收银");
        HomeItemBean takeOut = new HomeItemBean();
        takeOut.setItemGUID(ITEM_TAKE_OUT_GUID);
//        takeOut.setImageId(R.drawable.take_out_icon);
        takeOut.setOrderCount(orderCountE.getWmOrderCount());
        takeOut.setName("外卖订单");
        HomeItemBean queue = new HomeItemBean();
        queue.setItemGUID(ITEM_QUEUE_GUID);
//        queue.setImageId(R.drawable.queue_icon);
        queue.setOrderCount(orderCountE.getQueueUpCount());
        queue.setName("排队");
        HomeItemBean cash = new HomeItemBean();
        cash.setItemGUID(ITEM_CASH_REGISTER_GUID);
//        cash.setImageId(R.drawable.cash_icon);
        cash.setName("营业数据");
        HomeItemBean setting = new HomeItemBean();
        setting.setItemGUID(ITEM_SOFTWARE_SETTING_GUID);
//        setting.setImageId(R.drawable.setting_icon);
        setting.setName("功能设置");

        itemBeans.add(table);
        itemBeans.add(snack);
        itemBeans.add(takeOut);
        itemBeans.add(queue);
        itemBeans.add(cash);
        itemBeans.add(setting);

        return itemBeans;
    }
}
