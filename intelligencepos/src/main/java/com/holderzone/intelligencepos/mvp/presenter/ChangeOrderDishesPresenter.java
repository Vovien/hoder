package com.holderzone.intelligencepos.mvp.presenter;

import android.util.Pair;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BasePresenter;
import com.holderzone.intelligencepos.mvp.contract.ChangeOrderDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesBaseData;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeBindE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.RequestMethod;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.XmlData;
import com.holderzone.intelligencepos.mvp.model.network.errorhandling.BaseObserver;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CollectionUtils;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by LT on 2018-04-04.
 */

public class ChangeOrderDishesPresenter extends BasePresenter<ChangeOrderDishesContract.View> implements ChangeOrderDishesContract.Presenter {
    public ChangeOrderDishesPresenter(ChangeOrderDishesContract.View view) {
        super(view);
    }

    @Override
    public void getDishesEstimate() {
        mRepository.getAccountRecord()
                .flatMap(accountRecord -> {
                    DishesEstimateRecordE dishesEstimateRecordE = new DishesEstimateRecordE();
                    dishesEstimateRecordE.setBusinessDay(accountRecord.getBusinessDay());
                    return XmlData.Builder()
                            .setRequestMethod(RequestMethod.DishesEstimateRecordB.GetCurrentInfo)
                            .setRequestBody(dishesEstimateRecordE).buildRESTful()
                            .flatMap(mRepository::getXmlData);
                })
                .compose(RxTransformer.applyTransformer(mView))// 绑定订阅到activity生命周期;
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        Map<String, DishesEstimateRecordDishes> estumateMap = new HashMap<String, DishesEstimateRecordDishes>();
                        Map<String, Double> map = new HashMap<String, Double>();
                        if (xmlData.getDishesEstimateRecordE() != null && xmlData.getDishesEstimateRecordE().getArrayOfDishesEstimateRecordDishes() != null
                                && xmlData.getDishesEstimateRecordE().getArrayOfDishesEstimateRecordDishes().size() > 0) {
                            if (xmlData.getArrayOfSalesOrderDishesE() != null) {
                                for (SalesOrderDishesE salesOrderDishesE : xmlData.getArrayOfSalesOrderDishesE()) {
                                    map.put(salesOrderDishesE.getDishesGUID().toLowerCase(), salesOrderDishesE.getCheckCount());
                                }
                            }
                            for (DishesEstimateRecordDishes dishesEstimateRecordDishes : xmlData.getDishesEstimateRecordE().getArrayOfDishesEstimateRecordDishes()) {
                                Double CheckCount = map.get(dishesEstimateRecordDishes.getDishesGUID().toLowerCase());
                                dishesEstimateRecordDishes.setSaleCount(CheckCount == null ? 0d : CheckCount);
                                dishesEstimateRecordDishes.setResidueCount(ArithUtil.sub(dishesEstimateRecordDishes.getEstimateCount(), dishesEstimateRecordDishes.getSaleCount()));
                                dishesEstimateRecordDishes.setCurrentOrderCount(0d);
                                estumateMap.put(dishesEstimateRecordDishes.getDishesGUID().toLowerCase(), dishesEstimateRecordDishes);
                            }
                        }
                        mView.onGetDishesEstimateSuccess(estumateMap);
                    }
                });
    }

    @Override
    public void getDishesType() {
        Observable.zip(mRepository.getAllDishesTypeE(), mRepository.getAllDishesE(), Pair::new)
                .map(pair -> {
                    List<DishesTypeE> dishesTypeEList = new ArrayList<>(pair.first);
                    List<DishesE> dishesEList = new ArrayList<>(pair.second);
                    Map<String, DishesTypeE> map = new HashMap<>();
                    for (DishesTypeE dishesTypeE : dishesTypeEList) {
                        dishesTypeE.setArrayOfDishesE(new ArrayList<>());
                        map.put(dishesTypeE.getDishesTypeGUID(), dishesTypeE);
                    }
                    //做法
                    HashMap<String, List<DishesPracticeE>> practiceMap = new HashMap<>();
                    for (DishesE dishesE : dishesEList) {
                        List<DishesPracticeBindE> practiceBindEList = dishesE.getArrayOfDishesPracticeBindE();
                        if (!CollectionUtils.isEmpty(practiceBindEList)) {
                            List<DishesPracticeE> list = new ArrayList();
                            for (DishesPracticeBindE dishesPracticeBindE : practiceBindEList) {
                                dishesPracticeBindE.getDishesPracticeE().setFees(dishesPracticeBindE.getFees());
                                list.add(dishesPracticeBindE.getDishesPracticeE());
                            }
                            practiceMap.put(dishesE.getDishesGUID(), list);
                        }
                        map.get(dishesE.getDishesTypeGUID()).getArrayOfDishesE().add(dishesE);
                    }
                    Iterator<DishesTypeE> iterator = dishesTypeEList.iterator();
                    while (iterator.hasNext()) {
                        DishesTypeE dishesTypeE = iterator.next();
                        if (dishesTypeE.getArrayOfDishesE().size() == 0) {
                            iterator.remove();
                        }
                    }
                    DishesBaseData dishesBaseData = new DishesBaseData();
                    dishesBaseData.setDishesPracticeMap(practiceMap);
                    dishesBaseData.setDishesTypeEList(dishesTypeEList);
                    return dishesBaseData;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindUntilEventDestroy(mView))// 绑定订阅到activity生命周期;
                .subscribe(dishesBaseData -> {
                    mView.getDishesData(dishesBaseData.getDishesTypeEList(), dishesBaseData.getDishesPracticeMap());
                });
    }

    @Override
    public void updateDishesStopStatus(List<DishesTypeE> dishesList, Map<String, DishesEstimateRecordDishes> map) {
        Observable.create((ObservableOnSubscribe<List<DishesTypeE>>) e -> {
            for (DishesTypeE dishesType : dishesList) {
                for (DishesE dishes : dishesType.getArrayOfDishesE()) {
                    dishes.setStopSale(false);
                    if (map.containsKey(dishes.getDishesGUID().toLowerCase())) {
                        DishesEstimateRecordDishes estimate = map.get(dishes.getDishesGUID().toLowerCase());
                        if (estimate.getSalesStatus() == 0 || ArithUtil.sub(estimate.getEstimateCount(), estimate.getSaleCount()) <= 0) {
                            dishes.setStopSale(true);
                        }
                    }
                }
            }
            e.onNext(dishesList);
            e.onComplete();
        })
                .compose(RxTransformer.bindUntilEventDestroy(mView))// 绑定订阅到activity生命周期;
                .subscribe(dishesTypeEs -> mView.refreshDishesStatus(dishesTypeEs));
    }

    @Override
    public void changeDishes(String orderGuid, String originalDishesGuid, String confirmDishesGuid, Integer orderType, List<UnOrderDishesPracticeE> practiceList) {
        UnOrderDishesE order = new UnOrderDishesE();
        order.setOrderType(orderType);
        order.setUnOrderGUID(orderGuid);
        order.setArrayOfUnOrderDishesPracticeE(practiceList);
        order.setConfirmDishesGUID(confirmDishesGuid);
        order.setUnOrderDishesGUID(originalDishesGuid);
        XmlData.Builder().setRequestMethod(RequestMethod.UnOrderDishesB.DishesChange)
                .setRequestBody(order).buildRESTful()
                .flatMap(mRepository::getXmlData)
                .compose(RxTransformer.applyTransformer(mView))
                .subscribe(new BaseObserver<XmlData>(mView) {
                    @Override
                    protected void next(XmlData xmlData) {
                        mView.onChangeDishesSuccess();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        mView.onChangeDishesFiled();
                    }
                });

    }
}
