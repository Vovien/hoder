package com.holderzone.intelligencepos.mvp.model.db;

import com.holderzone.intelligencepos.mvp.model.ReleaseResourceManager;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.bean.db.Users;
import com.memoizrlabs.retrooptional.Optional;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by tcw on 2017/7/21.
 */

public interface DbManager extends ReleaseResourceManager {

    Observable<EnterpriseInfo> getEnterpriseInfo();

    Observable<Optional<EnterpriseInfo>> getOptionalEnterpriseInfo();

    Observable<EnterpriseInfo> saveEnterpriseInfo(EnterpriseInfo enterpriseInfo);

    Observable<Store> getStore();

    Observable<Optional<Store>> getOptionalStore();

    Observable<Store> saveStore(Store store);

    Observable<Users> getUsers();

    Observable<Users> saveUsers(Users users);

    Observable<AccountRecord> getAccountRecord();

    Observable<AccountRecord> saveAccountRecord(AccountRecord accountRecord);

    Observable<List<AdditionalFees>> getAllAdditionalFee();

    Observable<List<AdditionalFees>> saveAllAdditionalFee(List<AdditionalFees> allAdditionalFee);

    Observable<List<PaymentItem>> getAllPaymentItem();

    Observable<List<PaymentItem>> saveAllPaymentItem(List<PaymentItem> allPaymentItem);

    Observable<String> checkRemoteUrlInDbExist(String remoteUrl);

    Observable<Boolean> insertImageBeanToDb(ImageBean imageBean);

    Observable<Boolean> updateImageBeanToDb(ImageBean imageBean);

    /**
     * 查询所有可用的localUrls
     *
     * @return
     */
    public Observable<List<String>> queryAvailableLocalPath(ImageBean.Type type);
}
