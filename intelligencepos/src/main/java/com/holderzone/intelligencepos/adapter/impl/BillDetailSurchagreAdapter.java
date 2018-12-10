package com.holderzone.intelligencepos.adapter.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderAdditionalFeesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.utils.ArithUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 账单详情-附加费adapter
 * Created by chencao on 2017/6/5.
 */

public class BillDetailSurchagreAdapter extends RecyclerView.Adapter<BillDetailSurchagreAdapter.MyBillDetailViewHolder> {
    private Context context;
    private LayoutInflater inflater;
    private List<SalesOrderE> salesOrderEList;
    private List<Object> datas;

    public BillDetailSurchagreAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 设置数据
     *
     * @param salesOrderEList
     */
    public void setDatas(List<SalesOrderE> salesOrderEList) {
        if (this.salesOrderEList != null){
            if (equalList(this.salesOrderEList,salesOrderEList)) {
                return;
            }
        }
        this.salesOrderEList = salesOrderEList;
        if (this.salesOrderEList == null) {
            return;
        } else {
            setSurchargeData();
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
        setSurchargeData();
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
     * 附加费
     */
    private void setSurchargeData() {
        datas = new ArrayList<>();
        length = 0;
        for (int i = 0; i < salesOrderEList.size(); i++) {
            if (salesOrderEList.get(i) != null && salesOrderEList.get(i).getArrayOfSalesOrderAdditionalFeesE() != null && salesOrderEList.get(i).getArrayOfSalesOrderAdditionalFeesE().size() != 0) {
                datas.addAll(salesOrderEList.get(i).getArrayOfSalesOrderAdditionalFeesE());
                length++;
            }
        }
    }


    @Override
    public MyBillDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bill_detail_surchagre, parent, false);
        return new MyBillDetailViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyBillDetailViewHolder holder, int position) {
        if (position == 0){
            holder.view_title.setVisibility(View.VISIBLE);
        }else {
            holder.view_title.setVisibility(View.GONE);
        }
        /***附加费***/
        if (datas.get(position) instanceof SalesOrderAdditionalFeesE) {
            holder.item_3_Name.setText(((SalesOrderAdditionalFeesE) datas.get(position)).getName());
//            holder..setText(context.getString(R.string.amount, ((SalesOrderAdditionalFeesE) datas.get(position)).getPrice()));
            holder.item_3_Number.setText("x"+((SalesOrderAdditionalFeesE) datas.get(position)).getAdditionalFeesCount() );
            holder.item_3_Price.setText(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(((SalesOrderAdditionalFeesE) datas.get(position)).getSubTotal())));
        }
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    class MyBillDetailViewHolder extends RecyclerView.ViewHolder {
        View view_title;

        TextView item_3_Name;
        TextView item_3_Number;
        TextView item_3_Price;
        RelativeLayout item_3_Center_Layout;


        public MyBillDetailViewHolder(View itemView) {
            super(itemView);
            view_title = itemView.findViewById(R.id.title_view);

            item_3_Name = (TextView) itemView.findViewById(R.id.bill_detail_recode_item_3_name);
            item_3_Number = (TextView) itemView.findViewById(R.id.bill_detail_recode_item_3_number);
            item_3_Price = (TextView) itemView.findViewById(R.id.bill_detail_recode_item_3_price);
            item_3_Center_Layout = (RelativeLayout) itemView.findViewById(R.id.bill_detail_recode_item_3_center_layout);
        }

    }
}
