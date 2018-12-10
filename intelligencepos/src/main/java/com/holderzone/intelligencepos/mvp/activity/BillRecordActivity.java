package com.holderzone.intelligencepos.mvp.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.impl.BillRecordAdapter;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BillRecordContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.presenter.BillRecordPresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;
import com.wuxiaolong.pullloadmorerecyclerview.PullLoadMoreRecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账单记录页面
 * Created by chencao on 2017/6/3.
 */

public class BillRecordActivity extends BaseActivity<BillRecordContract.Presenter> implements BillRecordContract.View, BillRecordAdapter.OnItemClickListener {

    @BindView(R.id.back_image)
    ImageView billTitleBackImage;
    @BindView(R.id.title_text)
    TextView billTitleText;
    @BindView(R.id.title_more_image)
    ImageView billTitleChoiceImage;
    @BindView(R.id.title_refresh)
    TextView refresh;
    @BindView(R.id.bill_recode_pull_recycler)
    PullLoadMoreRecyclerView billRecodePullRecycler;
    @BindView(R.id.bill_recode_multistateview)
    MultiStateView billRecodeMultistateview;
    @BindView(R.id.bill_recode_bill_number)
    TextView billNumberTextView;

    private BillRecordAdapter adapter;
    /**
     * 分页加载页数
     */
    private int page = 1;
    /**
     * 营业日 日期
     */
    private String businessDay;

    private int showProgress = 1;

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        showProgress = extras.getInt("showProgress");
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    /**
     * 静态方法，对外暴露该activity需要的参数
     *
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        return new Intent(context, BillRecordActivity.class);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_bill_record;
    }

    @Override
    protected BillRecordContract.Presenter initPresenter() {
        return new BillRecordPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        refresh.setVisibility(View.VISIBLE);
        billTitleChoiceImage.setVisibility(View.GONE);
        billRecodeMultistateview.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        billRecodePullRecycler.setLinearLayout();
        adapter = new BillRecordAdapter(this);
        adapter.setOnClickItem(this);
        billRecodePullRecycler.setAdapter(adapter);
        billRecodePullRecycler.addItemDecoration(new SolidLineItemDecoration(this, LinearLayoutManager.VERTICAL));
        billRecodePullRecycler.setOnPullLoadMoreListener(new PullLoadMoreRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                page = 1;
                setRequestBillList();
            }

            @Override
            public void onLoadMore() {
                page++;
                setRequestBillList();
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        RxView.clicks(refresh).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            page = 1;
            setRequestBillList();
        });
        if (showProgress == 1) {
            showProgress++;
        }
        // 请求本地营业日
        mPresenter.requestLocalAccountRecord();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    /**
     * 请求账单列表信息
     */
    private void setRequestBillList() {
        mPresenter.setRequestBillRecodeList(page);
    }

    @Override
    public void onRequestLocalAccountRecordSucceed(AccountRecord accountRecord) {
        businessDay = accountRecord.getBusinessDay();
        setRequestBillList();
    }

    @Override
    public void getBillRecodeList(List<SalesOrderE> arrayOfSalesOrderE, int pageRowCount) {
        billRecodePullRecycler.setPullLoadMoreCompleted();
        billNumberTextView.setText("营业日：" + businessDay + "（" + pageRowCount + "单）");
        if (page == 1) {
            if (arrayOfSalesOrderE != null && arrayOfSalesOrderE.size() != 0) {
                billRecodeMultistateview.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                adapter.setDatas(arrayOfSalesOrderE);
            } else {
                billRecodeMultistateview.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        } else {
            billRecodeMultistateview.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            adapter.addDatas(arrayOfSalesOrderE);
        }
    }

    @Override
    public void showNetworkError() {
        billRecodePullRecycler.setPullLoadMoreCompleted();
        billRecodeMultistateview.setViewState(MultiStateView.VIEW_STATE_ERROR);
        View reflash = billRecodeMultistateview.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(reflash).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            page = 1;
            if (adapter != null) {
                adapter.setDatas(null);
            }
            setRequestBillList();
        });
    }

    @Override
    public void getError(String message) {
        showMessage(message);
    }

    @Override
    public void onDispose() {

    }

    @Override
    public void getOnClickItemListener(int position, SalesOrderE salesOrderE) {
        Intent intent = new Intent(this, BillDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("salesOrderE", salesOrderE);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick({R.id.back_image, R.id.title_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image:
            case R.id.title_text:
                finish();
                break;
            default:
                break;
        }
    }
}
