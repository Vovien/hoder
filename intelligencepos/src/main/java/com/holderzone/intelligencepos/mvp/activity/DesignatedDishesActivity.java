package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SpanUtils;
import com.blankj.utilcode.util.StringUtils;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.DesignatedDishesContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.mvp.presenter.DesignatedDishesPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 划菜activity
 * Created by chencao on 2018/1/8.
 */

public class DesignatedDishesActivity extends BaseActivity<DesignatedDishesContract.Presenter> implements DesignatedDishesContract.View {
    private static final String TABLE_GUID = "diningTableGUID";
    private static final String ORDER_GUID = "salesOrderGUID";
    private static final String TABLE_NAME = "TableName";
    private static final int REQUEST_CODE_DESIGNATE_SINGLE_DISH = 0x00000111;

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_return)
    LinearLayout llReturn;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.rv_designated)
    RecyclerView rvDesignated;
    @BindView(R.id.btn_designated_all)
    Button btnDesignatedAll;
    @BindView(R.id.msv_designated)
    MultiStateView msvDesignated;

    /**
     * 桌台GUID
     */
    private String diningTableGUID;
    /**
     * 订单GUID
     */
    private String salesOrderGUID;
    /**
     *
     */
    private String tableName;
    /**
     *
     */
    private CommonAdapter<SalesOrderBatchDishesE> adapter;
    /**
     * 已点菜品
     */
    private List<SalesOrderBatchDishesE> mSalesOrderBatchDishesEList = new ArrayList<>();
    /**
     * 是否是重新启动
     */
    private boolean isRestart = false;
    /**
     *
     */
    private boolean isChangeData = false;

    public static Intent newIntent(Context context, String diningTableGUID, String salesOrderGuid, String tableName) {
        Intent intent = new Intent(context, DesignatedDishesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(TABLE_GUID, diningTableGUID);
        bundle.putString(ORDER_GUID, salesOrderGuid);
        bundle.putString(TABLE_NAME, tableName);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        diningTableGUID = extras.getString(TABLE_GUID);
        salesOrderGUID = extras.getString(ORDER_GUID);
        tableName = extras.getString(TABLE_NAME);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_designated_dishes;
    }

    @Nullable
    @Override
    protected DesignatedDishesContract.Presenter initPresenter() {
        return new DesignatedDishesPresenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isRestart) {
            mPresenter.getSalesOrderBatchList(diningTableGUID);
            isRestart = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRestart = true;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        ivMenu.setVisibility(View.GONE);
        tvTitle.setText(tableName + "划菜");

        msvDesignated.setViewState(MultiStateView.VIEW_STATE_CONTENT);

        adapter = new CommonAdapter<SalesOrderBatchDishesE>(this, R.layout.item_dishes_ordered_designated, mSalesOrderBatchDishesEList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                if (position == 0) {
                    //如果是第一个item 则显示已叫起 title
                    holder.setVisible(R.id.include_title, true);
                    holder.setText(R.id.tv_title_text, "已叫起(" + mSalesOrderBatchDishesEList.size() + ")");
                } else {
                    holder.setVisible(R.id.include_title, false);
                }
                //是否是赠送菜品
                holder.setVisible(R.id.tv_gift, salesOrderBatchDishesE.getGift() == 1);

                holder.setText(R.id.tv_dishes_name, salesOrderBatchDishesE.getSimpleName());
                String servingCountStr = ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getServingCount());
                String orderCountStr = ArithUtil.stripTrailingZeros(salesOrderBatchDishesE.getOrderCount());
                Context context = getApplicationContext();
                CharSequence charSequence = new SpanUtils()
                        .append(servingCountStr)
                        .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_f56766))
                        .append("/" + orderCountStr)
                        .setForegroundColor(ContextCompat.getColor(context, R.color.common_text_color_707070))
                        .create();
//                CharSequence charSequence = setAppendTextColor(servingCountStr, R.color.common_text_color_f56766, "/" + orderCountStr, R.color.common_text_color_707070);
                holder.setText(R.id.tv_dishes_number, charSequence);
                String practiceStr = salesOrderBatchDishesE.getDishesPracticeStr();
                String subDishes = salesOrderBatchDishesE.getSubDishes();
                if (StringUtils.isEmpty(practiceStr) && StringUtils.isEmpty(subDishes)) {
                    holder.setVisible(R.id.tv_cook_method, false);
                } else {
                    if (!StringUtils.isEmpty(subDishes)) {
                        holder.setText(R.id.tv_cook_method, subDishes);
                    } else {
                        holder.setText(R.id.tv_cook_method, practiceStr);
                    }
                    holder.setVisible(R.id.tv_cook_method, true);
                }

                String time = salesOrderBatchDishesE.getBatchTime();
                String formatTime = DateUtils.anewFormat(time, "HH:mm");
                holder.setText(R.id.tv_dishes_total_price, getString(R.string.order_dishes_batch_time, formatTime));

                holder.setVisible(R.id.dashLine, position != mSalesOrderBatchDishesEList.size() - 1);
            }
        };
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                SalesOrderBatchDishesE batchDishesE = mSalesOrderBatchDishesEList.get(position);
                double couldChange = ArithUtil.sub(batchDishesE.getOrderCount(), batchDishesE.getServingCount());
                startActivityForResult(DesignatedOneDishesActivity
                        .newIntent(DesignatedDishesActivity.this, batchDishesE.getSimpleName(),
                                couldChange,
                                couldChange,
                                batchDishesE.getGift(), batchDishesE.getSalesOrderBatchDishesGUID()), REQUEST_CODE_DESIGNATE_SINGLE_DISH);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvDesignated.setLayoutManager(layoutManager);
        rvDesignated.setAdapter(adapter);

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.getSalesOrderBatchList(diningTableGUID);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.ll_return, R.id.ll_menu, R.id.btn_designated_all, R.id.btn_retry})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_return:
                finishActivityByResult();
                break;
            case R.id.ll_menu:
                mPresenter.getSalesOrderBatchList(diningTableGUID);
                break;
            case R.id.btn_designated_all:
                mDialogFactory.showConfirmDialog(tableName, true, "取消", "确定", new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {

                    }

                    @Override
                    public void onPosClick() {
                        mPresenter.upDataAll(diningTableGUID, salesOrderGUID);
                    }
                });
                break;
            case R.id.btn_retry:
                mPresenter.getSalesOrderBatchList(diningTableGUID);
                break;
            default:
        }
    }

    @Override
    public void getSalesOrderBatchList(List<DiningTableE> diningTableEList) {
        mSalesOrderBatchDishesEList.clear();
        if (diningTableEList == null || diningTableEList.size() == 0 || diningTableEList.get(0).getArrayOfSalesOrderBatchDishesE() == null || diningTableEList.get(0).getArrayOfSalesOrderBatchDishesE().size() == 0) {
            msvDesignated.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            return;
        } else {
            msvDesignated.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        DiningTableE diningTableE = diningTableEList.get(0);
        List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = diningTableE.getArrayOfSalesOrderBatchDishesE();
        mSalesOrderBatchDishesEList.addAll(salesOrderBatchDishesEList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getSalesOrderBatchListFail() {
        msvDesignated.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void upDataAllSuccess() {
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void upDataAllFail() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_DESIGNATE_SINGLE_DISH && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    isChangeData = extras.getBoolean(DesignatedOneDishesActivity.RESULT_CODE_DATA_CHANGED, false);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivityByResult();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 是直接finished还是需要返回值
     */
    private void finishActivityByResult() {
        if (isChangeData) {
            setResult(RESULT_OK);
        }
        finishActivity();
    }
}
