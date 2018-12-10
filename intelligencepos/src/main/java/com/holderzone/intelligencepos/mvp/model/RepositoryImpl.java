package com.holderzone.intelligencepos.mvp.model;

import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;
import com.holderzone.intelligencepos.mvp.model.bean.db.EnterpriseInfo;
import com.holderzone.intelligencepos.mvp.model.bean.db.ImageBean;
import com.holderzone.intelligencepos.mvp.model.bean.db.PaymentItem;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.model.bean.db.Users;
import com.holderzone.intelligencepos.mvp.model.cache.CacheManager;
import com.holderzone.intelligencepos.mvp.model.cache.CacheManagerImpl;
import com.holderzone.intelligencepos.mvp.model.db.DbManager;
import com.holderzone.intelligencepos.mvp.model.db.DbManagerImpl;
import com.holderzone.intelligencepos.mvp.model.file.FileManager;
import com.holderzone.intelligencepos.mvp.model.file.FileManagerImpl;
import com.holderzone.intelligencepos.mvp.model.network.NetworkManager;
import com.holderzone.intelligencepos.mvp.model.network.NetworkManagerImpl;
import com.holderzone.intelligencepos.mvp.model.prefs.PrefsManager;
import com.holderzone.intelligencepos.mvp.model.prefs.PrefsManagerImpl;
import com.holderzone.intelligencepos.printer.PushFeedback;
import com.holderzone.intelligencepos.printer.PushPrintResponse;
import com.memoizrlabs.retrooptional.Optional;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 模型层高层模块
 * Created by tcw on 2017/7/21.
 */

public class RepositoryImpl implements Repository {

    private volatile static RepositoryImpl sInstance;

    private CacheManager mCacheManager;

    private FileManager mFileManager;

    private PrefsManager mPrefsManager;

    private DbManager mDbManager;

    private NetworkManager mNetworkManager;

    public static RepositoryImpl getInstance() {
        if (sInstance == null) {
            synchronized (RepositoryImpl.class) {
                if (sInstance == null) {
                    sInstance = new RepositoryImpl();
                }
            }
        }
        return sInstance;
    }

    private RepositoryImpl() {
        mCacheManager = CacheManagerImpl.getInstance();
        mFileManager = FileManagerImpl.getInstance();
        mPrefsManager = PrefsManagerImpl.getInstance();
        mDbManager = DbManagerImpl.getInstance();
        mNetworkManager = NetworkManagerImpl.getInstance();
    }

    @Override
    public Observable<XmlData> getXmlData(XmlData xmlData) {
        return mNetworkManager.getXmlData(xmlData);
    }

    @Override
    public Observable<List<DishesTypeE>> getAllDishesTypeE() {
        Observable<List<DishesTypeE>> memory = mCacheManager.getAllDishesTypeE();
        Observable<List<DishesTypeE>> disk = mPrefsManager.getAllDishesTypeE()
                .flatMap(mCacheManager::saveAllDishesTypeE);
        return Observable.concat(memory, disk)
                .firstOrError().toObservable();
    }

    @Override
    public Observable<List<DishesTypeE>> saveAllDishesTypeE(List<DishesTypeE> allDishesTypeE) {
        return mCacheManager.saveAllDishesTypeE(allDishesTypeE)
                .flatMap(mPrefsManager::saveAllDishesTypeE);
    }

    @Override
    public Observable<List<DishesE>> getAllDishesE() {
        Observable<List<DishesE>> memory = mCacheManager.getAllDishesE();
        Observable<List<DishesE>> disk = mPrefsManager.getAllDishesE()
                .flatMap(mCacheManager::saveAllDishesE);
        return Observable.concat(memory, disk).firstOrError().toObservable();
    }

    @Override
    public Observable<List<DishesE>> saveAllDishesE(List<DishesE> allDishesE) {
        return mCacheManager.saveAllDishesE(allDishesE)
                .flatMap(mPrefsManager::saveAllDishesE);
    }

    @Override
    public Observable<Integer> getBtnStyle() {
        Observable<Integer> memory = mCacheManager.getBtnStyle();
        Observable<Integer> disk = mPrefsManager.getBtnStyle()
                .flatMap(mCacheManager::saveBtnStyle);
        return Observable.concat(memory, disk).first(0).toObservable();
    }

    @Override
    public Observable<Integer> saveBtnStyle(Integer btnStyle) {
        return mCacheManager.saveBtnStyle(btnStyle)
                .flatMap(mPrefsManager::saveBtnStyle);
    }

    @Override
    public Observable<Boolean> saveIsStartFlapper(boolean isStart) {
        return mCacheManager.saveIsStartFlapper(isStart)
                .flatMap(mPrefsManager::saveIsStartFlapper);
    }

    @Override
    public Observable<Boolean> getIsStartFlapper() {
        Observable<Boolean> memory = mCacheManager.getIsStartFlapper();
        Observable<Boolean> disk = mPrefsManager.getIsStartFlapper()
                .flatMap(mCacheManager::saveIsStartFlapper);
        return Observable.concat(memory, disk).first(false).toObservable();
    }

    @Override
    public Observable<Boolean> saveIsDesignatedDishes(boolean isDesignatedDishes) {
        return mCacheManager.saveIsDesignatedDishes(isDesignatedDishes)
                .flatMap(mPrefsManager::saveIsDesignatedDishes);
    }

    @Override
    public Observable<Boolean> getIsDesignatedDishes() {
        Observable<Boolean> memory = mCacheManager.getIsDesignatedDishes();
        Observable<Boolean> disk = mPrefsManager.getIsDesignatedDishes()
                .flatMap(mCacheManager::saveIsDesignatedDishes);
        return Observable.concat(memory, disk).first(false).toObservable();
    }

    @Override
    public Observable<ParametersConfig> saveSystemConfig(ParametersConfig parametersConfig) {
        return mPrefsManager.saveSystemConfig(parametersConfig);
    }

    @Override
    public Observable<ParametersConfig> getSystemConfig() {
        return mPrefsManager.getSystemConfig();
    }

    @Override
    public Observable<EnterpriseInfo> getEnterpriseInfo() {
        Observable<EnterpriseInfo> memory = mCacheManager.getEnterpriseInfo();
        Observable<EnterpriseInfo> disk = mDbManager.getEnterpriseInfo()
                .flatMap(mCacheManager::saveEnterpriseInfo);
        return Observable.concat(memory, disk)
                .firstOrError().toObservable();
    }

    @Override
    public Observable<Optional<EnterpriseInfo>> getOptionalEnterpriseInfo() {
        Observable<Optional<EnterpriseInfo>> memory = mCacheManager.getOptionalEnterpriseInfo()
                .filter(Optional::isPresent);
        Observable<Optional<EnterpriseInfo>> disk = mDbManager.getOptionalEnterpriseInfo()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(mCacheManager::saveEnterpriseInfo)
                .map(Optional::of);
        return Observable.concat(memory, disk).first(Optional.empty()).toObservable();
    }

    @Override
    public Observable<EnterpriseInfo> saveEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        return mCacheManager.saveEnterpriseInfo(enterpriseInfo)
                .flatMap(mDbManager::saveEnterpriseInfo);
    }

    @Override
    public Observable<Store> getStore() {
        Observable<Store> memory = mCacheManager.getStore();
        Observable<Store> disk = mDbManager.getStore()
                .flatMap(mCacheManager::saveStore);
        return Observable.concat(memory, disk).firstOrError().toObservable();
    }

    @Override
    public Observable<Optional<Store>> getOptionalStore() {
        Observable<Optional<Store>> memory = mCacheManager.getOptionalStore()
                .filter(Optional::isPresent);
        Observable<Optional<Store>> disk = mDbManager.getOptionalStore()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .flatMap(mCacheManager::saveStore)
                .map(Optional::of);
        return Observable.concat(memory, disk).first(Optional.empty()).toObservable();
    }

    @Override
    public Observable<Store> saveStore(Store store) {
        return mCacheManager.saveStore(store)
                .flatMap(mDbManager::saveStore);
    }

    @Override
    public Observable<Users> getUsers() {
        Observable<Users> memory = mCacheManager.getUsers();
        Observable<Users> disk = mDbManager.getUsers()
                .flatMap(mCacheManager::saveUsers);
        return Observable.concat(memory, disk).firstOrError().toObservable();
    }

    @Override
    public Observable<Users> saveUsers(Users users) {
        return mCacheManager.saveUsers(users)
                .flatMap(mDbManager::saveUsers);
    }

    @Override
    public Observable<AccountRecord> getAccountRecord() {
        Observable<AccountRecord> memory = mCacheManager.getAccountRecord();
        Observable<AccountRecord> disk = mDbManager.getAccountRecord()
                .flatMap(mCacheManager::saveAccountRecord);
        return Observable.concat(memory, disk).firstOrError().toObservable();
    }

    @Override
    public Observable<AccountRecord> saveAccountRecord(AccountRecord accountRecord) {
        return mCacheManager.saveAccountRecord(accountRecord)
                .flatMap(mDbManager::saveAccountRecord);
    }

    @Override
    public Observable<List<AdditionalFees>> getAllAdditionalFee() {
        Observable<List<AdditionalFees>> memory = mCacheManager.getAllAdditionalFee();
        Observable<List<AdditionalFees>> disk = mDbManager.getAllAdditionalFee()
                .flatMap(mCacheManager::saveAllAdditionalFee);
        return Observable.concat(memory, disk).firstOrError().toObservable();
    }

    @Override
    public Observable<List<AdditionalFees>> saveAllAdditionalFee(List<AdditionalFees> allAdditionalFee) {
        return mCacheManager.saveAllAdditionalFee(allAdditionalFee)
                .flatMap(mDbManager::saveAllAdditionalFee);
    }

    @Override
    public Observable<List<PaymentItem>> getAllPaymentItem() {
        Observable<List<PaymentItem>> memory = mCacheManager.getAllPaymentItem();
        Observable<List<PaymentItem>> disk = mDbManager.getAllPaymentItem()
                .flatMap(mCacheManager::saveAllPaymentItem);
        return Observable.concat(memory, disk).firstOrError().toObservable();
    }

    @Override
    public Observable<List<PaymentItem>> saveAllPaymentItem(List<PaymentItem> allPaymentItem) {
        return mCacheManager.saveAllPaymentItem(allPaymentItem)
                .flatMap(mDbManager::saveAllPaymentItem);
    }

    @Override
    public Observable<Boolean> insertImageBeanToDb(ImageBean imageBean) {
        return mDbManager.insertImageBeanToDb(imageBean);
    }

    @Override
    public Observable<Boolean> updateImageBeanToDb(ImageBean imageBean) {
        return mDbManager.updateImageBeanToDb(imageBean);
    }

    @Override
    public Observable<List<String>> queryAvailableLocalPath(ImageBean.Type type) {
        return mDbManager.queryAvailableLocalPath(type);
    }

    @Override
    public Observable<String> writeFileToDisk(String remoteUrl, ResponseBody responseBody, ImageBean.Type type) {
        return mFileManager.writeFileToDisk(remoteUrl, responseBody, type);
    }

    @Override
    public Observable<ResponseBody> downloadPicture(String url) {
        return mNetworkManager.downloadPicture(url);
    }

    @Override
    public Observable<PushPrintResponse> getPrintData(String key) {
        return mNetworkManager.getPrintData(key);
    }

    @Override
    public Observable<PushPrintResponse> pushRegister(String enterpriseInfoGUID, String storeGuid) {
        return mNetworkManager.pushRegister(enterpriseInfoGUID, storeGuid);
    }

    @Override
    public Observable<PushPrintResponse> pushFeedback(List<PushFeedback> list) {
        return mNetworkManager.pushFeedback(list);
    }

    @Override
    public Observable<String> checkRemoteUrlInDbExist(String remoteUrl) {
        return mDbManager.checkRemoteUrlInDbExist(remoteUrl);
    }

    @Override
    public Observable<String> saveBannerImageToAlbum(String path) {
        return mFileManager.saveBannerImageToAlbum(path);
    }

    @Override
    public Observable<Boolean> checkLocalFileInDiskExist(String localPath) {
        return mFileManager.checkLocalFileInDiskExist(localPath);
    }

    @Override
    public Observable<Boolean> releaseForReLogin() {
        return mCacheManager.releaseForReLogin()
                .flatMap(aBoolean -> mPrefsManager.releaseForReLogin())
                .flatMap(aBoolean -> mDbManager.releaseForReLogin())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Boolean> releaseForAppExit() {
        return mCacheManager.releaseForAppExit()
                .flatMap(aBoolean -> mPrefsManager.releaseForAppExit())
                .flatMap(aBoolean -> mDbManager.releaseForAppExit())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
