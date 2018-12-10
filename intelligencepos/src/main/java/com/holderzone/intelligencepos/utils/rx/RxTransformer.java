package com.holderzone.intelligencepos.utils.rx;

import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.base.BaseFragment;
import com.holderzone.intelligencepos.base.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * RxJava生命周期管理，界面进度条管理，线程切换之直接辅助类
 * Created by tcw on 2017/7/28.
 */

public class RxTransformer {

    public static <T> ObservableTransformer<T, T> applyTransformer(IView view) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> view.showLoading())
                        .subscribeOn(AndroidSchedulers.mainThread())//UI线程显示加载
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnDispose(view::onDispose)// UI线程取消订阅回调
                        .compose(RxTransformer.<T>bindUntilEventDestroy(view))//绑定生命周期
                        .doOnTerminate(view::hideLoading);//UI线程隐藏加载
            }
        };
    }

    public static <T> ObservableTransformer<T, T> applyTransformer(IView view, boolean showLoading, boolean hideLoading) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io())
                        .doOnSubscribe(disposable -> {
                            if (showLoading) {
                                view.showLoading();
                            }
                        })
                        .subscribeOn(AndroidSchedulers.mainThread())// UI线程显示加载
                        .observeOn(AndroidSchedulers.mainThread())// 切换到UI线程
                        .doOnDispose(view::onDispose)// UI线程取消订阅回调
                        .compose(RxTransformer.<T>bindUntilEventDestroy(view))// 绑定生命周期
                        .doOnTerminate(() -> {
                            if (hideLoading) {
                                view.hideLoading();//UI线程隐藏加载
                            }
                        });
            }
        };
    }

    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindToLifecycle();
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindToLifecycle();
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }

    public static <T> LifecycleTransformer<T> bindUntilEventPause(IView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindUntilEvent(ActivityEvent.PAUSE);
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindUntilEvent(FragmentEvent.PAUSE);
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }

    public static <T> LifecycleTransformer<T> bindUntilEventStop(IView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindUntilEvent(ActivityEvent.STOP);
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindUntilEvent(FragmentEvent.STOP);
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }

    public static <T> LifecycleTransformer<T> bindUntilEventDestroy(IView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindUntilEvent(ActivityEvent.DESTROY);
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindUntilEvent(FragmentEvent.DESTROY);
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }
}
