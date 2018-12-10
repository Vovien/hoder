package com.holderzone.intelligencepos.mvp.model.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecordDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFeesDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.DaoMaster;
import com.holderzone.intelligencepos.mvp.model.bean.db.DaoSession;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfoDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBeanDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItemDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.PrintBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.PrintBeanDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.bean.db.StoreDao;
import com.holderzone.intelligencepos.mvp.model.bean.db.Users;
import com.holderzone.intelligencepos.mvp.model.bean.db.UsersDao;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.memoizrlabs.retrooptional.Optional;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * 数据库模块
 * Created by tcw on 2017/7/21.
 */

public class DbManagerImpl implements DbManager, DbPrintModule {

    private volatile static DbManagerImpl sInstance;

    private final EnterpriseInfoDao mEenterpriseInfoDao;
    private final StoreDao mStoreDao;
    private final UsersDao mUsersDao;
    private final AccountRecordDao mAccountRecordDao;
    private final PrintBeanDao mPrintBeanDao;
    private final AdditionalFeesDao mAdditionalFeesDao;
    private final PaymentItemDao mPaymentItemDao;
    private final ImageBeanDao mImageBeanDao;

    public static DbManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (DbManagerImpl.class) {
                if (sInstance == null) {
                    sInstance = new DbManagerImpl();
                }
            }
        }
        return sInstance;
    }

    private DbManagerImpl() {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(BaseApplication.getContext(), "intelligencepad_db", null);
        SQLiteDatabase sqLiteDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(sqLiteDatabase);
        DaoSession daoSession = daoMaster.newSession();
        mEenterpriseInfoDao = daoSession.getEnterpriseInfoDao();
        mStoreDao = daoSession.getStoreDao();
        mUsersDao = daoSession.getUsersDao();
        mAccountRecordDao = daoSession.getAccountRecordDao();
        mPrintBeanDao = daoSession.getPrintBeanDao();
        mAdditionalFeesDao = daoSession.getAdditionalFeesDao();
        mPaymentItemDao = daoSession.getPaymentItemDao();
        mImageBeanDao = daoSession.getImageBeanDao();
    }

    @Override
    public Observable<EnterpriseInfo> getEnterpriseInfo() {
        return Observable.create(e -> {
            List<EnterpriseInfo> enterpriseInfos = mEenterpriseInfoDao.loadAll();
            if (enterpriseInfos.size() > 0) {
                e.onNext(enterpriseInfos.get(0));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<String>> queryAvailableLocalPath(ImageBean.Type type) {
        List<String> localUrls = new ArrayList<>();
        List<ImageBean> imageBeanList = mImageBeanDao.queryBuilder().where(ImageBeanDao.Properties.Type.eq(type.getType())).list();
        for (ImageBean imageBean : imageBeanList) {
            localUrls.add(imageBean.getLocalUrl());
        }
        return Observable.just(localUrls);
    }

    @Override
    public Observable<Optional<EnterpriseInfo>> getOptionalEnterpriseInfo() {
        return Observable.create(e -> {
            List<EnterpriseInfo> enterpriseInfos = mEenterpriseInfoDao.loadAll();
            if (enterpriseInfos.size() > 0) {
                e.onNext(Optional.of(enterpriseInfos.get(0)));
            } else {
                e.onNext(Optional.empty());
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<EnterpriseInfo> saveEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        return Observable.create(e -> {
            mEenterpriseInfoDao.deleteAll();
            mEenterpriseInfoDao.insert(enterpriseInfo);
            e.onNext(enterpriseInfo);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> insertImageBeanToDb(ImageBean imageBean) {
        mImageBeanDao.insert(imageBean);
        return Observable.just(true);
    }

    @Override
    public Observable<Boolean> updateImageBeanToDb(ImageBean imageBean) {
        Query<ImageBean> query = mImageBeanDao.queryBuilder()
                .where(ImageBeanDao.Properties.RemoteUrl.eq(imageBean.getRemoteUrl()))
                .build();
        ImageBean unique = query.unique();
        if (unique != null) {
            unique.setLocalUrl(imageBean.getLocalUrl());
            mImageBeanDao.update(unique);
            return Observable.just(true);
        }
        return Observable.error(new Exception("未找到匹配的ImageBean"));
    }

    @Override
    public Observable<Store> getStore() {
        return Observable.create(e -> {
            List<Store> storeList = mStoreDao.loadAll();
            if (storeList.size() > 0) {
                e.onNext(storeList.get(0));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Optional<Store>> getOptionalStore() {
        return Observable.create(e -> {
            List<Store> storeList = mStoreDao.loadAll();
            if (storeList.size() > 0) {
                e.onNext(Optional.of(storeList.get(0)));
            } else {
                e.onNext(Optional.empty());
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Store> saveStore(Store store) {
        return Observable.create(e -> {
            mStoreDao.deleteAll();
            mStoreDao.insert(store);
            e.onNext(store);
            e.onComplete();
        });
    }

    @Override
    public Observable<Users> getUsers() {
        return Observable.create(e -> {
            List<Users> usersList = mUsersDao.loadAll();
            if (usersList.size() > 0) {
                e.onNext(usersList.get(0));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Users> saveUsers(Users users) {
        return Observable.create(e -> {
            mUsersDao.deleteAll();
            mUsersDao.insert(users);
            e.onNext(users);
            e.onComplete();
        });
    }

    @Override
    public Observable<AccountRecord> getAccountRecord() {
        return Observable.create(e -> {
            List<AccountRecord> accountRecordList = mAccountRecordDao.loadAll();
            if (accountRecordList.size() > 0) {
                e.onNext(accountRecordList.get(0));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<AccountRecord> saveAccountRecord(AccountRecord accountRecord) {
        return Observable.create(e -> {
            mAccountRecordDao.deleteAll();
            mAccountRecordDao.insert(accountRecord);
            e.onNext(accountRecord);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<AdditionalFees>> getAllAdditionalFee() {
        return Observable.create(e -> {
            List<AdditionalFees> additionalFeesList = mAdditionalFeesDao.loadAll();
            if (additionalFeesList != null) {
                e.onNext(additionalFeesList);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<AdditionalFees>> saveAllAdditionalFee(List<AdditionalFees> allAdditionalFee) {
        return Observable.create(e -> {
            mAdditionalFeesDao.deleteAll();
            for (AdditionalFees additionalFees : allAdditionalFee) {
                mAdditionalFeesDao.insert(additionalFees);
            }
            e.onNext(allAdditionalFee);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<PaymentItem>> getAllPaymentItem() {
        return Observable.create(e -> {
            List<PaymentItem> paymentItemList = mPaymentItemDao.loadAll();
            if (paymentItemList != null) {
                e.onNext(paymentItemList);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<PaymentItem>> saveAllPaymentItem(List<PaymentItem> allPaymentItem) {
        return Observable.create(e -> {
            mPaymentItemDao.deleteAll();
            for (PaymentItem paymentItem : allPaymentItem) {
                mPaymentItemDao.insert(paymentItem);
            }
            e.onNext(allPaymentItem);
            e.onComplete();
        });
    }

    @Override
    public Observable<String> checkRemoteUrlInDbExist(String remoteUrl) {
        Query<ImageBean> query = mImageBeanDao.queryBuilder()
                .where(ImageBeanDao.Properties.RemoteUrl.eq(remoteUrl))
                .build();
        ImageBean unique = query.unique();
        if (unique != null) {
            return Observable.just(unique.getLocalUrl());
        }
        return Observable.just("");
    }

    @Override
    public Observable<Boolean> releaseForReLogin() {
        return Observable.create(e -> {
            mUsersDao.deleteAll();
            mAccountRecordDao.deleteAll();
            e.onNext(true);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> releaseForAppExit() {
        return Observable.create(e -> {
            mUsersDao.deleteAll();
            mAccountRecordDao.deleteAll();
            e.onNext(true);
            e.onComplete();
        });
    }

    /**
     * 保存打印信息和修改打印次数
     */
    @Override
    public Observable<Boolean> save(List<PrinterE> list) {
        return Observable.create(e -> {
            for (PrinterE printerE : list) {
                List<PrintBean> qb = mPrintBeanDao.queryBuilder().where(PrintBeanDao.Properties.Key.eq(printerE.getPrintKey())).list();
                PrintBean dbBean = null;
                if (qb.size() > 0) {
                    dbBean = qb.get(0);
                    Log.d("Tag", "db_has_data---" + dbBean.getId());
                    printerE.setId(dbBean.getId());
                    printerE.setPrintTimes(printerE.getPrintTimes() - dbBean.getPrintFinishTimes());
                } else {
                    dbBean = new PrintBean();
                    dbBean.setPrintTimes(printerE.getPrintTimes());
                    dbBean.setIsUpload(false);
                    dbBean.setKey(printerE.getPrintKey());
                    dbBean.setPrintFinishTimes(0);
                    mPrintBeanDao.save(dbBean);
                    Log.d("Tag", "saveAfter---" + dbBean.getId());
                    printerE.setId(dbBean.getId());
                }
            }
            e.onNext(true);
            e.onComplete();
        });
    }

    /**
     * 更新打印成功数据
     */
    @Override
    public void updateSuccessPrintBean(PrinterE printerE) {
        if (printerE.getPrintFinishTimes() > 0 && printerE.getId() != null) {
            PrintBean dbBean = mPrintBeanDao.loadByRowId(printerE.getId());
            if (dbBean != null) {
                dbBean.setPrintFinishTimes(dbBean.getPrintFinishTimes() + printerE.getPrintFinishTimes());
                if (dbBean.getPrintTimes() <= dbBean.getPrintFinishTimes()) {
                    dbBean.setIsPrintFinish(true);
                }
            }
            mPrintBeanDao.update(dbBean);
        }
    }

    /**
     * 更新打印成功数据
     */
    @Override
    public Observable<Boolean> updateSuccessPrintBean(List<PrinterE> list) {
        return Observable.create(e -> {
            for (PrinterE printerE : list) {
                if (printerE.getPrintFinishTimes() > 0 && printerE.getId() != null) {
                    PrintBean dbBean = mPrintBeanDao.loadByRowId(printerE.getId());
                    if (dbBean != null) {
                        dbBean.setPrintFinishTimes(dbBean.getPrintFinishTimes() + printerE.getPrintFinishTimes());
                        if (dbBean.getPrintTimes() <= dbBean.getPrintFinishTimes()) {
                            dbBean.setIsPrintFinish(true);
                        }
                    }
                    mPrintBeanDao.update(dbBean);
                }
            }
            e.onNext(true);
            e.onComplete();
        });
    }

    /**
     * 获取待清除数据
     */
    @Override
    public Observable<List<PrinterE>> getCleanPrintData() {
        return Observable.zip(RepositoryImpl.getInstance().getEnterpriseInfo(), RepositoryImpl.getInstance().getStore(), Pair::new)
                .flatMap(pair -> Observable.create(e -> {
                    List<PrintBean> list = mPrintBeanDao.queryBuilder().where(PrintBeanDao.Properties.IsPrintFinish.eq(true), PrintBeanDao.Properties.IsUpload.eq(false)).list();
                    List<PrinterE> returnList = new ArrayList<>();
                    if (list != null && list.size() > 0) {
                        for (PrintBean printBean : list) {
                            PrinterE printerE = new PrinterE();
                            printerE.setPrintKey(printBean.getKey());
                            printerE.setId(printBean.getId());
                            printerE.setEnterpriseInfoGUID(pair.first.getEnterpriseInfoGUID());
                            printerE.setStoreGUID(pair.second.getStoreGUID());
                            printerE.setDeviceID(DeviceHelper.getInstance().getDeviceID());
                            returnList.add(printerE);
                        }
                    }
                    e.onNext(returnList);
                    e.onComplete();
                }));
    }

    /**
     * 更新打印成功上传状态
     */
    @Override
    public Observable<Boolean> updatePrintBeanUploadStatus(List<Long> list) {
        return Observable.create(e -> {
            for (Long id : list) {
                PrintBean dbBean = mPrintBeanDao.loadByRowId(id);
                if (dbBean != null) {
                    dbBean.setIsUpload(true);
                }
                mPrintBeanDao.update(dbBean);
            }
            e.onNext(true);
            e.onComplete();
        });
    }
}
