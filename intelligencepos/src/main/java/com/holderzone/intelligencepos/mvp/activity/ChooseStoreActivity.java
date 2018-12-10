package com.holderzone.intelligencepos.mvp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.ChooseStoreContract;
import com.holderzone.intelligencepos.mvp.model.bean.StoreE;
import com.holderzone.intelligencepos.mvp.model.bean.db.Store;
import com.holderzone.intelligencepos.mvp.presenter.ChooseStorePresenter;
import com.holderzone.intelligencepos.utils.AppManager;
import com.holderzone.intelligencepos.widget.Title;
import com.holderzone.intelligencepos.widget.recyclerview.CommonAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.MultiItemTypeAdapter;
import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;

/**
 * 选择门店界面
 * Created by tcw on 2017/6/6.
 */

public class ChooseStoreActivity extends BaseActivity<ChooseStoreContract.Presenter> implements ChooseStoreContract.View {
    private final static String EXTRA_ARGUMENT_LAUNCH_FROM_USER_LOGIN_KEY = "cn.holdzone.intelligencepos.LaunchFromUserLogin";
    private final static String EXTRA_ARGUMENT_SOTRE_GUID_SELECTED_KEY = "cn.holdzone.intelligencepos.StoreGuidSelected";

    private static final String KEY_STORE_GUID_SELECTED = "STORE_GUID_SELECTED";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.rv_store)
    RecyclerView mRvStore;
    @BindView(R.id.btn_confirm)
    Button mBtnConfirm;
    @BindView(R.id.multi_state_view)
    MultiStateView mMultiStateView;

    /**
     * 门店列表适配器
     */
    private CommonAdapter<StoreE> mCommonAdapter;

    /**
     * 门店数据
     */
    private List<StoreE> mStoreEList = new ArrayList<>();

    /**
     * 是否从登录fragment跳转过来
     */
    private boolean mLaunchFromUserLogin;

    /**
     * 预先选择的门店GUID
     */
    private String mStoreGuidPreSelected;

    /**
     * 当前选择的门店GUID
     */
    private String mStoreGUIDSelected;

    /**
     * @param context
     * @return
     */
    public static Intent newIntent(Context context) {
        return newIntent(context, false, null);
    }

    /**
     * @param context
     * @param storeGUIDSelected
     * @return
     */
    public static Intent newIntent(Context context, boolean launchFromUserLogin, String storeGUIDSelected) {
        Intent intent = new Intent(context, ChooseStoreActivity.class);
        Bundle extras = new Bundle();
        extras.putBoolean(EXTRA_ARGUMENT_LAUNCH_FROM_USER_LOGIN_KEY, launchFromUserLogin);
        extras.putString(EXTRA_ARGUMENT_SOTRE_GUID_SELECTED_KEY, storeGUIDSelected);
        intent.putExtras(extras);
        return intent;
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mLaunchFromUserLogin = extras.getBoolean(EXTRA_ARGUMENT_LAUNCH_FROM_USER_LOGIN_KEY);
        mStoreGuidPreSelected = extras.getString(EXTRA_ARGUMENT_SOTRE_GUID_SELECTED_KEY);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
        mStoreGUIDSelected = savedInstanceState.getString(KEY_STORE_GUID_SELECTED);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_choose_store;
    }

    @Override
    protected ChooseStoreContract.Presenter initPresenter() {
        return new ChooseStorePresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        if (!mLaunchFromUserLogin) {
            // 确认按钮 添加防抖、设置监听
            RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS)
                    .subscribe(o -> mPresenter.saveSelectedStoreThenLaunch(getStoreSelected()));
            // 返回按钮
            mTitle.setReturnVisibility(false);
        } else {
            if (mStoreGuidPreSelected == null) {
                // 确认按钮 添加防抖、设置监听
                RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(o -> mPresenter.saveSelectedStoreThenReturn(getStoreSelected()));
                // 返回按钮
                mTitle.setReturnVisibility(false);
            } else {
                // 设置选中
                if (mStoreGUIDSelected == null) {
                    mStoreGUIDSelected = mStoreGuidPreSelected;
                }
                // 确认按钮 添加防抖、设置监听
                RxView.clicks(mBtnConfirm).throttleFirst(1, TimeUnit.SECONDS)
                        .subscribe(o -> mPresenter.saveSelectedStoreThenReturn(getStoreSelected()));
                // 返回按钮
                mTitle.setReturnVisibility(true);
                mTitle.setOnReturnClickListener(() -> {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                });
            }
        }
        // 门店adapter初始化
        mCommonAdapter = new CommonAdapter<StoreE>(ChooseStoreActivity.this, R.layout.item_login_choose_store, mStoreEList) {
            @Override
            protected void convert(ViewHolder holder, StoreE store, int position) {
                if (mStoreGUIDSelected == null || !mStoreGUIDSelected.equalsIgnoreCase(store.getStoreGUID())) {
                    holder.setInVisible(R.id.iv_store_selected, false);
                    holder.setImageResource(R.id.iv_store_identify_selected, R.drawable.store);
                } else {
                    holder.setInVisible(R.id.iv_store_selected, true);
                    holder.setImageResource(R.id.iv_store_identify_selected, R.drawable.store_selected);
                    mBtnConfirm.setEnabled(true);
                }
                if (store.getCode() == null || "null".equalsIgnoreCase(store.getCode())) {
                    holder.setText(R.id.tv_store_name, store.getName());
                } else {
                    holder.setText(R.id.tv_store_name, store.getName() + "[" + store.getCode() + "]");
                }
                if (store.getAddress() == null) {
                    holder.setVisible(R.id.tv_store_address, false);
                } else {
                    holder.setVisible(R.id.tv_store_address, true);
                    holder.setText(R.id.tv_store_address, store.getAddress());
                }
            }
        };
        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                StoreE storeE = mStoreEList.get(position);
                String storeGUID = storeE.getStoreGUID();
                if (mStoreGUIDSelected == null || !mStoreGUIDSelected.equalsIgnoreCase(storeGUID)) {
                    mStoreGUIDSelected = storeGUID;
                    mCommonAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
        mRvStore.setAdapter(mCommonAdapter);
        mRvStore.setLayoutManager(new LinearLayoutManager(ChooseStoreActivity.this));
        // MulitStateView
        Button btnRetry = (Button) mMultiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(btnRetry).throttleFirst(1, TimeUnit.SECONDS).subscribe(o -> mPresenter.requestStoreEList());
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        // 请求门店列表数据
        mPresenter.requestStoreEList();
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_STORE_GUID_SELECTED, mStoreGUIDSelected);
    }

    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mLaunchFromUserLogin || mStoreGuidPreSelected == null) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (System.currentTimeMillis() - mExitTime > 2000) {
                    showMessage("再按一次退出程序");
                    mExitTime = System.currentTimeMillis();
                } else {
                    AppManager.getInstance().AppExit(this);
                }
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        setResult(Activity.RESULT_CANCELED);
        finishActivity();
    }

    /*************************view callback begin***************************/

    @Override
    public void onStoreEListObtainSucceed(List<StoreE> storeEList) {
        // 更新数据源
        mStoreEList.clear();
        mStoreEList.addAll(storeEList);
        if (mStoreEList.size() == 0) {
            // 切换到空布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            // 切换到内容布局
            mMultiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            // 刷新adapter
            mCommonAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStoreEListObtainFailed() {
        // 切换到网络错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onNetworkError() {
        // 切换到网络错误布局
        mMultiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
    }

    @Override
    public void onDispose() {

    }

    @Override
    public void onSaveSelectedStoreSucceedThenReturn(Store store) {
        // 返回UserLoginActivity
        Bundle extras = new Bundle();
        extras.putParcelable("test", store);
        Intent intent = new Intent();
        intent.putExtras(extras);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onSaveSelectedStoreSucceedThenLaunch(Store store) {
        // 启动UserLoginActivity
        startActivity(UserLoginActivity.newIntent(ChooseStoreActivity.this));
        finish();
    }

    /*************************view callback end***************************/

    /**
     * 获取当前选中的Store实体
     *
     * @return
     */
    private Store getStoreSelected() {
        for (Store store : mStoreEList) {
            if (mStoreGUIDSelected.equalsIgnoreCase(store.getStoreGUID())) {
                return store;
            }
        }
        return null;
    }
}
