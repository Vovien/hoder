package com.holderzone.intelligencepos.mvp.model.prefs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;

import java.util.List;

import io.reactivex.Observable;

/**
 * SharedPreference模块
 * Created by tcw on 2017/7/21.
 */

public class PrefsManagerImpl implements PrefsManager {
    private static final String PREF_FILE_NAME = "prefs";
    private static final String PREF_KEY_DISHES_TYPE = "dishes_type";
    private static final String PREF_KEY_DISHES = "dishes";
    private static final String PREF_KEY_BTN_STYLE = "btn_style";
    private static final String PREF_KEY_IS_START = "is_start";
    private static final String PREF_KEY_PARAMETERS_CONFIG = "parametersConfig";

    private static final String PREF_KEY_IS_DESIGNATED = "is_designated_dishes";

    private volatile static PrefsManagerImpl sInstance;

    private final SharedPreferences mSharedPreferences;

    private final Gson mGson;

    public static PrefsManagerImpl getInstance() {
        if (sInstance == null) {
            synchronized (PrefsManagerImpl.class) {
                if (sInstance == null) {
                    sInstance = new PrefsManagerImpl();
                }
            }
        }
        return sInstance;
    }

    private PrefsManagerImpl() {
        mSharedPreferences = BaseApplication.getContext().getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    @Override
    public Observable<List<DishesTypeE>> getAllDishesTypeE() {
        return Observable.create(e -> {
            String dishesType = mSharedPreferences.getString(PREF_KEY_DISHES_TYPE, null);
            if (dishesType != null) {
                e.onNext(mGson.fromJson(dishesType, new TypeToken<List<DishesTypeE>>() {
                }.getType()));
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<DishesTypeE>> saveAllDishesTypeE(List<DishesTypeE> allDishesTypeE) {
        return Observable.create(e -> {
            mSharedPreferences.edit().putString(PREF_KEY_DISHES_TYPE, mGson.toJson(allDishesTypeE)).apply();
            e.onNext(allDishesTypeE);
            e.onComplete();
        });
    }

    @Override
    public Observable<List<DishesE>> getAllDishesE() {
        return Observable.create(e -> {
            String dishesString = mSharedPreferences.getString(PREF_KEY_DISHES, null);
            if (dishesString != null) {
                List<DishesE> temp = mGson.fromJson(dishesString, new TypeToken<List<DishesE>>() {
                }.getType());
                e.onNext(temp);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<List<DishesE>> saveAllDishesE(List<DishesE> allDishesE) {
        return Observable.create(e -> {
            mSharedPreferences.edit().putString(PREF_KEY_DISHES, mGson.toJson(allDishesE)).apply();
            e.onNext(allDishesE);
            e.onComplete();
        });
    }

    @Override
    public Observable<Integer> getBtnStyle() {
        return Observable.create(e -> {
            int btnStyle = mSharedPreferences.getInt(PREF_KEY_BTN_STYLE, -1);
            if (btnStyle != -1) {
                e.onNext(btnStyle);
            }
            e.onComplete();
        });
    }

    @Override
    public Observable<Integer> saveBtnStyle(Integer btnStyle) {
        return Observable.create(e -> {
            mSharedPreferences.edit().putInt(PREF_KEY_BTN_STYLE, btnStyle).apply();
            e.onNext(btnStyle);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> saveIsStartFlapper(boolean isStart) {
        return Observable.create(e -> {
            mSharedPreferences.edit().putBoolean(PREF_KEY_IS_START, isStart).apply();
            e.onNext(isStart);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> getIsStartFlapper() {
        return Observable.create(e -> {
            boolean isStart = mSharedPreferences.getBoolean(PREF_KEY_IS_START, false);
            e.onNext(isStart);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> releaseForReLogin() {
        return Observable.create(e -> {
            mSharedPreferences.edit().remove(PREF_KEY_DISHES_TYPE).apply();
            mSharedPreferences.edit().remove(PREF_KEY_DISHES).apply();
            e.onNext(true);
            e.onComplete();
        });
    }

    @Override
    public Observable<Boolean> releaseForAppExit() {
        return Observable.create(e -> {
            mSharedPreferences.edit().remove(PREF_KEY_IS_DESIGNATED).apply();
            mSharedPreferences.edit().remove(PREF_KEY_DISHES_TYPE).apply();
            mSharedPreferences.edit().remove(PREF_KEY_DISHES).apply();
            e.onNext(true);
            e.onComplete();
        });
    }

    /**
     * 保存是否开启划菜功能
     *
     * @param isDesignatedDishes
     * @return
     */
    @Override
    public Observable<Boolean> saveIsDesignatedDishes(boolean isDesignatedDishes) {
        return Observable.create(e -> {
            mSharedPreferences.edit().putBoolean(PREF_KEY_IS_DESIGNATED, isDesignatedDishes).apply();
            e.onNext(isDesignatedDishes);
            e.onComplete();
        });
    }

    /**
     * 返回 是否有开启划菜功能
     *
     * @return
     */
    @Override
    public Observable<Boolean> getIsDesignatedDishes() {
        return Observable.create(e -> {
            boolean isDesignatedDishes = mSharedPreferences.getBoolean(PREF_KEY_IS_DESIGNATED, false);
            e.onNext(isDesignatedDishes);
            e.onComplete();
        });
    }

    @Override
    public Observable<ParametersConfig> saveSystemConfig(ParametersConfig parametersConfig) {
        return Observable.create(e -> {
            mSharedPreferences.edit().putString(PREF_KEY_PARAMETERS_CONFIG, mGson.toJson(parametersConfig)).apply();
            e.onNext(parametersConfig);
            e.onComplete();
        });
    }

    @Override
    public Observable<ParametersConfig> getSystemConfig() {
        return Observable.create(e -> {
            String dishesString = mSharedPreferences.getString(PREF_KEY_PARAMETERS_CONFIG, null);
            if (dishesString != null) {
                ParametersConfig temp = mGson.fromJson(dishesString, ParametersConfig.class);
                e.onNext(temp);
            }
            e.onComplete();
        });
    }
}
