package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.SnackOpenTableContract;
import com.holderzone.intelligencepos.mvp.presenter.SnackOpenTablePresenter;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 快餐开台页面 Activity
 * Created by Administrator on 2018/3/24/024.
 */

public class SnackOpenTableActivity extends BaseActivity<SnackOpenTableContract.Presenter> implements SnackOpenTableContract.View {
    private static final String KEY_SEATE_COUNT = "KEY_SEATE_COUNT";
    public static final String KEY_CALLBACK = "KEY_CALLBACK";
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.et_dining_number)
    EditText mEtDiningNumber;
    @BindView(R.id.btn_number_1)
    Button mBtnNumber1;
    @BindView(R.id.btn_number_2)
    Button mBtnNumber2;
    @BindView(R.id.btn_number_3)
    Button mBtnNumber3;
    @BindView(R.id.btn_number_4)
    Button mBtnNumber4;
    @BindView(R.id.btn_number_5)
    Button mBtnNumber5;
    @BindView(R.id.btn_number_6)
    Button mBtnNumber6;
    @BindView(R.id.btn_number_7)
    Button mBtnNumber7;
    @BindView(R.id.btn_number_8)
    Button mBtnNumber8;
    @BindView(R.id.btn_number_9)
    Button mBtnNumber9;
    @BindView(R.id.btn_number_clear)
    Button mBtnNumberClear;
    @BindView(R.id.btn_number_0)
    Button mBtnNumber0;
    @BindView(R.id.iv_delete)
    ImageView mIvDelete;
    @BindView(R.id.btn_open_table_then_order_dishes)
    Button mBtnOpenTableThenOrderDishes;

    private String seateCount = "1";

    public static Intent newIntent(Context context, String seateCount) {
        Intent intent = new Intent(context, SnackOpenTableActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SEATE_COUNT, seateCount);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        seateCount = extras.getString(KEY_SEATE_COUNT);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_snack_open_table;
    }

    @Nullable
    @Override
    protected SnackOpenTableContract.Presenter initPresenter() {
        return new SnackOpenTablePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mEtDiningNumber.setText(seateCount);
        mTitle.setTitleText("就餐人数");
        if (seateCount.length() > 0) {
            mBtnOpenTableThenOrderDishes.setEnabled(true);
        } else {
            mBtnOpenTableThenOrderDishes.setEnabled(false);
        }
        initClickEvent();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    private void initClickEvent() {
        RxView.clicks(mBtnOpenTableThenOrderDishes)
                .throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            Intent intent = getIntent();
            intent.putExtra(KEY_CALLBACK, mEtDiningNumber.getText().toString());
            setResult(RESULT_OK);
            finishActivity();
        });

        mTitle.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
        mEtDiningNumber.setSelection(mEtDiningNumber.getText() != null ? mEtDiningNumber.getText().length() : 0);

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.btn_number_1, R.id.btn_number_2, R.id.btn_number_3, R.id.btn_number_4, R.id.btn_number_5, R.id.btn_number_6, R.id.btn_number_7, R.id.btn_number_8, R.id.btn_number_9, R.id.btn_number_clear, R.id.btn_number_0, R.id.iv_delete})
    public void onViewClicked(View view) {
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
            case R.id.btn_number_clear:
                inputChanged("clear");
                break;
            case R.id.btn_number_0:
                inputChanged("0");
                break;
            case R.id.iv_delete:
                inputChanged("-");
                break;
            default:
                break;
        }
    }

    /**
     * 键盘输入设置
     */
    private void inputChanged(String in) {
        String tel = mEtDiningNumber.getText().toString();
        if ("clear".equals(in)) {
            tel = "";
        } else if ("-".equals(in)) {
            if (tel.length() > 0) {
                tel = tel.substring(0, tel.length() - 1);
            }
        } else if (tel.length() < 5) {
            tel += in;
        }
        mBtnOpenTableThenOrderDishes.setEnabled(tel.length() > 0);
        mEtDiningNumber.setText(tel);
        mEtDiningNumber.setSelection(tel.length());
    }
}
