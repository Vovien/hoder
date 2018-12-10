package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.snack.activity.SnackOrderDetailActivity;
import com.holderzone.intelligencepos.widget.Title;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-8-4.
 * 选择号牌页面
 */

public class ChoosePlateNumberActivity extends BaseActivity {

    @BindView(R.id.title)
    Title title;
    @BindView(R.id.org_number)
    TextView orgNumber;
    @BindView(R.id.btn_number_1)
    Button btnNumber1;
    @BindView(R.id.btn_number_2)
    Button btnNumber2;
    @BindView(R.id.btn_number_3)
    Button btnNumber3;
    @BindView(R.id.btn_number_4)
    Button btnNumber4;
    @BindView(R.id.btn_number_5)
    Button btnNumber5;
    @BindView(R.id.btn_number_6)
    Button btnNumber6;
    @BindView(R.id.btn_number_7)
    Button btnNumber7;
    @BindView(R.id.btn_number_8)
    Button btnNumber8;
    @BindView(R.id.btn_number_9)
    Button btnNumber9;
    @BindView(R.id.btn_number_dot)
    Button btnNumberDot;
    @BindView(R.id.btn_number_0)
    Button btnNumber0;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.order_front)
    LinearLayout orderFront;
    @BindView(R.id.selected_org_number_sure)
    Button selectedOrgNumberSure;
    public static final String ORG_NUMBER = "ORG_NUMBER";

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ChoosePlateNumberActivity.class);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_choose_plate_number;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        btnNumberDot.setText(getString(R.string.selected_org_number_clean));
        btnNumberDot.setTextSize(18.7f);
        //初始化标题
        initTitle();
    }

    private void initTitle() {
        title.setTitleText(getString(R.string.selected_org_number_title));
        title.setOnReturnClickListener(this::finishActivity);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_dot, R.id.btn_number_0, R.id.iv_delete, R.id.selected_org_number_sure})
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.selected_org_number_sure:
                //回调号牌number
                Intent intent = new Intent(this, SnackOrderDetailActivity.class);
                Bundle bundle = new Bundle();
                String number = orgNumber.getText().toString();
                bundle.putString(ORG_NUMBER, number);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finishActivity();
                break;
            default:
                break;
        }
    }

    /**
     * 键盘输入设置
     */
    private void inputChanged(String in) {
        String tel = orgNumber.getText().toString();
        if ("clear".equals(in)) {
            tel = "";
        } else if ("-".equals(in)) {
            if (tel.length() > 0) {
                tel = tel.substring(0, tel.length() - 1);
            }
        } else if (tel.length() < 5) {
            tel += in;
        }
        selectedOrgNumberSure.setEnabled(tel.length() > 0);
        orgNumber.setText(tel);
    }

    @Override
    public void onDispose() {

    }
}
