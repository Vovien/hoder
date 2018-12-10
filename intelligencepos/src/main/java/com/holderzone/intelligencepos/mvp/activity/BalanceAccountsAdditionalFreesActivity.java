package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.base.RecycleHolder;
import com.holderzone.intelligencepos.adapter.base.RecyclerAdapter;
import com.holderzone.intelligencepos.adapter.decoration.DashLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsAdditionalFeesContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderAdditionalFeesE;
import com.holderzone.intelligencepos.mvp.model.bean.TableAdditionalFree;
import com.holderzone.intelligencepos.mvp.model.bean.db.AdditionalFees;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsAdditionalFeesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.CollectionUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算-附加费 界面
 * Created by zhaoping on 2017/6/2.
 */

public class BalanceAccountsAdditionalFreesActivity extends BaseActivity<BalanceAccountsAdditionalFeesContract.Presenter> implements BalanceAccountsAdditionalFeesContract.View {

    public static final String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";
    public static final String EXTRAS_TABLE_ADDITIONAL_FREES = "EXTRAS_TABLE_ADDITIONAL_FREES";

    @BindView(R.id.surchargeRecyclerView)
    RecyclerView surchargeRecyclerView;
    @BindView(R.id.tableRecyclerView)
    RecyclerView tableRecyclerView;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.content)
    MultiStateView content;
    @BindView(R.id.title)
    Title title;
    RecyclerAdapter<AdditionalFees> recyclerAdapter;
    private String mSalesOrderGuid = null;
    private List<AdditionalFees> mData = new java.util.ArrayList<>();
    private List<AdditionalFees> mAdditionalFeesEList = null;
    /**
     * 有并桌时 并桌的桌台adapter
     */
    private RecyclerAdapter<TableAdditionalFree> tableRecyclerAdapter;
    /**
     * 并桌桌台数据集合
     */
    private List<TableAdditionalFree> mTableAdditionalFeesE = new ArrayList<>();
    /**
     * 标记当前页面显示的是桌台的view 还是附加费的view
     */
    private boolean canFinishFragment = true;
    /**
     * 如果有并单  标记当前点击的是哪一个桌台
     */
    private int mCurrentIndex = -1;

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID);
        mTableAdditionalFeesE = extras.getParcelableArrayList(EXTRAS_TABLE_ADDITIONAL_FREES);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    public static Intent newIntent(Context context, String salesOrderGuid, List<TableAdditionalFree> tableAdditionalFree) {
        Intent intent = new Intent(context, BalanceAccountsAdditionalFreesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, salesOrderGuid);
        bundle.putParcelableArrayList(EXTRAS_TABLE_ADDITIONAL_FREES, (ArrayList<? extends Parcelable>) tableAdditionalFree);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts_additional_frees;
    }

    @OnClick({R.id.confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.confirm:
                List<SalesOrderAdditionalFeesE> list = new java.util.ArrayList<>();
                for (AdditionalFees bean : mData) {
                    if (bean.getCount() > 0) {
                        SalesOrderAdditionalFeesE salesOrderAdditionalFeesE = new SalesOrderAdditionalFeesE();
                        salesOrderAdditionalFeesE.setAdditionalFeesGUID(bean.getAdditionalFeesGUID());
                        salesOrderAdditionalFeesE.setAdditionalFeesCount(bean.getCount());
                        list.add(salesOrderAdditionalFeesE);
                    }
                }
                mPresenter.setFees(mSalesOrderGuid, list);
                break;
            default:
                break;
        }
    }

    @Override
    protected BalanceAccountsAdditionalFeesContract.Presenter initPresenter() {
        return new BalanceAccountsAdditionalFeesPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        if (mTableAdditionalFeesE.size() == 0) {
            //如果传递过来的桌台数据集合为空
            surchargeRecyclerView.setVisibility(View.VISIBLE);
        } else {
            title.setTitleText("选择桌台");
            tableRecyclerView.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.GONE);
        }
        title.setOnReturnClickListener(() -> {
            if (canFinishFragment) {
                setResult(RESULT_OK);
                finishActivity();
            } else {
                canFinishFragment = true;
                tableRecyclerView.setVisibility(View.VISIBLE);
                surchargeRecyclerView.setVisibility(View.GONE);
                confirm.setVisibility(View.GONE);
                title.setTitleText("选择桌台");
            }
        });

        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(v -> mPresenter.getOrderAdditionalFeesList(mSalesOrderGuid));
        recyclerAdapter = new RecyclerAdapter<AdditionalFees>(this, mData, R.layout.item_additional_frees) {
            @Override
            public void convert(RecycleHolder holder, final AdditionalFees data, int position) {
                holder.setText(R.id.additionalChargeName, data.getName());
                holder.setEnabled(R.id.minus, data.getCount() != 0);
                holder.setText(R.id.count, data.getCount() + "");
                holder.setTextColor(R.id.count, data.getCount() != 0 ? R.color.additional_frees_count_select : R.color.additional_frees_count_zero);
                holder.setText(R.id.additionalChargePrice, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(data.getAmount())));
                holder.setOnClickListener(R.id.add, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        data.setCount(data.getCount() + 1);
                        notifyDataChanged();
                    }
                });
                holder.setOnClickListener(R.id.minus, (View.OnClickListener) view -> {
                    data.setCount(data.getCount() - 1);
                    notifyDataChanged();
                });
            }
        };
        surchargeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        surchargeRecyclerView.addItemDecoration(new DashLineItemDecoration(this, DashLineItemDecoration.VERTICAL_LIST));
        surchargeRecyclerView.setAdapter(recyclerAdapter);

        tableRecyclerAdapter = new RecyclerAdapter<TableAdditionalFree>(this, mTableAdditionalFeesE, R.layout.item_additional_frees_table) {
            @Override
            public void convert(RecycleHolder holder, TableAdditionalFree data, int position) {
                holder.setText(R.id.tableName, data.getTableName());
                holder.setText(R.id.price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(data.getTotal())));
            }
        };
        tableRecyclerAdapter.setOnItemClickListener((view, position) -> {
            mCurrentIndex = position;
            getTableAdditionalFeesList(mTableAdditionalFeesE.get(position));
        });
        //桌台
        tableRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tableRecyclerView.addItemDecoration(new DashLineItemDecoration(this, DashLineItemDecoration.VERTICAL_LIST));
        tableRecyclerView.setAdapter(tableRecyclerAdapter);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (mTableAdditionalFeesE.size() == 0) {
            mPresenter.getAdditionalFeesList();
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    private void notifyDataChanged() {
        recyclerAdapter.notifyDataSetChanged();
        Double price = 0.0;
        for (AdditionalFees bean : mData) {
            if (bean.getCount() > 0) {
                price = ArithUtil.add(price, ArithUtil.mul(bean.getCount(), bean.getAmount()));
            }
        }
        if (mCurrentIndex != -1) {
            mTableAdditionalFeesE.get(mCurrentIndex).setTotal(price);
        }
        confirm.setText(getString(R.string.additional_frees_confirm, ArithUtil.stripTrailingZeros(price)));
    }

    @Override
    public void onGetAdditionalFeesSuccess(List<AdditionalFees> list) {
        if (list.size() == 0) {
            content.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }
        for (AdditionalFees bean : list) {
            bean.setCount(0);
        }
        mAdditionalFeesEList = list;
        mPresenter.getOrderAdditionalFeesList(mSalesOrderGuid);
    }

    @Override
    public void onGetOrderAdditionalFeesSuccess(List<SalesOrderAdditionalFeesE> list) {
        if (!CollectionUtils.isEmpty(mAdditionalFeesEList)) {
            if (!CollectionUtils.isEmpty(list)) {
                Map<String, Integer> map = new HashMap<>();
                for (SalesOrderAdditionalFeesE salesOrderAdditionalFeesE : list) {
                    map.put(salesOrderAdditionalFeesE.getAdditionalFeesGUID().toLowerCase(), salesOrderAdditionalFeesE.getAdditionalFeesCount());
                }
                for (AdditionalFees bean : mAdditionalFeesEList) {
                    if (map.containsKey(bean.getAdditionalFeesGUID().toLowerCase())) {
                        bean.setCount(map.get(bean.getAdditionalFeesGUID().toLowerCase()));
                    }
                }
            }
            mData.clear();
            mData.addAll(mAdditionalFeesEList);
            notifyDataChanged();
        }
    }

    @Override
    public void onSetFeesSuccess() {
        if (canFinishFragment) {
            setResult(RESULT_OK);
            finishActivity();
        } else {
            tableRecyclerAdapter.notifyDataSetChanged();
            canFinishFragment = true;
            tableRecyclerView.setVisibility(View.VISIBLE);
            surchargeRecyclerView.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            title.setTitleText("选择桌台");
        }
    }

    @Override
    public void showNetworkErrorLayout() {
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onBackPressed() {
        if (canFinishFragment) {
            setResult(RESULT_OK);
            finishActivity();
        } else {
            tableRecyclerAdapter.notifyDataSetChanged();
            canFinishFragment = true;
            tableRecyclerView.setVisibility(View.VISIBLE);
            surchargeRecyclerView.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);
            title.setTitleText("选择桌台");
        }
    }

    /**
     * 并单情况下获取订单附加费
     */
    private void getTableAdditionalFeesList(TableAdditionalFree bean) {
        canFinishFragment = false;
        surchargeRecyclerView.setVisibility(View.VISIBLE);
        tableRecyclerView.setVisibility(View.GONE);
        title.setTitleText(bean.getTableName());
        confirm.setVisibility(View.VISIBLE);
        mSalesOrderGuid = bean.getSalesOrderGuid();
        mPresenter.getAdditionalFeesList();
    }
}
