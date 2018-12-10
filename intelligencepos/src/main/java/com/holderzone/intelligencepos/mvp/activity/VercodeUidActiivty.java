package com.holderzone.intelligencepos.mvp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.VercodeUidContract;
import com.holderzone.intelligencepos.mvp.presenter.VercodeUidPresenter;
import com.holderzone.intelligencepos.widget.Title;
import com.jakewharton.rxbinding2.view.RxView;
import com.jungly.gridpasswordview.GridPasswordView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class VercodeUidActiivty extends BaseActivity<VercodeUidContract.Presenter> implements VercodeUidContract.View {
    private static final String EXTRA_VERCODEUID = "EXTRA_VERCODEUID";
    private static final String EXTRA_REGTEL = "EXTRA_REGTEL";

    @BindView(R.id.title)
    Title mTitle;
    @BindView(R.id.gridPasswordView)
    GridPasswordView mGridPasswordView;
    @BindView(R.id.get_vercodeuid)
    TextView getVercodeuid;

    /**
     * 验证码
     */
    private String mVercodeuid;

    /**
     * 手机号
     */
    private String regTel;

    /**
     * 倒计时dispose
     */
    private Disposable mDisposable;


    /**
     * 静态方法，对外暴露需要的参数
     *
     * @param context    上下文
     * @param vercodeuid 验证码
     * @return Intent意图
     */
    public static Intent newIntent(Context context, String vercodeuid, String regTel) {
        Intent intent = new Intent(context, VercodeUidActiivty.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_VERCODEUID, vercodeuid);
        extras.putString(EXTRA_REGTEL, regTel);
        intent.putExtras(extras);
        return intent;
    }


    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
        mVercodeuid = extras.getString(EXTRA_VERCODEUID);
        regTel = extras.getString(EXTRA_REGTEL);
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {

    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.actiivty_verification;
    }

    @Nullable
    @Override
    protected VercodeUidContract.Presenter initPresenter() {
        return new VercodeUidPresenter(this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

        mTitle.setOnReturnClickListener(new Title.OnReturnClickListener() {
            @Override
            public void onReturnClick() {
                finishActivity();
            }
        });

        initPasswordEditText();

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        requestVerCode(false);

        // 发送短信按钮 防抖、监听，初始化请求
        RxView.clicks(getVercodeuid).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            requestVerCode(true);
        });
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onDispose() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null) {
            mDisposable.dispose();
            mDisposable = null;
        }
    }

    @OnClick({R.id.bt_number_1, R.id.bt_number_2, R.id.bt_number_3, R.id.bt_number_4, R.id.bt_number_5, R.id.bt_number_6, R.id.bt_number_7, R.id.bt_number_8, R.id.bt_number_9, R.id.bt_number_clear, R.id.bt_number_0, R.id.payment_keyboard_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_number_1:
                writePassword("1");
                break;
            case R.id.bt_number_2:
                writePassword("2");
                break;
            case R.id.bt_number_3:
                writePassword("3");
                break;
            case R.id.bt_number_4:
                writePassword("4");
                break;
            case R.id.bt_number_5:
                writePassword("5");
                break;
            case R.id.bt_number_6:
                writePassword("6");
                break;
            case R.id.bt_number_7:
                writePassword("7");
                break;
            case R.id.bt_number_8:
                writePassword("8");
                break;
            case R.id.bt_number_9:
                writePassword("9");
                break;
            case R.id.bt_number_clear:
//                mGridPasswordView.clearPassword();
                break;
            case R.id.bt_number_0:
                writePassword("0");
                break;
            case R.id.payment_keyboard_delete:
                writePassword("delete");
                break;
            default:
                break;
        }
    }

    private void writePassword(String s) {
        String mPassWord = mGridPasswordView.getPassWord();
        if (s.equals("delete")) {
            int length = mPassWord.length();
            if (length == 0) {
                return;
            }
            mPassWord = mPassWord.substring(0, length - 1);
            mGridPasswordView.clearPassword();
            mGridPasswordView.setPassword(mPassWord);
        } else {
            mPassWord += s;
            mGridPasswordView.setPassword(mPassWord);
            String passWord = mGridPasswordView.getPassWord();
            if (passWord.length() == 6) {
                mPresenter.submitResetPassword(regTel, passWord, mVercodeuid);
                mGridPasswordView.clearPassword();
            }
        }
    }

    private void initPasswordEditText() {
        mGridPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                mPresenter.submitResetPassword(regTel, psw, mVercodeuid);
                mGridPasswordView.clearPassword();
            }
        });
    }

    private void requestVerCode(boolean b) {
        Observable.intervalRange(0, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (aLong < 60) {
                            getVercodeuid.setTextColor(getResources().getColor(R.color.list_item_card_text_other));
                            getVercodeuid.setTextSize(13);
                            getVercodeuid.setText(String.valueOf(60 - aLong) + "s后重新发送验证码");
                        } else {
                            getVercodeuid.setClickable(true);
                            getVercodeuid.setTextColor(getResources().getColor(R.color.tv_text_blue));
                            getVercodeuid.setTextSize(16);
                            getVercodeuid.setText("获取验证码");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mDisposable != null) {
                            mDisposable = null;
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mDisposable != null) {
                            mDisposable = null;
                        }
                    }
                });
        getVercodeuid.setClickable(false);
        if (b) {
            mPresenter.requestVerCode(regTel);
        }
    }

    /**
     * 获取验证码失败
     *
     * @param msg 失败内容
     */
    @Override
    public void onGetVerCodeFailed(String msg) {
        showMessage(msg);
    }

    /**
     * 获取验证码成功
     *
     * @param verCode 验证码
     */
    @Override
    public void onGetVerCodeSuccess(String verCode) {
        mVercodeuid = verCode;
    }

    /**
     * 重置密码成功
     */
    @Override
    public void onResetPasswordSuccess() {
        showMessage("重置密码成功");
        new Handler().postDelayed(() -> finishActivity(), 1000);

    }
}
