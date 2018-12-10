package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.DashLineItemDecoration;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.contract.QueueStatisticContract;
import com.holderzone.intelligencepos.mvp.model.bean.QueueUpReportE;
import com.holderzone.intelligencepos.mvp.model.bean.db.AccountRecord;
import com.holderzone.intelligencepos.mvp.presenter.QueueStatisticPresenter;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 排队统计 Activity
 */
public class QueueStatisticActivity extends BaseActivity<QueueStatisticContract.Presenter> implements QueueStatisticContract.View {
    private static final String KEY_BUSINESS_DAY = "BUSINESS_DAY";
    @BindView(R.id.back_image)
    ImageView backImage;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_refresh)
    TextView titleRefresh;
    @BindView(R.id.rv_context)
    RecyclerView rvContext;
    @BindView(R.id.tv_check_data)
    TextView tvCheckData;
    @BindView(R.id.ll_check_data)
    LinearLayout llCheckData;
    @BindView(R.id.msv_context)
    MultiStateView msvContext;
    @BindView(R.id.btn_retry)
    Button btnRetry;

    private String mBusinessDay;

    private CommonAdapter<QueueUpReportE> mQueueUpReportECommonAdapter;
    private List<QueueUpReportE> mQueueUpReportEList = new ArrayList<>();

    public static Intent newInstance(Context context) {
        return new Intent(context, QueueStatisticActivity.class);
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mBusinessDay = savedInstanceState.getString(KEY_BUSINESS_DAY);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_queue_statistic;
    }

    @Nullable
    @Override
    protected QueueStatisticContract.Presenter initPresenter() {
        return new QueueStatisticPresenter(this);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvCheckData.setText(mBusinessDay);
        initQueueUpReport();
        RxView.clicks(btnRetry).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(o -> {
            mPresenter.requestLocalAccountRecord();
        });
    }

    private void initQueueUpReport() {
        mQueueUpReportECommonAdapter = new CommonAdapter<QueueUpReportE>(QueueStatisticActivity.this, R.layout.queue_statistic_item, mQueueUpReportEList) {
            @Override
            protected void convert(ViewHolder holder, QueueUpReportE queueUpReportE, int position) {
                holder.setText(R.id.tv_queue_up_type_name, queueUpReportE.getQueueUpTypeName());
                holder.setText(R.id.tv_record_count, queueUpReportE.getRecordCount() + "(" + queueUpReportE.getRecordCustomerCount() + "人)");
                holder.setText(R.id.tv_confirm_count, queueUpReportE.getConfirmCount() + "(" + queueUpReportE.getConfirmCustomerCount() + "人)");
                holder.setText(R.id.tv_avg_waiting_time, queueUpReportE.getAvgWaitingTime() + "分钟");
                if (position == mQueueUpReportEList.size() - 1) {
                    holder.setTextColorRes(R.id.tv_queue_up_type_name, R.color.tv_text_blue_2495ee);
//                    holder.setTextColorRes(R.id.tv_record_count, R.color.tv_text_black_000000);
//                    holder.setTextColorRes(R.id.tv_confirm_count, R.color.tv_text_black_000000);
//                    holder.setTextColorRes(R.id.tv_avg_waiting_time, R.color.tv_text_black_000000);
                } else {
                    holder.setTextColorRes(R.id.tv_queue_up_type_name, R.color.tv_text_black_000000);
//                    holder.setTextColorRes(R.id.tv_record_count, R.color.tv_text_black_000000);
//                    holder.setTextColorRes(R.id.tv_confirm_count, R.color.tv_text_black_000000);
//                    holder.setTextColorRes(R.id.tv_avg_waiting_time, R.color.tv_text_black_000000);
                }
            }
        };
        rvContext.setAdapter(mQueueUpReportECommonAdapter);
        rvContext.setLayoutManager(new LinearLayoutManager(QueueStatisticActivity.this));
        rvContext.addItemDecoration(new DashLineItemDecoration(QueueStatisticActivity.this, LinearLayoutManager.VERTICAL));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestLocalAccountRecord();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onRequestLocalAccountRecordSucceed(AccountRecord accountRecord) {
        mBusinessDay = accountRecord.getBusinessDay();
        // 初始化日期
        String now = DateUtils.format(new Date(), DateUtils.FORMAT_SHORT);
        tvCheckData.setText(mBusinessDay + (now.equals(mBusinessDay) ? "（今天）" : ""));
        // 请求统计数据
        mPresenter.getStatisticalReport(mBusinessDay);
    }

    @Override
    public void onGetStatisticalReportSuccess(List<QueueUpReportE> arrayOfQueueUpReportE) {
        if (arrayOfQueueUpReportE != null) {
            if (arrayOfQueueUpReportE.size() > 0) {
                mQueueUpReportEList.clear();
                mQueueUpReportEList.addAll(arrayOfQueueUpReportE);
                mQueueUpReportECommonAdapter.notifyDataSetChanged();
                msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            } else {
                msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            }
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        }
    }

    @Override
    public void onNetworkError() {
        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onGetStatisticalReportFailed() {

    }

    @OnClick({R.id.back_image, R.id.title_text, R.id.title_refresh, R.id.ll_check_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image:
            case R.id.title_text:
                finishActivityEvent();
                break;
            case R.id.title_refresh:
                mPresenter.getStatisticalReport(mBusinessDay);
                break;
            case R.id.ll_check_data:
                Calendar calendar = DateUtils.dateString2Calendar(tvCheckData.getText().toString().split("（")[0], DateUtils.FORMAT_SHORT);
                new DatePickerDialog(QueueStatisticActivity.this, R.style.MyDatePickerDialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        String month = (i1 + 1) + "";
                        String day = i2 + "";
                        if (month.length() == 1) {
                            month = "0" + month;
                        }
                        if (day.length() == 1) {
                            day = "0" + day;
                        }
                        mBusinessDay = i + "-" + month + "-" + day;
                        String now = DateUtils.format(new Date(), DateUtils.FORMAT_SHORT);
                        tvCheckData.setText(mBusinessDay + (now.equals(mBusinessDay) ? "（今天）" : ""));
                        mPresenter.getStatisticalReport(mBusinessDay);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            default:
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_BUSINESS_DAY, mBusinessDay);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finishActivityEvent();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void finishActivityEvent() {
        setResult(RESULT_OK, getIntent());
        finishActivity();
    }
}
