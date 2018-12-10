package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.contract.DiscountDetailsContract;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDiscountE;
import com.holderzone.intelligencepos.mvp.presenter.DiscountDetailsPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 折扣明细
 * Created by chencao on 2018/2/2.
 */

public class DiscountDetailsFragment extends BaseFragment<DiscountDetailsContract.Presenter> implements DiscountDetailsContract.View {
    private final static String KEY_DISCOUNT_ARRAY_LIST = "KEY_DISCOUNT_ARRAY_LIST";
    @BindView(R.id.rv_discount_list)
    RecyclerView mRvDiscountList;
    @BindView(R.id.msv_context)
    MultiStateView mMsvContext;
    @BindView(R.id.btn_retry)
    Button mBtnRetry;
    @BindView(R.id.tv_empty)
    TextView mTvEmpty;
    @BindView(R.id.iv_empty)
    ImageView mIvEmpty;
    private List<SalesOrderDiscountE> salesOrderDiscountES;
    private CommonAdapter<SalesOrderDiscountE> mCommonAdapter;

    public static DiscountDetailsFragment getInstance(List<SalesOrderDiscountE> salesOrderDiscountES) {
        DiscountDetailsFragment fragment = new DiscountDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_DISCOUNT_ARRAY_LIST, (ArrayList<? extends Parcelable>) salesOrderDiscountES);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        salesOrderDiscountES = extras.getParcelableArrayList(KEY_DISCOUNT_ARRAY_LIST);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_discount_details;
    }

    @Override
    protected DiscountDetailsContract.Presenter initPresenter() {
        return new DiscountDetailsPresenter(this);
    }


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (salesOrderDiscountES.size() == 0) {
            mMsvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMsvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        mIvEmpty.setVisibility(View.GONE);
        mTvEmpty.setText("暂无折扣");
        mCommonAdapter = new CommonAdapter<SalesOrderDiscountE>(getContext(), R.layout.discount_item_layout, salesOrderDiscountES) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderDiscountE salesOrderDiscountE, int position) {
                holder.setVisible(R.id.rl_center, true);
                String name = "";
                String countStr = "";
                if (salesOrderDiscountE.getDiscountAmount() > 0) {
                    name = salesOrderDiscountE.getDiscountItem();
                    countStr = getString(R.string.minus_amount_str, ArithUtil.stripTrailingZeros(salesOrderDiscountE.getDiscountAmount()));
                }
                holder.setText(R.id.tv_discount_name, name);
                holder.setText(R.id.tv_discount_number, countStr);
            }
        };

        mRvDiscountList.setAdapter(mCommonAdapter);
        mRvDiscountList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }
}
