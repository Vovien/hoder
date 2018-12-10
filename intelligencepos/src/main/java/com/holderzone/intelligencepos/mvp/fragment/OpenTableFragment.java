package com.holderzone.intelligencepos.mvp.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.mvp.activity.OrderDishesActivity;
import com.holderzone.intelligencepos.mvp.activity.TableActivity;
import com.holderzone.intelligencepos.mvp.contract.OpenTableContract;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderRecordE;
import com.holderzone.intelligencepos.mvp.presenter.OpenTablePresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开台界面
 * Created by tcw on 2017/9/5.
 */
public class OpenTableFragment extends BaseFragment<OpenTableContract.Presenter> implements OpenTableContract.View {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_INTENT_ORDER_RECORD = "INTENT_ORDER_RECORD";

    private static final String KEY_SAVE_SALES_ORDER_GUID = "SAVE_SALES_ORDER_GUID";

    private static final String KEY_SAVE_DINING_NUMBER = "KEY_SAVE_DINING_NUMBER";

    @BindView(R.id.tv_dining_number)
    TextView mTvDiningNumber;
    @BindView(R.id.et_dining_number)
    EditText mEtDiningNumber;
    @BindView(R.id.btn_open_table_only)
    Button mBtnOpenTableOnly;
    @BindView(R.id.btn_open_table_then_order_dishes)
    Button mBtnOpenTableThenOrderDishes;

    // 当前桌台实体
    private DiningTableE mDiningTableE;

    // 开台所返回的订单GUID
    private String mSalesOrderGUID;

    // 保存的就餐人数（如果需要）
    private int mDiningNumber;

    // 预订guid，若无，置null
    private OrderRecordE mOrderRecordE;

    public static OpenTableFragment newInstance(DiningTableE diningTableE) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        OpenTableFragment fragment = new OpenTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static OpenTableFragment newInstance(DiningTableE diningTableE, OrderRecordE orderRecordE) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        args.putParcelable(KEY_INTENT_ORDER_RECORD, orderRecordE);
        OpenTableFragment fragment = new OpenTableFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
        mOrderRecordE = extras.getParcelable(KEY_INTENT_ORDER_RECORD);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mSalesOrderGUID = savedInstanceState.getString(KEY_SAVE_SALES_ORDER_GUID);
        mDiningNumber = savedInstanceState.getInt(KEY_SAVE_DINING_NUMBER);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_open_table;
    }

    @Override
    protected OpenTableContract.Presenter initPresenter() {
        return new OpenTablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化就餐人数
        initDiningNumber();
        // 初始化按钮
        initButton();
        // 状态恢复
        restoreSavedState();
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

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_clear, R.id.btn_number_0, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_number_1:
                keyBoardSetting("1");
                break;
            case R.id.btn_number_2:
                keyBoardSetting("2");
                break;
            case R.id.btn_number_3:
                keyBoardSetting("3");
                break;
            case R.id.btn_number_4:
                keyBoardSetting("4");
                break;
            case R.id.btn_number_5:
                keyBoardSetting("5");
                break;
            case R.id.btn_number_6:
                keyBoardSetting("6");
                break;
            case R.id.btn_number_7:
                keyBoardSetting("7");
                break;
            case R.id.btn_number_8:
                keyBoardSetting("8");
                break;
            case R.id.btn_number_9:
                keyBoardSetting("9");
                break;
            case R.id.btn_number_clear:
                keyBoardSetting("clear");
                break;
            case R.id.btn_number_0:
                keyBoardSetting("0");
                break;
            case R.id.iv_delete:
                keyBoardSetting("del");
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SAVE_SALES_ORDER_GUID, mSalesOrderGUID);
        String diningNumber = mEtDiningNumber.getText().toString();
        if (!TextUtils.isEmpty(diningNumber)) {
            outState.putInt(KEY_SAVE_DINING_NUMBER, Integer.valueOf(diningNumber));
        }
    }

    /*************************view callback begin*************************/

    @Override
    public void onSalesOrderCreated(String salesOrderGUID) {
        if (mOrderRecordE == null) {// 普通开台成功，回到TableActivity
            AppManager.getInstance().finishUntil(TableActivity.class);
        } else {// 预订开台成功，回退到TableActivity
            AppManager.getInstance().finishUntil(TableActivity.class);
        }
    }

    @Override
    public void onSalesOrderCreatedThenOrderDishes(String salesOrderGUID) {
        if (mOrderRecordE == null) {// 普通开台并点餐，回到TableActivity并进入DishesActivity
            AppManager.getInstance().finishUntil(TableActivity.class);
            mDiningTableE.setSalesOrderGUID(salesOrderGUID);
            Intent intent = OrderDishesActivity.newIntent(mBaseActivity, mDiningTableE, mDiningTableE.getSalesOrderE() == null ? null : mDiningTableE.getSalesOrderE().getMemberInfoE());
            startActivity(intent);
        } else {// 预订开台并点餐，回到TableActivity并进入DishesActivity
            AppManager.getInstance().finishUntil(TableActivity.class);
            mDiningTableE.setSalesOrderGUID(salesOrderGUID);
            Intent intent = OrderDishesActivity.newIntent(mBaseActivity, mDiningTableE, mDiningTableE.getSalesOrderE() == null ? null : mDiningTableE.getSalesOrderE().getMemberInfoE());
            startActivity(intent);
        }
    }
    /**************************view callback end**************************/
    /*************************private method begin*************************/

    /**
     * 初始化就餐人数
     */
    @SuppressLint("SetTextI18n")
    private void initDiningNumber() {
        mTvDiningNumber.setText("(" + mDiningTableE.getSeatsCount() + "人桌)");
        mEtDiningNumber.setHighlightColor(ContextCompat.getColor(getContext(), R.color.edit_text_ccf0ef));
        mEtDiningNumber.setInputType(InputType.TYPE_NULL);
        mEtDiningNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String number = editable.toString();
                int length = number.length();
                if (length > 3) {
                    editable.delete(length - 1, length);
                    return;
                }
                if (length > 0 && Integer.parseInt(number) > 0) {
                    mBtnOpenTableOnly.setEnabled(true);
                    mBtnOpenTableThenOrderDishes.setEnabled(true);
                } else {
                    mBtnOpenTableOnly.setEnabled(false);
                    mBtnOpenTableThenOrderDishes.setEnabled(false);
                }
            }
        });
    }

    /**
     * 初始化按钮
     */
    private void initButton() {
        mBtnOpenTableOnly.setEnabled(false);
        RxView.clicks(mBtnOpenTableOnly).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> PermissionManager.checkPermission(PermissionManager.PERMISSION_OPEN_TABLE, () -> {
                    mPresenter.submitOpenTable(mDiningTableE.getDiningTableGUID(), Integer.valueOf(mEtDiningNumber.getText().toString()), mOrderRecordE == null ? null : mOrderRecordE.getOrderRecordGUID());
                }));
        mBtnOpenTableThenOrderDishes.setEnabled(false);
        RxView.clicks(mBtnOpenTableThenOrderDishes).throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(o -> PermissionManager.checkPermission(PermissionManager.PERMISSION_OPEN_TABLE, () -> {
                    mPresenter.submitOpenTableThenOrderDishes(mDiningTableE.getDiningTableGUID(), Integer.valueOf(mEtDiningNumber.getText().toString()), mOrderRecordE == null ? null : mOrderRecordE.getOrderRecordGUID());
                }));
    }

    /**
     * 状态恢复
     */
    private void restoreSavedState() {
        if (mDiningNumber > 0) {
            mEtDiningNumber.setText(mDiningNumber + "");
        } else {
            if (mOrderRecordE == null) {
                mEtDiningNumber.setText(mDiningTableE.getSeatsCount() + "");
            } else {
                mEtDiningNumber.setText(mOrderRecordE.getGuestCount() + "");
            }
        }
        mEtDiningNumber.selectAll();
    }

    /**
     * 键盘操作
     *
     * @param number
     */
    private void keyBoardSetting(String number) {
        Editable editable = mEtDiningNumber.getText();
        int length = editable.length();
        if ("clear".equalsIgnoreCase(number)) {
            editable.delete(0, length);
        } else if ("del".equalsIgnoreCase(number)) {
            if (length == 0) {
                return;
            }
            if (mEtDiningNumber.hasSelection()) {
                editable.delete(0, length);
            } else {
                editable.delete(length - 1, length);
            }
        } else {
            if (length > 2) {
                return;
            }
            if (mEtDiningNumber.hasSelection()) {
                editable.delete(0, length);
            }
            editable.append(number);
        }
    }
    /**************************private method end**************************/
}