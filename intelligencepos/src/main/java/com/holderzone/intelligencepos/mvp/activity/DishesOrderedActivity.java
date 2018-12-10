package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.Constants;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.DishesOrderedContract;
import com.holderzone.intelligencepos.mvp.fragment.DishesOrderedFragment;
import com.holderzone.intelligencepos.mvp.fragment.DishesRecordFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.DishesOrderedPresenter;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesRecordViewBean;
import com.holderzone.intelligencepos.mvp.viewbean.OrderedDishesViewBean;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.DishesOrderedPopup;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 已点菜界面
 * Created by tcw on 2017/9/4.
 */
public class DishesOrderedActivity extends BaseActivity<DishesOrderedContract.Presenter> implements DishesOrderedContract.View, DishesOrderedPopup.OnItemClickListener {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_INTENT_ALL_DINING_TABLE = "INTENT_ALL_DINING_TABLE";

    private static final String KEY_TITLE_DISHES_ORDERED = "已点菜品";

    private static final String KEY_TITLE_DISHES_RECORD = "操作记录";

    private static final int REQUEST_CODE_BALANCE_ACCOUNT = 0;

    private static final int REQUEST_CODE_DESIGNATE_DISHES = 1;
    private static final int REQUEST_CODE_ADD_DISHES = 2;

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;
    @BindView(R.id.tl_func_type)
    TabLayout mTlFuncType;
    @BindView(R.id.vp_func)
    ViewPager mVpFunc;
    @BindView(R.id.btn_print)
    Button mBtnPrint;
    @BindView(R.id.btn_add)
    Button mBtnAdd;

    // 当前桌台实体
    private DiningTableE mDiningTableE;
    private List<DiningTableE> mAllDiningTableEList;

    // 所有批次数据
    private List<OrderedDishesRecordViewBean> recordList = new ArrayList<>();
    private List<OrderedDishesViewBean> activeList = new ArrayList<>();

    // tabLayout && viewPager
    private DishesOrderedAdapter mDishesOrderAdapter;
    private List<String> mArrayOfTbTitles = new ArrayList<>();

    // 是否开启划菜功能
    private boolean isDesignated = false;

    private boolean isReStartReflash = false;

    private String checkSalesOrderGUID;
    private MemberInfoE memberInfoE;

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, DishesOrderedActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent newIntent(Context context, DiningTableE diningTableE, List<DiningTableE> allDiningTableEList) {
        Intent intent = new Intent(context, DishesOrderedActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        extras.putParcelableArrayList(KEY_INTENT_ALL_DINING_TABLE, (ArrayList<? extends Parcelable>) allDiningTableEList);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
        mAllDiningTableEList = extras.getParcelableArrayList(KEY_INTENT_ALL_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_dishes_ordered;
    }

    @Override
    protected DishesOrderedContract.Presenter initPresenter() {
        return new DishesOrderedPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mPresenter.getHasOpenMemberPrice();
        // 初始化 title
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setTitleText(mDiningTableE.getName());
        mTitle.setOnMenuClickListener(() -> {
            DishesOrderedPopup dishesOrderedPopup = new DishesOrderedPopup(DishesOrderedActivity.this, isDesignated);
            dishesOrderedPopup.showOnAnchor(mTitle.findViewById(R.id.ll_menu),
                    RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
        });
        // 初始化 tabLayout
        mTlFuncType.setTabMode(TabLayout.MODE_FIXED);
        mTlFuncType.setTabGravity(TabLayout.GRAVITY_FILL);
        // 构造TabLayout数据
        mArrayOfTbTitles.add(KEY_TITLE_DISHES_ORDERED);
        mArrayOfTbTitles.add(KEY_TITLE_DISHES_RECORD);
        // 初始化 viewPager
        mDishesOrderAdapter = new DishesOrderedAdapter(getSupportFragmentManager());
        mVpFunc.setAdapter(mDishesOrderAdapter);
        // 设置 tabLayout && viewPager 联动
        mTlFuncType.setupWithViewPager(mVpFunc);
        // 打印预结单
        if (mAllDiningTableEList != null && mAllDiningTableEList.size() > 0) {// 有并单
            DiningTableE mainDiningTableE = mAllDiningTableEList.get(0);
            mBtnPrint.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mainDiningTableE.getSalesOrderE().getConsumeTotal())) + " 结算");
        } else {// 无并单
            mBtnPrint.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mDiningTableE.getSalesOrderE().getConsumeTotal())) + " 结算");
        }
        RxView.clicks(mBtnPrint)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    DiningTableE mainDiningTableE;
                    if (mAllDiningTableEList != null && mAllDiningTableEList.size() > 0) {
                        mainDiningTableE = mAllDiningTableEList.get(0);
                    } else {
                        mainDiningTableE = mDiningTableE;
                    }
                    SalesOrderE mainSalesOrderE = mainDiningTableE.getSalesOrderE();
                    checkSalesOrderGUID = mainSalesOrderE.getSalesOrderGUID();
                    if (isDesignated) {
                        mPresenter.checkBill(checkSalesOrderGUID);
                    } else {
                        startBalanceAccountsActivity();
                    }
                });
        RxView.clicks(mBtnAdd)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    PermissionManager.checkPermission(PermissionManager.PERMISSION_OPEN_TABLE,
                            () -> startActivityForResult(OrderDishesActivity.newIntent(this, mDiningTableE, memberInfoE), REQUEST_CODE_ADD_DISHES));
                });
    }

    /**
     * 跳转到结算页面操作
     */
    private void startBalanceAccountsActivity() {
        startActivityForResult(BalanceAccountsActivity.newIntent(DishesOrderedActivity.this,
                checkSalesOrderGUID, 3), REQUEST_CODE_BALANCE_ACCOUNT);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        requestSalesOrderBatchE();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isReStartReflash) {
            requestSalesOrderBatchE();
            isReStartReflash = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isReStartReflash = true;
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onDispose() {
    }

    @Override
    public void onItemClick(int itemType) {
        Intent intent = null;
        switch (itemType) {
            case DishesOrderedPopup.ITEM_TYPE_DESIGNATED_DISHES:
                if (isDesignated) {
                    startActivityForResult(DesignatedDishesActivity.newIntent(DishesOrderedActivity.this, mDiningTableE.getDiningTableGUID(), mDiningTableE.getSalesOrderGUID(), mDiningTableE.getName()), REQUEST_CODE_DESIGNATE_DISHES);
                }
                break;
            case DishesOrderedPopup.ITEM_TYPE_CALL_UP_DISHES:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_CALL_UP,
                        () -> startActivity(CallUpDishesActivity.newIntent(this, mDiningTableE)));
                break;
            case DishesOrderedPopup.ITEM_TYPE_REMIND_DISHES:
                intent = RemindDishesActivity.newIntent(this, mDiningTableE);
                break;
            case DishesOrderedPopup.ITEM_TYPE_RETURN_DISHES:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_RETURN_DISHES,
                        () -> startActivity(RetreatDishesActivity.newIntent(this, mDiningTableE)));
                break;
            case DishesOrderedPopup.ITEM_TYPE_CHANGE_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_CHANGE_TABLE,
                        () -> startActivity(ChangeTableActivity.newIntent(this, mDiningTableE)));
                break;
            case DishesOrderedPopup.ITEM_TYPE_MERGE_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_MERGE_TABLE,
                        () -> {
                            if (mAllDiningTableEList != null && mAllDiningTableEList.size() > 0) {
                                startActivity(MergeTableActivity.newIntent(this, mAllDiningTableEList.get(0)));
                            } else {
                                startActivity(MergeTableActivity.newIntent(this, mDiningTableE));
                            }
                        });
                break;
            default:
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_ADD_DISHES:
                if (Activity.RESULT_OK == resultCode) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null && bundle.containsKey(Constants.EXTRAS_MEMBER_INFO)) {
                        memberInfoE = bundle.getParcelable(Constants.EXTRAS_MEMBER_INFO);
                    }
                }
                break;
            case REQUEST_CODE_BALANCE_ACCOUNT:
            case REQUEST_CODE_DESIGNATE_DISHES:
                if (Activity.RESULT_OK == resultCode) {
                    AppManager.getInstance().finishUntil(TableActivity.class);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /*************************view callback begin*************************/

    @Override
    public void onRequestArrayOfSalesOrderBatchSucceed(List<OrderedDishesRecordViewBean> recordList, List<OrderedDishesViewBean> activeList, boolean isDesignated, MemberInfoE memberInfoE) {
        this.recordList.clear();
        this.recordList.addAll(recordList);
        this.activeList.clear();
        this.activeList.addAll(activeList);
        this.memberInfoE = memberInfoE;
        this.isDesignated = isDesignated;
        if (this.recordList.size() == 0) {
            // 切换到空布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            // 切换到内容布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            // 刷新adapter
            mDishesOrderAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestArrayOfSalesOrderBatchFailed() {
        // do nothing
    }

    @SuppressLint("CheckResult")
    @Override
    public void onNetworkError() {
        // 切换到网络错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        // 注册点击事件
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            requestSalesOrderBatchE();
        });
    }

    @Override
    public void onRequestPrintPrepaymentSucceed(String msg) {
    }

    @Override
    public void getChickBillResult(boolean result) {
        if (result) {
            startBalanceAccountsActivity();
        } else {
            mDialogFactory.showConfirmDialog("还有菜品未上齐，确认结算吗？", "取消", "确认结算", new ConfirmDialogFragment.ConfirmDialogListener() {
                @Override
                public void onNegClick() {
                }

                @Override
                public void onPosClick() {
                    startBalanceAccountsActivity();
                }
            });
        }
    }

    /**************************view callback end**************************/
    /*************************private method begin*************************/

    /**
     * 请求批次数据
     */
    private void requestSalesOrderBatchE() {
        mPresenter.requestArrayOfSalesOrderBatchE(mDiningTableE.getSalesOrderE().getSalesOrderGUID(), mDiningTableE.getDiningTableGUID());
    }
    /**************************private method end**************************/

    /**************************inner class begin**************************/

    private class DishesOrderedAdapter extends FragmentStatePagerAdapter {

        public DishesOrderedAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            String title = mArrayOfTbTitles.get(position);
            switch (title) {
                case KEY_TITLE_DISHES_ORDERED:
                    fragment = DishesOrderedFragment.newInstance(activeList, isDesignated);
                    break;
                case KEY_TITLE_DISHES_RECORD:
                    fragment = DishesRecordFragment.newInstance(recordList);
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return mArrayOfTbTitles.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mArrayOfTbTitles.get(position);
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }
    /***************************inner class end***************************/
}