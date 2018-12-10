package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.fragment.OpenTableFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.OrderRecordE;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.internal.Preconditions;

import butterknife.BindView;

/**
 * 预订开台界面
 * Created by tcw on 2017/9/4.
 */
public class OpenPredictTableActivity extends BaseActivity {

    private static final String KEY_INTENT_DINING_TABLE = "INTENT_DINING_TABLE";

    private static final String KEY_INTENT_ORDER_RECORD = "INTENT_ORDER_RECORD";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.tv_order_record_info)
    TextView mTvOrderRecordInfo;

    // 当前桌台实体
    private DiningTableE mDiningTableE;

    // 当前预订GUID
    private OrderRecordE mOrderRecordE;

    public static Intent newIntent(Context context, DiningTableE diningTableE, OrderRecordE orderRecordE) {
        Intent intent = new Intent(context, OpenPredictTableActivity.class);
        Bundle extras = new Bundle();
        extras.putParcelable(KEY_INTENT_DINING_TABLE, diningTableE);
        extras.putParcelable(KEY_INTENT_ORDER_RECORD, orderRecordE);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mDiningTableE = extras.getParcelable(KEY_INTENT_DINING_TABLE);
        mOrderRecordE = extras.getParcelable(KEY_INTENT_ORDER_RECORD);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_open_predict_table;
    }

    @Nullable
    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        // 初始化title
        mTitle.setOnReturnClickListener(this::finish);
        // 设置预订信息
        Integer sex = mOrderRecordE.getSex();
        Preconditions.checkNotNull(sex, "sex==null");
        String guestName;
        if (sex == 0) {// female
            guestName = mOrderRecordE.getGuestName() + "女士";
        } else {//male
            guestName = mOrderRecordE.getGuestName() + "先生";
        }
        String guestTel = mOrderRecordE.getGuestTel();
        if (RegexExUtils.isMobile(guestTel)) {
            guestTel = guestTel.substring(0, 3) + "****" + guestTel.substring(7);
        }
        mTvOrderRecordInfo.setText(guestName + "(" + guestTel + ")");
        // 托管fragment
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Fragment fragmentById = supportFragmentManager.findFragmentById(R.id.fl_fragment_container);
        if (fragmentById == null) {
            fragmentById = OpenTableFragment.newInstance(mDiningTableE, mOrderRecordE);
        }
        supportFragmentManager.beginTransaction().replace(R.id.fl_fragment_container, fragmentById).commit();
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
}