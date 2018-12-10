package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.BalanceAccountsActivity;
import com.holderzone.intelligencepos.mvp.contract.SnackOrderContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.SnackOrderPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by LiTao on 2017-8-3.
 * 快销待结账页面
 */

public class SnackOrderFragment extends BaseFragment<SnackOrderContract.Presenter> implements SnackOrderContract.View {

    @BindView(R.id.snack_order_rv)
    RecyclerView snackOrderRv;
    @BindView(R.id.content)
    MultiStateView content;

    public static SnackOrderFragment getInstance() {
        return new SnackOrderFragment();
    }

    /**
     * 账单实体集合
     */
    private List<SalesOrderE> mSalesOrderList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter<SalesOrderE> mSalesOrderAdapter;

    public void setOnDataRefreshed() {
        //请求账单数据
        mPresenter.getListNotCheckOut();
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_snack_order;
    }

    @Override
    protected SnackOrderContract.Presenter initPresenter() {
        return new SnackOrderPresenter(this);
    }


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // //重新加载点击事件
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.getListNotCheckOut();
            }
        });
        //初始化adapter
        mSalesOrderAdapter = new CommonAdapter<SalesOrderE>(getActivity(), R.layout.item_snack_order, mSalesOrderList) {

            @Override
            protected void convert(ViewHolder holder, SalesOrderE salesOrderE, int position) {
                //隐藏最后一个item的虚线
                if (position == mSalesOrderList.size() - 1) {
                    holder.setVisible(R.id.itemDivider, false);
                } else {
                    holder.setVisible(R.id.itemDivider, true);
                }
                //号牌
                holder.setText(R.id.snack_order_item_number, getString(R.string.snack_order_number));
                holder.setAppendText(R.id.snack_order_item_number, salesOrderE.getOrgNumber() == null ? "- -" : salesOrderE.getOrgNumber()
                        , R.color.bg_dishes_type_item_normal);
                //下单时间
                holder.setText(R.id.snack_order_item_time, getString(R.string.snack_order_time) + salesOrderE.getCreateTime());
                //交易流水号
                holder.setText(R.id.snack_order_item_serial_number, getString(R.string.serial_number, salesOrderE.getSerialNumber()));
                //金额
                holder.setText(R.id.snack_order_item_money, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(salesOrderE.getCheckTotal())));
            }
        };
        mSalesOrderAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                //跳转结账页面
                String salesOrderGUID = mSalesOrderList.get(position).getSalesOrderGUID();
                launchActivity(BalanceAccountsActivity.newIntent(getActivity(), salesOrderGUID, 2));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        snackOrderRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        snackOrderRv.setAdapter(mSalesOrderAdapter);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        //请求账单数据
        mPresenter.getListNotCheckOut();
    }

    @Override
    public void onGetOrderListSuccess(List<SalesOrderE> list) {
        content.setVisibility(View.VISIBLE);
        if (list.size() == 0) {
            //无数据
            content.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            //有账单数据
            content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            mSalesOrderList.clear();
            mSalesOrderList.addAll(list);
            mSalesOrderAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onGetOrderListFiled() {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }
}
