package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.ChangeDishesAdapter;
import com.holderzone.intelligencepos.adapter.impl.ChangeOrderDishesTypeAdapter;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ChangeDishesInnerDishesPracticeDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.ChangeOrderDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.UnOrderDishesPracticeE;
import com.holderzone.intelligencepos.mvp.presenter.ChangeOrderDishesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CollectionUtils;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by LT on 2018-04-04.
 * 外卖订单换菜页面
 */

public class ChangeDishesActivity extends BaseActivity<ChangeOrderDishesContract.Presenter> implements ChangeOrderDishesContract.View
        , ChangeDishesAdapter.CallBackClick
        , ChangeDishesInnerDishesPracticeDialogFragment.ConfirmListener {
    public static final String EXTRA_IS_TAKEAWAY = "EXTRA_IS_TAKEAWAY";
    public static final String EXTRA_ORDER_GUID = "EXTRA_ORDER_GUID";
    public static final String EXTRA_DISHES_GUID = "EXTRA_DISHES_GUID";
    public static final String EXTRA_UNORDER_TYPE = "EXTRA_UNORDER_TYPE";
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.ll_return)
    LinearLayout mLlReturn;
    @BindView(R.id.ll_menu)
    LinearLayout mLlMenu;
    @BindView(R.id.lv_dishes_type)
    ListView mLvDishesType;
    @BindView(R.id.lv_dishes)
    StickyListHeadersListView mLvDishes;
    @BindView(R.id.iv_order_no_dishes)
    ImageView ivOrderNoDishes;
    @BindView(R.id.tv_order_no_dishes)
    TextView tvOrderNoDishes;
    @BindView(R.id.iv_menu)
    ImageView mIvMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    /**
     * 是否是外卖
     */
    private boolean isTakeaway;
    /**
     * 订单GUID
     */
    private String mOrderGuid;
    /**
     * 菜品GUID
     */
    private String mDishesGuid;

    /**
     * 菜品适配器
     */
    private ChangeDishesAdapter orderDishesAdapter;
    /**
     * 菜品类型适配器
     */
    private ChangeOrderDishesTypeAdapter orderDishesTypeAdapter;
    /**
     * 已选择的菜品
     */
    private OrderDishesGroup orderDishesGroup = new OrderDishesGroup();
    /**
     * 缓存的菜品列表
     */
    private List<DishesE> dishesEList = new ArrayList<>();
    /**
     * 缓存的菜品类型列表
     */
    private List<DishesTypeE> dishesTypeEList = new ArrayList<>();
    /**
     * 对应的菜品做法列表
     */
    private HashMap<String, List<DishesPracticeE>> dishesPracticeMap;
    /**
     * 记录菜品类型列表的位置
     */
    private ArrayMap<String, Integer> mDishesEPositionHashMap = new ArrayMap<>();
    // 当前被点击的菜品类型GUID
    private String mCurrentDishesTypeGuid;

    // 估清信息
    private Map<String, DishesEstimateRecordDishes> mEstimateMap = new HashMap<>();

    // 是否从OrderDeailActivity返回
    private boolean isCallBackFromeActivity = false;

    // startActivity#OrderDeailActivity.class 请求码
    public static final int REQUESTCODE = 100;
    //菜品滚动Listener
    private MyOnDishesScrollListener myOnDishesScrollListener;
    // 记录菜品类型列表的位置
    private ArrayMap<String, Integer> mDishesTypeEPositionHashMap = new ArrayMap<>();
    private DishesE changeDishes;
    private List<UnOrderDishesPracticeE> practiceList = new ArrayList<>();


    public static Intent newIntent(Context context, String orderGuid, String dishesGuid, boolean isTakeaway) {
        Intent intent = new Intent(context, ChangeDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_ORDER_GUID, orderGuid);
        bundle.putString(EXTRA_DISHES_GUID, dishesGuid);
        bundle.putBoolean(EXTRA_IS_TAKEAWAY, isTakeaway);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mOrderGuid = extras.getString(EXTRA_ORDER_GUID);
        mDishesGuid = extras.getString(EXTRA_DISHES_GUID);
        isTakeaway = extras.getBoolean(EXTRA_IS_TAKEAWAY);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_change_dishes;
    }

    @Nullable
    @Override
    protected ChangeOrderDishesContract.Presenter initPresenter() {
        return new ChangeOrderDishesPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //initTitle
        mTvTitle.setText("更换菜品");
        //返回
        RxView.clicks(mLlReturn).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> finishActivity());
        //隐藏刷新按钮
        mIvMenu.setVisibility(View.GONE);
        //刷新
        RxView.clicks(tvMenu).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> mPresenter.getDishesEstimate());

        orderDishesAdapter = new ChangeDishesAdapter(this, isTakeaway);
        orderDishesAdapter.setCallBackClick(this);
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
        mLvDishes.setAdapter(orderDishesAdapter);
        myOnDishesScrollListener = new MyOnDishesScrollListener();
        mLvDishes.setOnScrollListener(myOnDishesScrollListener);
        mLvDishes.setDivider(null);
        orderDishesTypeAdapter = new ChangeOrderDishesTypeAdapter(this, dishesTypeEList);
        mLvDishesType.setAdapter(orderDishesTypeAdapter);
        mLvDishesType.setOnItemClickListener((parent, view, position, id) -> {
            if (dishesTypeEList.get(position).getDishesTypeGUID().equals(mCurrentDishesTypeGuid)) {
                return;
            } else {
                mCurrentDishesTypeGuid = dishesTypeEList.get(position).getDishesTypeGUID();
                orderDishesTypeAdapter.setChickedGUID(mCurrentDishesTypeGuid);

                if (mDishesEPositionHashMap.get(mCurrentDishesTypeGuid) == null) {
                    return;
                }
                mLvDishes.setSelection(mDishesEPositionHashMap.get(mCurrentDishesTypeGuid));
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getDishesEstimate();
        mPresenter.getDishesType();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (isCallBackFromeActivity) {
            mPresenter.getDishesEstimate();
        }
    }

    @Override
    public void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map) {
        mEstimateMap.putAll(map);
        mPresenter.updateDishesStopStatus(dishesTypeEList, map);
        if (isCallBackFromeActivity) {
            isCallBackFromeActivity = false;
            if (orderDishesAdapter != null) {
                orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
            }
        }
    }

    @Override
    public void getDishesData(List<DishesTypeE> dishesTypeList, HashMap<String, List<DishesPracticeE>> dishesPracticeMap) {
        dishesTypeEList.clear();
        if (dishesTypeList != null && dishesTypeList.size() != 0) {
            mLvDishesType.setVisibility(View.VISIBLE);
            dishesTypeEList.addAll(dishesTypeList);
        } else {
            mLvDishes.setVisibility(View.GONE);
            mLvDishesType.setVisibility(View.GONE);
            return;
        }

        dishesEList.clear();
        if (dishesTypeList != null && dishesTypeList.size() != 0) {
            for (int i = 0; i < dishesTypeList.size(); i++) {
                List<DishesE> dishesEs = dishesTypeList.get(i).getArrayOfDishesE();
                if (dishesEs == null || dishesEs.size() == 0) {
                    continue;
                }
                dishesEs.get(0).setDishesTypeName(dishesTypeList.get(i).getName());
                dishesEList.addAll(dishesEList.size(), dishesEs);
            }
        }
        mCurrentDishesTypeGuid = dishesTypeList.size() == 0 ? null : dishesTypeList.get(0).getDishesTypeGUID();

        if (orderDishesAdapter != null) {
            orderDishesTypeAdapter.setDishesTypeEList(dishesTypeEList);
            orderDishesTypeAdapter.notifyDataSetChanged();
        }

        mDishesTypeEPositionHashMap.clear();
        for (int i = 0; i < dishesTypeEList.size(); i++) {
            DishesTypeE dishesType = dishesTypeEList.get(i);
            mDishesTypeEPositionHashMap.put(dishesType.getDishesTypeGUID(), i);
        }
        mDishesEPositionHashMap.clear();
        for (int i = 0; i < dishesEList.size(); i++) {
            DishesE dishesE = dishesEList.get(i);
            String dishesTypeGUID = dishesE.getDishesTypeGUID();
            if (mDishesEPositionHashMap.get(dishesTypeGUID) != null) {
                continue;
            } else {
                mDishesEPositionHashMap.put(dishesE.getDishesTypeGUID(), i);
            }
        }
        if (orderDishesAdapter != null && dishesEList != null && dishesEList.size() != 0) {
            mLvDishes.setVisibility(View.VISIBLE);
            orderDishesAdapter.setDishesEList(dishesEList, dishesTypeEList, mDishesTypeEPositionHashMap);
            orderDishesTypeAdapter.setChickedGUID(dishesEList
                    .get(0)
                    .getDishesTypeGUID());
        } else {
            mLvDishes.setVisibility(View.GONE);
            orderDishesTypeAdapter.setChickedGUID(dishesTypeEList
                    .get(0)
                    .getDishesTypeGUID());
        }
        orderDishesAdapter.setDishesPracticeMap(dishesPracticeMap);
        this.dishesPracticeMap = dishesPracticeMap;
        mPresenter.getDishesEstimate();
    }

    @Override
    public void refreshDishesStatus(List<DishesTypeE> list) {
        Map<String, Boolean> stopSalse = new ArrayMap<>();
        if (list != null && list.size() != 0) {
            for (int i = 1; i < list.size(); i++) {
                List<DishesE> dishesEs = list.get(i).getArrayOfDishesE();
                if (dishesEs == null || dishesEs.size() == 0) {
                    continue;
                }

                for (int j = 0; j < dishesEs.size(); j++) {
                    stopSalse.put(dishesEs.get(j).getDishesGUID(), dishesEs.get(j).isStopSale());
                }
            }
        }

        orderDishesAdapter.setStopSalse(stopSalse, mEstimateMap);
    }

    @Override
    public void onChangeDishesSuccess() {
        //换菜成功
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void onChangeDishesFiled() {
        //换菜失败
    }

    @Override
    public void addDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        changeDishes = new DishesE();
        changeDishes.setSimpleName(newSalesOrderBatchDishesE.getSimpleName());
        changeDishes.setDishesGUID(newSalesOrderBatchDishesE.getDishesGUID());
        //请求换菜
        mPresenter.changeDishes(mOrderGuid, mDishesGuid, changeDishes.getDishesGUID(), isTakeaway ? null : 1
                , CollectionUtils.isEmpty(practiceList) ? null : practiceList);
    }

    @Override
    public void addDishesEPractice(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE, Map<String, Double> dishesOrderCount) {
        getDialogFactory().showChangeDishesInnerChoosePracticeDialog(dishesPracticeEs, salesOrderBatchDishesE, ChangeDishesActivity.this);
    }

    @Override
    public void onChoosePracticeClick(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        changeDishes = new DishesE();
        changeDishes.setSimpleName(salesOrderBatchDishesE.getSimpleName());
        changeDishes.setDishesGUID(salesOrderBatchDishesE.getDishesGUID());
        //做法
        practiceList.clear();
        if (!CollectionUtils.isEmpty(salesOrderBatchDishesE.getArrayOfDishesPracticeE())) {
            double cookMethodPrice = 0.0;
            for (DishesPracticeE dishesPractice : salesOrderBatchDishesE.getArrayOfDishesPracticeE()) {
                cookMethodPrice = ArithUtil.add(cookMethodPrice, ArithUtil.mul(dishesPractice.getFees(), 1));
                UnOrderDishesPracticeE practiceE = new UnOrderDishesPracticeE();
                practiceE.setDishesPracticeGUID(dishesPractice.getDishesPracticeGUID());
                practiceList.add(practiceE);
            }
        }
        //请求换菜
        mPresenter.changeDishes(mOrderGuid, mDishesGuid, changeDishes.getDishesGUID(), isTakeaway ? null : 1, CollectionUtils.isEmpty(practiceList) ? null : practiceList);
    }

    /**
     * 处理滑动 是两个ListView联动
     */
    private class MyOnDishesScrollListener implements AbsListView.OnScrollListener {

        private int scrollState;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            this.scrollState = scrollState;

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE || dishesEList == null || dishesEList.size() == 0) {
                return;
            }
            if (!(dishesEList.get(firstVisibleItem).getDishesTypeGUID().equals(mCurrentDishesTypeGuid))) {
                mCurrentDishesTypeGuid = dishesEList.get(firstVisibleItem).getDishesTypeGUID();
                mLvDishesType.smoothScrollToPosition(mDishesTypeEPositionHashMap.get(mCurrentDishesTypeGuid));
                orderDishesTypeAdapter.setChickedGUID(mCurrentDishesTypeGuid);
            }
        }
    }

    /**
     * 获取菜品数量
     *
     * @return
     */
    private String dishesNumber() {
        if (orderDishesGroup == null || orderDishesGroup.getDishesList() == null || orderDishesGroup.getDishesList().size() == 0) {
            return "0";
        }
        double tempCount = 0;
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
            tempCount = ArithUtil.add(tempCount, salesOrderBatchDishesE.getOrderCount());
        }
        if (tempCount == (int) tempCount) {
            return (int) tempCount + "";
        } else {
            return tempCount + "";
        }
    }
}
