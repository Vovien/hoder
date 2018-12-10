package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
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
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.contract.TableMergedContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.TableMergedPresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.TableMergedPopup;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.internal.Preconditions;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 已并单界面
 * Created by tcw on 2017/9/4.
 */
public class TableMergedActivity extends BaseActivity<TableMergedContract.Presenter> implements TableMergedContract.View, TableMergedPopup.OnItemClickListener {

    private static final String KEY_ALL_TABLE_MERGED = "ALL_TABLE_MERGED";

    private static final int REQUEST_CODE_BALANCE_ACCOUNT = 0;

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_merged_table_count)
    TextView mTvMergedTableCount;
    @BindView(R.id.rv_table_merged)
    RecyclerView mRvTableMerged;
    @BindView(R.id.btn_print)
    Button mBtnPrint;

    // 所有已并桌台列表，index == 0 是主单
    private List<DiningTableE> mAllDiningTableEList = new ArrayList<>();

    // rv && adapter
    private CommonAdapter<DiningTableE> mDiningTableAdapter;

    public static Intent newIntent(Context context, List<DiningTableE> diningTableEList) {
        Intent intent = new Intent(context, TableMergedActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelableArrayList(KEY_ALL_TABLE_MERGED, (ArrayList<? extends Parcelable>) diningTableEList);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mAllDiningTableEList = extras.getParcelableArrayList(KEY_ALL_TABLE_MERGED);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_table_merged;
    }

    @Override
    protected TableMergedContract.Presenter initPresenter() {
        return new TableMergedPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // title
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setTitleText("合并账单");
        mTitle.setOnMenuClickListener(() -> {
            TableMergedPopup tableMergedPopup = new TableMergedPopup(this);
            tableMergedPopup.showOnAnchor(mTitle.findViewById(R.id.ll_menu),
                    RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
        });
        // table count
        mTvMergedTableCount.setText("已并桌台(" + mAllDiningTableEList.size() + ")");
        // adatper && rv
        mDiningTableAdapter = new CommonAdapter<DiningTableE>(this, R.layout.item_table_merged, mAllDiningTableEList) {
            @Override
            protected void convert(ViewHolder holder, DiningTableE diningTableE, int position) {
                // 显示桌台名字
                holder.setText(R.id.tv_table_name, diningTableE.getName());
                // 显示价格
                SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                Preconditions.checkNotNull(salesOrderE, "salesOrderE==null");
                Integer upperState = salesOrderE.getUpperState();
                Preconditions.checkNotNull(upperState, "upperState==null");
                if (1 == upperState) {// 主单
                    holder.setText(R.id.tv_table_total_price, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(salesOrderE.getConsumeTotal())));
                } else {// 非主单
                    holder.setText(R.id.tv_table_total_price, "已并单");
                }
                // dash line
                holder.setVisible(R.id.divider_dash_line, position != mAllDiningTableEList.size() - 1);
            }
        };
        mDiningTableAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                DiningTableE diningTableE = mAllDiningTableEList.get(position);
                SalesOrderE salesOrderE = diningTableE.getSalesOrderE();
                Integer orderBatchCount = salesOrderE.getOrderBatchCount();
                if (orderBatchCount > 0) {// 进入DishesOrderedActivity
                    startActivity(DishesOrderedActivity.newIntent(TableMergedActivity.this, diningTableE, mAllDiningTableEList));
                } else {// 进入TableOpenedActivity
                    startActivity(TableOpenedActivity.newIntent(TableMergedActivity.this, diningTableE, mAllDiningTableEList));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvTableMerged.setAdapter(mDiningTableAdapter);
        mRvTableMerged.setLayoutManager(new LinearLayoutManager(this));
        // print button
        mBtnPrint.setText(getString(R.string.amount_str, ArithUtil.stripTrailingZeros(mAllDiningTableEList.get(0).getSalesOrderE().getConsumeTotal()) + " 结算"));
        RxView.clicks(mBtnPrint)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> startActivityForResult(BalanceAccountsActivity.newIntent(TableMergedActivity.this,
                        mAllDiningTableEList.get(0).getSalesOrderE().getSalesOrderGUID(), 3), REQUEST_CODE_BALANCE_ACCOUNT));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_BALANCE_ACCOUNT:
                if (Activity.RESULT_OK == resultCode) {
                    AppManager.getInstance().finishUntil(TableActivity.class);
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }

    /*************************view callback begin*************************/

    @Override
    public void onRequestPrintPrepaymentSucceed(String msg) {
    }

    /**************************view callback end**************************/

    @Override
    public void onItemClick(int itemType) {
        switch (itemType) {
            case TableMergedPopup.ITEM_TYPE_MERGE_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_MERGE_TABLE,
                        () -> startActivity(MergeTableActivity.newIntent(this, mAllDiningTableEList.get(0))));
                break;
            case TableMergedPopup.ITEM_TYPE_SPLIT_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_SPLIT_TABLE,
                        () -> startActivity(SplitTableActivity.newIntent(this, mAllDiningTableEList.get(0))));
                break;
            default:
                break;
        }
    }
}