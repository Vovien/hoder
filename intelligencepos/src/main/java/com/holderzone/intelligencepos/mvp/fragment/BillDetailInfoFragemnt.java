package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;
import com.kennyc.view.MultiStateView;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * 账单详情-账单信息fragment
 * Created by chencao on 2017/6/5.
 */

public class BillDetailInfoFragemnt extends BaseFragment {

    @BindView(R.id.activity_bill_details_serial_number)
    TextView activityBillDetailsSerialNumber;
    @BindView(R.id.activity_bill_details_serial_table)
    TextView activityBillDetailsSerialTable;
    @BindView(R.id.activity_bill_details_serial_people_number)
    TextView activityBillDetailsSerialPeopleNumber;
    @BindView(R.id.activity_bill_details_serial_type)
    TextView activityBillDetailsSerialType;
    @BindView(R.id.activity_bill_details_serial_begin_time)
    TextView activityBillDetailsSerialBeginTime;
    @BindView(R.id.activity_bill_details_serial_end_time)
    TextView activityBillDetailsSerialEndTime;
    @BindView(R.id.activity_bill_end_time)
    TextView endTimeText;
    @BindView(R.id.activity_bill_details_serial_all_money)
    TextView activityBillDetailsSerialAllMoney;
    @BindView(R.id.activity_bill_details_serial_discount)
    TextView activityBillDetailsSerialDiscount;
    @BindView(R.id.activity_bill_details_serial_all_surcharge)
    TextView activityBillDetailsSerialAllSurcharge;
    @BindView(R.id.activity_bill_details_serial_all_payment)
    TextView activityBillDetailsSerialAllPayment;
    @BindView(R.id.activity_bill_details_serial_payment_discount)
    TextView activityBillDetailsSerialPaymentDiscount;
    @BindView(R.id.activity_bill_details_serial_bill_state)
    TextView activityBillDetailsSerialBillState;
    @BindView(R.id.activity_bill_details_serial_under_single)
    TextView activityBillDetailsSerialUnderSingle;
    @BindView(R.id.activity_bill_details_serial_bill_holder)
    TextView activityBillDetailsSerialBillHolder;
    @BindView(R.id.bill_detail_info_multistateview)
    MultiStateView multiStateView;
    Unbinder unbinder;
    private static String SALSE_ORDERE = "SALSE_ORDERE";

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        salesOrderE = extras.getParcelable(SALSE_ORDERE);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    private SalesOrderE salesOrderE = null;

    public static BillDetailInfoFragemnt getInstanse(SalesOrderE salesOrderE) {
        BillDetailInfoFragemnt fragemnt = new BillDetailInfoFragemnt();
        Bundle args = new Bundle();
        args.putParcelable(SALSE_ORDERE, salesOrderE);
        fragemnt.setArguments(args);
        return fragemnt;
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragemnt_bill_detail_info;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        setShowState(salesOrderE);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    public void setShowState(SalesOrderE salesOrderE) {
        this.salesOrderE = salesOrderE;
        setViewShow(salesOrderE);
    }

    private void setViewShow(SalesOrderE salesOrderE) {
        if (salesOrderE == null) {
            return;
        }

        setTextColor(R.id.activity_bill_details_serial_number, salesOrderE.getSerialNumber());
        String tableName = null;
        if (salesOrderE.getTradeMode() != null) {
            switch (salesOrderE.getTradeMode()){
                case 0:
                    tableName = salesOrderE.getDiningTableName() != null ?salesOrderE.getDiningTableName():"桌台数据损坏";
                    break;
                case 1:
                    tableName = "快餐";
                    break;
                case 2:/**外卖**/
                    break;
                default:
                    break;
            }

        } else {
            tableName = "桌台数据损坏";
        }
        setTextColor(R.id.activity_bill_details_serial_table, tableName);
        setTextColor(R.id.activity_bill_details_serial_people_number,salesOrderE.getGuestCount() + "人");
        activityBillDetailsSerialType.setText("");
//        setAppendText(R.id.activity_bill_details_serial_type, "交易类型：", R.color.layout_bg_light_black);
        switch (salesOrderE.getTradeMode()){
            case 0:
                setAppendText(R.id.activity_bill_details_serial_type, "堂食", R.color.common_text_color_000000);
                break;
            case 1:
                setAppendText(R.id.activity_bill_details_serial_type, "快餐", R.color.common_text_color_000000);
                break;
//            case 2:/**外卖**/
//                setAppendText(R.id.activity_bill_details_serial_type, "w", R.color.common_text_color_000000);
//                break;

            default:
                break;
        }
        if (salesOrderE.getCreateTime() != null) {
            String time_Start = DateUtils.anewFormat(salesOrderE.getCreateTime(), "yyyy-MM-dd HH:mm");
            setTextColor(R.id.activity_bill_details_serial_begin_time,  time_Start);
        } else {
            setTextColor(R.id.activity_bill_details_serial_begin_time,  "");
        }

        setTextColor(R.id.activity_bill_details_serial_all_money, getString(R.string.amount_money, ArithUtil.stripTrailingZeros(salesOrderE.getDishesConsumeTotal())));
        setTextColor(R.id.activity_bill_details_serial_discount, getString(R.string.negative_amount_str, ArithUtil.stripTrailingZeros(salesOrderE.getDiscountTotal())));
        setTextColor(R.id.activity_bill_details_serial_all_surcharge, getString(R.string.amount_money, ArithUtil.stripTrailingZeros(salesOrderE.getAdditionalFeesTotal())));
        setTextColor(R.id.activity_bill_details_serial_all_payment, getString(R.string.amount_money, ArithUtil.stripTrailingZeros(salesOrderE.getPayTotal())));
        setTextColor(R.id.activity_bill_details_serial_payment_discount, getString(R.string.negative_amount_str, ArithUtil.stripTrailingZeros(salesOrderE.getPaymentDiscountTotal())));
        setTextColor(R.id.activity_bill_details_serial_bill_state,  salesOrderE.getOrderStatCN());
        setTextColor(R.id.activity_bill_details_serial_under_single,  salesOrderE.getUserName());
        if (salesOrderE.getOrderStat() == -1) {
            if (salesOrderE.getCancelTime() != null) {
                String time_End = DateUtils.anewFormat(salesOrderE.getCancelTime(), "yyyy-MM-dd HH:mm");
                setTextColor(R.id.activity_bill_end_time, "作废时间：", "");
                setTextColor(R.id.activity_bill_details_serial_end_time,time_End);
            } else {
                setTextColor(R.id.activity_bill_end_time, "作废时间：", "");
            }
            setTextColor(R.id.activity_bill_details_serial_bill_holder,salesOrderE.getCancelStaffName());
        } else {
            if (salesOrderE.getChecOutkTime() != null) {
                String time_End = DateUtils.anewFormat(salesOrderE.getChecOutkTime(), "yyyy-MM-dd HH:mm");
                setTextColor(R.id.activity_bill_end_time, "结账时间：", "");
                setTextColor(R.id.activity_bill_details_serial_end_time,time_End);
            } else {
                setTextColor(R.id.activity_bill_end_time, "结账时间：", "");
            }
            setTextColor(R.id.activity_bill_details_serial_bill_holder,salesOrderE.getCheckOutStaffName());
        }
    }

    /**
     * 更改同一个textview 中不同文字颜色
     *
     * @param viewId
     * @param text
     * @param colorRes
     * @return
     */
    public TextView setAppendText(int viewId, String text, int colorRes) {
        TextView view = (TextView) getActivity().findViewById(viewId);
        if (text == null) {
            return view;
        }
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(mBaseActivity, colorRes)), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.append(spanText);
        return view;
    }

    View bootView = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bootView = super.onCreateView(inflater, container, savedInstanceState);
        return bootView;
    }

    private void setTextColor(int viewId, String text1, String text2) {
        TextView view = (TextView) bootView.findViewById(viewId);
        view.setText("");
        setAppendText(viewId, text1, R.color.layout_bg_light_black);
        setAppendText(viewId, text2, R.color.common_text_color_707070);
    }
    private void setTextColor(int viewId, String text1) {
        TextView view = (TextView) bootView.findViewById(viewId);
        view.setText("");
        setAppendText(viewId, text1, R.color.layout_bg_light_black);
//        setAppendText(viewId, text2, R.color.common_text_color_707070);
    }

    @Override
    public void onDispose() {

    }
}
