package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.SnackNewOrderLeftAdapter;
import com.holderzone.intelligencepos.adapter.impl.SnackNewOrderRightAdapter;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.dialog.impl.AddPracticeDishesDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ChangeOrderDishesCountDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ReducePracticeDishesDialogFragment;
import com.holderzone.intelligencepos.mvp.activity.PackGroupOrderDishesActivity;
import com.holderzone.intelligencepos.mvp.activity.SnackDishesActivity;
import com.holderzone.intelligencepos.mvp.contract.SnackNewOrderContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroupDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.SnackNewOrderPresenter;
import com.holderzone.intelligencepos.mvp.snack.activity.SnackOrderDetailActivity;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.Clone;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * 快餐 新点单fragment
 * Created by chencao on 2017/8/2.
 */

public class SnackNewOrderFragment extends BaseFragment<SnackNewOrderContract.Presenter> implements SnackNewOrderContract.View,
        StickyListHeadersListView.OnHeaderClickListener,
        StickyListHeadersListView.OnStickyHeaderChangedListener,
        StickyListHeadersListView.OnStickyHeaderOffsetChangedListener,
        SnackNewOrderRightAdapter.CallBackClick,
        AddPracticeDishesDialogFragment.CallBackPracticeOK,
        ReducePracticeDishesDialogFragment.CallBackReduceDialog,
        ChangeOrderDishesCountDialogFragment.ChangeOrderDishesCountListener {

    @BindView(R.id.snack_new_order_left_list)
    ListView snackNewOrderLeftList;
    @BindView(R.id.snack_new_order_right_list)
    StickyListHeadersListView snackNewOrderRightList;
    @BindView(R.id.snack_dishes_number)
    TextView snackDishesNumber;
    @BindView(R.id.snack_dishes_sure)
    Button snackDishesSure;
    @BindView(R.id.snack_dishes_button_layout)
    LinearLayout snackDishesButtonLayout;
    @BindView(R.id.no_dishes_layout)
    RelativeLayout noDishesLayout;
    @BindView(R.id.order_dishes_car)
    LinearLayout mOrderDishesCar;
    @BindView(R.id.order_total_amount)
    TextView mOrderTotalAmount;
    @BindView(R.id.bottom_sheet_layout)
    BottomSheetLayout mBottomSheetLayout;
    private List<DishesTypeE> dishesTypeEList = new ArrayList<>();
    private SnackNewOrderRightAdapter mDishesECommonAdapter;
    private SnackNewOrderLeftAdapter mDishesTypeCommonAdapter;
    private List<DishesE> dishesEList = new ArrayList<>();
    private static final String TYPE_COMMON = "TYPE_COMMON";
    private static final String TYPE_PACKAGE = "TYPE_PACKAGE";
    private static final String SHOW_TYPE_PRACTICE = "SHOW_TYPE_PRACTICE";
    private static final String SHOW_TYPE_COMMON = "SHOW_TYPE_COMMON";
    /**
     * 当前被点击的菜品类型GUID
     **/
    private String mCurrentDishesTypeGuid;

    /**
     * 菜品做法信息
     */
    private Map<String, List<DishesPracticeE>> dishesPracticeMap;
    /**
     * 记录各个菜品中同一类型菜品第一项的位置
     */
    private ArrayMap<String, Integer> mDishesEPositionHashMap = new ArrayMap<>();
    /**
     * 记录菜品类型列表的位置
     */
    private ArrayMap<String, Integer> mDishesTypeEPositionHashMap = new ArrayMap<>();

    /**
     * 估清信息
     */
    private Map<String, DishesEstimateRecordDishes> mEstimateMap = new HashMap<>();
    private static final String ORDERDISHESGROUP = "OrderDishesGroup";

    /**
     * 购物车View
     */
    private View mCarBottomSheetView;
    /**
     * 购物车Adapter
     */
    private CommonAdapter<SalesOrderBatchDishesE> mCarAdapter;
    /**
     * 购物车数据
     */
    private List<SalesOrderBatchDishesE> mCarDateList = new ArrayList<>();
    /**
     * 购物车列表RV
     */
    private RecyclerView mCarRv;
    /**
     * 做法菜品弹出框
     */
    private AddPracticeDishesDialogFragment mAddPracticeDishesDialogFragment;

    private MyOnDishesScrollListener myOnDishesScrollListener;
    public OrderDishesGroup orderDishesGroup = new OrderDishesGroup();
    private boolean isReflash = false;

    public static SnackNewOrderFragment getInstanse(OrderDishesGroup orderDishesGroup) {
        SnackNewOrderFragment fragment = new SnackNewOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ORDERDISHESGROUP, orderDishesGroup);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        orderDishesGroup = extras.getParcelable(ORDERDISHESGROUP);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_snack_new_order;
    }

    @Override
    protected SnackNewOrderContract.Presenter initPresenter() {
        return new SnackNewOrderPresenter(this);
    }

    @android.annotation.SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        snackDishesSure.setEnabled(false);
        RxView.clicks(snackDishesSure).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            if (orderDishesGroup != null && orderDishesGroup.getOrderDishesRecordList() != null) {
                for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getOrderDishesRecordList()) {
                    if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
                        for (SalesOrderBatchDishesE sub : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                            sub.setOrderCount(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), sub.getPackageDishesUnitCount()));
                        }
                    }
                }
            }
            SnackDishesActivity activity = (SnackDishesActivity) getActivity();
            activity.startActivityForResult(SnackOrderDetailActivity.newIntent(activity, orderDishesGroup, mEstimateMap, activity.getUseMemberPrice()
                    , activity.getMemberGuid()), SnackDishesActivity.REQESTCOdE);
        });
        snackNewOrderLeftList.setVisibility(View.VISIBLE);
        noDishesLayout.setVisibility(View.GONE);
        snackNewOrderRightList.setVisibility(View.VISIBLE);
        snackDishesNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String temp = s.toString();
                if (temp == null || temp.length() == 0) {
                    snackDishesSure.setEnabled(false);
                } else if (Double.valueOf(temp) == 0) {
                    snackDishesSure.setEnabled(false);
                } else {
                    snackDishesSure.setEnabled(true);
                }
                //动态改变字体大小
                if (temp.length() > 0 && temp.length() <= 4) {
                    snackDishesNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);
                } else if (temp.length() > 4) {
                    snackDishesNumber.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mDishesECommonAdapter = new SnackNewOrderRightAdapter(getContext());
        mDishesECommonAdapter.setCallBackClick(this);
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
        snackNewOrderRightList.setAdapter(mDishesECommonAdapter);
        myOnDishesScrollListener = new MyOnDishesScrollListener();
        snackNewOrderRightList.setOnScrollListener(myOnDishesScrollListener);
        snackNewOrderRightList.setDivider(null);
        mDishesTypeCommonAdapter = new SnackNewOrderLeftAdapter(getContext(), dishesTypeEList);
        snackNewOrderLeftList.setAdapter(mDishesTypeCommonAdapter);
        snackNewOrderLeftList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dishesTypeEList.get(position).getDishesTypeGUID().equals(mCurrentDishesTypeGuid)) {
                    return;
                } else {
                    mCurrentDishesTypeGuid = dishesTypeEList.get(position).getDishesTypeGUID();
                    mDishesTypeCommonAdapter.setChickedGUID(mCurrentDishesTypeGuid);
                    if (mDishesEPositionHashMap.get(mCurrentDishesTypeGuid) == null) {
                        return;
                    }
                    snackNewOrderRightList.setSelection(mDishesEPositionHashMap.get(mCurrentDishesTypeGuid));
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        setDishesSureAndDishesNumber();
        initAmount();
    }


    private String dishesNumber() {
        if (orderDishesGroup == null || orderDishesGroup.getOrderDishesRecordList() == null || orderDishesGroup.getOrderDishesRecordList().size() == 0) {
            return "0";
        }
        double tempCount = 0;
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getOrderDishesRecordList()) {
//            tempCount = ArithUtil.add(tempCount, salesOrderBatchDishesE.getOrderCount());
            tempCount++;
        }
        if (tempCount == (int) tempCount) {
            return (int) tempCount + "";
        } else {
            return tempCount + "";
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        snackDishesSure.setEnabled(false);
        snackDishesNumber.setText(0 + "");
        //关闭购物车
        if (mCarBottomSheetView != null) {
            if (mBottomSheetLayout.isSheetShowing()) {
                mBottomSheetLayout.dismissSheet();
            }
            mCarBottomSheetView = null;
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mDialogFactory.showProgressDialog(mBaseActivity);
        mPresenter.getDishesType();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    /**
     * 刷新估清和停售信息
     */
    public void setReFresh() {
        if (mPresenter != null) {
            mPresenter.getDishesEstimate(true);
        }
    }

    @Override
    public void onHeaderClick(StickyListHeadersListView l, View header, int itemPosition, long headerId, boolean currentlySticky) {
    }

    @Override
    public void onStickyHeaderChanged(StickyListHeadersListView l, View header, int itemPosition, long headerId) {
    }

    @Override
    public void onStickyHeaderOffsetChanged(StickyListHeadersListView l, View header, int offset) {
    }

    @Override
    public void refreshDishesData(List<DishesTypeE> dishesTypeList) {
        dishesTypeEList.clear();
        if (dishesTypeList != null && dishesTypeList.size() != 0) {
            snackNewOrderLeftList.setVisibility(View.VISIBLE);
            dishesTypeEList.addAll(dishesTypeList.subList(0, dishesTypeList.size()));
        } else {
            noDishesLayout.setVisibility(View.VISIBLE);
            snackNewOrderRightList.setVisibility(View.GONE);
            snackNewOrderLeftList.setVisibility(View.GONE);
            mDialogFactory.dismissProgressDialog();
            return;
        }
        dishesEList.clear();
        if (dishesTypeList.size() != 0) {
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
        if (mDishesECommonAdapter != null) {
            mDishesTypeCommonAdapter.setDishesTypeEList(dishesTypeEList);
            mDishesTypeCommonAdapter.notifyDataSetChanged();
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
        if (mDishesECommonAdapter != null && dishesEList != null && dishesEList.size() != 0) {
            noDishesLayout.setVisibility(View.GONE);
            snackNewOrderRightList.setVisibility(View.VISIBLE);
            mDishesECommonAdapter.setDishesEList(dishesEList, dishesTypeEList, mDishesTypeEPositionHashMap);
            mDishesTypeCommonAdapter.setChickedGUID(dishesEList
                    .get(0)
                    .getDishesTypeGUID());
        } else {
            noDishesLayout.setVisibility(View.VISIBLE);
            snackNewOrderRightList.setVisibility(View.GONE);
            mDishesTypeCommonAdapter.setChickedGUID(dishesTypeEList
                    .get(0)
                    .getDishesTypeGUID());
        }
        mPresenter.getDishesEstimate(false);
    }

    @Override
    public void setDishesPracticeData(Map<String, List<DishesPracticeE>> dishesPracticeList) {
        dishesPracticeMap = dishesPracticeList;
        mDishesECommonAdapter.setDishesPracticeMap(dishesPracticeMap);
    }

    public void resetUseMemberPrice(boolean useMemberPrice) {
        if (mDishesECommonAdapter != null) {
            mDishesECommonAdapter.resetUseMemberPrice(useMemberPrice);
        }
        //刷新价格
        initAmount();
    }

    /**
     * 获取估清信息
     *
     * @param map
     */
    @Override
    public void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map) {
        mEstimateMap.clear();
        mEstimateMap.putAll(map);
        if (isReflash) {
            isReflash = false;
            setDishesSureAndDishesNumber();
            initAmount();
            if (mDishesECommonAdapter != null) {
                mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
            }
        }
        //根据估清信息设置菜品停售信息
        mPresenter.updateDishesStopStatus(dishesTypeEList, map);
    }

    /**
     * 设置点菜数量和确认按钮状态
     */
    private void setDishesSureAndDishesNumber() {
        String temp = dishesNumber();
        if ("0".equals(temp)) {
            snackDishesSure.setEnabled(true);
            snackDishesNumber.setText(temp);
        } else {
            snackDishesSure.setEnabled(false);
            snackDishesNumber.setText(temp);
        }
    }

    /**
     * 停售信息
     *
     * @param list
     */
    @Override
    public void refreshDishesStatus(List<DishesTypeE> list) {
        mDialogFactory.dismissProgressDialog();
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
        mDishesECommonAdapter.setStopSalse(stopSalse, mEstimateMap);
    }

    @Override
    public void showNetworkError() {
        mDialogFactory.dismissProgressDialog();
        if (isReflash) {
            isReflash = false;
            setDishesSureAndDishesNumber();
            initAmount();
            if (mDishesECommonAdapter != null) {
                mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
            }
        }
    }


    /**
     * 用于接收activity 传入的订单列表
     *
     * @param orderDishesGroup
     */
    public void setOrderDishesGroup(OrderDishesGroup orderDishesGroup) {
        this.orderDishesGroup = orderDishesGroup;
        isReflash = true;
        setReFresh();
    }

    /**
     * 用于接收新数据
     */
    public void setNewDate(String dishesGUID, double count, double rise, List<PackageGroupDishesE> GroupList) {
        //设置订单数据
        if (orderDishesGroup == null) orderDishesGroup = new OrderDishesGroup();
        SalesOrderBatchDishesE batchDishesE = new SalesOrderBatchDishesE();
        DishesE dishesE = null;
        for (DishesE dishes : dishesEList) {
            if (dishesGUID.equalsIgnoreCase(dishes.getDishesGUID())) {
                dishesE = dishes;
            }
        }
        batchDishesE.setSalesOrderBatchGUID(UUID.randomUUID().toString());
        batchDishesE.setDishesGUID(dishesGUID);
        batchDishesE.setDishesE(dishesE);
        batchDishesE.setCheckStatus(1);
        batchDishesE.setKitchenPrintStatus(1);
        batchDishesE.setDishesUnit(dishesE.getCheckUnit());
        batchDishesE.setGift(0);
        batchDishesE.setIsPackageDishes(1);
        batchDishesE.setPackageType(1);
        batchDishesE.setOrderCount(count);
        batchDishesE.setPrice(ArithUtil.add(dishesE.getCheckPrice(), rise));
        batchDishesE.setMemberPrice(ArithUtil.add(dishesE.getMemberPrice(), rise));
        batchDishesE.setSimpleName(dishesE.getSimpleName());
        batchDishesE.setDishesName(dishesE.getSimpleName());
        //设置估清数据
        DishesEstimateRecordDishes recordDishes = mEstimateMap.get(dishesGUID.toLowerCase());
        batchDishesE.setMaxValue(recordDishes != null ? recordDishes.getResidueCount() : Double.MAX_VALUE);
        //套餐子项数据
        List<SalesOrderBatchDishesE> groupList = new ArrayList<>();
        for (PackageGroupDishesE groupDishesE : GroupList) {
            SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
            salesOrderBatchDishesE.setDishesGUID(groupDishesE.getDishesGUID());
            salesOrderBatchDishesE.setIsPackageDishes(2);
            salesOrderBatchDishesE.setCheckUnit(groupDishesE.getCheckUnit());
            salesOrderBatchDishesE.setPackageDishesUnitCount(groupDishesE.getSingleCount());
            salesOrderBatchDishesE.setPackageDishesOrderCount(groupDishesE.getPackageDishesOrderCount());
            salesOrderBatchDishesE.setDishesName(groupDishesE.getSimpleName());
            salesOrderBatchDishesE.setSingleCount(groupDishesE.getSingleCount());
            salesOrderBatchDishesE.setRiseAmount(groupDishesE.getRiseAmount());
            groupList.add(salesOrderBatchDishesE);
        }
        batchDishesE.setArrayOfSalesOrderBatchDishesE(groupList);

        if (orderDishesGroup.getDishesList() == null) {
            ArrayList<SalesOrderBatchDishesE> list = new ArrayList<>();
            list.add(batchDishesE);
            orderDishesGroup.setDishesList(list);
        } else {
            orderDishesGroup.getDishesList().add(batchDishesE);
        }
//        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
//        setDishesSureAndDishesNumber();
//        initAmount();
        isReflash = true;
        setReFresh();
    }

    /**
     * 获得订单列表信息
     *
     * @return
     */
    public OrderDishesGroup getOrderDishesGroup() {
        return orderDishesGroup;
    }

    @Override
    public void reduceDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        boolean isRemove = false;
        if (orderDishesGroup != null && orderDishesGroup.getOrderDishesRecordList() != null && orderDishesGroup.getOrderDishesRecordList().size() != 0) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getOrderDishesRecordList()) {
                if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                    if (salesOrderBatchDishesE.getOrderCount() > 1) {
                        isRemove = false;
                        salesOrderBatchDishesE.setOrderCount(ArithUtil.sub(salesOrderBatchDishesE.getOrderCount(), 1));
                    } else {
                        isRemove = true;
                    }
                }
            }
        }
        if (isRemove) {
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getOrderDishesRecordList().iterator(); iterator.hasNext(); ) {
                if (iterator.next().getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                    iterator.remove();
                }
            }
        }
        setDishesSureAndDishesNumber();
        initAmount();
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
    }

    @Override
    public void addDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        if (orderDishesGroup == null) {
            orderDishesGroup = new OrderDishesGroup();
            orderDishesGroup.setOrderDishesRecordList(new ArrayList<>());
        } else if (orderDishesGroup.getOrderDishesRecordList() == null) {
            orderDishesGroup.setOrderDishesRecordList(new ArrayList<>());
        }
        boolean isContain = false;
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getOrderDishesRecordList()) {
            if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                if (newSalesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || newSalesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                    if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                        salesOrderBatchDishesE.setOrderCount(ArithUtil.add(newSalesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getOrderCount()));
                        isContain = true;
                    } else {
                        continue;
                    }
                } else {
                    isContain = false;
                }
            }
        }
        if (!isContain) {
            orderDishesGroup.getOrderDishesRecordList().add(newSalesOrderBatchDishesE);
        }
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
        setDishesSureAndDishesNumber();
        initAmount();
    }

    /**
     * 克隆 SalesOrderBatchDishesE
     *
     * @param salesOrderBatchDishesE
     * @return
     */
    private SalesOrderBatchDishesE cloneSalesOrderBatchDishesE(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        SalesOrderBatchDishesE newSalesOrderBatchDishesE = new SalesOrderBatchDishesE();
        newSalesOrderBatchDishesE.setDishesE(salesOrderBatchDishesE.getDishesE());
        newSalesOrderBatchDishesE.setCheckStatus(salesOrderBatchDishesE.getCheckStatus());
        newSalesOrderBatchDishesE.setKitchenPrintStatus(salesOrderBatchDishesE.getKitchenPrintStatus());
        newSalesOrderBatchDishesE.setNew(salesOrderBatchDishesE.getNew());
        newSalesOrderBatchDishesE.setDishesUnit(salesOrderBatchDishesE.getDishesUnit());
        newSalesOrderBatchDishesE.setGift(salesOrderBatchDishesE.getGift());
        newSalesOrderBatchDishesE.setIsPackageDishes(salesOrderBatchDishesE.getIsPackageDishes());
        newSalesOrderBatchDishesE.setOrderCount(salesOrderBatchDishesE.getOrderCount());
        newSalesOrderBatchDishesE.setArrayOfDishesPracticeE(salesOrderBatchDishesE.getArrayOfDishesPracticeE());
        if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
            List<SalesOrderBatchDishesE> subList = new ArrayList();
            for (SalesOrderBatchDishesE subDishes : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
//                SalesOrderBatchDishesE subDishes = new SalesOrderBatchDishesE();
//                subDishes.setDishesGUID(packageItemE.getSubDishesGUID());
//                subDishes.setOrderCount(ArithUtil.mul(newSalesOrderBatchDishesE.getOrderCount(), packageItemE.getDishesCount()));
//                subDishes.setGift(newSalesOrderBatchDishesE.getGift());
//                subDishes.setIsPackageDishes(2);
//                subDishes.setPackageDishesUnitCount(packageItemE.getDishesCount());
//                subDishes.setCheckStatus(newSalesOrderBatchDishesE.getCheckStatus());
//                subDishes.setKitchenPrintStatus(newSalesOrderBatchDishesE.getKitchenPrintStatus());
                subList.add(subDishes);
            }
            newSalesOrderBatchDishesE.setPackageDishesUnitCount(Double.parseDouble(subList.size() + ""));
            newSalesOrderBatchDishesE.setArrayOfSalesOrderBatchDishesE(subList);
        }
        newSalesOrderBatchDishesE.setPrice(salesOrderBatchDishesE.getPrice());
        newSalesOrderBatchDishesE.setDishesGUID(salesOrderBatchDishesE.getDishesGUID());
        newSalesOrderBatchDishesE.setDishesName(salesOrderBatchDishesE.getDishesName());
        return newSalesOrderBatchDishesE;
    }

    @Override
    public void addDishesEPractice(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE, Map<String, Double> dishesOrderCount) {
        DishesEstimateRecordDishes temp = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        Double tempCount = dishesOrderCount.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        Double tempEstimate = temp != null ? (temp.getResidueCount()) : Double.MAX_VALUE;
        if (mAddPracticeDishesDialogFragment != null) {
            mAddPracticeDishesDialogFragment = null;
        }
        mAddPracticeDishesDialogFragment = AddPracticeDishesDialogFragment.newInstance(dishesPracticeEs
                , salesOrderBatchDishesE, ArithUtil.sub(tempEstimate, tempCount != null ? tempCount : 0));
        mAddPracticeDishesDialogFragment.setCallBack(this);
        mAddPracticeDishesDialogFragment.show(getActivity().getSupportFragmentManager(), "AddPracticeDishesDialogFragment");
    }

    @Override
    public void reduceDishesEDialog(String dishesGUID) {
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEs = new ArrayList<>();
        if (orderDishesGroup != null && orderDishesGroup.getOrderDishesRecordList() != null) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getOrderDishesRecordList()) {
                if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                    SalesOrderBatchDishesE tempSalesOrder = null;
                    try {
                        tempSalesOrder = (SalesOrderBatchDishesE) Clone.deepClone(salesOrderBatchDishesE);
                    } catch (Exception e) {
                        tempSalesOrder = cloneSalesOrderBatchDishesE(salesOrderBatchDishesE);
                        e.printStackTrace();
                    }
                    salesOrderBatchDishesEs.add(tempSalesOrder);
                }
            }
        }
        DishesEstimateRecordDishes temp = mEstimateMap.get(dishesGUID.toLowerCase());
        Double tempEstimate = temp != null ? (temp.getResidueCount()) : Double.MAX_VALUE;
        ReducePracticeDishesDialogFragment dialogFragment = ReducePracticeDishesDialogFragment.newInstance(salesOrderBatchDishesEs, tempEstimate);
        dialogFragment.setCallBack(this);
        dialogFragment.show(getActivity().getSupportFragmentManager(), "ReducePracticeDishesDialogFragment");
    }

    @Override
    public void onChangeCount(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        //估清数据
        DishesEstimateRecordDishes recordDishesCount = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        //菜品剩余数量
        double tempEstimate = recordDishesCount != null ? recordDishesCount.getResidueCount() : Double.MAX_VALUE;
        //已点菜品数量
        double orderDishesCount = salesOrderBatchDishesE.getOrderCount();

        //修改点菜数量弹出框
        getDialogFactory().showChangeOrderDishesCountDialog(salesOrderBatchDishesE, orderDishesCount, tempEstimate, TYPE_COMMON,
                SHOW_TYPE_COMMON, SnackNewOrderFragment.this);
    }

    @Override
    public void onAddPackageGroup(String dishesGUID, Map<String, Double> dishesOrderCount) {
        //估清数据
        DishesEstimateRecordDishes recordDishesCount = mEstimateMap.get(dishesGUID.toLowerCase());
        Double tempCount = dishesOrderCount.get(dishesGUID.toLowerCase());
        //菜品剩余数量
        double tempEstimate = recordDishesCount != null ? recordDishesCount.getResidueCount() : Double.MAX_VALUE;
        //判断是否还可以点菜
        if (ArithUtil.sub(tempEstimate, tempCount != null ? tempCount : 0) < 1) {
            showMessage(getString(R.string.add_dishes_failed_by_estimate));
            return;
        }
        SnackDishesActivity activity = (SnackDishesActivity) getActivity();
        for (DishesE dishesE : dishesEList) {
            if (dishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                List<PackageGroup> list = dishesE.getArrayOfPackageGroup();
                activity.startActivityForResult(PackGroupOrderDishesActivity.newIntent(dishesE.getSimpleName(), dishesE.getDishesGUID()
                        , activity.getUseMemberPrice() ? dishesE.getMemberPrice() : dishesE.getCheckPrice()
                        , ArithUtil.sub(tempEstimate, tempCount != null ? tempCount : 0), list, getActivity()), SnackDishesActivity.REQUEST_CODE_PACKAGE_GROUP);
            }
        }
    }

    @Override
    public void onCattyDishes(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        if (orderDishesGroup == null) {
            orderDishesGroup = new OrderDishesGroup();
            orderDishesGroup.setDishesList(new ArrayList<>());
        } else if (orderDishesGroup.getDishesList() == null) {
            orderDishesGroup.setDishesList(new ArrayList<>());
        }
        boolean isContain = false;
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
            if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                if (newSalesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || newSalesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                    if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() == null || salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() == 0) {
                        salesOrderBatchDishesE.setOrderCount(newSalesOrderBatchDishesE.getOrderCount());
                        isContain = true;
                    } else {
                        continue;
                    }
                } else {
                    isContain = false;
                }
            }
        }
        if (!isContain) {
            orderDishesGroup.getDishesList().add(newSalesOrderBatchDishesE);
        }
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
        initAmount();
        setDishesSureAndDishesNumber();
        onChangeCount(newSalesOrderBatchDishesE);
    }

    @Override
    public void callBackOK(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        addDishesE(salesOrderBatchDishesE);
    }

    @Override
    public void changePracticeCount(double currentCount, double maxCount) {
        //修改做法数量弹框
        getDialogFactory().showChangeOrderDishesCountDialog(new SalesOrderBatchDishesE(), currentCount, maxCount
                , TYPE_COMMON, SHOW_TYPE_PRACTICE, this);//修改做法数量弹框
        getDialogFactory().showChangeOrderDishesCountDialog(new SalesOrderBatchDishesE(), currentCount, maxCount
                , TYPE_COMMON, SHOW_TYPE_PRACTICE, this);
    }

    @Override
    public void callBackShowMessage(String message) {
        showMessage(message);
    }

    @Override
    public void callBackChange(List<SalesOrderBatchDishesE> newSalesOrderBatchDishesEList) {
        String dishesGUID = newSalesOrderBatchDishesEList.get(0).getDishesGUID();
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = new ArrayList<>();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getOrderDishesRecordList()) {
            if (!salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                salesOrderBatchDishesEList.add(salesOrderBatchDishesE);
            }
        }
        for (Iterator<SalesOrderBatchDishesE> iterator = newSalesOrderBatchDishesEList.iterator(); iterator.hasNext(); ) {
            Double temp = iterator.next().getOrderCount();
            if (temp == null || temp == 0) {
                iterator.remove();
            }
        }
        salesOrderBatchDishesEList.addAll(newSalesOrderBatchDishesEList);
        orderDishesGroup.getOrderDishesRecordList().clear();
        orderDishesGroup.setOrderDishesRecordList(salesOrderBatchDishesEList);
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
        setDishesSureAndDishesNumber();
        initAmount();
    }

    @Override
    public void onDispose() {
    }

    public void onCarOrderDishesChangeClick(String batchGUID, double count) {
        if (count == 0) {
            //移除数据
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                if (iterator.next().getSalesOrderBatchGUID().equalsIgnoreCase(batchGUID)) {
                    iterator.remove();
                }
            }
        } else {
            //修改点菜数量
            for (SalesOrderBatchDishesE batchDishesE : orderDishesGroup.getDishesList()) {
                if (batchGUID.equalsIgnoreCase(batchDishesE.getSalesOrderBatchGUID())) {
                    batchDishesE.setOrderCount(count);
                }
            }
        }
        //判断购物车是否还有数据
        if (orderDishesGroup.getDishesList() == null || orderDishesGroup.getDishesList().size() <= 0) {
            //关闭购物车
            if (mCarBottomSheetView != null) {
                if (mBottomSheetLayout.isSheetShowing()) {
                    mBottomSheetLayout.dismissSheet();
                }
            }
        }
        //矫正数据
        setDishesSureAndDishesNumber();
        initAmount();
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
    }

    public void onCarOrderDishesCountSetClick(SalesOrderBatchDishesE salesOrderBatchDishesE, double currentCount, double maxCount, String type) {
        getDialogFactory().showChangeOrderDishesCountDialog(salesOrderBatchDishesE, currentCount, maxCount
                , type, SHOW_TYPE_COMMON, SnackNewOrderFragment.this);
    }

    public void onCarOrderDishesClearAll() {
        getDialogFactory().showConfirmDialog("清空已选菜品？", "取消", "清空", new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {

            }

            @Override
            public void onPosClick() {
                if (mCarBottomSheetView != null) {
                    if (mBottomSheetLayout.isSheetShowing()) {
                        mBottomSheetLayout.dismissSheet();
                    }
                }
                //清空数据
                orderDishesGroup.getDishesList().clear();
                //矫正数据
                setDishesSureAndDishesNumber();
                initAmount();
                mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
            }
        });
    }

    @Override
    public void onDishesCountChanged(SalesOrderBatchDishesE salesOrderBatchDishesE, double count) {
        //修改点菜数量回掉
        if ((salesOrderBatchDishesE.getArrayOfDishesPracticeE() != null && salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() > 0)
                || (salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1)) {//自选套餐或者有做法
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                SalesOrderBatchDishesE next = iterator.next();
                List<DishesPracticeE> arrayOfDishesPracticeE = next.getArrayOfDishesPracticeE();
                boolean isPackage = next.getIsPackageDishes() == 1 && next.getPackageType() == 1;
                boolean isPractice = arrayOfDishesPracticeE != null && arrayOfDishesPracticeE.size() > 0;
                if (next.getSalesOrderBatchGUID().equalsIgnoreCase(salesOrderBatchDishesE.getSalesOrderBatchGUID())
                        && (isPackage || isPractice)) {
                    if (count <= 0) {
                        iterator.remove();
                    } else {
                        next.setOrderCount(count);
                    }
                }
            }
        } else {//普通菜品
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                SalesOrderBatchDishesE next = iterator.next();
                List<DishesPracticeE> arrayOfDishesPracticeE = next.getArrayOfDishesPracticeE();
                boolean isPackage = next.getIsPackageDishes() == 1 && next.getPackageType() == 1;
                boolean isPractice = arrayOfDishesPracticeE != null && arrayOfDishesPracticeE.size() > 0;
                if ((next.getDishesGUID().equalsIgnoreCase(salesOrderBatchDishesE.getDishesGUID())) &&
                        !isPractice && !isPackage) {
                    if (count == 0) {
                        iterator.remove();
                    } else {
                        next.setOrderCount(count);
                    }
                }
            }
        }
        //判断是否可以关闭购物车
        if (orderDishesGroup.getDishesList() == null || orderDishesGroup.getDishesList().size() <= 0) {
            if (mCarBottomSheetView != null) {
                if (mBottomSheetLayout.isSheetShowing()) {
                    mBottomSheetLayout.dismissSheet();
                }
            }
        }
        //刷新数据
        setDishesSureAndDishesNumber();
        initAmount();
        mDishesECommonAdapter.setOrderDishesGroup(orderDishesGroup);
        if (mCarAdapter != null) {
            mCarAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPracticeCountChanged(double count) {
        //修改做法菜品数量回掉
        //修改做法菜品数量 回掉
        if (mAddPracticeDishesDialogFragment != null) {
            mAddPracticeDishesDialogFragment.setNetPracticeCount(count);
        }
    }

    @OnClick({R.id.snack_dishes_button_layout, R.id.order_dishes_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.snack_dishes_button_layout:
            case R.id.order_dishes_car:
                showCarView();
                break;
            default:
                break;
        }
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
            if (!(dishesEList
                    .get(firstVisibleItem)
                    .getDishesTypeGUID().equals(mCurrentDishesTypeGuid))) {
                mCurrentDishesTypeGuid = dishesEList
                        .get(firstVisibleItem)
                        .getDishesTypeGUID();
                // 滑动至rv中间，有bug，故注释
//                int dyPos = mDishesTypeEPositionHashMap.get(dishesEList.get(firstVisibleItem).getDishesTypeGUID()) - snackNewOrderLeftList.getFirstVisiblePosition();
//                if (0 <= dyPos && dyPos < snackNewOrderLeftList.getChildCount()) {
//                    View childAt = snackNewOrderLeftList.getChildAt(dyPos);
//                    int y = (childAt.getTop() - snackNewOrderLeftList.getHeight() / 2);
////                snackNewOrderLeftList.smoothScrollBy(y,1000);
//                    snackNewOrderLeftList.smoothScrollByOffset(y);
//                } else {
                snackNewOrderLeftList.smoothScrollToPosition(mDishesTypeEPositionHashMap.get(dishesEList.get(firstVisibleItem).getDishesTypeGUID()));
//                }
                mDishesTypeCommonAdapter.setChickedGUID(dishesEList
                        .get(firstVisibleItem)
                        .getDishesTypeGUID());
                mDishesTypeCommonAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 计算下单总价
     **/
    private void initAmount() {
        double amount = 0;
        SnackDishesActivity activity = (SnackDishesActivity) getActivity();
        if (orderDishesGroup == null || orderDishesGroup.getDishesList() == null
                || orderDishesGroup.getDishesList().size() == 0) {
            mOrderTotalAmount.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(amount)));
            return;
        }
        for (SalesOrderBatchDishesE orderBatchDishesE : orderDishesGroup.getDishesList()) {
            //菜品总价
            amount = ArithUtil.add(amount, ArithUtil.mul(orderBatchDishesE.getOrderCount(),
                    activity.getUseMemberPrice() ? orderBatchDishesE.getMemberPrice() : orderBatchDishesE.getPrice()));
            //菜品做法总价
            if (orderBatchDishesE.getArrayOfDishesPracticeE() != null) {
                for (DishesPracticeE dishesPracticeE : orderBatchDishesE.getArrayOfDishesPracticeE()) {
                    amount = ArithUtil.add(amount, ArithUtil.mul(dishesPracticeE.getFees(), orderBatchDishesE.getOrderCount()));
                }
            }
        }
        //底部按钮确认下单价格显示
        mOrderTotalAmount.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(amount)));
    }

    /**
     * 创建购物车弹出框
     */
    private View createCarView() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_order_dishes_car, (ViewGroup) getActivity().getWindow().getDecorView(), false);
        mCarRv = view.findViewById(R.id.dishes_car_RV);
        //清空按钮
        LinearLayout clear = view.findViewById(R.id.clear_all);
        //清空购物车
        clear.setOnClickListener(v -> onCarOrderDishesClearAll());
        //初始化adapter
        initCarAdapter();
        mCarRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mCarRv.setAdapter(mCarAdapter);
        return view;
    }

    /**
     * 购物车adapter
     */
    private void initCarAdapter() {
        SnackDishesActivity activity = (SnackDishesActivity) getActivity();
        mCarAdapter = new CommonAdapter<SalesOrderBatchDishesE>(getActivity(), R.layout.item_dishes_order_car, mCarDateList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                boolean weighEnable = salesOrderBatchDishesE.isWeighEnable();
                //菜品名称
                holder.setText(R.id.dishes_name, salesOrderBatchDishesE.getDishesName());
                //点菜数量
                SpanUtils spanUtils = new SpanUtils();
                spanUtils.append(ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getOrderCount())).setUnderline();

                if (salesOrderBatchDishesE.getMaxValue() != null && salesOrderBatchDishesE.getOrderCount() > salesOrderBatchDishesE.getMaxValue()) {
                    holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.common_text_color_f56766));
                } else {
                    holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                }

                if (weighEnable) {
                    holder.setVisible(R.id.ll_catty, true);
                    holder.setVisible(R.id.ll_dishes_number, false);
                    holder.setText(R.id.tv_catty_number, spanUtils.create());
                } else {
                    holder.setVisible(R.id.ll_catty, false);
                    holder.setVisible(R.id.ll_dishes_number, true);
                    holder.setText(R.id.tv_dishes_count, spanUtils.create());
                }

                if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() != null && salesOrderBatchDishesE.getArrayOfDishesPracticeE().size() > 0) {
                    //有做法
                    holder.setVisible(R.id.dishes_remake, true);
                    StringBuilder practice = new StringBuilder();
                    for (DishesPracticeE dishesPracticeE : salesOrderBatchDishesE.getArrayOfDishesPracticeE()) {
                        practice.append(dishesPracticeE.getName()).append(",");
                    }
                    practice.deleteCharAt(practice.length() - 1);
                    holder.setText(R.id.dishes_remake, practice.toString());
                    //计算价格（加做法价格）
                    double amount = 0;
                    double totalAmount;
                    for (DishesPracticeE dishesPracticeE : salesOrderBatchDishesE.getArrayOfDishesPracticeE()) {
                        amount = ArithUtil.add(amount, ArithUtil.mul(dishesPracticeE.getFees(), salesOrderBatchDishesE.getOrderCount()));
                    }
                    if (activity.getUseMemberPrice()) {
                        totalAmount = ArithUtil.add(amount, ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getMemberPrice()));
                    } else {
                        totalAmount = ArithUtil.add(amount, ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getPrice()));
                    }
                    holder.setText(R.id.dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(totalAmount)));
                } else if (salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1) {
                    //自选套餐
                    holder.setVisible(R.id.dishes_remake, true);
                    StringBuilder packGroup = new StringBuilder();
                    for (SalesOrderBatchDishesE batchDishesE : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                        //套餐子项
                        int count = batchDishesE.getPackageDishesOrderCount();
                        double singleCount = batchDishesE.getSingleCount();
                        String unit = batchDishesE.getCheckUnit();
                        if (unit == null) unit = "";
                        if (singleCount == 1) {
                            if (count == 1) {
                                packGroup.append(batchDishesE.getDishesName()).append("+");
                            } else {
                                packGroup.append(batchDishesE.getDishesName()).append("×").append(count).append("+");
                            }
                        } else {
                            if (count == 1) {
                                packGroup.append(batchDishesE.getDishesName()).append(ArithUtil.stripTrailingZeros(singleCount))
                                        .append(unit).append("+");
                            } else {
                                packGroup.append(batchDishesE.getDishesName()).append(ArithUtil.stripTrailingZeros(singleCount))
                                        .append(unit).append("×").append(count).append("+");
                            }
                        }
                    }
                    packGroup.deleteCharAt(packGroup.length() - 1);
                    holder.setText(R.id.dishes_remake, packGroup.toString());
                    //总价
                    holder.setText(R.id.dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(activity.getUseMemberPrice() ? ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getMemberPrice()) :
                            ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getPrice()))));
                } else {
                    //总价
                    holder.setText(R.id.dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(activity.getUseMemberPrice() ? ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getMemberPrice()) :
                            ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getPrice()))));
                    holder.setVisible(R.id.dishes_remake, false);
                }
                //底部分割线的显示
                if (position == mCarDateList.size() - 1) {
                    holder.setVisible(R.id.divider, false);
                    holder.setVisible(R.id.bottom_tv, true);
                } else {
                    holder.setVisible(R.id.divider, true);
                    holder.setVisible(R.id.bottom_tv, false);
                }
                //加菜按钮点击事件
                holder.setOnClickListener(R.id.iv_add, v -> {
                    DishesEstimateRecordDishes estimate = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
                    //估清数量
                    double residueCount;
                    if (estimate != null) {
                        residueCount = estimate.getResidueCount();
                    } else {
                        residueCount = Double.MAX_VALUE;
                    }
                    //菜品剩余可点数量
                    double maxCount;
                    //菜品已点数量
                    double currentCount = 0;
                    for (SalesOrderBatchDishesE batchDishesE : mCarDateList) {
                        if (batchDishesE.getDishesGUID().equalsIgnoreCase(salesOrderBatchDishesE.getDishesGUID())) {
                            currentCount = ArithUtil.add(currentCount, batchDishesE.getOrderCount());
                        }
                    }
                    maxCount = ArithUtil.sub(residueCount, currentCount);
                    //已点数量
                    double orderCount = salesOrderBatchDishesE.getOrderCount();
                    double changeCount;
                    if (salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1) {
                        if (maxCount <= 0 || (maxCount > 0 && maxCount < 1)) {
                            showMessage(getString(R.string.add_dishes_failed_by_estimate));
                            return;
                        } else {
                            changeCount = ArithUtil.add(orderCount, 1);
                        }
                        //处理套餐加菜业务
                        onCarOrderDishesChangeClick(salesOrderBatchDishesE.getSalesOrderBatchGUID(), changeCount);
                    } else {
                        if (maxCount <= 0) {
                            showMessage(getString(R.string.add_dishes_failed_by_estimate));
                            return;
                        } else if (maxCount > 0 && maxCount < 1) {
                            //菜品不足1
                            changeCount = ArithUtil.add(orderCount, maxCount);
                        } else {
                            changeCount = ArithUtil.add(orderCount, 1);
                        }
                        //处理一般菜品加菜业务
                        onCarOrderDishesChangeClick(salesOrderBatchDishesE.getSalesOrderBatchGUID(), changeCount);
                    }
                    notifyDataSetChanged();
                });
                //减菜按钮
                holder.setOnClickListener(R.id.iv_reduce, v -> {
                    //已点数量
                    double orderCount = salesOrderBatchDishesE.getOrderCount();
                    if (orderCount <= 0) return;
                    double changCount;
                    if (orderCount > 0 && orderCount < 1) {
                        changCount = 0;
                    } else {
                        changCount = ArithUtil.sub(orderCount, 1);
                    }
                    if (changCount == 0) {
                        //移除数据
                        for (Iterator<SalesOrderBatchDishesE> iterator = mCarDateList.iterator(); iterator.hasNext(); ) {
                            if (iterator.next().getSalesOrderBatchGUID().equalsIgnoreCase(salesOrderBatchDishesE.getSalesOrderBatchGUID())) {
                                iterator.remove();
                            }
                        }
                    } else {
                        mCarDateList.get(position).setOrderCount(changCount);
                    }
                    onCarOrderDishesChangeClick(salesOrderBatchDishesE.getSalesOrderBatchGUID(), changCount);
                    notifyDataSetChanged();
                });
                holder.setOnClickListener(R.id.tv_catty_number, v -> {
                    //估清数据
                    DishesEstimateRecordDishes recordDishesCount = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
                    //菜品剩余数量
                    double tempEstimate = recordDishesCount != null ? recordDishesCount.getResidueCount() : Double.MAX_VALUE;

                    //已点数量
                    double orderCount = salesOrderBatchDishesE.getOrderCount();
                    onCarOrderDishesCountSetClick(salesOrderBatchDishesE
                            , orderCount, tempEstimate, TYPE_COMMON);
                });
                //菜品数量点击事件
                holder.setOnClickListener(R.id.tv_dishes_count, v -> {
                    DishesEstimateRecordDishes estimate = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
                    //估清数量
                    double residueCount;
                    if (estimate != null) {
                        residueCount = estimate.getResidueCount();
                    } else {
                        residueCount = Double.MAX_VALUE;
                    }
                    //菜品剩余可点数量
                    double maxCount;
                    //菜品已点数量
                    double currentCount = 0;
                    for (SalesOrderBatchDishesE batchDishesE : mCarDateList) {
                        if (!batchDishesE.getSalesOrderBatchGUID().equalsIgnoreCase(salesOrderBatchDishesE.getSalesOrderBatchGUID())) {
                            if (batchDishesE.getDishesGUID().equalsIgnoreCase(salesOrderBatchDishesE.getDishesGUID())) {
                                currentCount = ArithUtil.add(currentCount, batchDishesE.getOrderCount());
                            }
                        }
                    }
                    maxCount = ArithUtil.sub(residueCount, currentCount);
                    //已点数量
                    double orderCount = salesOrderBatchDishesE.getOrderCount();
                    if (salesOrderBatchDishesE.getIsPackageDishes() == 1 && salesOrderBatchDishesE.getPackageType() == 1) {
                        //自选套餐
                        if (maxCount <= 0 || (maxCount > 0 && maxCount < 1)) {
                            showMessage(getString(R.string.add_dishes_failed_by_estimate));
                            return;
                        }
                        onCarOrderDishesCountSetClick(salesOrderBatchDishesE
                                , orderCount, maxCount, TYPE_PACKAGE);
                    } else {
                        onCarOrderDishesCountSetClick(salesOrderBatchDishesE
                                , orderCount, maxCount, TYPE_COMMON);
                    }
                });
            }
        };
    }

    /**
     * 显示购物车
     */
    private void showCarView() {
        if (orderDishesGroup == null) return;
        mCarDateList = orderDishesGroup.getDishesList();
        if (mCarDateList == null || mCarDateList.size() == 0) {
            return;
        }
        if (mCarBottomSheetView == null) {
            mCarBottomSheetView = createCarView();
        }
        if (mBottomSheetLayout.isSheetShowing()) {
            mBottomSheetLayout.dismissSheet();
        } else {
            mCarAdapter.notifyDataSetChanged();
            mBottomSheetLayout.showWithSheetView(mCarBottomSheetView);
        }
    }
}
