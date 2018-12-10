package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
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
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.dialog.impl.AddDishesRemarkDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.contract.RemarkSettingContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.presenter.RemarkSettingPresenter;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-8-9.
 * 快餐备注管理页面
 */

public class RemarkSettingActivity extends BaseActivity<RemarkSettingContract.Presenter> implements RemarkSettingContract.View, AddDishesRemarkDialogFragment.AddRemarkClickListener {

    private static final String REMAREK_HINT = "填写备注...";

    @BindView(R.id.title)
    Title title;
    @BindView(R.id.remark_setting_rv)
    RecyclerView remarkSettingRv;
    @BindView(R.id.content)
    MultiStateView content;
    @BindView(R.id.dishes_remark_bottom_add)
    Button dishesRemarkBottomAdd;
    @BindView(R.id.dishes_remark_bottom_cancel)
    Button dishesRemarkBottomCancel;
    @BindView(R.id.dishes_remark_bottom_delete)
    Button dishesRemarkBottomDelete;
    @BindView(R.id.dishes_remark_bottom_behind)
    LinearLayout dishesRemarkBottomBehind;
    @BindView(R.id.dishes_remark_bottom)
    FrameLayout dishesRemarkBottom;

    /**
     * 全部的备注实体集合
     */
    private List<DishesRemarkE> mAllRemarkList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter mDishesRemarkAdapter;
    /**
     * 记录全部备注的GUID与位置的对应关系
     */
    private HashMap<String, Integer> mAllRemarkMap = new HashMap<>();
    /**
     * 选中的菜品备注集合
     */
    private List<DishesRemarkE> mDishesRemarkSelected = new ArrayList<>();
    /**
     * 记录item的点击状态
     */
    private List<Integer> mSelectedList = new ArrayList<>();
    /**
     * 记录备注选中的个数
     */
    private int mSelectedCount;

    public static Intent newIntent(Context context) {
        return new Intent(context, RemarkSettingActivity.class);
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_remark_setting;
    }

    @Override
    protected RemarkSettingContract.Presenter initPresenter() {
        return new RemarkSettingPresenter(this);
    }


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //初始化adapter
        initAdapter();
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //请求菜品备注数据
        mPresenter.requestDishesRemark();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void getDishesRemarkSuccess(List<DishesRemarkE> arrayOfDishesRemarkE) {
        content.setVisibility(View.VISIBLE);
        //底部按钮状态设置
        dishesRemarkBottom.setVisibility(View.VISIBLE);
        dishesRemarkBottomBehind.setVisibility(View.GONE);
        dishesRemarkBottomAdd.setVisibility(View.VISIBLE);
        mAllRemarkList.clear();
        mAllRemarkList.addAll(arrayOfDishesRemarkE);
        if (arrayOfDishesRemarkE.size() == 0) {
            //暂无备注
            content.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            content.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            //初始化Selected
            generateItemSelected(arrayOfDishesRemarkE);
            //刷新adapter
            mDishesRemarkAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addRemarkSuccess() {
        mPresenter.requestDishesRemark();
    }

    @Override
    public void deleteRemarkSuccess() {
        mPresenter.requestDishesRemark();
    }

    @Override
    public void showNetworkError() {
        content.setVisibility(View.VISIBLE);
        content.setViewState(MultiStateView.VIEW_STATE_ERROR);
        dishesRemarkBottom.setVisibility(View.GONE);
    }

    @Override
    public void addDishesRemarkFiled(String msg, String remark) {
        showMessage(msg);
        mDialogFactory.showAddDishesRemarkDialog(remark, REMAREK_HINT, this);
    }

    @Override
    public void onRemarkAdd(String remark) {
        mPresenter.requestAddDishesRemark(remark);
    }

    @Override
    public void onDispose() {

    }

    @OnClick({R.id.dishes_remark_bottom_add, R.id.dishes_remark_bottom_cancel, R.id.dishes_remark_bottom_delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dishes_remark_bottom_add:
                //添加备注
                mDialogFactory.showAddDishesRemarkDialog("", REMAREK_HINT, this);
                break;
            case R.id.dishes_remark_bottom_cancel:
                //取消按钮
                mSelectedList.clear();
                for (int i = 0; i < mAllRemarkList.size(); i++) {
                    mSelectedList.add(i, 0);
                }
                mDishesRemarkAdapter.notifyDataSetChanged();
                setState();
                break;
            case R.id.dishes_remark_bottom_delete:
                //删除菜品备注
                mDishesRemarkSelected.clear();
                for (int i = 0; i < mSelectedList.size(); i++) {
                    if (mSelectedList.get(i) == 1) {
                        mDishesRemarkSelected.add(mAllRemarkList.get(i));
                    }
                }
                mDialogFactory.showConfirmDialog(getString(R.string.delete_remark_content), true, getString(R.string.cancel), R.drawable.selector_button_green, true, getString(R.string.delete_remark_sure), R.drawable.selector_button_red, new ConfirmDialogFragment.ConfirmDialogListener() {
                    @Override
                    public void onNegClick() {
                    }

                    @Override
                    public void onPosClick() {
                        mPresenter.requestDeleteDishesRemark(mDishesRemarkSelected);
                    }
                });
                break;
            default:
                break;
        }
    }

    private void initTitle() {
        // title设置点击事件监听
        title.setOnReturnClickListener(this::finishActivity);
        // retry设置点击事件监听
        content.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry)
                .setOnClickListener(view -> mPresenter.requestDishesRemark());
    }

    private void initAdapter() {
        mDishesRemarkAdapter = new CommonAdapter<DishesRemarkE>(this, R.layout.item_dishes_remark, mAllRemarkList) {
            @Override
            protected void convert(ViewHolder holder, DishesRemarkE dishesRemarkE, int position) {
                int pos = mAllRemarkList.indexOf(dishesRemarkE);
                Integer itemState = mSelectedList.get(pos);
                holder.setBackgroundRes(R.id.tv_content_info, itemState == 1 ? R.drawable.shape_flexbox_tv_bg_focused
                        : R.drawable.shape_flexbox_tv_bg);
                holder.setTextColorRes(R.id.tv_content_info, itemState == 1 ? R.color.bg_dishes_type_item_normal
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
                        mDishesRemarkAdapter.notifyDataSetChanged();
                        changeBottomEnable();
                    }
                });
            }
        };
        remarkSettingRv.setAdapter(mDishesRemarkAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        remarkSettingRv.setLayoutManager(layoutManager);
    }

    private void generateItemSelected(List<DishesRemarkE> dishesRemarkEs) {
        if (mSelectedList.size() > 0) {
            mSelectedList.clear();
        }
        for (int i = 0; i < dishesRemarkEs.size(); i++) {
            mSelectedList.add(i, 0);
        }
    }

    /**
     * 改变底部删除按钮的状态
     */
    private void changeBottomEnable() {
        mSelectedCount = 0;
        for (int j = 0; j < mSelectedList.size(); j++) {
            if (mSelectedList.get(j) == 1) {
                mSelectedCount++;
            }
        }
        //利用mSelectedCount是否为0来判断是否点击了备注item
        if (mSelectedCount > 0) {
            dishesRemarkBottomAdd.setVisibility(View.GONE);
            dishesRemarkBottomBehind.setVisibility(View.VISIBLE);
        } else {
            dishesRemarkBottomAdd.setVisibility(View.VISIBLE);
            dishesRemarkBottomBehind.setVisibility(View.GONE);
        }
    }

    /**
     * 底部按钮状态设置
     */
    private void setState() {
        dishesRemarkBottomAdd.setVisibility(View.VISIBLE);
        dishesRemarkBottomBehind.setVisibility(View.GONE);
    }
}
