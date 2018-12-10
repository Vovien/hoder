package com.holderzone.intelligencepos.mvp.balancehes.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.balancehes.contract.HesMemberRedPackageContract;
import com.holderzone.intelligencepos.mvp.balancehes.presenter.HesMemberRedPackagePresenter;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.mvp.model.bean.RedPackageModel;
import com.holderzone.intelligencepos.utils.ArithUtil;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 红包
 */
public class HesMemberRedPackageActivity extends BaseActivity<HesMemberRedPackageContract.Presenter> implements HesMemberRedPackageContract.View {
    private static final String KEY_MEMBERINFO = "KEY_MEMBERINFO";
    private static final String KEY_SALESORDERGUID = "KEY_SALESORDERGUID";
    /**
     * 成功使用了多少张红包
     */
    public static final String KEY_USE_RED_PACKAGE_NUMBER = "KEY_USE_RED_PACKAGE_NUMBER";
    /**
     * 是否成功使用所有红包
     */
    public static final String KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE = "KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE";

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_return)
    LinearLayout llReturn;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.rl_layout_title)
    RelativeLayout rlLayoutTitle;
    @BindView(R.id.rv_content)
    RecyclerView rvContent;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.msv_context)
    MultiStateView msvContext;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.tv_empty_text)
    TextView tvEmpty;
    @BindView(R.id.btn_retry)
    Button btnRetry;


    private List<String> mCheckedRedPackageCord = new ArrayList<>();
    private String mSalesOrderGuid;
    private MemberInfoE mMemberInfo;
    /**
     * 红包列表
     */
    private List<RedPackageModel> mRedPackageModelList = new ArrayList<>();
    private CommonAdapter<RedPackageModel> mCommonAdapter;

    public static Intent newInstance(Context context, MemberInfoE mMemberInfo, String mSalesOrderGuid) {
        Intent intent = new Intent(context, HesMemberRedPackageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MEMBERINFO, mMemberInfo);
        bundle.putString(KEY_SALESORDERGUID, mSalesOrderGuid);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mMemberInfo = extras.getParcelable(KEY_MEMBERINFO);
        mSalesOrderGuid = extras.getString(KEY_SALESORDERGUID);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_red_package_hes;
    }

    @Override
    protected HesMemberRedPackageContract.Presenter initPresenter() {
        return new HesMemberRedPackagePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        tvTitle.setText("红包");
        ivSearch.setVisibility(View.GONE);
        llMenu.setVisibility(View.GONE);
        initClickEvent();

        if (mMemberInfo == null) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            ivEmpty.setVisibility(View.GONE);
            tvEmpty.setText(getString(R.string.hes_not_loading));
            return;
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_LOADING);
        }
        setButtonState();
        initRecycler();
    }

    /**
     * 设置按钮状态
     */
    private void setButtonState() {
        mCheckedRedPackageCord = removeDuplicate(mCheckedRedPackageCord);
        if (mCheckedRedPackageCord.size() == 0) {
            btnConfirm.setText("完成");
        } else {
            btnConfirm.setText(getString(R.string.hes_red_package, mCheckedRedPackageCord.size()));
        }
    }

    /**
     * list  去重
     *
     * @param list
     * @return
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<>(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    private void initRecycler() {
        mCommonAdapter = new CommonAdapter<RedPackageModel>(getApplicationContext(), R.layout.hes_balance_account_red_envelopes, mRedPackageModelList) {
            @Override
            protected void convert(ViewHolder holder, RedPackageModel redPackageModel, int position) {
                String redPacketName = redPackageModel.getRedPacketName();
                if (redPacketName.length() > 6) {
                    holder.setTextSize(R.id.tv_red_envelopes_name, 16);
                } else {
                    holder.setTextSize(R.id.tv_red_envelopes_name, 20);
                }
                holder.setVisible(R.id.space, position == mRedPackageModelList.size() - 1);
                holder.setText(R.id.tv_red_envelopes_name, redPacketName);
                holder.setText(R.id.tv_red_envelopes_code, getString(R.string.red_envelopes_code, redPackageModel.getQrCode()));
                holder.setText(R.id.tv_red_envelopes_amount, getString(R.string.amount_str, ArithUtil.stripTrailingZeros(redPackageModel.getMoney())));

                holder.setText(R.id.tv_red_envelopes_term_validity, getString(R.string.hes_coupon_term_validity, redPackageModel.getStartTime(), redPackageModel.getEndTime()));
                if (redPackageModel.getSelected()) {
                    holder.setImageResource(R.id.iv_checked, R.drawable.check_checked);
                } else {
                    holder.setImageResource(R.id.iv_checked, R.drawable.check_normal);
                }
                holder.setOnClickListener(R.id.ll_item_layout, view -> {
                    String code = redPackageModel.getQrCode();
                    boolean packageSelected = redPackageModel.getSelected();
                    redPackageModel.setSelected(!packageSelected);
                    if (packageSelected) {
                        mCheckedRedPackageCord.remove(code);
                    } else {
                        mCheckedRedPackageCord.add(code);
                    }
                    setButtonState();
                    notifyDataSetChanged();
                });
            }
        };

        rvContent.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvContent.setAdapter(mCommonAdapter);
    }

    @SuppressLint("CheckResult")
    private void initClickEvent() {
        RxView.clicks(llReturn)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    finishActivity();
                });

        RxView.clicks(btnRetry)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (mMemberInfo != null) {
                        mPresenter.getMemberRedPackageList(mSalesOrderGuid, mMemberInfo.getMemberInfoGUID());
                    }
                });

        RxView.clicks(btnConfirm)
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    mPresenter.requestUseRedPackage(mCheckedRedPackageCord, mSalesOrderGuid, mMemberInfo.getMemberInfoGUID());
                });

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        if (mMemberInfo != null) {
            mPresenter.getMemberRedPackageList(mSalesOrderGuid, mMemberInfo.getMemberInfoGUID());
        }
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void responseMemberRedEnvelopesListSuccess(List<RedPackageModel> redPackageModels) {
        mCheckedRedPackageCord.clear();
        if (redPackageModels == null || redPackageModels.size() == 0) {
            msvContext.setViewState(MultiStateView.VIEW_STATE_EMPTY);
            ivEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getString(R.string.hes_not_red_package));
            return;
        } else {
            msvContext.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        }
        mRedPackageModelList.clear();
        mRedPackageModelList.addAll(redPackageModels);
        for (RedPackageModel redPackageModel : redPackageModels) {
            if (redPackageModel.getSelected()) {
                mCheckedRedPackageCord.add(redPackageModel.getQrCode());
            }
        }
        setButtonState();
        if (mCommonAdapter != null) {
            mCommonAdapter.notifyDataSetChanged();
        } else {
            initRecycler();
        }
    }

    @Override
    public void responseMemberRedEnvelopesListFail() {
        msvContext.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void responseUseRedEnvelopesSuccess() {
        finishToOperation(true, "");
    }

    @Override
    public void responseUseRedEnvelopesNotAll(String number) {
        finishToOperation(false, number);
    }

    private void finishToOperation(boolean isUseAll, String number) {
        Intent intent = getIntent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_HAS_SUCCESS_USE_ALL_RED_PACKAGE, isUseAll);
        bundle.putString(KEY_USE_RED_PACKAGE_NUMBER, number);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finishActivity();
    }

    @Override
    public void responseUseRedEnvelopesFail() {
        mCheckedRedPackageCord.clear();
        mPresenter.getMemberRedPackageList(mSalesOrderGuid, mMemberInfo.getMemberInfoGUID());
    }

}
