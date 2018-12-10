package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.Constants;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.BalanceAccountsMemberLoginContract;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.presenter.BalanceAccountsMemberLoginPresenter;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 会员登录
 * Created by zhaoping on 2017/5/31.
 */

public class BalanceAccountsMemberLoginActivity extends BaseActivity<BalanceAccountsMemberLoginContract.Presenter> implements BalanceAccountsMemberLoginContract.View {

    public static String EXTRAS_SALES_ORDER_GUID = "EXTRAS_SALES_ORDER_GUID";
    @BindView(R.id.telephone)
    TextView telephoneEditText;
    @BindView(R.id.loginButton)
    Button loginButton;
    @BindView(R.id.btn_number_dot)
    Button clearButton;
    @BindView(R.id.title)
    Title title;
    private String mSalesOrderGuid;

    public static Intent newIntent(Context context, String salesOrderGuid) {
        Intent intent = new Intent(context, BalanceAccountsMemberLoginActivity.class);
        Bundle bundle = new Bundle();
        if (salesOrderGuid != null) {
            bundle.putString(EXTRAS_SALES_ORDER_GUID, salesOrderGuid);
        }
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        if (extras.containsKey(EXTRAS_SALES_ORDER_GUID)) {
            mSalesOrderGuid = extras.getString(EXTRAS_SALES_ORDER_GUID, null);
        }
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_balance_accounts_member_login;
    }

    @Override
    protected BalanceAccountsMemberLoginContract.Presenter initPresenter() {
        return new BalanceAccountsMemberLoginPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        clearButton.setText("清空");
        clearButton.setTextSize(18.7f);
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onQueryMemberSuccess(MemberInfoE memberInfoE) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRAS_MEMBER_INFO, memberInfoE);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    // 通过 onActivityResult的方法获取 扫描回来的 值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                // ScanResult 为 获取到的字符串
                String tel = intentResult.getContents();
                if (RegexExUtils.isMobile(tel)) {
                    if (mSalesOrderGuid != null) {
                        mPresenter.memberLogin(mSalesOrderGuid, tel);
                    } else {
                        mPresenter.requestMemberInfo(tel);
                    }
                } else {
                    Toast.makeText(this, "手机号格式错误", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @OnClick({R.id.scanBarCode, R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.loginButton,
            R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanBarCode:
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
                        .setPrompt("")//写那句提示的话
                        .setOrientationLocked(false)//扫描方向固定
                        .addExtra(CaptureActivity.EXTRA_TITLE, "会员扫码登录")
                        .addExtra(CaptureActivity.EXTRA_MESSAGE, "请扫描顾客的会员二维码")
                        .setCaptureActivity(CaptureActivity.class) // 设置自定义的activity是CustomActivity
                        .initiateScan(); // 初始化扫描
                break;
            case R.id.btn_number_1:
                inputChanged("1");
                break;
            case R.id.btn_number_2:
                inputChanged("2");
                break;
            case R.id.btn_number_3:
                inputChanged("3");
                break;
            case R.id.btn_number_4:
                inputChanged("4");
                break;
            case R.id.btn_number_5:
                inputChanged("5");
                break;
            case R.id.btn_number_6:
                inputChanged("6");
                break;
            case R.id.btn_number_7:
                inputChanged("7");
                break;
            case R.id.btn_number_8:
                inputChanged("8");
                break;
            case R.id.btn_number_9:
                inputChanged("9");
                break;
            case R.id.btn_number_dot:
                inputChanged("clear");
                break;
            case R.id.btn_number_0:
                inputChanged("0");
                break;
            case R.id.iv_delete:
                inputChanged("-");
                break;
            case R.id.loginButton:
                if (mSalesOrderGuid != null) {
                    mPresenter.memberLogin(mSalesOrderGuid, telephoneEditText.getText().toString());
                } else {
                    mPresenter.requestMemberInfo(telephoneEditText.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    private void inputChanged(String in) {
        String tel = telephoneEditText.getText().toString();
        if ("clear".equals(in)) {
            tel = "";
        } else if ("-".equals(in)) {
            if (tel.length() > 0) {
                tel = tel.substring(0, tel.length() - 1);
            }
        } else if (tel.length() < 11) {
            tel += in;
        }
        loginButton.setEnabled(tel.length() == 11);
        telephoneEditText.setText(tel);
    }

    @Override
    public void onMemberLoginSuccess(MemberInfoE memberInfoE) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.EXTRAS_MEMBER_INFO, memberInfoE);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void onMemberLoginFailByLoginOther() {
        mDialogFactory.showConfirmDialog("该会员已登录其他账单。", false, "", R.drawable.selector_button_red, true, "确定", R.drawable.selector_button_blue, new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
            }

            @Override
            public void onPosClick() {
                finishActivity();
            }
        });
    }

    @Override
    public void onMemberLoginFailByNoRegister() {
        mDialogFactory.showConfirmDialog("未查询到该会员。", false, "", R.drawable.selector_button_red, true, "确定", R.drawable.selector_button_blue, new ConfirmDialogFragment.ConfirmDialogListener() {
            @Override
            public void onNegClick() {
            }

            @Override
            public void onPosClick() {
                inputChanged("clear");
            }
        });
    }

    @Override
    public void onDispose() {
    }
}
