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
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.utils.helper.DeviceHelper;
import com.holderzone.intelligencepos.utils.rx.RxTransformer;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * 注册(RegisterActivity) 已协议页面(RegisterActivedFragment)
 * Created by tcw on 2017/3/28.
 */

public class RegisterActivedFragment extends BaseFragment {

    @BindView(R.id.tv_authorized_account)
    TextView mTvAuthorizedAccount;
    @BindView(R.id.tv_authorized_business)
    TextView mTvAuthorizedBusiness;
    @BindView(R.id.tv_device_id)
    TextView mTvDeviceId;

    private ActivedListener mActivedListener;

    public interface ActivedListener {
        void onStartUsingClick();

        void onUserAgreementClickFromActived();
    }

    public static RegisterActivedFragment newInstance() {
        Bundle args = new Bundle();
        RegisterActivedFragment fragment = new RegisterActivedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ActivedListener) {
            mActivedListener = (ActivedListener) context;
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
        return R.layout.fragment_register_actived;
    }

    @Override
    protected IPresenter initPresenter() {
        return null;
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        RepositoryImpl.getInstance().getEnterpriseInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(RxTransformer.bindToLifecycle(this))
                .subscribe(enterpriseInfo -> {
                    mTvAuthorizedAccount.setText(enterpriseInfo.getRegTel());
                    mTvAuthorizedBusiness.setText(enterpriseInfo.getName());
                    mTvDeviceId.setText(DeviceHelper.getInstance().getDeviceID());
                });
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
        if (mActivedListener != null) {
            mActivedListener = null;
        }
    }

    @OnClick({R.id.btn_start_using, R.id.tv_service_contract})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_start_using:
                if (mActivedListener != null) {
                    mActivedListener.onStartUsingClick();
                }
                break;
            case R.id.tv_service_contract:
                if (mActivedListener != null) {
                    mActivedListener.onUserAgreementClickFromActived();
                }
                break;
            default:
                break;
        }
    }
}
