package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.TableOpenedContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.presenter.TableOpenedPresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.TableOpenedPopup;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

import static com.holderzone.intelligencepos.base.Constants.EXTRAS_MEMBER_INFO;

/**
 * 已开台未点餐界面
 * Created by tcw on 2017/9/4.
 */
public class TableOpenedActivity extends BaseActivity<TableOpenedContract.Presenter> implements TableOpenedContract.View, TableOpenedPopup.OnItemClickListener {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_INTENT_ALL_DINING_TABLE = "INTENT_ALL_DINING_TABLE";
    private static final int REQUEST_CODE_ADD_DISHES = 1;

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.btn_order_dishes)
    Button mBtnOrderDishes;

    // 当前桌台
    private DiningTableE mDiningTableE;

    // 并单情况下所有桌台
    private List<DiningTableE> mAllDiningTableEList;
    /**
     * 是否进入过点菜页面
     */
    private boolean hasBack;
    private MemberInfoE mMemberInfoE;

    public static Intent newIntent(Context context, DiningTableE diningTableE) {
        Intent intent = new Intent(context, TableOpenedActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        intent.putExtras(extras);
        return intent;
    }

    public static Intent newIntent(Context context, DiningTableE diningTableE, List<DiningTableE> allDiningTableEList) {
        Intent intent = new Intent(context, TableOpenedActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        extras.putParcelableArrayList(KEY_INTENT_ALL_DINING_TABLE, (ArrayList<? extends Parcelable>) allDiningTableEList);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
        mAllDiningTableEList = extras.getParcelableArrayList(KEY_INTENT_ALL_DINING_TABLE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_table_opened;
    }

    @Override
    protected TableOpenedContract.Presenter initPresenter() {
        return new TableOpenedPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTitle.setOnReturnClickListener(this::finish);
        mTitle.setTitleText(mDiningTableE.getName() + "(" + mDiningTableE.getSalesOrderE().getSerialNumber() + ")");
        mTitle.setOnMenuClickListener(() -> {
            TableOpenedPopup tableOpenedPopup = new TableOpenedPopup(this);
            if (mAllDiningTableEList != null && mAllDiningTableEList.size() > 0) {
                tableOpenedPopup.hideCloseFunction();
            }
            tableOpenedPopup.showOnAnchor(mTitle.findViewById(R.id.ll_menu),
                    RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
        });
        RxView.clicks(mBtnOrderDishes)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> PermissionManager.checkPermission(PermissionManager.PERMISSION_OPEN_TABLE,
                        () ->
                        {
                            MemberInfoE memberInfoE = mDiningTableE.getSalesOrderE().getMemberInfoE();
                            String mainOrderGuid = null;
                            if (mAllDiningTableEList != null && mAllDiningTableEList.size() > 1) {
                                for (DiningTableE diningTableE : mAllDiningTableEList) {
                                    if (diningTableE.getSalesOrderE().getUpperState() == 1) {
                                        mainOrderGuid = diningTableE.getSalesOrderE().getSalesOrderGUID();
                                        memberInfoE = diningTableE.getSalesOrderE().getMemberInfoE();
                                    }
                                }
                            }
                            startActivityForResult(OrderDishesActivity.newIntent(TableOpenedActivity.this, mDiningTableE, mainOrderGuid, hasBack ? mMemberInfoE : memberInfoE), REQUEST_CODE_ADD_DISHES);
                        }
                ));
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

    /*************************view callback begin*************************/

    @Override
    public void onTableClosed() {
        AppManager.getInstance().finishUntil(TableActivity.class);
    }

    /**************************view callback end**************************/

    @Override
    public void onItemClick(int itemType) {
        switch (itemType) {
            case TableOpenedPopup.ITEM_TYPE_CHANGE_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_CHANGE_TABLE,
                        () -> startActivity(ChangeTableActivity.newIntent(this, mDiningTableE)));
                break;
            case TableOpenedPopup.ITEM_TYPE_MERGE_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_MERGE_TABLE,
                        () -> {
                            if (mAllDiningTableEList != null && mAllDiningTableEList.size() > 0) {
                                startActivity(MergeTableActivity.newIntent(this, mAllDiningTableEList.get(0)));
                            } else {
                                startActivity(MergeTableActivity.newIntent(this, mDiningTableE));
                            }
                        });
                break;
            case TableOpenedPopup.ITEM_TYPE_CLOSE_TABLE:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_CLOSE_TABLE,
                        () -> mDialogFactory.showConfirmDialog("该桌台未点餐，确认关台吗？", "取消", "确认关台", new ConfirmDialogFragment.ConfirmDialogListener() {
                            @Override
                            public void onNegClick() {
                            }

                            @Override
                            public void onPosClick() {
                                mPresenter.submitCloseTable(mDiningTableE.getSalesOrderE().getSalesOrderGUID());
                            }
                        }));
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_DISHES && RESULT_OK == resultCode) {
            hasBack = true;
            Bundle bundle = data.getExtras();
            if (bundle != null && bundle.containsKey(EXTRAS_MEMBER_INFO)) {
                mMemberInfoE = bundle.getParcelable(EXTRAS_MEMBER_INFO);
            } else {
                mMemberInfoE = null;
            }
        }
    }
}