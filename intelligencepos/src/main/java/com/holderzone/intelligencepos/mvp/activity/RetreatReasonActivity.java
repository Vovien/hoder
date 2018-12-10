package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.RetreatDishesReasonContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.RetreatDishesReasonPresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-9-6.
 * 退菜原因
 */

public class RetreatReasonActivity extends BaseActivity<RetreatDishesReasonContract.Presenter> implements RetreatDishesReasonContract.View {
    @BindView(R.id.title)
    Title title;
    @BindView(R.id.remark_add_et)
    EditText mRemarkAddEt;
    @BindView(R.id.remark_rv)
    RecyclerView mRemarkRv;
    @BindView(R.id.remark_sure)
    Button mRemarkSure;
    public static final String ARGUMENT_EXTRA_TABLE_NAME = "ARGUMENT_EXTRA_TABLE_NAME";
    public static final String ARGUMENT_EXTRA_SALES_ORDER_GUID = "ARGUMENT_EXTRA_SALES_ORDER_GUID";
    public static final String ARGUMENT_EXTRA_DINTABLE_GUID = "ARGUMENT_EXTRA_DINTABLE_GUID";
    public static final String ARGUMENT_EXTRA_BATCH_DISHESE = "ARGUMENT_EXTRA_BATCH_DISHESE";
    @BindView(R.id.add_remark)
    Button mAddRemark;
    /**
     * 桌台名称
     */
    private String mTableName;
    /**
     * 已经选择的退菜原因实体集合
     */
    private List<DishesReturnReasonE> mSelectedRetreatList;
    /**
     * 全部的退菜原因集合
     */
    private List<DishesReturnReasonE> mAllRetreatReasonList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter<DishesReturnReasonE> mRetreatAdapter;
    /**
     * 记录全部备注的GUID与位置的对应关系
     */
    private HashMap<String, Integer> mAllRemarkMap = new HashMap<>();
    /**
     * 记录选中的备注GUID与是否选中的对应关系
     */
    private HashMap<String, Boolean> mSelectedRemaekMap = new HashMap<>();
    /**
     * 账单GUID
     */
    private String mSalesOrderGUID;
    /**
     * 桌台GUID
     */
    private String mDinTableGUID;
    /**
     * 账单
     */
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesE = new ArrayList<>();

    public static Intent newIntent(Context context, String tableName, String salesOrderGUID, String dinTableGUID, List<SalesOrderBatchDishesE> salesOrderBatchDishesE) {
        Intent intent = new Intent(context, RetreatReasonActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_EXTRA_TABLE_NAME, tableName);
        bundle.putString(ARGUMENT_EXTRA_SALES_ORDER_GUID, salesOrderGUID);
        bundle.putString(ARGUMENT_EXTRA_DINTABLE_GUID, dinTableGUID);
        bundle.putParcelableArrayList(ARGUMENT_EXTRA_BATCH_DISHESE, (ArrayList<SalesOrderBatchDishesE>) salesOrderBatchDishesE);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mTableName = extras.getString(ARGUMENT_EXTRA_TABLE_NAME);
        mSalesOrderGUID = extras.getString(ARGUMENT_EXTRA_SALES_ORDER_GUID);
        mDinTableGUID = extras.getString(ARGUMENT_EXTRA_DINTABLE_GUID);
        mSalesOrderBatchDishesE = extras.getParcelableArrayList(ARGUMENT_EXTRA_BATCH_DISHESE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_retreat_reason;
    }

    @Nullable
    @Override
    protected RetreatDishesReasonContract.Presenter initPresenter() {
        return new RetreatDishesReasonPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //初始化adapter
        initAdapter();
        //修正数据
        if (mSelectedRetreatList == null) {
            mSelectedRetreatList = new ArrayList<>();
        }
        //editText监听
        mRemarkAddEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (!RegexExUtils.isDishesRemarksInputLegal(editable)) {
                    editable.delete(length - 1, length);
                    return;
                }
                if (RegexExUtils.isDishesRemarks(editable)) {
                    mAddRemark.setEnabled(true);
                } else {
                    mAddRemark.setEnabled(false);
                }
            }
        });
    }

    private void initAdapter() {
        mRetreatAdapter = new CommonAdapter<DishesReturnReasonE>(this, R.layout.item_dishes_remark, mAllRetreatReasonList) {
            @Override
            protected void convert(ViewHolder holder, DishesReturnReasonE dishesRemarkE, int position) {
                Boolean isSelected = mSelectedRemaekMap.get(dishesRemarkE.getDishesReturnReasonGUID());
                holder.setBackgroundRes(R.id.tv_content_info, isSelected ? R.drawable.shape_flexbox_tv_bg_focused
                        : R.drawable.shape_flexbox_tv_bg);
                holder.setTextColorRes(R.id.tv_content_info, isSelected ? R.color.layout_stroke_focused
                        : R.color.layout_bg_disable);
                holder.setText(R.id.tv_content_info, dishesRemarkE.getName());
            }
        };
        mRetreatAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DishesReturnReasonE dishesReturnReasonE = mAllRetreatReasonList.get(position);
                String remarkGUID = dishesReturnReasonE.getDishesReturnReasonGUID();
                Boolean isSelected = mSelectedRemaekMap.get(remarkGUID);
                mSelectedRemaekMap.put(remarkGUID, isSelected ? false : true);
                mRetreatAdapter.notifyItemChanged(mAllRemarkMap.get(remarkGUID));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRemarkRv.setAdapter(mRetreatAdapter);

        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        mRemarkRv.setLayoutManager(layoutManager);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //请求退菜原因
        mPresenter.requestReturnRemark();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void getReturnReasonSuccess(List<DishesReturnReasonE> arrayOfDishesReturnReasonE) {
        mAllRetreatReasonList.clear();
        mAllRetreatReasonList.addAll(arrayOfDishesReturnReasonE);
        generateAllRemaekHashMap();
        generateSelectedRemaekHashMap();
        mRetreatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestRetreatReasonsFailed() {

    }

    @Override
    public void onAddNewRetreatReasonSucceed(String msg, DishesReturnReasonE dishesReturnReasonE) {
        //清空输入框
        mRemarkAddEt.setText("");
        //添加新增的实体
        mAllRetreatReasonList.add(0, dishesReturnReasonE);
        //从新构造全部备注与position的对应关系
        generateAllRemaekHashMap();
        //更新选中Map
        mSelectedRemaekMap.put(dishesReturnReasonE.getDishesReturnReasonGUID(), true);
        //刷新adapter
        mRetreatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNewRetreatReasonFailed() {

    }

    @Override
    public void onRetreatDishesSucceed() {
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    @Override
    public void onRetreatDishesFailed() {

    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.add_remark, R.id.remark_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_remark:
                mPresenter.addNewRetreatReason(mRemarkAddEt.getText().toString());
                break;
            case R.id.remark_sure:
                //回传数据
                List<DishesReturnReasonE> selectedDataList = new ArrayList<>();
                for (DishesReturnReasonE dishesRemarkE : mAllRetreatReasonList) {
                    if (mSelectedRemaekMap.get(dishesRemarkE.getDishesReturnReasonGUID())) {
                        selectedDataList.add(dishesRemarkE);
                    }
                }
                mPresenter.submitRetreatDishes(mSalesOrderGUID, mDinTableGUID, mSalesOrderBatchDishesE, selectedDataList);
                break;
            default:
                break;
        }
    }

    private void initTitle() {
        title.setTitleText(mTableName + "退菜");
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
    }

    /**
     * 初始化全部备注GUID与位置的关系Map
     */
    private void generateAllRemaekHashMap() {
        mAllRemarkMap.clear();
        for (int i = 0; i < mAllRetreatReasonList.size(); i++) {
            DishesReturnReasonE dishesReturnReasonE = mAllRetreatReasonList.get(i);
            mAllRemarkMap.put(dishesReturnReasonE.getDishesReturnReasonGUID(), i);
        }
    }

    /**
     * 初始化选中备注GUID与是否选中
     */
    private void generateSelectedRemaekHashMap() {
        mSelectedRemaekMap.clear();
        for (DishesReturnReasonE DishesReturnReasonE : mAllRetreatReasonList) {
            mSelectedRemaekMap.put(DishesReturnReasonE.getDishesReturnReasonGUID(), false);
        }
        for (DishesReturnReasonE DishesReturnReasonE : mSelectedRetreatList) {
            mSelectedRemaekMap.put(DishesReturnReasonE.getDishesReturnReasonGUID(), true);
        }
    }
}
