package com.holderzone.intelligencepos.mvp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IPresenter;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册(RegisterActivity) 未激活页面(RegisterInactiveFragment)
 * Created by tcw on 2017/3/28.
 */

public class RegisterInactiveFragment extends BaseFragment {

    @BindView(R.id.tv_device_id)
    TextView mTvDeviceId;

    /**
     * 接口回调
     */
    private InactiveListener mInactiveListener;

    public interface InactiveListener {

        void onExitClick();

        void onUserAgreementClickFromInactive();
    }

    public static RegisterInactiveFragment newInstance() {
        Bundle args = new Bundle();
        RegisterInactiveFragment fragment = new RegisterInactiveFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof InactiveListener) {
            mInactiveListener = (InactiveListener) context;
        }
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {

    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.fragment_register_inactived;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mTvDeviceId.setText(DeviceHelper.getInstance().getDeviceID());
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
        if (mInactiveListener != null) {
            mInactiveListener = null;
        }
    }

    @OnClick({R.id.ll_exit, R.id.tv_service_contract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_exit:
                if (mInactiveListener != null) {
                    mInactiveListener.onExitClick();
                }
                break;
            case R.id.tv_service_contract:
                if (mInactiveListener != null) {
                    mInactiveListener.onUserAgreementClickFromInactive();
                }
                break;
            default:
                break;
        }
    }
}
