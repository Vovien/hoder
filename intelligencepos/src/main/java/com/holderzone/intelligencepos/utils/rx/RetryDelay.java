package com.holderzone.intelligencepos.utils.rx;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * 错误重试，重试时间间隔指数级
 * Created by tcw on 2017/6/9.
 */

public class RetryDelay implements Function<Observable<? extends Throwable>, Observable<?>> {

    /**
     * 重试次数
     */
    private int mRetryCount;

    /**
     * 延迟基数
     */
    private int mDelayBase;

    /**
     * 计数器
     */
    private AtomicInteger mCounter = new AtomicInteger();

    public static RetryDelay newInstance() {
        return newInstance(3, 5);
    }

    public static RetryDelay newInstance(int retryCount, int delayBase) {
        return new RetryDelay(retryCount, delayBase);
    }

    private RetryDelay(int retryCount, int delayBase) {
        mRetryCount = retryCount;
        mDelayBase = delayBase;
    }

    @Override
    public Observable<?> apply(@NonNull Observable<? extends Throwable> observable) throws Exception {
        return observable.takeWhile(o -> mCounter.getAndIncrement() < mRetryCount)
                .flatMap(o -> Observable.timer((long) Math.pow(mDelayBase, mCounter.get()), TimeUnit.SECONDS));
    }
}
