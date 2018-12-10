package com.holderzone.intelligencepos.adapter.impl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import static com.holderzone.intelligencepos.R.id;

/**
 * 账单记录
 * Created by chencao on 2017/6/5.
 */

public class BillRecordAdapter extends RecyclerView.Adapter<BillRecordAdapter.MyBillRecodeViewHolder> implements View.OnClickListener {
    private Context context;
    private LayoutInflater inflater;
    private List<SalesOrderE > datas;

    public BillRecordAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setDatas(List<SalesOrderE> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<SalesOrderE> datas){
        if (this.datas == null){
            this.datas = new ArrayList<>();
        }
        this.datas.addAll(this.datas.size(),datas);
        notifyDataSetChanged();
    }

    @Override
    public MyBillRecodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_bill_record_recycler,parent, false);
        view.setOnClickListener(this);
        return new MyBillRecodeViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(MyBillRecodeViewHolder holder, int position) {
        if (datas == null){
            return;
        }
        SalesOrderE salesOrderE = datas.get(position);
        if (salesOrderE == null){
            return;
        }
        if (salesOrderE.getTradeMode()  != null){
            switch (salesOrderE.getTradeMode()){
                case 0:
                    holder.imageView.setImageResource(R.drawable.tablefood);
                    holder.tableName.setText(salesOrderE.getDiningTableName() != null ?salesOrderE.getDiningTableName() :"桌台数据损坏" );
                    break;
                case 1:
                    holder.imageView.setImageResource(R.drawable.fastfood);
                    holder.tableName.setText("快餐");
                    break;
//                case 2:/**外卖**/
//                    break;

                default:
                    break;
            }
        }
//        if (salesOrderE.getTradeMode() == 0) {
//            holder.imageView.setImageResource(R.drawable.housefoodreceipt);
//            holder.tableName.setText(salesOrderE.getDiningTableName());
//        } else {
//            holder.imageView.setImageResource(R.drawable.fastfoodcashier);
//            holder.tableName.setText("快餐");
//        }
        holder.serialNumber.setText(context.getString(R.string.serial_number,salesOrderE.getSerialNumber()));
        String timeEnd = null;
        if (salesOrderE.getOrderStat() == -1){
            if (salesOrderE.getCancelTime() != null){
                timeEnd = DateUtils.anewFormat(salesOrderE.getCancelTime(),"yyyy-MM-dd HH:mm");
            }
        }else {
            if (salesOrderE.getChecOutkTime() != null){
                timeEnd = DateUtils.anewFormat(salesOrderE.getChecOutkTime(),"yyyy-MM-dd HH:mm");
            }
        }
        holder.endTime.setText(timeEnd == null ? " " : timeEnd);
        holder.money.setText(context.getString(R.string.amount_str, ArithUtil.stripTrailingZeros(salesOrderE.getPayTotal())));
        holder.billState.setText(salesOrderE.getOrderStatCN());

    }

    @Override
    public int getItemCount() {
        return datas == null? 0 : datas.size();
    }

    @Override
    public void onClick(View v) {

    }
    public SalesOrderE getItem(int position){
        return datas.get(position);
    }
    private OnItemClickListener listener;
    public interface OnItemClickListener{
        void getOnClickItemListener(int position,SalesOrderE salesOrderE);
    }

    public void setOnClickItem(OnItemClickListener listener){
        this.listener = listener;
    }

    public class MyBillRecodeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private OnItemClickListener listener;
        ImageView imageView;
        TextView tableName;
        TextView serialNumber;
        TextView endTime;
        TextView money;
        TextView billState;
        public MyBillRecodeViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(this);
            this.listener =listener;
            imageView = (ImageView) itemView.findViewById(id.bill_recode_recycler_item_image);
            tableName = (TextView) itemView.findViewById(id.bill_recode_recycler_table_name);
            serialNumber = (TextView) itemView.findViewById(id.bill_recode_recycler_serial_number);
            endTime = (TextView) itemView.findViewById(id.bill_recode_recycler_end_time);
            money = (TextView) itemView.findViewById(id.bill_recode_recycler_money);
            billState = (TextView) itemView.findViewById(id.bill_recode_recycler_bill_state);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.getOnClickItemListener(getLayoutPosition(),datas.get(getLayoutPosition()));
            }
        }
    }
}
