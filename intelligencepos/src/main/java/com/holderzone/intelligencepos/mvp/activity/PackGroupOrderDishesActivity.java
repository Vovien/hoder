package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.donkingliang.groupedadapter.widget.StickyHeaderLayout;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.dialog.impl.ChangeOrderDishesCountDialogFragment;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroup;
import com.holderzone.intelligencepos.mvp.model.bean.PackageGroupDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.PackageGroupOrderDishesAdapter;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2018-04-18.
 * 套餐页面
 */

public class PackGroupOrderDishesActivity extends BaseActivity implements ChangeOrderDishesCountDialogFragment.ChangeOrderDishesCountListener {
    private static final String ARGS_DISHES_NAME = "ARGS_DISHES_NAME";
    private static final String ARGS_PACKAGE_GROUP = "ARGS_PACKAGE_GROUP";
    private static final String ARGS_AMOUNT = "ARGS_AMOUNT";
    private static final String ARGS_ESTIMATE_COUNT = "ARGS_ESTIMATE_COUNT";
    private static final String ARGS_DISHES_GUID = "ARGS_DISHES_GUID";
    private static final String INTENT_ORDER_COUNT = "INTENT_ORDER_COUNT";
    private static final String INTENT_PACKAGE_GROUP = "INTENT_PACKAGE_GROUP";
    private static final String INTENT_DISHES_GUID = "INTENT_DISHES_GUID";
    private static final String INTENT_RISE_GUID = "INTENT_RISE_GUID";
    private static final String TYPE_PACKAGE = "TYPE_PACKAGE";
    private static final String SHOW_TYPE_COMMON = "SHOW_TYPE_COMMON";
    @BindView(R.id.dishes_name)
    TextView mDishesNameTv;
    @BindView(R.id.package_group_RV)
    RecyclerView mPackageGroupRV;
    @BindView(R.id.dishes_number)
    TextView mDishesNumberTv;
    @BindView(R.id.confirm_btn)
    Button mConfirmBtn;
    @BindView(R.id.sticky_layout)
    StickyHeaderLayout stickyLayout;
    @BindView(R.id.ll_add_reduce_dishes)
    LinearLayout llAddOrReduceDishes;
    @BindView(R.id.rl_order_dishes)
    RelativeLayout rlOrderDishes;
    @BindView(R.id.tv_order_dishes)
    TextView tvOrderDishes;
    /**
     * 套餐名称
     */
    private String mDishesName;
    /**
     * adapter
     */
    private PackageGroupOrderDishesAdapter mAdapter;
    /**
     * 列表数据
     */
    private List<PackageGroup> mPackageGroupList = new ArrayList<>();
    /**
     * 套餐价格
     */
    private double mAmount;
    /**
     * 当前点击的groupPosition
     */
    private int mGroupPosition;
    /**
     * 当前点击的childPosition
     */
    private int mChildPosition;
    /**
     * 能点的菜品数量 估清数量
     */
    private double mEstimateCount;
    /**
     * 当前下单的菜品数量 套餐数量并不是子菜
     */
    private double mDishesCount;
    /**
     * 套餐GUID
     */
    private String mDishesGUID;
    /**
     * 标记点击的位置
     */
    private boolean isClickGroupDishes;

    public static Intent newIntent(String name, String guid, double amount, double estimateCount, List<PackageGroup> groupList, Context context) {
        Intent intent = new Intent(context, PackGroupOrderDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGS_DISHES_NAME, name);
        bundle.putString(ARGS_DISHES_GUID, guid);
        bundle.putDouble(ARGS_AMOUNT, amount);
        bundle.putDouble(ARGS_ESTIMATE_COUNT, estimateCount);
        bundle.putParcelableArrayList(ARGS_PACKAGE_GROUP, (ArrayList<? extends Parcelable>) groupList);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDishesName = extras.getString(ARGS_DISHES_NAME);
        mDishesGUID = extras.getString(ARGS_DISHES_GUID);
        mPackageGroupList = extras.getParcelableArrayList(ARGS_PACKAGE_GROUP);
        mAmount = extras.getDouble(ARGS_AMOUNT);
        mEstimateCount = extras.getDouble(ARGS_ESTIMATE_COUNT);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_package_group;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        Window window = getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        wlp.height = getResources().getDimensionPixelSize(R.dimen.package_group_height);
        window.setAttributes(wlp);
        //套餐名称
        mDishesNameTv.setText(mDishesName);
        //套餐默认价格
        mConfirmBtn.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mAmount)));
        //设置套餐默认数量为0
        mDishesCount = 1;
        setShowCount();
        //adapter
        mAdapter = new PackageGroupOrderDishesAdapter(this, mPackageGroupList);
        //设置头部悬浮
        stickyLayout.setSticky(true);
        //mAdapter的点击事件
        mAdapter.setItemClickListener(new PackageGroupOrderDishesAdapter.ItemViewClickListener() {
            @Override
            public void onAddDishesClick(int groupPosition, int childPosition, int count, double groupSelectedCount) {
                //更新数据源
                mPackageGroupList.get(groupPosition).getArrayOfPackageGroupDishesE().get(childPosition).setPackageDishesOrderCount(count);
                mPackageGroupList.get(groupPosition).setSelectedCount(groupSelectedCount);
                isButtonCanClick();
                mAdapter.changeDataSet();
            }

            @Override
            public void onReduceDishesClick(int groupPosition, int childPosition, int count, double groupSelectedCount) {
                //更新数据源
                mPackageGroupList.get(groupPosition).getArrayOfPackageGroupDishesE().get(childPosition).setPackageDishesOrderCount(count);
                mPackageGroupList.get(groupPosition).setSelectedCount(groupSelectedCount);
                isButtonCanClick();
                mAdapter.changeDataSet();
            }

            @Override
            public void onChangeCountClick(int groupPosition, int childPosition, int count, double canSelectedCount) {
                mGroupPosition = groupPosition;
                mChildPosition = childPosition;
                isClickGroupDishes = true;
                getDialogFactory().showChangeOrderDishesCountDialog(new SalesOrderBatchDishesE(), count, canSelectedCount, TYPE_PACKAGE
                        , SHOW_TYPE_COMMON, PackGroupOrderDishesActivity.this);
            }
        });
        //设置RV
        mPackageGroupRV.setLayoutManager(new LinearLayoutManager(this));
        mPackageGroupRV.setAdapter(mAdapter);

        RxView.clicks(tvOrderDishes).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            addDishes();
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDishesCountChanged(SalesOrderBatchDishesE salesOrderBatchDishesE, double count) {
        if (isClickGroupDishes) {
            mPackageGroupList.get(mGroupPosition).getArrayOfPackageGroupDishesE().get(mChildPosition).setPackageDishesOrderCount((int) count);
            //计算已经点了的菜品数量
            double orderCount = 0;
            for (PackageGroupDishesE dishesE : mPackageGroupList.get(mGroupPosition).getArrayOfPackageGroupDishesE()) {
                if (dishesE.getPackageDishesOrderCount() > 0) {
                    orderCount = ArithUtil.add(orderCount, dishesE.getPackageDishesOrderCount());
                }
            }
            //设置新数据
            mPackageGroupList.get(mGroupPosition).setSelectedCount(orderCount);
            //重置底部按钮数据
            isButtonCanClick();
            //刷新界面
            mAdapter.changeDataSet();
        } else {
            mDishesCount = count;
            setShowCount();
            isButtonCanClick();
        }
    }

    @Override
    public void onPracticeCountChanged(double count) {

    }

    @OnClick({R.id.dishes_dialog_image, R.id.reduce_dishes_image, R.id.add_dishes_image, R.id.confirm_btn, R.id.dishes_number})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dishes_dialog_image:
                finishActivity();
                break;
            case R.id.dishes_number:
                isClickGroupDishes = false;
                getDialogFactory().showChangeOrderDishesCountDialog(new SalesOrderBatchDishesE(), mDishesCount, mEstimateCount, TYPE_PACKAGE
                        , SHOW_TYPE_COMMON, PackGroupOrderDishesActivity.this);
                break;
            case R.id.reduce_dishes_image:
                reduceDishes();
                break;
            case R.id.add_dishes_image:
                addDishes();
                break;
            case R.id.confirm_btn:
                //回传数据
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putString(INTENT_DISHES_GUID, mDishesGUID);
                bundle.putDouble(INTENT_ORDER_COUNT, mDishesCount);
                bundle.putDouble(INTENT_RISE_GUID, getPackageRisePrice());
                //已经点的子菜数据
                List<PackageGroupDishesE> dishesEList = new ArrayList<>();
                for (PackageGroup packageGroup : mPackageGroupList) {
                    for (PackageGroupDishesE packageGroupDishesE : packageGroup.getArrayOfPackageGroupDishesE()) {
                        if (packageGroupDishesE.getPackageDishesOrderCount() > 0) {
                            dishesEList.add(packageGroupDishesE);
                        }
                    }
                }
                bundle.putParcelableArrayList(INTENT_PACKAGE_GROUP, (ArrayList<? extends Parcelable>) dishesEList);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
        }
    }

    private void reduceDishes() {
        //减菜
        if (mDishesCount <= 0) return;
        if (mDishesCount > 0 && mDishesCount < 1) {
            mDishesCount = 0;
            setShowCount();
            isButtonCanClick();
            return;
        }
        mDishesCount -= 1;
        setShowCount();
        isButtonCanClick();
    }

    private void addDishes() {
        //加菜
        if (ArithUtil.sub(mEstimateCount, mDishesCount) <= 0) {
            BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate));
            return;
        }
        if (ArithUtil.sub(mEstimateCount, mDishesCount) > 0 && ArithUtil.sub(mEstimateCount, mDishesCount) < 1) {
            //菜品不足1
            BaseApplication.showMessage(getString(R.string.add_dishes_failed_by_estimate));
            return;
        }
        mDishesCount += 1;
        setShowCount();
        isButtonCanClick();
    }

    /**
     * 计算套餐浮动价格
     */
    private double getPackageRisePrice() {
        double price = 0;
        for (PackageGroup packageGroup : mPackageGroupList) {
            for (PackageGroupDishesE dishesE : packageGroup.getArrayOfPackageGroupDishesE()) {
                if (dishesE.getPackageDishesOrderCount() > 0) {
                    //此菜品有点菜
                    if (dishesE.getRiseAmount() > 0) {
                        price = ArithUtil.add(price, ArithUtil.mul(dishesE.getRiseAmount(), dishesE.getPackageDishesOrderCount()));
                    }
                }
            }
        }
        return price;
    }

    /**
     * 判断确定按钮是否可以点击
     */
    private void isButtonCanClick() {
        boolean isCanClick = true;
        //如果子套餐没选完  不能点击
        for (PackageGroup packageGroup : mPackageGroupList) {
            if (ArithUtil.sub(packageGroup.getOptionalCount(), packageGroup.getSelectedCount()) != 0) {
                isCanClick = false;
            }
        }
        //如果 套餐数量为0 不能点击
        if (mDishesCount <= 0) isCanClick = false;
        //自选套餐价格 = （套餐单价+浮动价）x 套餐数量
        double totalAmount = ArithUtil.mul(mDishesCount, ArithUtil.add(mAmount, getPackageRisePrice()));
        mConfirmBtn.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(totalAmount)));
        mConfirmBtn.setEnabled(isCanClick);
    }

    /**
     * 数量显示设置
     */
    private void setShowCount() {
        if (mDishesCount <= 0) {
            rlOrderDishes.setVisibility(View.VISIBLE);
            llAddOrReduceDishes.setVisibility(View.GONE);
        } else {
            rlOrderDishes.setVisibility(View.GONE);
            llAddOrReduceDishes.setVisibility(View.VISIBLE);
            SpanUtils count = new SpanUtils();
            count.append(ArithUtil.stripTrailingZeros(mDishesCount)).setUnderline();
            mDishesNumberTv.setText(count.create());
        }
    }
}
