package com.holderzone.intelligencepos.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 注册(RegisterActivity) 协议页面(RegisterServiceContractFragment)
 * Created by tcw on 2017/3/28.
 */

public class RegisterServiceContractFragment extends BaseFragment {

    public static final String EXTRA_LAUNCH_FROM_ACTIVED = "EXTRA_LAUNCH_FROM_ACTIVED";
    public static final String EXTRA_LAUNCH_FROM_INACTIVED = "EXTRA_LAUNCH_FROM_INACTIVED";

    @BindView(R.id.tv_service_contract)
    TextView mTvServiceContract;

    /**
     * 是否是从已激活界面跳转来的
     */
    private boolean mLaunchFromActived;

    /**
     * 是否是从未激活界面跳转来的
     */
    private boolean mLaunchFromInactived;


    public static RegisterServiceContractFragment newInstance() {
        return newInstance(false, false);
    }

    public static RegisterServiceContractFragment newInstance(boolean launchFromActived, boolean launchFromInactived) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_LAUNCH_FROM_ACTIVED, launchFromActived);
        args.putBoolean(EXTRA_LAUNCH_FROM_INACTIVED, launchFromInactived);
        RegisterServiceContractFragment fragment = new RegisterServiceContractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private NextStepListener mNextStepListener;

    @OnClick({R.id.tv_next_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_next_step:
                if (mNextStepListener != null) {
                    mNextStepListener.onGoOnClick(mLaunchFromActived, mLaunchFromInactived);
                }
                break;
            default:
                break;
        }
    }

    public interface NextStepListener {
        void onGoOnClick(boolean launchFromActived, boolean launchFromInactived);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NextStepListener) {
            mNextStepListener = (NextStepListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(mBacklistener);
        return view;
    }

    private View.OnKeyListener mBacklistener = (view, i, keyEvent) -> {
        if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            if (i == KeyEvent.KEYCODE_BACK) {  //表示按返回键 时的操作
                if (!mLaunchFromActived && !mLaunchFromInactived) {
                    finishActivity();
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    };

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mLaunchFromActived = extras.getBoolean(EXTRA_LAUNCH_FROM_ACTIVED);
        mLaunchFromInactived = extras.getBoolean(EXTRA_LAUNCH_FROM_INACTIVED);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_register_service_contract;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTvServiceContract.setText(Html.fromHtml(getString(R.string.user_service_contract_content)));
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mNextStepListener != null) {
            mNextStepListener = null;
        }
    }
}
