package com.holderzone.intelligencepos.adapter.impl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.mvp.model.bean.DishesTypeE;

import java.util.List;

/**
 * 快销左侧的适配器
 * Created by chencao on 2017/8/3.
 */

public class SnackNewOrderLeftAdapter extends BaseAdapter {
    private Context context;
    private List<DishesTypeE> dishesTypeEList;
    private LayoutInflater inflater;

    public SnackNewOrderLeftAdapter(Context context, List<DishesTypeE> dishesTypeEList) {
        this.context = context;
        this.dishesTypeEList = dishesTypeEList;
        inflater = LayoutInflater.from(context);
    }

    public void setDishesTypeEList(List<DishesTypeE> dishesTypeEList) {
        this.dishesTypeEList = dishesTypeEList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dishesTypeEList != null ? dishesTypeEList.size() :0;
    }

    @Override
    public Object getItem(int position) {
        return dishesTypeEList != null ?dishesTypeEList.get(position) :null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private String chickedGUID =null;
    public void setChickedGUID(String chickedGUID){
        this.chickedGUID = chickedGUID;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder holder ;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_snack_new_order_left,parent,false);
            holder = new MyViewHolder();
            holder.dishesTypeName = (TextView) convertView.findViewById(R.id.snack_new_order_left_name);
            holder.dishesItemLayout = (RelativeLayout) convertView.findViewById(R.id.snack_new_order_left_item_layout);
            holder.dishesView = convertView.findViewById(R.id.select_view);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.dishesTypeName.setText(dishesTypeEList.get(position).getName());
        if(chickedGUID != null && chickedGUID.equals(dishesTypeEList.get(position).getDishesTypeGUID())){
            holder.dishesItemLayout.setBackgroundColor(context.getResources().getColor(R.color.common_text_color_ffffff));
            holder.dishesTypeName.setTextColor(context.getResources().getColor(R.color.common_text_color_2495ee));
            holder.dishesView.setVisibility(View.VISIBLE);
        }else {
            holder.dishesItemLayout.setBackgroundColor(context.getResources().getColor(R.color.common_text_color_eff3f5));
            holder.dishesTypeName.setTextColor(context.getResources().getColor(R.color.common_text_color_555555));
            holder.dishesView.setVisibility(View.GONE);
        }
        return convertView;
    }
    private class MyViewHolder{
        RelativeLayout dishesItemLayout;
        View dishesView;
        TextView dishesTypeName;
    }
}
