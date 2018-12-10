package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.adapter.decoration.SolidLineItemDecoration;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by chencao on 2017/8/15.
 */

public class ReducePracticeDishesDialogFragment extends BaseDialogFragment {
    @BindView(R.id.dishes_name)
    TextView dishesName;
    @BindView(R.id.dishes_dialog_image)
    ImageView dishesDialogImage;
    @BindView(R.id.reduce_dishes_recycler)
    RecyclerView reduceDishesRecycler;
    @BindView(R.id.reduce_dishes_button_cancel)
    Button reduceDishesButtonCancel;
    @BindView(R.id.reduce_dishes_button_sure)
    Button reduceDishesButtonSure;
    private List<SalesOrderBatchDishesE> salesOrderBatchDishesEList = new ArrayList<>();
    /**
     * 菜品数量
     */
    private Double salesOrderBatchDishesECount;
    private CommonAdapter<SalesOrderBatchDishesE> dishesECommonAdapter;
    /**
     * 估清信息
     */
    private Double mEstimate = Double.MAX_VALUE;
    private static final String BATCHDISHESE = "SalesOrderBatchDishesE";
    private static final String ESTIMATE = "Estimate";
    private String dishesEName;

    public static ReducePracticeDishesDialogFragment newInstance(List<SalesOrderBatchDishesE> salesOrderBatchDishesEList, Double mEstimate) {
        Bundle args = new Bundle();
        args.putParcelableArrayList(BATCHDISHESE, (ArrayList<? extends Parcelable>) salesOrderBatchDishesEList);
        args.putDouble(ESTIMATE, mEstimate);
        ReducePracticeDishesDialogFragment dishesEDialogFragment = new ReducePracticeDishesDialogFragment();
        dishesEDialogFragment.setArguments(args);
        return dishesEDialogFragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        salesOrderBatchDishesEList = args.getParcelableArrayList(BATCHDISHESE);
        if (salesOrderBatchDishesEList != null && salesOrderBatchDishesEList.size() != 0){
            dishesEName = salesOrderBatchDishesEList.get(0).getDishesName();
        }
        mEstimate = args.getDouble(ESTIMATE);
    }

    /**
     * 计算该菜品所有点菜数量
     *
     * @return
     */
    private void getDishesECount() {
        salesOrderBatchDishesECount = 0d;
        if (salesOrderBatchDishesEList != null) {
            for (SalesOrderBatchDishesE salesOrderBatchDishesE : salesOrderBatchDishesEList) {
                salesOrderBatchDishesECount = ArithUtil.add(salesOrderBatchDishesECount, salesOrderBatchDishesE.getOrderCount());
            }
        }
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_reduce_dishese;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @Override
    protected void initView() {
        dishesName.setText(dishesEName);
        getDishesECount();
        dishesECommonAdapter = new CommonAdapter<SalesOrderBatchDishesE>(getContext(), R.layout.item_dishes_reduce_recycler, salesOrderBatchDishesEList) {
            @Override
            protected void convert(ViewHolder holder, SalesOrderBatchDishesE salesOrderBatchDishesE, int position) {
                StringBuffer practiceBuffer = new StringBuffer();
                if (salesOrderBatchDishesE.getArrayOfDishesPracticeE() != null){
                    for (DishesPracticeE dishesPracticeE: salesOrderBatchDishesE.getArrayOfDishesPracticeE()) {
                        practiceBuffer.append(dishesPracticeE.getName()+",");
                    }
                }
                holder.setText(R.id.reduce_dishes_name, (practiceBuffer.length() != 0)?practiceBuffer.substring(0,practiceBuffer.length()-1):"--");
                setDishesNumber(holder,salesOrderBatchDishesE.getOrderCount());
                if (salesOrderBatchDishesE.getOrderCount() == null || salesOrderBatchDishesE.getOrderCount() <= 0) {
                    holder.setVisible(R.id.reduce_dishes_image, false);
                    holder.setVisible(R.id.dishes_number, false);
                } else {
                    holder.setVisible(R.id.reduce_dishes_image, true);
                    holder.setVisible(R.id.dishes_number, true);
                }

                if (salesOrderBatchDishesECount != null && ArithUtil.sub(mEstimate, salesOrderBatchDishesECount) > 0) {
                    holder.setImageResource(R.id.add_dishes_image, R.drawable.add_blue_max);
                    holder.setTextColorRes(R.id.dishes_number, R.color.common_text_color_000000);
                } else {
                    holder.setImageResource(R.id.add_dishes_image, R.drawable.add_gray);
                    if (ArithUtil.sub(mEstimate, getRestCount(position)) < 0){
                        holder.setTextColorRes(R.id.dishes_number, R.color.tv_text_light_red);
                    }else {
                        holder.setTextColorRes(R.id.dishes_number, R.color.common_text_color_000000);
                    }
                }

                holder.setOnClickListener(R.id.reduce_dishes_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double count = salesOrderBatchDishesE.getOrderCount();
                        if (count == null || count <= 0) {
                            return;
                        } else if (count <= 1) {
                            salesOrderBatchDishesE.setOrderCount(0d);
                        } else {
                            salesOrderBatchDishesE.setOrderCount(ArithUtil.sub(count, 1));
                        }
                        getDishesECount();
                        notifyDataSetChanged();
                    }
                });

                holder.setOnClickListener(R.id.add_dishes_image, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Double count = salesOrderBatchDishesE.getOrderCount();
                        if (ArithUtil.sub(mEstimate, salesOrderBatchDishesECount) <= 0) {
                            callBack.callBackShowMessage(getString(R.string.add_dishes_failed_by_estimate));
                            return;
                        } else if (ArithUtil.sub(mEstimate, salesOrderBatchDishesECount) < 1) {
                            callBack.callBackShowMessage(getString(R.string.add_dishes_failed_by_estimate_small, salesOrderBatchDishesE.getDishesUnit()==null?"":salesOrderBatchDishesE.getDishesUnit()));
                            salesOrderBatchDishesE.setOrderCount(ArithUtil.add(count != null ? count : 0, ArithUtil.sub(mEstimate, salesOrderBatchDishesECount)));
                        } else {
                            salesOrderBatchDishesE.setOrderCount(ArithUtil.add(count != null ? count : 0, 1));
                        }
                        getDishesECount();
                        notifyDataSetChanged();
                    }
                });
            }
        };
        reduceDishesRecycler.addItemDecoration(new SolidLineItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        reduceDishesRecycler.setAdapter(dishesECommonAdapter);
    }

    private double getRestCount(int position){
        double count = 0d;
        if (salesOrderBatchDishesEList != null) {
            for (int i = 0;i<=position;i++){
                SalesOrderBatchDishesE salesOrderBatchDishesE = salesOrderBatchDishesEList.get(i);
                count = ArithUtil.add(count ,salesOrderBatchDishesE.getOrderCount());
            }
        }
        return count;
    }

    @Override
    protected void initData() {

    }

    private void setDishesNumber(ViewHolder holder,double dishesENumber){

        if (dishesENumber == (int)dishesENumber){
            holder.setText(R.id.dishes_number,(int)dishesENumber+"");
        }else {
            holder.setText(R.id.dishes_number, getString(R.string.two_decimal_str, ArithUtil.stripTrailingZeros(dishesENumber)));
        }
    }

    @Override
    public void setDialogListener(Object o) {

    }


    @OnClick({R.id.dishes_dialog_image,R.id.reduce_dishes_button_cancel, R.id.reduce_dishes_button_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dishes_dialog_image:
            case R.id.reduce_dishes_button_cancel:
                dismiss();
                break;
            case R.id.reduce_dishes_button_sure:
                callBack.callBackChange(salesOrderBatchDishesEList);
                dismiss();
                break;
            default:
                break;
        }
    }

    public interface CallBackReduceDialog{
        void callBackShowMessage(String message);
        void callBackChange(List<SalesOrderBatchDishesE>  salesOrderBatchDishesEList);
    }
    private CallBackReduceDialog callBack;
    public void setCallBack(CallBackReduceDialog callBack){
        this.callBack = callBack;
    }
}
