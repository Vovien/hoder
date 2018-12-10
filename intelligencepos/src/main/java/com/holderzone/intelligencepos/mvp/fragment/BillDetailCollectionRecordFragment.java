package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.BillDetailCollectionAdapter;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 账单详情-收款记录fragment
 * Created by chencao on 2017/6/5.
 */

public class BillDetailCollectionRecordFragment extends BaseFragment {

    @BindView(R.id.bill_detail_recode_recycler)
    RecyclerView billDetailRecodeRecycler;
    @BindView(R.id.bill_detail_multistate_view)
    MultiStateView multiStateView;
    private BillDetailCollectionAdapter adapter;

    private static String SALSE_ORDERE = "SALSE_ORDERE";
    private List<SalesOrderE> salesOrderEList;
    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        salesOrderEList = extras.getParcelableArrayList(SALSE_ORDERE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }


    public static BillDetailCollectionRecordFragment getInstanse(List<SalesOrderE> salesOrderEList) {
        BillDetailCollectionRecordFragment fragment = new BillDetailCollectionRecordFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(SALSE_ORDERE, (ArrayList<? extends Parcelable>) salesOrderEList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_bill_detail_center_collection_record;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        adapter = new BillDetailCollectionAdapter(getContext());
        billDetailRecodeRecycler.addItemDecoration(new SolidLineItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        billDetailRecodeRecycler.setAdapter(adapter);
        billDetailRecodeRecycler.setLayoutManager(new LinearLayoutManager(mBaseActivity));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        setShowState(salesOrderEList);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    public void setShowState(List<SalesOrderE> salesOrderEList) {
        adapter.setDatas(salesOrderEList);
        if (adapter.getLength() == 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
    }

    @Override
    public void onDispose() {

    }
}
