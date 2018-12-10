package com.holderzone.intelligencepos.mvp.fragment;

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
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.OpenPredictTableActivity;
import com.holderzone.intelligencepos.mvp.contract.PredictedTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderRecordE;
import com.holderzone.intelligencepos.mvp.presenter.PredictedTablePresenter;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 预订列表界面
 * Created by tcw on 2017/9/5.
 */
public class PredictedTableFragment extends BaseFragment<PredictedTableContract.Presenter> implements PredictedTableContract.View {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_SAVE_ARRAY_OF_ORDER_RECORD = "SAVE_ARRAY_OF_ORDER_RECORD";

    private static final String KEY_SAVE_NETWORK_ERROR = "SAVE_NETWORK_ERROR";

    @BindView(R.id.rv_predict_table)
    RecyclerView mRvPredictTable;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    // 当前桌台实体
    private DiningTableE mDiningTableE;

    // rv && adapter
    private CommonAdapter<OrderRecordE> mOrderRecordAdapter;
    private List<OrderRecordE> mOrderRecordEs = new ArrayList<>();

    // 网络错误标志位
    private boolean mNetworkError;

    public static PredictedTableFragment newInstance(DiningTableE diningTableE) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        PredictedTableFragment fragment = new PredictedTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mOrderRecordEs = savedInstanceState.getParcelableArrayList(KEY_SAVE_ARRAY_OF_ORDER_RECORD);
        mNetworkError = savedInstanceState.getBoolean(KEY_SAVE_NETWORK_ERROR);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_predicted_table;
    }

    @Override
    protected PredictedTableContract.Presenter initPresenter() {
        return new PredictedTablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化预订桌台 rv && adapter
        initPredictTable();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (mNetworkError) {
                // 切换到网络错误布局
                mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
                // 注册点击事件
                Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
                RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> requestPredictTable());
            } else {
                if (mOrderRecordEs.size() == 0) {
                    // 切换到空布局
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
                } else {
                    // 切换到内容布局
                    mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
                    // 刷新adapter
                    mOrderRecordAdapter.notifyDataSetChanged();
                }
            }
        } else {
            requestPredictTable();
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(KEY_SAVE_ARRAY_OF_ORDER_RECORD, (ArrayList<? extends Parcelable>) mOrderRecordEs);
        outState.putBoolean(KEY_SAVE_NETWORK_ERROR, mNetworkError);
    }

    /*************************view callback begin*************************/

    @Override
    public void onDispose() {

    }

    @Override
    public void onOrderRecordObtainSuccess(List<OrderRecordE> orderRecordEList) {
        if (orderRecordEList.size() == 0) {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            Map<String, List<OrderRecordE>> hashMap = new HashMap<>();
            for (OrderRecordE orderRecordE : orderRecordEList) {
                List<OrderRecordE> orderRecordEs = hashMap.get(orderRecordE.getDiningTableGUID());
                if (orderRecordEs == null) {
                    orderRecordEs = new ArrayList<>();
                    hashMap.put(orderRecordE.getDiningTableGUID(), orderRecordEs);
                }
                orderRecordEs.add(orderRecordE);
            }

            List<OrderRecordE> listTheTable = new ArrayList<>();
            List<OrderRecordE> listOtherTable = new ArrayList<>();
            for (String key : hashMap.keySet()) {
                if (key.equalsIgnoreCase(mDiningTableE.getDiningTableGUID())) {
                    listTheTable.addAll(hashMap.get(key));
                } else {
                    listOtherTable.addAll(hashMap.get(key));
                }
            }

            mOrderRecordEs.clear();
            mOrderRecordEs.addAll(listTheTable);
            mOrderRecordEs.addAll(listOtherTable);
            mOrderRecordAdapter.notifyDataSetChanged();
        }

        // 网络错误标志位复位
        mNetworkError = false;
    }

    @Override
    public void onOrderRecordObtainFailed() {
        // 网络错误标志位复位
        mNetworkError = false;
    }

    @Override
    public void onNetworkError() {
        // 网络错误标志位置位
        mNetworkError = true;
        // 切换到网络错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    /**************************view callback end**************************/

    /*************************private method begin*************************/

    /**
     * 初始化预订桌台 rv && adapter
     */
    private void initPredictTable() {
        mOrderRecordAdapter = new CommonAdapter<OrderRecordE>(mBaseActivity, R.layout.item_predict_table, mOrderRecordEs) {
            @Override
            protected void convert(ViewHolder holder, OrderRecordE orderRecordE, int position) {
                // divider block
                if (position == 0 || !orderRecordE.getDiningTableGUID().equalsIgnoreCase(mOrderRecordEs.get(position - 1).getDiningTableGUID())) {
                    holder.setVisible(R.id.divider_block, true);
                } else {
                    holder.setVisible(R.id.divider_block, false);
                }
                // title && divider line
                if (position == 0 || !orderRecordE.getDiningTableGUID().equalsIgnoreCase(mOrderRecordEs.get(position - 1).getDiningTableGUID())) {
                    holder.setVisible(R.id.ll_predict_title, true);
                    holder.setVisible(R.id.divider_line, true);
                    holder.setText(R.id.tv_table_name, orderRecordE.getTableAreaName() + "-" + orderRecordE.getTableName());
                    if (orderRecordE.getDiningTableGUID().equalsIgnoreCase(mDiningTableE.getDiningTableGUID())) {
                        holder.setVisible(R.id.tv_table_self, true);
                    } else {
                        holder.setVisible(R.id.tv_table_self, false);
                    }
                } else {
                    holder.setVisible(R.id.ll_predict_title, false);
                    holder.setVisible(R.id.divider_line, false);
                }
                // content
                Integer sex = orderRecordE.getSex();
                if (sex != null) {
                    if (sex == 0) {// female
                        holder.setText(R.id.tv_guest_name, orderRecordE.getGuestName() + "女士");
                    } else {//male
                        holder.setText(R.id.tv_guest_name, orderRecordE.getGuestName() + "先生");
                    }
                }
                String guestTel = orderRecordE.getGuestTel();
                if (RegexExUtils.isMobile(guestTel)) {
                    holder.setText(R.id.tv_guest_phone, guestTel.substring(0, 3) + "****" + guestTel.substring(7));
                }
                String orderTime1 = orderRecordE.getOrderTime1();
                String[] split = orderTime1.split("\\s");
                if (split.length >= 2) {
                    String[] time = split[1].split(":");
                    if (time.length >= 3) {
                        holder.setText(R.id.tv_predict_time, time[0] + ":" + time[1] + (orderRecordE.getOrderStat() == -1 ? " 逾期" : ""));
                    }
                }
                holder.setText(R.id.tv_guest_count, orderRecordE.getGuestCount() + "人");
                // divider dash line
                if (position == mOrderRecordEs.size() - 1 || !orderRecordE.getDiningTableGUID().equalsIgnoreCase(mOrderRecordEs.get(position + 1).getDiningTableGUID())) {
                    holder.setVisible(R.id.divider_dash_line, false);
                } else {
                    holder.setVisible(R.id.divider_dash_line, true);
                }
            }
        };
        mOrderRecordAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = OpenPredictTableActivity.newIntent(mBaseActivity, mDiningTableE, mOrderRecordEs.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvPredictTable.setAdapter(mOrderRecordAdapter);
        mRvPredictTable.setLayoutManager(new LinearLayoutManager(mBaseActivity));
    }

    /**
     * 请求预订桌台列表
     */
    private void requestPredictTable() {
        // 0==预定中、-1==已逾期
        mPresenter.requestOrderRecord(Arrays.asList(0, -1));
    }

    /**************************private method end**************************/
}