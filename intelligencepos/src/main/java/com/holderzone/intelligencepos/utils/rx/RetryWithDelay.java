package com.holderzone.intelligencepos.utils.rx;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Function;


/**
 * 错误重试，重试时间间隔线性级
 * Created by tcw on 2017/6/9.
 */
public class RetryWithDelay implements Function<Observable<? extends Throwable>, Observable<?>> {
    public final String TAG = this.getClass().getSimpleName();

    private final int maxRetries;
    private final int retryDelayMillis;
    private int retryCount;

    public RetryWithDelay(int maxRetries, int retryDelayMillis) {
        this.maxRetries = maxRetries;
        this.retryDelayMillis = retryDelayMillis;
    }

    @Override
    public Observable<?> apply(Observable<? extends Throwable> observable) throws Exception {
        return observable
                .flatMap(new Function<Throwable, Observable<?>>() {
                    @Override
                    public Observable<?> apply(Throwable throwable) {
                        if (++retryCount <= maxRetries) {
                            // When this Observable calls onNext, the original Observable will be retried (i.e. re-subscribed).
                            Log.d(TAG, "get error, it will try after " + retryDelayMillis + " millisecond, retry count " + retryCount);
                            return Observable.timer(retryDelayMillis, TimeUnit.MILLISECONDS);
                        }
                        // Max retries hit. Just pass the error along.
                        return Observable.error(throwable);
                    }
                });
    }
}