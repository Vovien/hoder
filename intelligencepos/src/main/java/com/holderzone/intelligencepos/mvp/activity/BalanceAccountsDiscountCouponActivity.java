package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.base.RecycleHolder;
import com.holderzone.intelligencepos.adapter.base.RecyclerAdapter;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsDiscountCouponContract;
import com.holderzone.intelligencepos.mvp.model.bean.CouponsE;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsDiscountCouponPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DensityUtils;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.kennyc.view.MultiStateView;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 结算-折扣-优惠券 界面
 * Created by zhaoping on 2017/6/1.
 */

public class BalanceAccountsDiscountCouponActivity extends BaseActivity<BalanceAccountsDiscountCouponContract.Presenter> implements TextWatcher, ConfirmDialogFragment.ConfirmDialogListener,
        RadioGroup.OnCheckedChangeListener, BalanceAccountsDiscountCouponContract.View {

    public static final String EXTRAS_MEMBER_INFO = "EXTRAS_MEMBER_INFO";
    public static final String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";

    @BindView(R.id.couponRadioGroup)
    RadioGroup couponRadioGroup;
    @BindView(R.id.selectCouponRadioGroup)
    RadioGroup selectCouponRadioGroup;
    @BindView(R.id.inputCouponCode)
    EditText inputCouponCode;
    @BindView(R.id.clear)
    View clear;
    @BindView(R.id.scan)
    View scan;
    @BindView(R.id.validateCouponButton)
    Button validateCouponButton;
    @BindView(R.id.inputCouponRecyclerView)
    RecyclerView mInputCouponRecyclerView;
    @BindView(R.id.emptyText)
    TextView emptyText;
    @BindView(R.id.inputCouponPanel)
    LinearLayout inputCouponPanel;
    @BindView(R.id.couponRecyclerView)
    RecyclerView mSelectCouponRecyclerView;
    @BindView(R.id.couponListPanel)
    LinearLayout couponListPanel;
    @BindView(R.id.confirm)
    Button confirm;
    @BindView(R.id.content)
    MultiStateView content;
    private int mCurrentType = R.id.validateCouponRadio;
    private int mCurrentSelectCouponType = R.id.usable;
    private String mSwipingCardsChipNo = null;
    private String mSalesOrderGuid = null;
    private MemberInfoE mMemberInfoE;
    private List<CouponsE> mSelectCouponResourceList = new java.util.ArrayList<>();
    private List<CouponsE> mSelectCouponList = new java.util.ArrayList<>();
    private List<CouponsE> mInputCouponList = new java.util.ArrayList<>();
    RecyclerAdapter<CouponsE> mSelectCouponsRecyclerAdapter;
    RecyclerAdapter<CouponsE> mInputCouponsRecyclerAdapter;
    private String mSelectCardsChipNo;
    private CouponsE mCurrentInputCoupon;

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMemberInfoE = extras.getParcelable(EXTRAS_MEMBER_INFO);
        mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    public static Intent newIntent(Context context, String salesOrderGuid, MemberInfoE memberInfoE) {
        Intent intent = new Intent(context, BalanceAccountsDiscountCouponActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRAS_SALES_ORDER_GUID, salesOrderGuid);
        if (memberInfoE != null) {
            bundle.putParcelable(EXTRAS_MEMBER_INFO, memberInfoE);
        }
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts_coupon;
    }

    @Override
    protected BalanceAccountsDiscountCouponContract.Presenter initPresenter() {
        return new BalanceAccountsDiscountCouponPresenter(this);
    }

    private boolean isNetworkError;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMemberInfoE != null) {
                    mPresenter.getListByMember(mMemberInfoE.getMemberInfoGUID(), mSalesOrderGuid);
                }
            }
        });
        inputCouponCode.addTextChangedListener(this);
        couponRadioGroup.setOnCheckedChangeListener(this);
        selectCouponRadioGroup.setOnCheckedChangeListener(this);
        if (mMemberInfoE != null) {
            emptyText.setVisibility(View.GONE);
        }
        mSelectCouponsRecyclerAdapter = new RecyclerAdapter<CouponsE>(getApplicationContext(), mSelectCouponList, R.layout.item_coupon) {
            @Override
            public void convert(RecycleHolder holder, CouponsE data, int position) {
                holder.setVisible(R.id.couponDisableImage, mCurrentSelectCouponType == R.id.died || mCurrentSelectCouponType == R.id.invalid);
                holder.setVisible(R.id.select, data.isSelected());
                holder.setTextColor(R.id.couponName, mCurrentSelectCouponType != R.id.died ? R.color.tv_text_black : R.color.common_text_color_707070);
                holder.setBackgroundResource(R.id.amountTextView, mCurrentSelectCouponType == R.id.died ? R.drawable.coupon_amount_disabled : R.drawable.coupon_amount_enable);
                holder.setText(R.id.amountTextView, (data.getCouponTypeE().getDeductType() == null || data.getCouponTypeE().getDeductType() == 0) ? getString(R.string.amount_str, ArithUtil.stripTrailingZeros(data.getCouponTypeE().getAmount())) : "菜品券");
                holder.setText(R.id.couponName, data.getCouponTypeE().getCouponTypeName());
                holder.setText(R.id.couponCode, getString(R.string.coupon_code, data.getCouponsNumber()));
                holder.setText(R.id.validityDate, getString(R.string.date_section, data.getEffectTime().substring(0, 11), data.getInvalidTime().substring(0, 11)));
            }
        };
        mSelectCouponRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mSelectCouponsRecyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
                if (mCurrentSelectCouponType == R.id.usable) {
                    CouponsE couponsE = mSelectCouponList.get(position);
                    couponsE.setSelected(!couponsE.isSelected());
                    notifySelectCouponChanged();
                }
            }
        });
        mSelectCouponRecyclerView.setAdapter(mSelectCouponsRecyclerAdapter);
        mInputCouponsRecyclerAdapter = new RecyclerAdapter<CouponsE>(getApplicationContext(), mInputCouponList, R.layout.item_coupon) {
            @Override
            public void convert(RecycleHolder holder, final CouponsE data, int position) {
                holder.setVisible(R.id.delete, true);
                holder.setText(R.id.amountTextView, (data.getCouponTypeE().getDeductType() == null || data.getCouponTypeE().getDeductType() == 0) ? getString(R.string.amount_str, ArithUtil.stripTrailingZeros(data.getCouponTypeE().getAmount())) : "菜品券");
                holder.setText(R.id.couponName, data.getCouponTypeE().getCouponTypeName());
                holder.setText(R.id.couponCode, getString(R.string.coupon_code, data.getCouponsNumber()));
                holder.setText(R.id.validityDate, getString(R.string.date_section, data.getEffectTime().substring(0, 11), data.getInvalidTime().substring(0, 11)));
                holder.setOnClickListener(R.id.delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCurrentInputCoupon = data;
                        mDialogFactory.showConfirmDialog(getString(R.string.delete_coupon_prompt_content), getString(R.string.cancel),
                                getString(R.string.confirm_button_forgive), BalanceAccountsDiscountCouponActivity.this);
                    }
                });
            }
        };
        mInputCouponRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mInputCouponRecyclerView.addItemDecoration(new SolidLineItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getApplicationContext(), 14.7f), ContextCompat.getColor(BalanceAccountsDiscountCouponActivity.this, R.color.colorBackground)));
        mSelectCouponRecyclerView.addItemDecoration(new SolidLineItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL,
                DensityUtils.dp2px(getApplicationContext(), 14.7f), ContextCompat.getColor(BalanceAccountsDiscountCouponActivity.this, R.color.colorBackground)));
        mInputCouponsRecyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void OnItemClickListener(View view, int position) {
            }
        });
        mInputCouponRecyclerView.setAdapter(mInputCouponsRecyclerAdapter);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (mMemberInfoE != null) {
            mPresenter.getListByMember(mMemberInfoE.getMemberInfoGUID(), mSalesOrderGuid);
        }

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        validateCouponButton.setEnabled(s.toString().length() > 0);
        clear.setVisibility(s.toString().length() > 0 ? View.VISIBLE : View.INVISIBLE);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        if (group.getId() == R.id.couponRadioGroup) {
            mCurrentType = checkedId;
            if (checkedId == R.id.validateCouponRadio) {//验券
                confirm.setEnabled(mInputCouponList.size() > 0 );
                couponListPanel.setVisibility(View.GONE);
                inputCouponPanel.setVisibility(View.VISIBLE);
                scan.setVisibility(View.VISIBLE);
            } else {
                scan.setVisibility(View.GONE);
                KeyboardUtils.hideSoftInput(this);
                confirm.setEnabled(mSelectCouponList.size() > 0 );
                couponListPanel.setVisibility(View.VISIBLE);
                inputCouponPanel.setVisibility(View.GONE);
            }
            notifySelectCouponChanged();
        } else {
            mCurrentSelectCouponType = checkedId;
            notifySelectCouponChanged();
        }
    }

    private void notifySelectCouponChanged() {
        if (isNetworkError) {
            return;
        }
        List<CouponsE> list = new java.util.ArrayList<>();
        boolean selected = false;
        for (CouponsE couponsE : mSelectCouponResourceList) {
            if ((!selected) && couponsE.isSelected()) {
                selected = true;
            }
            if (mCurrentSelectCouponType == R.id.invalid && (couponsE.getUseCondition_UseTime() == 1|| (couponsE.getIsAvaliableUseCondition() == 0 & couponsE.getUseCondition_UseTime() != -1))) {//未生效
                list.add(couponsE);
            } else if (mCurrentSelectCouponType == R.id.usable && couponsE.getIsAvaliableUseCondition() == 1) {//可用
                list.add(couponsE);
            } else if (mCurrentSelectCouponType == R.id.died && (couponsE.getIsAvaliableUseCondition() == -1 || couponsE.getIsUse() == 1|| couponsE.getUseCondition_UseTime() == -1)) {//失效
                list.add(couponsE);
            }
        }
        if (mCurrentType == R.id.selectCouponRadio && mMemberInfoE != null) {
            emptyText.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
            emptyText.setText("暂无相关优惠券");
        }
        if (mCurrentType != R.id.validateCouponRadio) {
            confirm.setEnabled(selected );
        }
        mSelectCouponList.clear();
        mSelectCouponList.addAll(list);
        mSelectCouponsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNegClick() {
    }

    @Override
    public void onPosClick() {
        mInputCouponList.remove(mCurrentInputCoupon);
        notifyInputCouponChanged();
    }

    @Override
    public void onValidateCouponSuccess(CouponsE couponsE) {
        if (couponsE.getIsAvaliableUseCondition() == 1) {
            boolean has = false;
            for (CouponsE bean : mInputCouponList) {
                if (bean.getCouponsGUID().equalsIgnoreCase(couponsE.getCouponsGUID())) {
                    has = true;
                    break;
                }
            }
            if (!has) {
                mInputCouponList.add(couponsE);
                notifyInputCouponChanged();
            }
        } else {
            showMessage("该券不满足使用条件");
        }
    }

    private void notifyInputCouponChanged() {
        if (mCurrentType == R.id.validateCouponRadio) {
            confirm.setEnabled(mInputCouponList.size() > 0 );
        }
        mInputCouponsRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onGetCouponListByMemberSuccess(List<CouponsE> list) {
        isNetworkError = false;
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        mSelectCouponResourceList.clear();
        mSelectCouponResourceList.addAll(list);
        notifySelectCouponChanged();
    }

    @Override
    public void onUseCouponsSuccess() {
        showMessage("使用成功");
        setResult(RESULT_OK);
        finishActivity();
    }

    @Override
    public void onNetworkError() {
        isNetworkError = true;
        emptyText.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }
    // 通过 onActivityResult的方法获取 扫描回来的 值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                // ScanResult 为 获取到的字符串
                String ScanResult = intentResult.getContents();
                if (RegexExUtils.isSerialNumber(ScanResult)) {
                    mPresenter.validateCoupon(ScanResult,mSalesOrderGuid);
                } else {
                    Toast.makeText(this, "格式错误", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({R.id.confirm, R.id.validateCouponButton, R.id.clear, R.id.iv_return, R.id.scan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan:
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("")//写那句提示的话
                        .setOrientationLocked(false)//扫描方向固定
                        .addExtra(CaptureActivity.EXTRA_TITLE, "扫码优惠券")
                        .addExtra(CaptureActivity.EXTRA_MESSAGE, "请扫扫码优惠券")
                        .setCaptureActivity(CaptureActivity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                break;
            case R.id.iv_return:
                finishActivity();
                break;
            case R.id.clear:
                inputCouponCode.setText("");
                break;
            case R.id.validateCouponButton:
                mPresenter.validateCoupon(inputCouponCode.getText().toString(), mSalesOrderGuid);
                break;
            case R.id.confirm:
                if (mCurrentType == R.id.validateCouponRadio) {//验券
                    List<CouponsE> list = new java.util.ArrayList<>();
                    for (CouponsE couponsE : mInputCouponList) {
                        CouponsE bean = new CouponsE();
                        bean.setSalesOrderGUID(mSalesOrderGuid);
                        bean.setCouponsNumber(couponsE.getCouponsNumber());
                        bean.setCouponsGUID(couponsE.getCouponsGUID());
                        list.add(bean);
                    }
                    mPresenter.useCoupons(list);
                } else {
                    List<CouponsE> list = new java.util.ArrayList<>();
                    for (CouponsE couponsE : mSelectCouponList) {
                        if (couponsE.isSelected()) {
                            CouponsE bean = new CouponsE();
                            bean.setSalesOrderGUID(mSalesOrderGuid);
                            bean.setCouponsNumber(couponsE.getCouponsNumber());
                            bean.setCouponsGUID(couponsE.getCouponsGUID());
                            list.add(bean);
                        }
                    }
                    mPresenter.useCoupons(list);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDispose() {

    }
}
