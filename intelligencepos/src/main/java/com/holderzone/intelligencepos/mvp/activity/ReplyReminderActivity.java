package com.holderzone.intelligencepos.mvp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.ReplyReminderContract;
import com.holderzone.intelligencepos.mvp.model.bean.UnReminderReplyContentE;
import com.holderzone.intelligencepos.mvp.presenter.ReplyReminderPresenter;
import com.holderzone.intelligencepos.utils.RegexExUtils;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LT on 2018-04-03.
 * 外卖订单回复页面
 */

public class ReplyReminderActivity extends BaseActivity<ReplyReminderContract.Presenter> implements ReplyReminderContract.View {
    public static final String ARGUMENT_EXTRA_GUID__KEY = "ARGUMENT_EXTRA_GUID__KEY";
    public static final String ARGUMENT_EXTRA_MSG_KEY = "ARGUMENT_EXTRA_MSG_KEY";
    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.remark_add_et)
    EditText mReplyAddEt;
    @BindView(R.id.add_remark)
    Button mAddReplay;
    @BindView(R.id.remark_rv)
    RecyclerView mReplayRv;
    @BindView(R.id.remark_sure)
    Button mReplaySure;
    /**
     * 全部的回复实体集合
     */
    private List<UnReminderReplyContentE> mTotalReplyContentList = new ArrayList<>();
    /**
     * adapter
     */
    private CommonAdapter<UnReminderReplyContentE> mAdapter;
    /**
     * 记录全部回复的GUID与位置的对应关系
     */
    private HashMap<String, Integer> mAllReplayMap = new HashMap<>();
    /**
     * 记录选中的回复GUID与是否选中的对应关系
     */
    private HashMap<String, Boolean> mSelectedReplayMap = new HashMap<>();
    /**
     * 外卖订单GUID
     */
    private String mUnOrderGUID;
    /**
     * 消息GUID
     */
    private String mReceiverMsgGUID;

    public static Intent newIntent(Context context, String unOrderGUID, String receiveMsgGUID) {
        Intent intent = new Intent(context, ReplyReminderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_EXTRA_GUID__KEY, unOrderGUID);
        bundle.putString(ARGUMENT_EXTRA_MSG_KEY, receiveMsgGUID);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mUnOrderGUID = extras.getString(ARGUMENT_EXTRA_GUID__KEY);
        mReceiverMsgGUID = extras.getString(ARGUMENT_EXTRA_MSG_KEY);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_reply_reminder;
    }

    @Nullable
    @Override
    protected ReplyReminderContract.Presenter initPresenter() {
        return new ReplyReminderPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        //初始化title
        initTitle();
        //初始化adapter
        initAdapter();
        //editText监听
        mReplyAddEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (!RegexExUtils.isDishesRemarksInputLegal(s)) {
                    s.delete(length - 1, length);
                    return;
                }
                if (RegexExUtils.isDishesRemarks(s)) {
                    mAddReplay.setEnabled(true);
                } else {
                    mAddReplay.setEnabled(false);
                }
            }
        });
    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<UnReminderReplyContentE>(this, R.layout.item_replay, mTotalReplyContentList) {
            @Override
            protected void convert(ViewHolder holder, UnReminderReplyContentE unReminderReplyContentE, int position) {
                Boolean isSelected = mSelectedReplayMap.get(unReminderReplyContentE.getUnReminderReplyContentGUID());
                holder.setBackgroundRes(R.id.tv_content_info, isSelected ? R.drawable.shape_flexbox_tv_bg_focused
                        : R.drawable.shape_flexbox_tv_bg);
                holder.setTextColorRes(R.id.tv_content_info, isSelected ? R.color.bg_dishes_type_item_normal
                        : R.color.layout_bg_disable);
                holder.setText(R.id.tv_content_info, unReminderReplyContentE.getContent());
            }
        };
        mAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                UnReminderReplyContentE replyContentE = mTotalReplyContentList.get(position);
                String replayGUID = replyContentE.getUnReminderReplyContentGUID();
                Boolean isSelected = mSelectedReplayMap.get(replayGUID);
                mSelectedReplayMap.put(replayGUID, !isSelected);
                //判断确定按钮是否可以点击
                canClickButton();
                mAdapter.notifyItemChanged(mAllReplayMap.get(replayGUID));
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        //设置adapter
        mReplayRv.setAdapter(mAdapter);
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(this);
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setFlexWrap(FlexWrap.WRAP);
        layoutManager.setJustifyContent(JustifyContent.FLEX_START);
        layoutManager.setAlignItems(AlignItems.STRETCH);
        mReplayRv.setLayoutManager(layoutManager);
    }

    private void initTitle() {
        mTitle.setTitleText("催单回复");
        mTitle.setOnReturnClickListener(this::finishActivity);
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        mPresenter.requestReminderReplyContent();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @OnClick({R.id.add_remark, R.id.remark_sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_remark:
                UnReminderReplyContentE unReminderReplyContentE = new UnReminderReplyContentE();
                unReminderReplyContentE.setContent(mReplyAddEt.getText().toString());
                mPresenter.addNewReminderReplyContent(unReminderReplyContentE);
                break;
            case R.id.remark_sure:
                List<UnReminderReplyContentE> selectedData = new ArrayList<>();
                for (UnReminderReplyContentE reminderReplyContentE : mTotalReplyContentList) {
                    if (mSelectedReplayMap.get(reminderReplyContentE.getUnReminderReplyContentGUID())) {
                        selectedData.add(reminderReplyContentE);
                    }
                }
                mPresenter.submitReplyReminder(mUnOrderGUID, mReceiverMsgGUID, selectedData);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestReminderReplyContentSucceed(List<UnReminderReplyContentE> arrayOfUnReminderReplyContentE) {
        //清空数据
        if (mTotalReplyContentList.size() > 0) {
            mTotalReplyContentList.clear();
        }
        mTotalReplyContentList.addAll(arrayOfUnReminderReplyContentE);
        //初始化对应关系map
        generateAllReplyHashMap();
        generateSelectedReplyHashMap();
        //更新adapter
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestReminderReplyContentFailed() {

    }

    @Override
    public void onSubmitReplyReminderSucceed() {
        finishActivity();
    }

    @Override
    public void onSubmitReplyReminderFailed() {

    }

    @Override
    public void onAddNewReminderReplyContentSucceed(UnReminderReplyContentE data) {
        //清空输入框
        mReplyAddEt.setText("");
        //添加新实体
        mTotalReplyContentList.add(0, data);
        //从新构造全部备注与position的对应关系
        generateAllReplyHashMap();
        //更新选中map
        mSelectedReplayMap.put(data.getUnReminderReplyContentGUID(), true);
        //判断确定按钮是否可以点击
        canClickButton();
        //更新Adapter
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAddNewReminderReplyContentFailed() {

    }

    private void generateAllReplyHashMap() {
        mAllReplayMap.clear();
        for (int i = 0; i < mTotalReplyContentList.size(); i++) {
            UnReminderReplyContentE unReminderReplyContentE = mTotalReplyContentList.get(i);
            mAllReplayMap.put(unReminderReplyContentE.getUnReminderReplyContentGUID(), i);
        }
    }

    private void generateSelectedReplyHashMap() {
        mSelectedReplayMap.clear();
        for (int i = 0; i < mTotalReplyContentList.size(); i++) {
            UnReminderReplyContentE unReminderReplyContentE = mTotalReplyContentList.get(i);
            mSelectedReplayMap.put(unReminderReplyContentE.getUnReminderReplyContentGUID(), false);
        }
    }

    /**
     * 判断确定按钮是否可以点击
     */
    private void canClickButton() {
        int i = 0;
        for (UnReminderReplyContentE reminderReplyContentE : mTotalReplyContentList) {
            if (mSelectedReplayMap.get(reminderReplyContentE.getUnReminderReplyContentGUID())) {
                i++;
            }
        }
        mReplaySure.setEnabled(i != 0);
    }
}
