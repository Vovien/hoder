package com.holderzone.intelligencepos.dialog.impl;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2018-04-04.
 * 外卖换菜 选择作法弹框
 */

public class ChangeDishesInnerDishesPracticeDialogFragment extends BaseDialogFragment<ChangeDishesInnerDishesPracticeDialogFragment.ConfirmListener> {
    @BindView(R.id.dishes_name)
    TextView dishesName;
    @BindView(R.id.practice_recycler)
    RecyclerView practiceRecycler;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    /**
     * 接口
     */
    private ConfirmListener listener;
    /**
     * 菜品预定的做法集合
     */
    private List<DishesPracticeE> dishesPracticeEs = new ArrayList<>();
    private SalesOrderBatchDishesE salesOrderBatchDishesE;
    private static final String DISHESPRACTICE = "DishesPracticeE";
    private static final String SALESORDERBATCHDISHESE = "SalesOrderBatchDishesE";
    /**
     * 已经选择的菜品做法集合
     */
    private List<DishesPracticeE> choiceDishesPracticeEs = new ArrayList<>();
    private CommonAdapter<DishesPracticeE> dishesPracticeECommonAdapter;
    private String dishesEName;

    public static ChangeDishesInnerDishesPracticeDialogFragment newInstance(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE) {
        Bundle args = new Bundle();
        args.putParcelable(SALESORDERBATCHDISHESE, salesOrderBatchDishesE);
        args.putParcelableArrayList(DISHESPRACTICE, (ArrayList<? extends Parcelable>) dishesPracticeEs);
        ChangeDishesInnerDishesPracticeDialogFragment fragment = new ChangeDishesInnerDishesPracticeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        dishesPracticeEs = args.getParcelableArrayList(DISHESPRACTICE);
        salesOrderBatchDishesE = args.getParcelable(SALESORDERBATCHDISHESE);
        dishesEName = salesOrderBatchDishesE.getDishesName();
        choiceDishesPracticeEs = salesOrderBatchDishesE.getArrayOfDishesPracticeE();
    }

    @Override
    protected void setAttributesOnceCreate() {
        // 设置无标题
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_change_practice;
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
        //默认确定按钮不可点击
        btnConfirm.setEnabled(false);
        dishesPracticeECommonAdapter = new CommonAdapter<DishesPracticeE>(getContext(), R.layout.dialog_practice_choice_item, dishesPracticeEs) {
            @Override
            protected void convert(ViewHolder holder, DishesPracticeE dishesPracticeE, int position) {
                holder.setText(R.id.practice_choice_item, dishesPracticeE.getName() + "+" + getString(R.string.amount_str, ArithUtil.stripTrailingZeros(dishesPracticeE.getFees())));
                boolean isContain = false;
                if (choiceDishesPracticeEs != null) {
                    for (int i = 0; i < choiceDishesPracticeEs.size(); i++) {
                        if (choiceDishesPracticeEs.get(i).getDishesPracticeGUID().equalsIgnoreCase(dishesPracticeE.getDishesPracticeGUID())) {
                            isContain = true;
                        }
                    }
                }
                if (isContain) {
                    holder.setTextColorRes(R.id.practice_choice_item, R.color.common_text_color_2495ee);
                    holder.setBackgroundRes(R.id.practice_choice_item, R.drawable.shape_edit_choose_blue_bg);
                } else {
                    holder.setTextColorRes(R.id.practice_choice_item, R.color.confirm_text_color);
                    holder.setBackgroundRes(R.id.practice_choice_item, R.drawable.shape_edit_choose_gray_bg);
                }
            }
        };

        dishesPracticeECommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                if (choiceDishesPracticeEs == null) {
                    choiceDishesPracticeEs = new ArrayList<DishesPracticeE>();
                }
                boolean isRemove = false;
                for (int i = 0; i < choiceDishesPracticeEs.size(); i++) {
                    if (dishesPracticeEs.get(position).getDishesPracticeGUID().equalsIgnoreCase(choiceDishesPracticeEs.get(i).getDishesPracticeGUID())) {
                        isRemove = true;
                    }
                }
                if (isRemove) {
                    for (Iterator<DishesPracticeE> iterator = choiceDishesPracticeEs.iterator(); iterator.hasNext(); ) {
                        if (iterator.next().getDishesPracticeGUID().equalsIgnoreCase(dishesPracticeEs.get(position).getDishesPracticeGUID())) {
                            iterator.remove();
                        }
                    }
                } else {
                    choiceDishesPracticeEs.add(dishesPracticeEs.get(position));
                }
                //确定按钮是否可以点击
                btnConfirm.setEnabled(choiceDishesPracticeEs.size() > 0);
                dishesPracticeECommonAdapter.notifyDataSetChanged();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        practiceRecycler.setAdapter(dishesPracticeECommonAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(ConfirmListener confirmListener) {
        listener = confirmListener;
    }

    @OnClick({R.id.dishes_dialog_image, R.id.btn_cancel, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.dishes_dialog_image:
                dismiss();
                break;
            case R.id.btn_cancel:
                dismiss();
                break;
            case R.id.btn_confirm:
                if (listener != null) {
                    salesOrderBatchDishesE.setArrayOfDishesPracticeE(choiceDishesPracticeEs);
                    listener.onChoosePracticeClick(salesOrderBatchDishesE);
                }
                break;
            default:
                break;
        }
    }

    public interface ConfirmListener {
        void onChoosePracticeClick(SalesOrderBatchDishesE salesOrderBatchDishesE);
    }
}
