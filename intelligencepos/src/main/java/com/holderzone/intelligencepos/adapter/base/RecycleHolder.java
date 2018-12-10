package com.holderzone.intelligencepos.adapter.base;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.SparseArray;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.holderzone.intelligencepos.widget.recyclerview.base.ViewHolder;

/**
 * Created by 橘子桑 on 2016/1/2.
 */
public class RecycleHolder extends RecyclerView.ViewHolder {

    /**
     * 用于存储当前item当中的View
     */
    private SparseArray<View> mViews;

    public RecycleHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
    }

    public <T extends View> T findView(int ViewId) {
        View view = mViews.get(ViewId);
        //集合中没有，则从item当中获取，并存入集合当中
        if (view == null) {
            view = itemView.findViewById(ViewId);
            mViews.put(ViewId, view);
        }
        return (T) view;
    }

    public RecycleHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = findView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public RecycleHolder setText(int viewId, String text) {
        TextView tv = findView(viewId);
        tv.setText(text);
        return this;
    }
    public RecycleHolder setText(int viewId, CharSequence text) {
        TextView tv = findView(viewId);
        tv.setText(text);
        return this;
    }
    public RecycleHolder setBackgroundResource(int viewId, int resid) {
        View tv = findView(viewId);
        tv.setBackgroundResource(resid);
        return this;
    }

    public RecycleHolder setPadding(int viewId, int left, int top, int right, int bottom) {
        View tv = findView(viewId);
        tv.setPadding(left, top, right, bottom);
        return this;
    }

    public RecycleHolder clearOnCheckedChangeListener(int viewId) {
        CompoundButton compoundButton = findView(viewId);
        compoundButton.setOnCheckedChangeListener(null);
        return this;
    }

    public RecycleHolder setChecked(int viewId,boolean b){
        CompoundButton compoundButton = findView(viewId);
        compoundButton.setChecked(b);
        return this;
    }

    public RecycleHolder setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        CompoundButton compoundButton = findView(viewId);
        compoundButton.setOnCheckedChangeListener(listener);
        return this;
    }

    public RecycleHolder setAppendTextColor(int viewId, CharSequence str, int resColorId, int resDimen) {
        TextView tv = findView(viewId);
        SpannableString spannableString = new SpannableString(str);
        spannableString.setSpan(new ForegroundColorSpan(tv.getContext().getResources().getColor(resColorId)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan((int) tv.getContext().getResources().getDimension(resDimen)), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv.append(spannableString);
        return this;
    }

    public RecycleHolder setSelected(int viewId, boolean selected) {
        View tv = findView(viewId);
        tv.setSelected(selected);
        return this;
    }

    public RecycleHolder setEnabled(int viewId, boolean enabled) {
        TextView tv = findView(viewId);
        tv.setEnabled(enabled);
        return this;
    }

    public RecycleHolder setText(int viewId, int text) {
        TextView tv = findView(viewId);
        tv.setText(text);
        return this;
    }

    public RecycleHolder setTextColor(int viewId, int colorResId) {
        TextView tv = findView(viewId);
        tv.setTextColor(tv.getContext().getResources().getColor(colorResId));
        return this;
    }

    public RecycleHolder setImageResource(int viewId, int ImageId) {
        ImageView image = findView(viewId);
        image.setImageResource(ImageId);
        return this;
    }

    public RecycleHolder setImageBitmap(int viewId, Bitmap bitmap) {
        ImageView image = findView(viewId);
        image.setImageBitmap(bitmap);
        return this;
    }

    public RecycleHolder setImageNet(int viewId, String url) {
        ImageView image = findView(viewId);
        //使用你所用的网络框架等
        return this;
    }

    public RecycleHolder setVisible(int viewId, boolean visible) {
        View image = findView(viewId);
        image.setVisibility(visible ? View.VISIBLE : View.GONE);
        //使用你所用的网络框架等
        return this;
    }
    public RecycleHolder setVisible(int viewId, int visible) {
        View image = findView(viewId);
        image.setVisibility(visible);
        //使用你所用的网络框架等
        return this;
    }
    public RecycleHolder setVisible_invisible(int viewId, boolean visible) {
        View image = findView(viewId);
        image.setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
        //使用你所用的网络框架等
        return this;
    }

    public RecycleHolder setProgesss(int viewId, int percent) {
        ProgressBar progressBar = findView(viewId);
        progressBar.setProgress(percent);
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
    public RecycleHolder setAppendText(int viewId, int textRes,int colorRes) {
        TextView view = findView(viewId);
        SpannableString spanText = new SpannableString(view.getResources().getString(textRes));
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
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
    public RecycleHolder setAppendText(int viewId, int textRes,int resDimen,int colorRes) {
        TextView view = findView(viewId);
        SpannableString spanText = new SpannableString(view.getResources().getString(textRes));
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        spanText.setSpan(new AbsoluteSizeSpan((int) view.getContext().getResources().getDimension(resDimen)), 0, spanText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
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
    public RecycleHolder setAppendText(int viewId, String text,int colorRes) {
        TextView view = findView(viewId);
        SpannableString spanText = new SpannableString(text);
        spanText.setSpan(new ForegroundColorSpan(view.getResources().getColor(colorRes)), 0, spanText.length(),Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        view.append(spanText);
        return this;
    }
    public RecycleHolder setTextColorRes(int viewId, int textColorRes) {
        TextView view = findView(viewId);
        view.setTextColor(view.getResources().getColor(textColorRes));
        return this;
    }

}
