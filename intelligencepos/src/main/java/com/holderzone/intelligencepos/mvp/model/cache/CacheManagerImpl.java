package com.holderzone.intelligencepos.mvp.model.cache;

import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;
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
 * 缓存模块
 * Created by tcw on 2017/7/21.
 */

public class CacheManagerImpl implements CacheManager {

    private volatile static CacheManagerImpl sInstance;

    // 当前企业实体
    private EnterpriseInfo mEnterpriseInfo;

    // 当前门店实体
    private Store mStore;

    // 当前登陆用户实体
    private Users mUsers;

    // 所有菜品类型实体
    private List<DishesTypeE> mAllDishesTypeE;

    // 所有菜品实体
    private List<DishesE> mAllDishesE;

    // 当前营业日实体
    private AccountRecord mAccountRecord;

    // 按钮风格
    private Integer mBtnStyle;

    // 是否启用号牌
    private boolean mIsStart;

    // 是否启用划菜功能
    private boolean mIsDesignatedDishes;

    private ParametersConfig mParametersConfig;


    // 所有附加费
    private List<AdditionalFees> mAllAdditionFees;

    // 所有支付方式
    private List<PaymentItem> mAllPaymentItem;

    public static CacheManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (CacheManagerImpl.class) {
                if (sInstance == null) {
                    sInstance = new CacheManagerImpl();
                }
            }
        }
        return sInstance;
    }

    private CacheManagerImpl() {
    }

    @Override
    public Observable<List<DishesTypeE>> getAllDishesTypeE() {
        return Observable.create(e -> {
            if (mAllDishesTypeE != null) {
                e.onNext(mAllDishesTypeE);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<DishesTypeE>> saveAllDishesTypeE(List<DishesTypeE> allDishesTypeE) {
        return Observable.create(e -> {
            mAllDishesTypeE = allDishesTypeE;
            e.onNext(allDishesTypeE);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<DishesE>> getAllDishesE() {
        return Observable.create(e -> {
            if (mAllDishesE != null) {
                e.onNext(mAllDishesE);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<DishesE>> saveAllDishesE(List<DishesE> allDishesE) {
        return Observable.create(e -> {
            mAllDishesE = allDishesE;
            e.onNext(allDishesE);
            e.onComplete();
        });
    }

    @Override
    public Observable<Integer> getBtnStyle() {
        return Observable.create(e -> {
            if (mBtnStyle != null) {
                e.onNext(mBtnStyle);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Integer> saveBtnStyle(Integer btnStyle) {
        return Observable.create(e -> {
            mBtnStyle = btnStyle;
            e.onNext(btnStyle);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> saveIsStartFlapper(boolean isStart) {
        return Observable.create(e -> {
            mIsStart = isStart;
            e.onNext(isStart);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> getIsStartFlapper() {
        return Observable.create(e -> {
            e.onNext(mIsStart);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> saveIsDesignatedDishes(boolean isDesignatedDishes) {
        return Observable.create(e -> {
            mIsDesignatedDishes = isDesignatedDishes;
            e.onNext(isDesignatedDishes);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> getIsDesignatedDishes() {
        return Observable.create(e -> {
            e.onNext(mIsDesignatedDishes);
            e.onComplete();
        });
    }

    @Override
    public Observable<ParametersConfig> saveSystemConfig(ParametersConfig parametersConfig) {
        return Observable.create(e -> {
            mParametersConfig = parametersConfig;
            e.onNext(parametersConfig);
            e.onComplete();
        });
    }

    @Override
    public Observable<ParametersConfig> getSystemConfig() {
        return Observable.create(e -> {
            if (mParametersConfig != null) {
                e.onNext(mParametersConfig);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<EnterpriseInfo> getEnterpriseInfo() {
        return Observable.create(e -> {
            if (mEnterpriseInfo != null) {
                e.onNext(mEnterpriseInfo);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Optional<EnterpriseInfo>> getOptionalEnterpriseInfo() {
        return Observable.create(e -> {
            e.onNext(Optional.of(mEnterpriseInfo));
            e.onComplete();
        });
    }

    @Override
    public Observable<EnterpriseInfo> saveEnterpriseInfo(EnterpriseInfo enterpriseInfo) {
        return Observable.create(e -> {
            mEnterpriseInfo = enterpriseInfo;
            e.onNext(enterpriseInfo);
            e.onComplete();
        });
    }

    @Override
    public Observable<Store> getStore() {
        return Observable.create(e -> {
            if (mStore != null) {
                e.onNext(mStore);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Optional<Store>> getOptionalStore() {
        return Observable.create(e -> {
            e.onNext(Optional.of(mStore));
            e.onComplete();
        });
    }

    @Override
    public Observable<Store> saveStore(Store store) {
        return Observable.create(e -> {
            mStore = store;
            e.onNext(store);
            e.onComplete();
        });
    }

    @Override
    public Observable<Users> getUsers() {
        return Observable.create(e -> {
            if (mUsers != null) {
                e.onNext(mUsers);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Users> saveUsers(Users users) {
        return Observable.create(e -> {
            mUsers = users;
            e.onNext(users);
            e.onComplete();
        });
    }

    @Override
    public Observable<AccountRecord> getAccountRecord() {
        return Observable.create(e -> {
            if (mAccountRecord != null) {
                e.onNext(mAccountRecord);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<AccountRecord> saveAccountRecord(AccountRecord accountRecord) {
        return Observable.create(e -> {
            mAccountRecord = accountRecord;
            e.onNext(accountRecord);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<AdditionalFees>> getAllAdditionalFee() {
        return Observable.create(e -> {
            if (mAllAdditionFees != null) {
                e.onNext(mAllAdditionFees);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<AdditionalFees>> saveAllAdditionalFee(List<AdditionalFees> allAdditionalFee) {
        return Observable.create(e -> {
            mAllAdditionFees = allAdditionalFee;
            e.onNext(allAdditionalFee);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<PaymentItem>> getAllPaymentItem() {
        return Observable.create(e -> {
            if (mAllPaymentItem != null) {
                e.onNext(mAllPaymentItem);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<PaymentItem>> saveAllPaymentItem(List<PaymentItem> allPaymentItem) {
        return Observable.create(e -> {
            mAllPaymentItem = allPaymentItem;
            e.onNext(allPaymentItem);
            e.onComplete();
        });
    }

    @Override
    public Observable<String> checkRemoteUrlInDbExist(String remoteUrl) {
        return null;
    }

    @Override
    public Observable<Boolean> insertImageBeanToDb(ImageBean imageBean) {
        return null;
    }

    @Override
    public Observable<Boolean> updateImageBeanToDb(ImageBean imageBean) {
        return null;
    }

    @Override
    public Observable<List<String>> queryAvailableLocalPath(ImageBean.Type type) {
        return null;
    }

    @Override
    public Observable<Boolean> releaseForReLogin() {
        return Observable.create(e -> {
            mUsers = null;
            mAllDishesTypeE = null;
            mAllDishesE = null;
            mAccountRecord = null;
            mBtnStyle = null;
            e.onNext(true);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> releaseForAppExit() {
        return Observable.create(e -> {
            mEnterpriseInfo = null;
            mStore = null;
            mUsers = null;
            mAllDishesTypeE = null;
            mAllDishesE = null;
            mAccountRecord = null;
            mBtnStyle = null;
            e.onNext(true);
            e.onComplete();
        });
    }
}
