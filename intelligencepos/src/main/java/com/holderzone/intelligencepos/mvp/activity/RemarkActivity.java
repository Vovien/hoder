package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.RemarkContract;
import com.holderzone.intelligencepos.mvp.model.bean.DishesRemarkE;
import com.holderzone.intelligencepos.mvp.presenter.RemarkPresenter;
import com.holderzone.intelligencepos.mvp.snack.activity.SnackOrderDetailActivity;
import com.holderzone.intelligencepos.utils.EditInputStringFilter;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by LiTao on 2017-8-2.
 * 菜品备注页面（添加、选择菜品备注）
 */

public class RemarkActivity extends BaseActivity<RemarkContract.Presenter> implements RemarkContract.View {

    @BindView(R.id.title)
    Title title;
    @BindView(R.id.remark_rv)
    RecyclerView remarkRv;
    @BindView(R.id.et_remarks)
    EditText etRemarks;
    @BindView(R.id.tv_remark_number)
    TextView tvRemarkNumber;
    public static final String ARGUMENT_EXTRA_SELECTED_KEY = "ARGUMENT_EXTRA_SELECTED_KEY";
    public static final String ARGUMENT_EXTRA_NEW_SELECTED_KEY = "ARGUMENT_EXTRA_NEW_SELECTED_KEY";
    public static final String DISHES_ITEM_POSITION = "DISHES_ITEM_POSITION";
    public static final int DISHES_RESULT_CODE = 102;
    /**
     * 全部的备注实体集合
     */
    private List<DishesRemarkE> mAllRemarkList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter<DishesRemarkE> mDishesRemarkAdapter;
    private String inputRemarks;
    private boolean isFirstIn = false;
    /**
     * item的位置信息
     */
    private int mPosition;

    public static Intent newIntent(Context context, String mInputRemark, int position) {
        Intent intent = new Intent(context, RemarkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_EXTRA_SELECTED_KEY, mInputRemark);
        bundle.putInt(DISHES_ITEM_POSITION, position);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        inputRemarks = extras.getString(ARGUMENT_EXTRA_SELECTED_KEY, "");
        mPosition = extras.getInt(DISHES_ITEM_POSITION);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_dishes_remark;
    }

    @Override
    protected RemarkContract.Presenter initPresenter() {
        return new RemarkPresenter(this);
    }


    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //初始化adapter
        initAdapter();
        //修正数据
        tvRemarkNumber.setText("0/50字");
        isFirstIn = true;
        etRemarks.requestFocus();
        if (!StringUtils.isEmpty(inputRemarks)) {
            etRemarks.setText(inputRemarks);
        } else {
            etRemarks.setText("");
        }
        etRemarks.setSelection(etRemarks.length());
        //过滤备注信息
        etRemarks.setFilters(new InputFilter[]{new EditInputStringFilter(50)});
        etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    tvRemarkNumber.setText(s.length() + "/50字");
                    if (isFirstIn) {
                        etRemarks.setSelection(s.length());
                        isFirstIn = false;
                    }
                } else {
                    tvRemarkNumber.setText("0/50字");
                }
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void addDishesRemark(DishesRemarkE dishesRemarkE) {
        String checkRemarks = etRemarks.getText().toString();
        String newRemark = dishesRemarkE.getName();
        if (checkRemarks.length() + 1 + newRemark.length() > 50) {
            showMessage("最多填写50字");
            return;
        }

        if (StringUtils.isEmpty(checkRemarks)) {
            etRemarks.setText(newRemark);
        } else {
            etRemarks.setText(checkRemarks + "，" + newRemark);
        }
        etRemarks.setSelection(etRemarks.getText().length());
    }

    private void initTitle() {
        title.setTitleText("备注信息");
        title.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });
    }

    private void initAdapter() {
        mDishesRemarkAdapter = new CommonAdapter<DishesRemarkE>(getApplicationContext(), R.layout.choice_dishes_remark_item, mAllRemarkList) {
            @Override
            protected void convert(ViewHolder holder, DishesRemarkE dishesRemarkE, int position) {
                holder.setText(R.id.tv_remark, dishesRemarkE.getName());
                holder.setOnClickListener(R.id.tv_remark, view -> {
                    addDishesRemark(dishesRemarkE);
                    mDishesRemarkAdapter.notifyDataSetChanged();
                });
            }
        };
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getApplicationContext());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        remarkRv.setLayoutManager(layoutManager);
        remarkRv.setAdapter(mDishesRemarkAdapter);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        //请求备注信息数据
        mPresenter.requestDishesRemark();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.remark_clear, R.id.remark_sure})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.remark_clear:
                etRemarks.setText("");
                break;
            case R.id.remark_sure:
                String inputRemark = etRemarks.getText().toString();
                DishesRemarkE dishesRemarkE = new DishesRemarkE();
                dishesRemarkE.setName(inputRemark);
                dishesRemarkE.setType(0);
                mPresenter.addDishesRemark(dishesRemarkE);

                break;
            default:
                break;
        }
    }

    @Override
    public void getDishesRemarkSuccess(List<DishesRemarkE> arrayOfDishesRemarkE) {
        mAllRemarkList.clear();
        mAllRemarkList.addAll(arrayOfDishesRemarkE);
        mDishesRemarkAdapter.notifyDataSetChanged();
    }

    @Override
    public void getDishesRemarkFiled() {

    }

    @Override
    public void onAddDishesRemarkSucceed(String msg, DishesRemarkE dishesRemarkE) {
        Intent intent = new Intent(this, SnackOrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_EXTRA_NEW_SELECTED_KEY, dishesRemarkE);
        bundle.putInt(DISHES_ITEM_POSITION, mPosition);
        intent.putExtras(bundle);
        setResult(DISHES_RESULT_CODE, intent);
        finishActivity();
    }

    @Override
    public void onAddDishesRemarkFailed() {

    }

    @Override
    public void onDispose() {

    }
}
