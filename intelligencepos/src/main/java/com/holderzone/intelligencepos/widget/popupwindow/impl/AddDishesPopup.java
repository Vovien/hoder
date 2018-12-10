package com.holderzone.intelligencepos.widget.popupwindow.impl;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.mvp.model.bean.MemberInfoE;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 加菜页面Popup（登录会员，刷新）
 */
public class AddDishesPopup extends RelativePopupWindow {

    @BindView(R.id.memberInfo)
    TextView memberInfo;
    @BindView(R.id.itemDivider)
    View itemDivider;
    @BindView(R.id.member_login_exit)
    View member;
    private MemberInfoE memberInfoE;
    private boolean isHes;
    private AddDishesPopupListener mOnItemClickListener;

    public interface AddDishesPopupListener {

        void onMemeberLoginExitClick(boolean isLogin);

        void onRefreshClick();
    }

    public AddDishesPopup(Context context, MemberInfoE memberInfoE, boolean isHes) {
        this(context, isHes);
        this.memberInfoE = memberInfoE;
        this.isHes = isHes;
    }

    public AddDishesPopup(Context context, boolean isHes) {
        this.isHes = isHes;
        View view = null;
        setContentView(view = LayoutInflater.from(context).inflate(R.layout.popwindow_add_dishes, null));
        ButterKnife.bind(this, view);
        if (isHes) {
            member.setVisibility(View.GONE);
            itemDivider.setVisibility(View.GONE);
        }
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // Disable default animation for circular reveal
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            setAnimationStyle(0);
//        }
        if (context instanceof AddDishesPopupListener) {
            mOnItemClickListener = (AddDishesPopupListener) context;
        }
    }

    @Override
    public void showOnAnchor(@NonNull View anchor, int vertPos, int horizPos, int x, int y) {
        super.showOnAnchor(anchor, vertPos, horizPos, x, y);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            circularReveal(anchor);
//        }
        if (memberInfoE != null) {
            memberInfo.setText(memberInfoE.getRegTel());
        } else {
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void circularReveal(@NonNull final View anchor) {
        final View contentView = getContentView();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                final int[] myLocation = new int[2];
                final int[] anchorLocation = new int[2];
                contentView.getLocationOnScreen(myLocation);
                anchor.getLocationOnScreen(anchorLocation);
                final int cx = anchorLocation[0] - myLocation[0] + anchor.getWidth() / 2;
                final int cy = anchorLocation[1] - myLocation[1] + anchor.getHeight() / 2;
                contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
                final int dx = Math.max(cx, contentView.getMeasuredWidth() - cx);
                final int dy = Math.max(cy, contentView.getMeasuredHeight() - cy);
                final float finalRadius = (float) Math.hypot(dx, dy);
                Animator animator = ViewAnimationUtils.createCircularReveal(contentView, cx, cy, 0f, finalRadius);
                animator.setDuration(200);
                animator.start();
            }
        });
    }

    @OnClick({R.id.member_login_exit, R.id.refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.member_login_exit:
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onMemeberLoginExitClick(memberInfoE != null);
                    dismiss();
                }
                break;
            case R.id.refresh:
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onRefreshClick();
                    dismiss();
                }
                break;
            default:
        }
    }
}