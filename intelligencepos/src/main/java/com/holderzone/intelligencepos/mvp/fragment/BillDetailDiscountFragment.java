package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.BillDetailDiecountAdapter;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 账单详情-折扣明细fragment
 * Created by chencao on 2017/6/5.
 */

public class BillDetailDiscountFragment extends BaseFragment {

    @BindView(R.id.bill_detail_recode_recycler)
    RecyclerView billDetailRecodeRecycler;
    @BindView(R.id.bill_detail_multistate_view)
    MultiStateView multiStateView;
    private BillDetailDiecountAdapter adapter;
    private static String SALSE_ORDERE = "SALSE_ORDERE";
    private List<SalesOrderE> salesOrderEList;

    public static BillDetailDiscountFragment getInstanse(List<SalesOrderE> salesOrderEList) {
        BillDetailDiscountFragment fragment = new BillDetailDiscountFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SALSE_ORDERE, (ArrayList<? extends Parcelable>) salesOrderEList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        salesOrderEList = extras.getParcelableArrayList(SALSE_ORDERE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_bill_detail_center_discount;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        adapter = new BillDetailDiecountAdapter(getContext());
        billDetailRecodeRecycler.addItemDecoration(new SolidLineItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        billDetailRecodeRecycler.setAdapter(adapter);
        billDetailRecodeRecycler.setLayoutManager(new LinearLayoutManager(mBaseActivity));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (salesOrderEList == null){
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            return;
        }else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        adapter.setDatas(salesOrderEList);
        if (adapter.getLength() == 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }
}
