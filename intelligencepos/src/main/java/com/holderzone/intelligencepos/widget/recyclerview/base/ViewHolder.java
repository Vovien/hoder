package com.holderzone.intelligencepos.widget.recyclerview.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.holderzone.intelligencepos.utils.DensityUtils;

public class ViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;
    private Context mContext;

    public ViewHolder(Context context, View itemView) {
        super(itemView);
        mContext = context;
        mConvertView = itemView;
        mViews = new SparseArray<View>();
    }


    public static ViewHolder createViewHolder(Context context, View itemView) {
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    public static ViewHolder createViewHolder(Context context,
                                              ViewGroup parent, int layoutId) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                false);
        ViewHolder holder = new ViewHolder(context, itemView);
        return holder;
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }


    /****以下为辅助方法*****/

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, CharSequence text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public ViewHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

    public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView view = getView(viewId);
        view.setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageDrawable(int viewId, Drawable drawable) {
        ImageView view = getView(viewId);
        view.setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int viewId, int color) {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    public ViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundRes);
        return this;
    }

    public ViewHolder setTextColor(int viewId, int textColor) {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    public ViewHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = getView(viewId);
        view.setTextColor(mContext.getResources().getColor(textColorRes));
        return this;
    }

    @SuppressLint("NewApi")
    public ViewHolder setAlpha(int viewId, float value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getView(viewId).setAlpha(value);
        } else {
            // Pre-honeycomb hack to set Alpha value
            AlphaAnimation alpha = new AlphaAnimation(value, value);
            alpha.setDuration(0);
            alpha.setFillAfter(true);
            getView(viewId).startAnimation(alpha);
        }
        return this;
    }

    public ViewHolder setVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public ViewHolder setTextSize(int viewId, float spSize) {
        TextView view = getView(viewId);
        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, spSize);
        return this;
    }

    public ViewHolder setInVisible(int viewId, boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public ViewHolder linkify(int viewId) {
        TextView view = getView(viewId);
        Linkify.addLinks(view, Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... viewIds) {
        for (int viewId : viewIds) {
            TextView view = getView(viewId);
            view.setTypeface(typeface);
            view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
        }
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress) {
        ProgressBar view = getView(viewId);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setProgress(int viewId, int progress, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        view.setProgress(progress);
        return this;
    }

    public ViewHolder setMax(int viewId, int max) {
        ProgressBar view = getView(viewId);
        view.setMax(max);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating) {
        RatingBar view = getView(viewId);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setRating(int viewId, float rating, int max) {
        RatingBar view = getView(viewId);
        view.setMax(max);
        view.setRating(rating);
        return this;
    }

    public ViewHolder setTag(int viewId, Object tag) {
        View view = getView(viewId);
        view.setTag(tag);
        return this;
    }

    public ViewHolder setTag(int viewId, int key, Object tag) {
        View view = getView(viewId);
        view.setTag(key, tag);
        return this;
    }

    public ViewHolder setChecked(int viewId, boolean checked) {
        Checkable view = (Checkable) getView(viewId);
        view.setChecked(checked);
        return this;
    }

    public ViewHolder setSelected(int viewId, boolean selected) {
        View view = getView(viewId);
        view.setSelected(selected);
        return this;
    }


    /**
     * 关于事件的
     */
    public ViewHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public ViewHolder setOnTouchListener(int viewId,
                                         View.OnTouchListener listener) {
        View view = getView(viewId);
        view.setOnTouchListener(listener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int viewId,
                                             View.OnLongClickListener listener) {
        View view = getView(viewId);
        view.setOnLongClickListener(listener);
        return this;
    }

    public ViewHolder setEnable(int viewId, boolean enable) {
        View view = getView(viewId);
        view.setEnabled(enable);
        return this;
    }

    /**
     * @param viewId
     * @param str
     * @param resColorId
     * @param resDimen
     * @return
     */
    public ViewHolder setAppendTextColor(int viewId, CharSequence str, int resColorId, int resDimen) {
        TextView tv = getView(viewId);
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(tv.getContext().getResources().getColor(resColorId)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan((int) tv.getContext().getResources().getDimension(resDimen)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spannableString);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param textRes
     * @param colorRes
     * @return
     */
    public ViewHolder setAppendText(int viewId, int textRes, int colorRes) {
        TextView view = getView(viewId);
        SpannableString spanText = new SpannableString(view.getResources().getString(textRes));
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.append(spanText);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param textRes
     * @param colorRes
     * @return
     */
    public ViewHolder setAppendText(int viewId, int textRes, float textSpSize, int colorRes) {
        TextView view = getView(viewId);
        SpannableString spanText = new SpannableString(view.getResources().getString(textRes));
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(view.getContext(), textSpSize)), 0, spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.append(spanText);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @param colorRes
     * @return
     */
    public ViewHolder setAppendText(int viewId, String text, float textSpSize, int colorRes) {
        TextView view = getView(viewId);
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan(DensityUtils.sp2px(view.getContext(), textSpSize)), 0, spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.append(spanText);
        return this;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @param colorRes
     * @return
     */
    public ViewHolder setAppendText(int viewId, String text, int colorRes) {
        TextView view = getView(viewId);
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.append(spanText);
        return this;
    }

}
