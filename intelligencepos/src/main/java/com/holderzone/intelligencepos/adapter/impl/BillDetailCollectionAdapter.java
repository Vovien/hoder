package com.holderzone.intelligencepos.adapter.impl;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderPaymentE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 收账单详情-收款记录adapter
 * Created by chencao on 2017/6/5.
 */

public class BillDetailCollectionAdapter extends RecyclerView.Adapter<BillDetailCollectionAdapter.MyBillDetailViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<SalesOrderE> salesOrderEList;
    private List<Object> datas;

    public BillDetailCollectionAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 设置数据
     *
     * @param salesOrderEList
     */
    public void setDatas(List<SalesOrderE> salesOrderEList) {
        if (this.salesOrderEList != null) {
            if (equalList(this.salesOrderEList, salesOrderEList)) {
                return;
            }
        }
        this.salesOrderEList = salesOrderEList;
        if (this.salesOrderEList == null) {
            return;
        } else {
            setCollectionRecordData();
        }
        notifyDataSetChanged();
    }

    public boolean equalList(List list1, List list2) {
        return (list1.size() == list2.size()) && list1.containsAll(list2);
    }

    /**
     * 添加数据
     *
     * @param salesOrderEList
     */
    public void addDatas(List<SalesOrderE> salesOrderEList) {
        if (this.salesOrderEList == null) {
            this.salesOrderEList = new ArrayList<>();
        }
        this.salesOrderEList.addAll(this.salesOrderEList.size() - 1, salesOrderEList);
        setCollectionRecordData();
        notifyDataSetChanged();
    }


    private int length = 0;

    /**
     * 返回列表长度
     *
     * @return
     */
    public int getLength() {
        return length;
    }

    /**
     * 收款记录
     */
    private void setCollectionRecordData() {
        datas = new ArrayList<>();
        length = 0;
        for (int i = 0; i < salesOrderEList.size(); i++) {
            if (salesOrderEList.get(i) != null && salesOrderEList.get(i).getArrayOfSalesOrderPaymentE() != null && salesOrderEList.get(i).getArrayOfSalesOrderPaymentE().size() != 0) {
                datas.addAll(salesOrderEList.get(i).getArrayOfSalesOrderPaymentE());
                length++;
            }
        }
    }


    @Override
    public MyBillDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bill_detail_collection_record, parent, false);
        return new MyBillDetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyBillDetailViewHolder holder, int position) {
        if (position == 0) {
            holder.view_title.setVisibility(View.VISIBLE);
        } else {
            holder.view_title.setVisibility(View.GONE);
        }
        /***收款记录***/
        if (datas.get(position) instanceof SalesOrderPaymentE) {
            holder.item_4_Table_Name.setText(((SalesOrderPaymentE) datas.get(position)).getPaymentItemName());
            holder.item_4_Money.setText(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(((SalesOrderPaymentE) datas.get(position)).getActuallyAmount())));
            if ("SYB01".equals(((SalesOrderPaymentE) datas.get(position)).getPaymentItemCode())) {
                //通联支付
                String refNumber = ((SalesOrderPaymentE) datas.get(position)).getRemark();//参考号
                holder.item_4_Serial_Number.setText("参考号:" + refNumber);
            } else {
                String number = ((SalesOrderPaymentE) datas.get(position)).getTransactionNumber();
                if (number == null || "".equals(number)) {
                    holder.item_4_Serial_Number.setVisibility(View.GONE);
                } else {
                    holder.item_4_Serial_Number.setVisibility(View.VISIBLE);
                    holder.item_4_Serial_Number.setText(context.getString(R.string.serial_number, number));
                }

            }
            String time_Start = DateUtils.anewFormat(((SalesOrderPaymentE) datas.get(position)).getCreateTime(), "yyyy-MM-dd HH:mm");
            holder.item_4_End_Time.setText(time_Start);
            holder.item_4_Bill_State.setText(((SalesOrderPaymentE) datas.get(position)).getCreateStaffName());
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyBillDetailViewHolder extends RecyclerView.ViewHolder {
        View view_title;

        LinearLayout item_4_Layout;
        TextView item_4_Table_Name;
        TextView item_4_Serial_Number;
        TextView item_4_End_Time;
        TextView item_4_Money;
        TextView item_4_Bill_State;

        public MyBillDetailViewHolder(View itemView) {
            super(itemView);
            view_title = itemView.findViewById(R.id.title_view);

            item_4_Layout = (LinearLayout) itemView.findViewById(R.id.bill_recode_recycler_item_4_layout);
            item_4_Table_Name = (TextView) itemView.findViewById(R.id.bill_recode_recycler_item_4_table_name);
            item_4_Serial_Number = (TextView) itemView.findViewById(R.id.bill_recode_recycler_item_4_serial_number);
            item_4_End_Time = (TextView) itemView.findViewById(R.id.bill_recode_recycler_item_4_end_time);
            item_4_Money = (TextView) itemView.findViewById(R.id.bill_recode_recycler_item_4_money);
            item_4_Bill_State = (TextView) itemView.findViewById(R.id.bill_recode_recycler_item_4_bill_state);
        }

        public MyBillDetailViewHolder setAppendText(int viewId, String text, int colorRes) {
            TextView view = (TextView) itemView.findViewById(viewId);
            SpannableString spanText = new SpannableString(text);
            spanText.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colorRes)), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            view.append(spanText);
            return this;
        }
    }
}
