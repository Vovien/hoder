package com.holderzone.intelligencepos.mvp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.dialog.impl.AddDishesRemarkDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.RetreatReasonSettingContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesReturnReasonE;
import com.holderzone.intelligencepos.mvp.presenter.RetreatReasonSettingPresenter;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-9-6.
 * 设置页面中的退菜原因
 */

public class RetreatDishesReasonFragment extends BaseFragment<RetreatReasonSettingContract.Presenter> implements RetreatReasonSettingContract.View, AddDishesRemarkDialogFragment.AddRemarkClickListener {
    @BindView(R.id.remark_setting_rv)
    RecyclerView mRetreatReasonRv;
    @BindView(R.id.content)
    MultiStateView mContent;
    @BindView(R.id.dishes_remark_bottom_add)
    Button mRetreatReasonBottomAdd;
    @BindView(R.id.dishes_remark_bottom_behind)
    LinearLayout mRetreatReasonBottomBehind;
    @BindView(R.id.dishes_remark_bottom)
    FrameLayout mRetreatReasonBottom;
    /**
     * 全部的备注实体集合
     */
    private List<DishesReturnReasonE> mAllRetreatReasonList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter mRetreatReasonAdapter;
    /**
     * 选中的菜品备注集合
     */
    private List<DishesReturnReasonE> mRetreatReasonSelected = new ArrayList<>();
    /**
     * 记录item的点击状态
     */
    private List<Integer> mSelectedList = new ArrayList<>();
    /**
     * 记录备注选中的个数
     */
    private int mSelectedCount;
    private static final String RETREAT_REASON_HINT = "填写退菜原因...";

    public static RetreatDishesReasonFragment newInstance() {
        return new RetreatDishesReasonFragment();
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_dishes_remark;
    }

    @Override
    protected RetreatReasonSettingContract.Presenter initPresenter() {
        return new RetreatReasonSettingPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化adapter
        initAdapter();
        //网络错误 点击刷新
        Button refresh = mContent.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(refresh).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> {
            mPresenter.requestRetreatReasons();
        });
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //请求退菜原因数据
        mPresenter.requestRetreatReasons();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onRequestRetreatReasonsSucceed(List<DishesReturnReasonE> arrayOfDishesReturnReasonE) {
        mContent.setVisibility(View.VISIBLE);
        //底部按钮状态设置
        mRetreatReasonBottom.setVisibility(View.VISIBLE);
        mRetreatReasonBottomBehind.setVisibility(View.GONE);
        mRetreatReasonBottomAdd.setVisibility(View.VISIBLE);
        mAllRetreatReasonList.clear();
        mAllRetreatReasonList.addAll(arrayOfDishesReturnReasonE);
        if (arrayOfDishesReturnReasonE.size() == 0) {
            //暂无备注
            mContent.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            mContent.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            //初始化Selected
            generateItemSelected(arrayOfDishesReturnReasonE);
            //刷新adapter
            mRetreatReasonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestRetreatReasonsFailed() {
        mContent.setVisibility(View.VISIBLE);
        mContent.setViewState(MultiStateView.VIEW_STATE_ERROR);
        mRetreatReasonBottom.setVisibility(View.GONE);
    }

    @Override
    public void onAddNewRetreatReasonSucceed(String msg, DishesReturnReasonE dishesReturnReasonE) {
        mPresenter.requestRetreatReasons();
    }


    @Override
    public void onAddNewRetreatReasonFailed(String retreatName) {
        getDialogFactory().showAddDishesRemarkDialog(retreatName, RETREAT_REASON_HINT, this);
    }

    @Override
    public void onDeleteRetreatReasonSucceed() {
        mPresenter.requestRetreatReasons();
    }

    @Override
    public void onDeleteRetreatReasonFailed() {
        showMessage("删除失败!");
    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.dishes_remark_bottom_add, R.id.dishes_remark_bottom_cancel, R.id.dishes_remark_bottom_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dishes_remark_bottom_add:
                //添加备注
                getDialogFactory().showAddDishesRemarkDialog("", RETREAT_REASON_HINT, this);
                break;
            case R.id.dishes_remark_bottom_cancel:
                //取消按钮
                mSelectedList.clear();
                for (int i = 0; i < mAllRetreatReasonList.size(); i++) {
                    mSelectedList.add(i, 0);
                }
                mRetreatReasonAdapter.notifyDataSetChanged();
                setState();
                break;
            case R.id.dishes_remark_bottom_delete:
                //删除菜品备注
                mRetreatReasonSelected.clear();
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (mSelectedList.get(i) == 1) {
                        mRetreatReasonSelected.add(mAllRetreatReasonList.get(i));
                    }
                }
                getDialogFactory().showConfirmDialog(getString(R.string.delete_retreat_reason_content), true, getString(R.string.cancel), R.drawable.selector_button_green, true, getString(R.string.affirm_button_delete), R.drawable.selector_button_red, new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {
                    }

                    @Override
                    public void onPosClick() {
                        mPresenter.deleteRetreatReason(mRetreatReasonSelected);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initAdapter() {
        mRetreatReasonAdapter = new CommonAdapter<DishesReturnReasonE>(getActivity(), R.layout.item_dishes_remark, mAllRetreatReasonList) {
            @Override
            protected void convert(ViewHolder holder, DishesReturnReasonE dishesRemarkE, int position) {
                int pos = mAllRetreatReasonList.indexOf(dishesRemarkE);
                Integer itemState = mSelectedList.get(pos);
                holder.setBackgroundRes(R.id.tv_content_info, itemState == 1 ? R.drawable.shape_flexbox_tv_bg_focused
                        : R.drawable.shape_flexbox_tv_bg);
                holder.setTextColorRes(R.id.tv_content_info, itemState == 1 ? R.color.btn_stroke_focused
                        : R.color.layout_bg_disable);
                holder.setText(R.id.tv_content_info, dishesRemarkE.getName());
                holder.setOnClickListener(R.id.tv_content_info, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mSelectedList.get(pos) == 0) {
                            mSelectedList.set(pos, 1);
                        } else {
                            mSelectedList.set(pos, 0);
                        }
                        mRetreatReasonAdapter.notifyDataSetChanged();
                        changeBottomEnable();
                    }
                });
            }
        };
        mRetreatReasonRv.setAdapter(mRetreatReasonAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        mRetreatReasonRv.setLayoutManager(layoutManager);
    }

    //改变底部删除按钮的状态
    private void changeBottomEnable() {
        mSelectedCount = 0;
        for (int j = 0; j < mSelectedList.size(); j++) {
            if (mSelectedList.get(j) == 1) {
                mSelectedCount++;
            }
        }
        //利用mSelectedCount是否为0来判断是否点击了备注item
        if (mSelectedCount > 0) {
            mRetreatReasonBottomAdd.setVisibility(View.GONE);
            mRetreatReasonBottomBehind.setVisibility(View.VISIBLE);
        } else {
            mRetreatReasonBottomAdd.setVisibility(View.VISIBLE);
            mRetreatReasonBottomBehind.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRemarkAdd(String remark) {
        mPresenter.addNewRetreatReason(remark);
    }

    private void generateItemSelected(List<DishesReturnReasonE> dishesRemarkEs) {
        if (mSelectedList.size() > 0) {
            mSelectedList.clear();
        }
        for (int i = 0; i < dishesRemarkEs.size(); i++) {
            mSelectedList.add(i, 0);
        }
    }

    //底部按钮状态设置
    private void setState() {
        mRetreatReasonBottomAdd.setVisibility(View.VISIBLE);
        mRetreatReasonBottomBehind.setVisibility(View.GONE);
    }
}
