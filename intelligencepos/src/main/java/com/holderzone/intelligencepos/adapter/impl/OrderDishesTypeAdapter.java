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
 * 点菜界面菜品类型adapter
 * Created by chencao on 2017/9/4.
 */

public class OrderDishesTypeAdapter extends BaseAdapter {
    private Context context;
    private List<DishesTypeE> dishesTypeEList;
    private LayoutInflater inflater;

    public OrderDishesTypeAdapter(Context context, List<DishesTypeE> dishesTypeEList) {
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
            convertView = inflater.inflate(R.layout.item_order_dishes_type,parent,false);
            holder = new MyViewHolder();
            holder.dishesItemLayout = (RelativeLayout) convertView.findViewById(R.id.layout_order_dishes_type);
            holder.dishesTypeName = (TextView) convertView.findViewById(R.id.tv_order_dishes_type_name);
            holder.dishesView = convertView.findViewById(R.id.view_select);
            convertView.setTag(holder);
        }else {
            holder = (MyViewHolder) convertView.getTag();
        }
        holder.dishesTypeName.setText(dishesTypeEList.get(position).getName());
        if(chickedGUID != null && chickedGUID.equals(dishesTypeEList.get(position).getDishesTypeGUID())){
            holder.dishesItemLayout.setBackgroundColor(context.getResources().getColor(R.color.btn_text_white_ffffff));
            holder.dishesTypeName.setTextColor(context.getResources().getColor(R.color.layout_bg_blue_2495ee));
            holder.dishesView.setVisibility(View.VISIBLE);
        }else {
            holder.dishesItemLayout.setBackgroundColor(context.getResources().getColor(R.color.btn_text_eff3f5));
            holder.dishesTypeName.setTextColor(context.getResources().getColor(R.color.btn_text_gray_555555));
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
