package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.OrderDishesAdapter;
import com.holderzone.intelligencepos.adapter.impl.OrderDishesTypeAdapter;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.Constants;
import com.holderzone.intelligencepos.dialog.impl.AddPracticeDishesDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ChangeOrderDishesCountDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ReducePracticeDishesDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.OrderDishesContract;
import com.holderzone.intelligencepos.mvp.hall.activity.OrderDetailActivity;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesEstimateRecordDishes;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderDishesGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroupDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.ParametersConfig;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.OrderDishesPersenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CloneUtils;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.AddDishesPopup;
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
import io.reactivex.Observable;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import static com.holderzone.intelligencepos.base.Constants.EXTRAS_MEMBER_INFO;
import static com.holderzone.intelligencepos.mvp.activity.SearchDishesActivity.CALL_BACK_ORDER_GROUP;

/**
 * 点菜加菜页面
 * Created by chencao on 2017/9/4.
 */

public class OrderDishesActivity extends BaseActivity<OrderDishesContract.Presenter> implements OrderDishesContract.View,
        StickyListHeadersListView.OnHeaderClickListener
        , StickyListHeadersListView.OnStickyHeaderChangedListener
        , StickyListHeadersListView.OnStickyHeaderOffsetChangedListener
        , AddDishesPopup.AddDishesPopupListener
        , OrderDishesAdapter.CallBackClick
        , AddPracticeDishesDialogFragment.CallBackPracticeOK
        , ReducePracticeDishesDialogFragment.CallBackReduceDialog
        , ChangeOrderDishesCountDialogFragment.ChangeOrderDishesCountListener {

    private static String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";
    private static String EXTRAS_DINING_TABLE_GUID = "EXTRAS_DINING_TABLE_GUID";
    private static String EXTRAS_MAIN_ORDER_GUID = "EXTRAS_MAIN_ORDER_GUID";
    private static String EXTRAS_TABLE_NAME = "EXTRAS_TABLE_NAME";
    private static final String INTENT_ORDER_COUNT = "INTENT_ORDER_COUNT";
    private static final String INTENT_PACKAGE_GROUP = "INTENT_PACKAGE_GROUP";
    private static final String INTENT_DISHES_GUID = "INTENT_DISHES_GUID";
    private static final String INTENT_RISE_GUID = "INTENT_RISE_GUID";

    private static int REQUEST_CODE_MEMBER_LOGIN = 01;
    private static int REQUEST_CODE_PACKAGE_GROUP = 110;
    private static int KEY_SERACH_CODE = 222;

    private static final String TYPE_COMMON = "TYPE_COMMON";
    private static final String TYPE_PACKAGE = "TYPE_PACKAGE";
    private static final String SHOW_TYPE_PRACTICE = "SHOW_TYPE_PRACTICE";
    private static final String SHOW_TYPE_COMMON = "SHOW_TYPE_COMMON";


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_return)
    LinearLayout llReturn;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.lv_dishes_type)
    ListView lvDishesType;
    @BindView(R.id.lv_dishes)
    StickyListHeadersListView lvDishes;
    @BindView(R.id.iv_order_no_dishes)
    ImageView ivOrderNoDishes;
    @BindView(R.id.tv_order_no_dishes)
    TextView tvOrderNoDishes;
    @BindView(R.id.no_dishes_layout)
    RelativeLayout noDishesLayout;
    @BindView(R.id.snack_dishes_number_title)
    TextView snackDishesNumberTitle;
    @BindView(R.id.snack_dishes_number)
    TextView snackDishesNumber;
    @BindView(R.id.snack_dishes_sure)
    Button snackDishesSure;
    @BindView(R.id.order_dishes_car)
    LinearLayout mOrderDishesCar;
    @BindView(R.id.order_total_amount)
    TextView mOrderTotalAmount;
    @BindView(R.id.bottom_sheet_layout)
    BottomSheetLayout mBottomSheetLayout;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.snack_dishes_button_layout)
    LinearLayout snackDishesButtonLayout;

    // 点单GUID
    private String mSalesOrderGuid;

    // 桌台GUID
    private String mDiningTableGuid;

    // 桌台名称
    private String mTableName;

    // 菜品适配器
    private OrderDishesAdapter orderDishesAdapter;

    // 菜品类型适配器
    private OrderDishesTypeAdapter orderDishesTypeAdapter;

    // 已选择的菜品
    public OrderDishesGroup orderDishesGroup = new OrderDishesGroup();

    // 缓存的菜品列表
    private List<DishesE> dishesEList = new ArrayList<>();

    // 缓存的菜品类型列表
    private List<DishesTypeE> dishesTypeEList = new ArrayList<>();

    // 对应的菜品做法列表
    private HashMap<String, List<DishesPracticeE>> dishesPracticeMap;

    // 记录各个菜品中同一类型菜品第一项的位置
    private ArrayMap<String, Integer> mDishesEPositionHashMap = new ArrayMap<>();

    // 记录菜品类型列表的位置
    private ArrayMap<String, Integer> mDishesTypeEPositionHashMap = new ArrayMap<>();

    // 当前被点击的菜品类型GUID
    private String mCurrentDishesTypeGuid;

    // 估清信息
    private Map<String, DishesEstimateRecordDishes> mEstimateMap = new HashMap<>();

    // 是否从OrderDeailActivity返回
    private boolean isCallBackFromeActivity = false;
    private boolean mHasOpenMemberPrice;
    private boolean isHesMember;

    // 菜品滚动Listener
    private MyOnDishesScrollListener myOnDishesScrollListener;

    // startActivity#OrderDeailActivity.class 请求码
    public static final int REQUESTCODE = 100;
    private MemberInfoE mMemberInfo;
    private String mainOrderGuid;
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
     * 选择做法弹出框
     */
    private AddPracticeDishesDialogFragment mAddPracticeDishesDialogFragment;
    private Map<String, Boolean> stopSalse;

    public static Intent newIntent(Context context, DiningTableE diningTableE, MemberInfoE memberInfoE) {
        String mSalesOrderGuid = diningTableE.getSalesOrderGUID();
        String mDiningTableGuid = diningTableE.getDiningTableGUID();
        String mTableName = diningTableE.getName();
        Intent intent = new Intent(context, OrderDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, mSalesOrderGuid);
        bundle.putString(EXTRAS_DINING_TABLE_GUID, mDiningTableGuid);
        bundle.putString(EXTRAS_TABLE_NAME, mTableName);
        if (memberInfoE != null) {
            bundle.putParcelable(EXTRAS_MEMBER_INFO, memberInfoE);
        }
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newIntent(Context context, DiningTableE diningTableE, String mainOrderGuid, MemberInfoE memberInfoE) {
        String mSalesOrderGuid = diningTableE.getSalesOrderGUID();
        String mDiningTableGuid = diningTableE.getDiningTableGUID();
        String mTableName = diningTableE.getName();
        Intent intent = new Intent(context, OrderDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, mSalesOrderGuid);
        if (memberInfoE != null) {
            bundle.putParcelable(EXTRAS_MEMBER_INFO, memberInfoE);
        }
        if (mainOrderGuid != null) {
            bundle.putString(EXTRAS_MAIN_ORDER_GUID, mainOrderGuid);
        }
        bundle.putString(EXTRAS_DINING_TABLE_GUID, mDiningTableGuid);
        bundle.putString(EXTRAS_TABLE_NAME, mTableName);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID);
        mDiningTableGuid = extras.getString(EXTRAS_DINING_TABLE_GUID);
        mTableName = extras.getString(EXTRAS_TABLE_NAME);
        if (extras.containsKey(EXTRAS_MEMBER_INFO)) {
            mMemberInfo = extras.getParcelable(EXTRAS_MEMBER_INFO);
        }
        if (extras.containsKey(EXTRAS_MAIN_ORDER_GUID)) {
            mainOrderGuid = extras.getString(EXTRAS_MAIN_ORDER_GUID);
        }
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_order_dishes;
    }

    @Nullable
    @Override
    protected OrderDishesContract.Presenter initPresenter() {
        return new OrderDishesPersenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter.getHasOpenMemberPrice();
        String showTableName = mTableName.length() > 5 ? mTableName : mTableName + " 选择菜品";
        tvTitle.setText(showTableName);
        Observable.merge(RxView.clicks(ivReturn), RxView.clicks(tvTitle))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(aBoolean ->
                        clickBack());

        ivSearch.setVisibility(View.VISIBLE);
        RxView.clicks(ivSearch).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            startActivityForResult(SearchDishesActivity.newIntent(this, orderDishesGroup, mHasOpenMemberPrice, mMemberInfo != null), KEY_SERACH_CODE);
        });

        tvMenu.setVisibility(View.GONE);
        ivMenu.setImageResource(R.drawable.more_blue);
        mPresenter.getDishesEstimate();
        RxView.clicks(snackDishesSure).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            if (orderDishesGroup != null && orderDishesGroup.getDishesList() != null) {
                for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
                    if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
                        for (SalesOrderBatchDishesE sub : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
                            sub.setOrderCount(ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), sub.getPackageDishesUnitCount()));
                        }
                    }
                }
            }
            startActivityForResult(OrderDetailActivity.newIntent(this, orderDishesGroup, mEstimateMap
                    , mSalesOrderGuid, mDiningTableGuid, mTableName
                    , mMemberInfo != null && mHasOpenMemberPrice), REQUESTCODE);
        });
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
        //菜品adapter
        orderDishesAdapter = new OrderDishesAdapter(this, mHasOpenMemberPrice && mMemberInfo != null);
        orderDishesAdapter.setCallBackClick(this);
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
        lvDishes.setAdapter(orderDishesAdapter);
        myOnDishesScrollListener = new MyOnDishesScrollListener();
        lvDishes.setOnScrollListener(myOnDishesScrollListener);
        lvDishes.setDivider(null);
        //菜品类型adapter
        orderDishesTypeAdapter = new OrderDishesTypeAdapter(this, dishesTypeEList);
        lvDishesType.setAdapter(orderDishesTypeAdapter);
        lvDishesType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (dishesTypeEList.get(position).getDishesTypeGUID().equals(mCurrentDishesTypeGuid)) {
                    return;
                } else {
                    mCurrentDishesTypeGuid = dishesTypeEList.get(position).getDishesTypeGUID();
                    orderDishesTypeAdapter.setChickedGUID(mCurrentDishesTypeGuid);
                    if (mDishesEPositionHashMap.get(mCurrentDishesTypeGuid) == null) {
                        return;
                    }
                    lvDishes.setSelection(mDishesEPositionHashMap.get(mCurrentDishesTypeGuid));
                }
            }
        });
        mPresenter.getDishesType();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        setDishesSureAndDishesNumber();
        initAmount();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDishesSureAndDishesNumber();
        initAmount();
        if (isCallBackFromeActivity) {
            mPresenter.getDishesEstimate();
        }
    }

    @Override
    protected void onStop() {
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

    private void finishAddDishesActivity() {
        Intent intent = new Intent();
        if (mMemberInfo != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.EXTRAS_MEMBER_INFO, mMemberInfo);
            intent.putExtras(bundle);
        }
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUESTCODE && resultCode == RESULT_OK) {
//            mPresenter.getListNotCheckOut();
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    int tag = bundle.getInt(OrderDetailActivity.BACK_STATE);
                    if (tag == OrderDetailActivity.BACK_STATE_0) {
                        orderDishesGroup = bundle.getParcelable(OrderDetailActivity.SALES_ORDER_DISHES_GROUP_KEY);
                        if (orderDishesAdapter != null) {
                            orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
                            isCallBackFromeActivity = true;
                        } else {
                            initView(null);
                        }
                    } else {
                        finishAddDishesActivity();
                    }
                }
            }
        } else if (requestCode == REQUEST_CODE_MEMBER_LOGIN && resultCode == RESULT_OK) {
            mMemberInfo = data.getParcelableExtra(EXTRAS_MEMBER_INFO);
            orderDishesAdapter.resetUseMemberPrice(mHasOpenMemberPrice && mMemberInfo != null);
        } else if (requestCode == REQUEST_CODE_PACKAGE_GROUP) {
            //套餐页面传回来的数据
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String dishesGUID = bundle.getString(INTENT_DISHES_GUID);
                    double count = bundle.getDouble(INTENT_ORDER_COUNT);//套餐数量
                    double rise = bundle.getDouble(INTENT_RISE_GUID);//浮动价
                    List<PackageGroupDishesE> packageGroupList = bundle.getParcelableArrayList(INTENT_PACKAGE_GROUP);
                    //设置订单数据
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
                    for (PackageGroupDishesE groupDishesE : packageGroupList) {
                        SalesOrderBatchDishesE salesOrderBatchDishesE = new SalesOrderBatchDishesE();
                        salesOrderBatchDishesE.setDishesGUID(groupDishesE.getDishesGUID());
                        salesOrderBatchDishesE.setIsPackageDishes(2);
                        salesOrderBatchDishesE.setCheckUnit(groupDishesE.getCheckUnit());
                        salesOrderBatchDishesE.setPackageDishesUnitCount(groupDishesE.getSingleCount());
                        salesOrderBatchDishesE.setPackageDishesOrderCount(groupDishesE.getPackageDishesOrderCount());
                        salesOrderBatchDishesE.setDishesName(groupDishesE.getSimpleName());
                        salesOrderBatchDishesE.setSingleCount(groupDishesE.getSingleCount());
                        salesOrderBatchDishesE.setSimpleName(groupDishesE.getSimpleName());
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
                    orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
                    setDishesSureAndDishesNumber();
                    initAmount();
                }
            }
        } else if (requestCode == KEY_SERACH_CODE && resultCode == RESULT_OK) {
            //搜索页面返回的数据
            if (data != null && data.getExtras() != null) {
                Bundle bundle = data.getExtras();
                orderDishesGroup = bundle.getParcelable(CALL_BACK_ORDER_GROUP);
                isCallBackFromeActivity = true;
                if (orderDishesGroup != null) {
                    orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
                    setDishesSureAndDishesNumber();
                    initAmount();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({R.id.ll_return, R.id.ll_menu, R.id.snack_dishes_button_layout, R.id.order_dishes_car})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_return:
                // REFACTOR: 2017/9/9 待重构 此处已被Line168完全取代了作用
                finishAddDishesActivity();
                break;
            case R.id.ll_menu:
                AddDishesPopup dishesOrderedPopup = new AddDishesPopup(OrderDishesActivity.this, mMemberInfo, isHesMember);
                dishesOrderedPopup.showOnAnchor(llMenu,
                        RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
                break;
            case R.id.order_dishes_car:
            case R.id.snack_dishes_button_layout:
                showCarView();
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetDishesEstimateSuccess(Map<String, DishesEstimateRecordDishes> map) {
        /***根据估清信息设置菜品停售信息***/
        mEstimateMap.clear();
        mEstimateMap.putAll(map);
        if (isCallBackFromeActivity) {
            isCallBackFromeActivity = false;
            setDishesSureAndDishesNumber();
            initAmount();
            if (orderDishesAdapter != null) {
                orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
            }
        }
        mPresenter.updateDishesStopStatus(dishesTypeEList, map);
    }

    @Override
    public void callBackShowMessage(String message) {
        showMessage(message);
    }

    @Override
    public void callBackChange(List<SalesOrderBatchDishesE> newSalesOrderBatchDishesEList) {
        String dishesGUID = newSalesOrderBatchDishesEList.get(0).getDishesGUID();
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = new ArrayList<>();
        for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
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
        orderDishesGroup.getDishesList().clear();
        orderDishesGroup.setDishesList((ArrayList<SalesOrderBatchDishesE>) salesOrderBatchDishesEList);
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
        setDishesSureAndDishesNumber();
        initAmount();
    }

    @Override
    public void callBackOK(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        addDishesE(salesOrderBatchDishesE);
    }

    @Override
    public void changePracticeCount(double currentCount, double maxCount) {
        //修改做法数量弹框
        getDialogFactory().showChangeOrderDishesCountDialog(new SalesOrderBatchDishesE(), currentCount, maxCount
                , TYPE_COMMON, SHOW_TYPE_PRACTICE, this);
    }

    @Override
    public void getDishesData(List<DishesTypeE> dishesTypeList, HashMap<String, List<DishesPracticeE>> dishesPracticeMap) {
        dishesTypeEList.clear();
        if (dishesTypeList != null && dishesTypeList.size() != 0) {
            lvDishesType.setVisibility(View.VISIBLE);
            dishesTypeEList.addAll(dishesTypeList);
        } else {
            noDishesLayout.setVisibility(View.VISIBLE);
            lvDishes.setVisibility(View.GONE);
            lvDishesType.setVisibility(View.GONE);
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
            noDishesLayout.setVisibility(View.GONE);
            lvDishes.setVisibility(View.VISIBLE);
            orderDishesAdapter.setDishesEList(dishesEList, dishesTypeEList, mDishesTypeEPositionHashMap);
            orderDishesTypeAdapter.setChickedGUID(dishesEList
                    .get(0)
                    .getDishesTypeGUID());
        } else {
            noDishesLayout.setVisibility(View.VISIBLE);
            lvDishes.setVisibility(View.GONE);
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
        stopSalse = new ArrayMap<>();
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
    public void onGetSystemConfigSuccess(ParametersConfig config) {
        this.mHasOpenMemberPrice = config.isMemberPrice();
        this.isHesMember = config.isHesMember();
    }

    @Override
    public void onMemberExitSuccess() {
        showMessage("会员退出成功！");
        mMemberInfo = null;
        orderDishesAdapter.resetUseMemberPrice(mHasOpenMemberPrice && mMemberInfo != null);
        //刷新价格
        initAmount();
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
    public void reduceDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
        boolean isRemove = false;
        if (orderDishesGroup != null && orderDishesGroup.getDishesList() != null && orderDishesGroup.getDishesList().size() != 0) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
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
            for (Iterator<SalesOrderBatchDishesE> iterator = orderDishesGroup.getDishesList().iterator(); iterator.hasNext(); ) {
                if (iterator.next().getDishesGUID().equalsIgnoreCase(newSalesOrderBatchDishesE.getDishesGUID())) {
                    iterator.remove();
                }
            }
        }
        setDishesSureAndDishesNumber();
        initAmount();
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
    }

    @Override
    public void addDishesE(SalesOrderBatchDishesE newSalesOrderBatchDishesE) {
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
            orderDishesGroup.getDishesList().add(newSalesOrderBatchDishesE);
        }
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
        initAmount();
        setDishesSureAndDishesNumber();
    }

    @Override
    public void addDishesEPractice(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE, Map<String, Double> dishesOrderCount) {
        DishesEstimateRecordDishes temp = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        Double tempCount = dishesOrderCount.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
        Double tempEstimate = temp != null ? (temp.getResidueCount()) : Double.MAX_VALUE;
        if (mAddPracticeDishesDialogFragment != null) {
            mAddPracticeDishesDialogFragment = null;
        }
        mAddPracticeDishesDialogFragment = AddPracticeDishesDialogFragment.newInstance(dishesPracticeEs, salesOrderBatchDishesE, ArithUtil.sub(tempEstimate
                , tempCount != null ? tempCount : 0));
        mAddPracticeDishesDialogFragment.setCallBack(this);
        mAddPracticeDishesDialogFragment.show(getSupportFragmentManager(), "AddPracticeDishesDialogFragment");
    }

    @Override
    public void reduceDishesEDialog(String dishesGUID) {
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEs = new ArrayList<>();
        if (orderDishesGroup != null && orderDishesGroup.getDishesList() != null) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : orderDishesGroup.getDishesList()) {
                if (salesOrderBatchDishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                    SalesOrderBatchDishesE tempSalesOrder = null;
                    try {
                        tempSalesOrder = (SalesOrderBatchDishesE) CloneUtils.deepClone(salesOrderBatchDishesE);
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
        dialogFragment.show(getSupportFragmentManager(), "ReducePracticeDishesDialogFragment");
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
        getDialogFactory().showChangeOrderDishesCountDialog(salesOrderBatchDishesE, orderDishesCount, tempEstimate, TYPE_COMMON
                , SHOW_TYPE_COMMON, OrderDishesActivity.this);
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
        for (DishesE dishesE : dishesEList) {
            if (dishesE.getDishesGUID().equalsIgnoreCase(dishesGUID)) {
                List<PackageGroup> list = dishesE.getArrayOfPackageGroup();
                startActivityForResult(PackGroupOrderDishesActivity.newIntent(dishesE.getSimpleName(), dishesE.getDishesGUID()
                        , mHasOpenMemberPrice && mMemberInfo != null ? dishesE.getMemberPrice() : dishesE.getCheckPrice()
                        , ArithUtil.sub(tempEstimate, tempCount != null ? tempCount : 0), list, this), REQUEST_CODE_PACKAGE_GROUP);
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
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
        initAmount();
        setDishesSureAndDishesNumber();
        onChangeCount(newSalesOrderBatchDishesE);
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
        setDishesSureAndDishesNumber();
        initAmount();
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
        if (mCarAdapter != null) {
            mCarAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPracticeCountChanged(double count) {
        //修改做法菜品数量 回掉
        if (mAddPracticeDishesDialogFragment != null) {
            mAddPracticeDishesDialogFragment.setNetPracticeCount(count);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            clickBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回
     */
    private void clickBack() {
        if (orderDishesGroup == null || orderDishesGroup.getDishesList() == null || orderDishesGroup.getDishesList().size() == 0) {
            finishAddDishesActivity();
        } else {
            mDialogFactory.showConfirmDialog("返回将不再保留已选菜品。确认返回吗？", "取消", "确认返回", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {
                }

                @Override
                public void onPosClick() {
                    finishAddDishesActivity();
                }
            });
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
            tempCount++;
//            tempCount = ArithUtil.add(tempCount, salesOrderBatchDishesE.getOrderCount());
        }
        if (tempCount == (int) tempCount) {
            return (int) tempCount + "";
        } else {
            return tempCount + "";
        }
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
     * 克隆 SalesOrderBatchDishesE
     *
     * @param salesOrderBatchDishesE
     * @return
     */
    private SalesOrderBatchDishesE cloneSalesOrderBatchDishesE(SalesOrderBatchDishesE salesOrderBatchDishesE) {
        SalesOrderBatchDishesE newSalesOrderBatchDishesE = new SalesOrderBatchDishesE();
        newSalesOrderBatchDishesE.setDishesE(salesOrderBatchDishesE.getDishesE());
        newSalesOrderBatchDishesE.setDishesGUID(salesOrderBatchDishesE.getDishesGUID());
        newSalesOrderBatchDishesE.setCheckStatus(salesOrderBatchDishesE.getCheckStatus());
        newSalesOrderBatchDishesE.setKitchenPrintStatus(salesOrderBatchDishesE.getKitchenPrintStatus());
//        newSalesOrderBatchDishesE.setNew(salesOrderBatchDishesE.getNew());
        newSalesOrderBatchDishesE.setDishesUnit(salesOrderBatchDishesE.getDishesUnit());
        newSalesOrderBatchDishesE.setGift(salesOrderBatchDishesE.getGift());
        newSalesOrderBatchDishesE.setIsPackageDishes(salesOrderBatchDishesE.getIsPackageDishes());
        newSalesOrderBatchDishesE.setOrderCount(salesOrderBatchDishesE.getOrderCount());
        newSalesOrderBatchDishesE.setArrayOfDishesPracticeE(salesOrderBatchDishesE.getArrayOfDishesPracticeE());
        if (salesOrderBatchDishesE.getIsPackageDishes() == 1) {//套餐
            List<SalesOrderBatchDishesE> subList = new ArrayList();
            for (SalesOrderBatchDishesE subDishes : salesOrderBatchDishesE.getArrayOfSalesOrderBatchDishesE()) {
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
    public void onMemeberLoginExitClick(boolean isLogin) {
        if (isLogin) {
            getDialogFactory().showConfirmDialog(getString(R.string.member_exit), getString(R.string.cancel), getString(R.string.confirm_exit), new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {
                }

                @Override
                public void onPosClick() {
                    mPresenter.memberLoginOut(mainOrderGuid != null ? mainOrderGuid : mSalesOrderGuid);
                }
            });
        } else {
            startActivityForResult(BalanceAccountsMemberLoginActivity.newIntent(getApplicationContext(), mainOrderGuid != null ? mainOrderGuid : mSalesOrderGuid), REQUEST_CODE_MEMBER_LOGIN);
        }
    }

    @Override
    public void onRefreshClick() {
        mPresenter.getDishesEstimate();
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
        //判断是否可以关闭购物车
        if (orderDishesGroup.getDishesList() == null || orderDishesGroup.getDishesList().size() <= 0) {
            if (mCarBottomSheetView != null) {
                if (mBottomSheetLayout.isSheetShowing()) {
                    mBottomSheetLayout.dismissSheet();
                }
            }
        }
        //矫正数据
        setDishesSureAndDishesNumber();
        initAmount();
        orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
    }

    public void onCarOrderDishesCountSetClick(SalesOrderBatchDishesE salesOrderBatchDishesE, double currentCount, double maxCount, String type) {
        getDialogFactory().showChangeOrderDishesCountDialog(salesOrderBatchDishesE, currentCount, maxCount
                , type, SHOW_TYPE_COMMON, this);
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
                orderDishesAdapter.setOrderDishesGroup(orderDishesGroup);
            }
        });
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
                lvDishesType.smoothScrollToPosition(mDishesTypeEPositionHashMap.get(mCurrentDishesTypeGuid));
                orderDishesTypeAdapter.setChickedGUID(mCurrentDishesTypeGuid);
            }
        }
    }

    /**
     * 计算下单总价
     **/
    private void initAmount() {
        double amount = 0;
        if (orderDishesGroup == null || orderDishesGroup.getDishesList() == null
                || orderDishesGroup.getDishesList().size() == 0) {
            mOrderTotalAmount.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(amount)));
            return;
        }
        for (SalesOrderBatchDishesE orderBatchDishesE : orderDishesGroup.getDishesList()) {
            //菜品总价
            amount = ArithUtil.add(amount, ArithUtil.mul(orderBatchDishesE.getOrderCount(), mMemberInfo != null && mHasOpenMemberPrice ?
                    orderBatchDishesE.getMemberPrice() : orderBatchDishesE.getPrice()));
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
        View view = LayoutInflater.from(this).inflate(R.layout.activity_order_dishes_car, (ViewGroup) getWindow().getDecorView(), false);
        mCarRv = view.findViewById(R.id.dishes_car_RV);
        //清空按钮
        LinearLayout clear = view.findViewById(R.id.clear_all);
        //清空购物车
        clear.setOnClickListener(v -> onCarOrderDishesClearAll());
        //初始化adapter
        initCarAdapter();
        mCarRv.setLayoutManager(new LinearLayoutManager(this));
        mCarRv.setAdapter(mCarAdapter);
        return view;
    }

    /**
     * 购物车adapter
     */
    private void initCarAdapter() {
        mCarAdapter = new CommonAdapter<SalesOrderBatchDishesE>(this, R.layout.item_dishes_order_car, mCarDateList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                boolean weighEnable = salesOrderBatchDishesE.isWeighEnable();
                //菜品名称
                holder.setText(R.id.dishes_name, salesOrderBatchDishesE.getDishesName());
                DishesEstimateRecordDishes estimate = mEstimateMap.get(salesOrderBatchDishesE.getDishesGUID().toLowerCase());
                //估清数量
                double residueCount;
                if (estimate != null) {
                    residueCount = estimate.getResidueCount();
                } else {
                    residueCount = Double.MAX_VALUE;
                }
                if (salesOrderBatchDishesE.getMaxValue() != null && salesOrderBatchDishesE.getOrderCount() > salesOrderBatchDishesE.getMaxValue()) {
                    holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.common_text_color_f56766));
                } else {
                    holder.setTextColor(R.id.tv_dishes_count, getResources().getColor(R.color.btn_text_black_000000));
                }

                SpanUtils spanUtils = new SpanUtils();
                spanUtils.append(ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getOrderCount())).setUnderline();
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
                    if (mMemberInfo != null && mHasOpenMemberPrice) {
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
                    holder.setText(R.id.dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mMemberInfo != null && mHasOpenMemberPrice ? ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getMemberPrice()) :
                            ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getPrice()))));
                } else {
                    //总价
                    holder.setText(R.id.dishes_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mMemberInfo != null && mHasOpenMemberPrice ? ArithUtil.mul(salesOrderBatchDishesE.getOrderCount(), salesOrderBatchDishesE.getMemberPrice()) :
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

                //加菜按钮点击事件
                holder.setOnClickListener(R.id.iv_add, v -> {
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
                //菜品数量点击事件
                holder.setOnClickListener(R.id.tv_dishes_count, v -> {
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
