package com.holderzone.intelligencepos.widget;

import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.holderzone.intelligencepos.R;


/**
 * @author: SvenHe(heshiweij@gmail.com)
 * @Date: 2016-04-25
 * @Time: 12:02
 * @des 圆形转圈的 View
 */
public class CircleProgress extends View {

    /**
     * 自定义属性：
     * <p/>
     * 1. 外层圆的颜色 roundColor
     * <p/>
     * 2. 弧形进度圈的颜色 rouncProgressColor
     * <p/>
     * 3. 中间百分比文字的颜色 progressTextColor
     * <p/>
     * 4. 中间百分比文字的大小 progressTextSize
     * <p/>
     * 5. 圆环的宽度（以及作为弧形进度的圆环宽度）
     * <p/>
     * 6. 圆环的风格（Paint.Style.FILL  Paint.Syle.Stroke）
     */


    private static final String TAG = "CircleProgress";

    private int mWidth;
    private int mHeight;

    private Paint mPaint;
    private Paint mProgressTextPaint;
    private Paint mIdentifyTextPaint;
    private Paint mPercentTextPaint;

    private float progress = 0f;
    private final float maxProgress = 100f; // 不可以修改的最大值

    //region 自定义属性的值
    int roundColor;
    int roundProgressColor;
    int mProgressTextColor;
    float mProgressTextSize;
    int mIdentifyTextColor;
    float mIdentifyTextSize;
    int mPercentTextColor;
    float mPercentTextSize;
    float mMarginBetweenProgressAndPercent;
    float mMarginBetweenProgressAndIdentify;
    //endregion

    // 画笔的粗细（默认为40f, 在 onLayout 已修改）
    private float mStrokeWidth = 40f;
    private ValueAnimator mAnimator;
    private float mLastProgress = -1;

    public CircleProgress(Context context) {
        this(context, null);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 初始化属性
        initAttrs(context, attrs, defStyleAttr);

        // 初始化点击事件
        initClickListener();
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = null;
        try {
            a = context.obtainStyledAttributes(attrs, R.styleable.CircleProgress);

            roundColor = a.getColor(R.styleable.CircleProgress_roundColor, Color.parseColor("#eeeeee"));
            roundProgressColor = a.getColor(R.styleable.CircleProgress_roundProgressColor, Color.parseColor("#2495ee"));
            mProgressTextColor = a.getColor(R.styleable.CircleProgress_progressTextColor, Color.parseColor("#2495ee"));
            mProgressTextSize = a.getDimension(R.styleable.CircleProgress_progressTextSize, 120f);
            mPercentTextColor = a.getColor(R.styleable.CircleProgress_percentTextColor, Color.parseColor("#2495ee"));
            mPercentTextSize = a.getDimension(R.styleable.CircleProgress_percentTextSize, 60f);
            mIdentifyTextColor = a.getColor(R.styleable.CircleProgress_identifyTextColor, Color.parseColor("#292929"));
            mIdentifyTextSize = a.getDimension(R.styleable.CircleProgress_identifyTextSize, 30f);
            mMarginBetweenProgressAndPercent = a.getDimension(R.styleable.CircleProgress_marginBetweenProgressAndPercent, 15f);
            mMarginBetweenProgressAndIdentify = a.getDimension(R.styleable.CircleProgress_marginBetweenProgressAndIdentify, 30f);
        } finally {
            // 注意，别忘了 recycle
            a.recycle();
        }
    }

    /**
     * 初始化点击事件
     */
    private void initClickListener() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                // 重新开启动画
                restartAnimate();
            }
        });
    }

    /**
     * 当开始布局时候调用
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // 获取总的宽高
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        // 初始化各种值
        initValue();

        // 设置圆环画笔
        setupPaint();

        // 设置文字画笔
        setupTextPaint();
    }

    /**
     * 初始化各种值
     */
    private void initValue() {
        // 画笔的粗细为总宽度的 1 / 25
        mStrokeWidth = mWidth / 40f;
    }

    /**
     * 设置圆环画笔
     */
    private void setupPaint() {
        // 创建圆环画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(roundColor);
        mPaint.setStyle(Paint.Style.STROKE); // 边框风格
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    /**
     * 设置文字画笔
     */
    private void setupTextPaint() {
        mProgressTextPaint = new Paint();
        mProgressTextPaint.setAntiAlias(true);
        mProgressTextPaint.setColor(mProgressTextColor);
        mProgressTextPaint.setTextSize(mProgressTextSize);

        mPercentTextPaint = new Paint();
        mPercentTextPaint.setAntiAlias(true);
        mPercentTextPaint.setColor(mPercentTextColor);
        mPercentTextPaint.setTextSize(mPercentTextSize);

        mIdentifyTextPaint = new Paint();
        mIdentifyTextPaint.setAntiAlias(true);
        mIdentifyTextPaint.setColor(mIdentifyTextColor);
        mIdentifyTextPaint.setTextSize(mIdentifyTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 第一步：绘制一个圆环
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(roundColor);

        float cx = mWidth / 2.0f;
        float cy = mHeight / 2.0f;
        float radius = mWidth / 2.0f - mStrokeWidth / 2.0f;
        canvas.drawCircle(cx, cy, radius, mPaint);

        // 第二步：绘制文字
        String progressText = ((int) (progress / maxProgress * 100)) + "";
        Rect progressBounds = new Rect();
        mProgressTextPaint.getTextBounds(progressText, 0, progressText.length(), progressBounds);

        String percentText = "%";
        Rect percentBounds = new Rect();
        mPercentTextPaint.getTextBounds(percentText, 0, percentText.length(), percentBounds);

        String identifyText = "同步数据";
        Rect identifyBounds = new Rect();
        mIdentifyTextPaint.getTextBounds(identifyText, 0, identifyText.length(), identifyBounds);

        canvas.drawText(progressText, mWidth / 2 - progressBounds.width() / 2, mHeight / 2 + progressBounds.height() / 4, mProgressTextPaint);
        canvas.drawText(percentText, mWidth / 2 + progressBounds.width() / 2 + mMarginBetweenProgressAndPercent, mHeight / 2 - 3 * progressBounds.height() / 4 + percentBounds.height(), mPercentTextPaint);
        canvas.drawText(identifyText, mWidth / 2 - identifyBounds.width() / 2, mHeight / 2 + progressBounds.height() / 4 + mMarginBetweenProgressAndIdentify + identifyBounds.height(), mIdentifyTextPaint);


        // 第三步：绘制动态进度圆环
        mPaint.setDither(true);
        mPaint.setStrokeJoin(Paint.Join.BEVEL);
        mPaint.setStrokeCap(Paint.Cap.SQUARE); //  设置笔触为圆形

        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setColor(roundProgressColor);
        RectF oval = new RectF(0 + mStrokeWidth / 2, 0 + mStrokeWidth / 2,
                mWidth - mStrokeWidth / 2, mHeight - mStrokeWidth / 2);

        canvas.drawArc(oval, -90, progress / maxProgress * 360, false, mPaint);
    }

    /**
     * 重新开启动画
     */
    private void restartAnimate() {
        if (mLastProgress > 0) {
            // 取消动画
            cancelAnimate();
            // 重置进度
            setProgress(0f);
            // 重新开启动画
            runAnimate(mLastProgress);
        }
    }

    /**
     * 设置当前显示的进度条
     *
     * @param progress
     */
    public void setProgress(float progress) {
        this.progress = progress;

        // 使用 postInvalidate 比 postInvalidat() 好，线程安全
        postInvalidate();
    }


    /**
     * 开始执行动画
     *
     * @param targetProgress 最终到达的进度
     */
    public void runAnimate(float targetProgress) {
        // 运行之前，先取消上一次动画
        cancelAnimate();

        mLastProgress = targetProgress;

        mAnimator = ValueAnimator.ofObject(new FloatEvaluator(), 0, targetProgress);
        // 设置差值器
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setProgress(value);
            }
        });

        mAnimator.setDuration((long) (targetProgress * 33));
        mAnimator.start();
    }

    /**
     * 开始执行动画
     *
     * @param targetProgress 最终到达的进度
     */
    public void runAnimate(float startProgress, float targetProgress) {
        // 运行之前，先取消上一次动画
        cancelAnimate();

        mLastProgress = targetProgress;

        mAnimator = ValueAnimator.ofObject(new FloatEvaluator(), startProgress, targetProgress);
        // 设置差值器
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                setProgress(value);
            }
        });

        mAnimator.setDuration((long) (targetProgress - startProgress) * 3);
        mAnimator.start();
    }

    /**
     * 取消动画
     */
    public void cancelAnimate() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }
}
