package com.holderzone.intelligencepos.utils.keyboard;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.IBinder;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ScrollView;

import java.util.List;

/**
 * Created by tcw on 2017/5/8.
 */

public class SoftKeyboardUtil {

    /**
     * Android定义了一个属性，名字为windowSoftInputMode, 这个属性用于设置Activity主窗口与软键盘的交互模式
     * 该属性可选的值有两部分，一部分为软键盘的状态控制，控制软键盘是隐藏还是显示，另一部分是Activity窗口的调整，以便腾出空间展示软键盘
     *
     * stateUnspecified-未指定状态：当我们没有设置android:windowSoftInputMode属性的时候，软件默认采用的就是这种交互方式，系统会根据界面采取相应的软键盘的显示模式。
     * stateUnchanged-不改变状态：当前界面的软键盘状态，取决于上一个界面的软键盘状态，无论是隐藏还是显示。
     * stateHidden-隐藏状态：当设置该状态时，软键盘总是被隐藏，不管是否有输入的需求。
     * stateAlwaysHidden-总是隐藏状态：当设置该状态时，软键盘总是被隐藏，和stateHidden不同的是，当我们跳转到下个界面，如果下个页面的软键盘是显示的，而我们再次回来的时候，软键盘就会隐藏起来。
     * stateVisible-可见状态：当设置为这个状态时，软键盘总是可见的，即使在界面上没有输入框的情况下也可以强制弹出来出来。
     * stateAlwaysVisible-总是显示状态：当设置为这个状态时，软键盘总是可见的，和stateVisible不同的是，当我们跳转到下个界面，如果下个页面软键盘是隐藏的，而我们再次回来的时候，软键盘就会显示出来。
     *
     * adjustUnspecified-未指定模式：设置软键盘与软件的显示内容之间的显示关系。当你跟我们没有设置这个值的时候，这个选项也是默认的设置模式。在这中情况下，系统会根据界面选择不同的模式。
     * adjustResize-调整模式：该模式下窗口总是调整屏幕的大小用以保证软键盘的显示空间；这个选项不能和adjustPan同时使用，如果这两个属性都没有被设置，系统会根据窗口中的布局自动选择其中一个。
     * adjustPan-默认模式：该模式下通过不会调整来保证软键盘的空间，而是采取了另外一种策略，系统会通过布局的移动，来保证用户要进行输入的输入框肯定在用户的视野范围里面，从而让用户可以看到自己输入的内容。
     */

    /**
     * 进入Activity后不希望系统自动弹出软键盘
     *
     * 方法一：
     * Android:windowSoftInputMode="adjustUnspecified|stateHidden"
     *
     * 方法二：
     * EditText edit = (EditText) findViewById(R.id.edit); edit.clearFocus();
     *
     * 方法三：
     * EditText edit = (EditText) findViewById(R.id.edit);
     * InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
     * imm.hideSoftInputFromWindow(edit.getWindowToken(),0);
     *
     * 方法四：
     * EditText edit = (EditText) findViewById(R.id.edit);
     * edit.setInputType(InputType.TYPE_NULL);
     */


    /**
     * 当软键盘已经显示， 调用该方法软键盘将隐藏
     * 当软键盘已经隐藏， 调用该方法软键盘将重新显示(两次调用需要有点延时，需当软键盘已经隐藏时才是这么一个特性)
     *
     * @param activity
     */
    public static void hideSoftKeyboardByFlag(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment，目前测试都适用)
     *
     * @param activity 当前Activity
     */
    public static void hideSoftKeyboardByFocusView(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 隐藏软键盘(只适用于Activity，不适用于Fragment，目前测试都适用)
     *
     * @param activity 当前Activity
     */
    public static void hideSoftKeyboardByDecorView(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     *
     * @param context
     * @param view    一般是传入EditText的引用
     */
    public static void hideSoftKeyboardBySpecifiedView(Context context, View view) {
        if (view == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 隐藏软键盘(可用于Activity，Fragment)
     *
     * @param context
     * @param viewList 一般是传入EditText的引用列表
     */
    public static void hideSoftKeyboardBySpecifiedViews(Context context, List<View> viewList) {
        if (viewList == null) {
            return;
        }
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 注册touchEvent事件，实现点击键盘区域外隐藏的效果
     * @param activity
     */
    public static void registerTouchEvent(Activity activity) {
        registerTouchEvent(activity, null);
    }

    /**
     * 注册touchEvent事件，实现点击键盘区域外隐藏的效果
     * @param activity
     * @param content
     */
    public static void registerTouchEvent(Activity activity, ViewGroup content) {
        if (content == null) {
            content = (ViewGroup) activity.findViewById(android.R.id.content);
        }
        getScrollView(content, activity);
        content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                dispatchTouchEvent(activity, motionEvent);

                return false;
            }
        });
    }

    /**
     *
     * @param viewGroup
     * @param activity
     */
    private static void getScrollView(ViewGroup viewGroup, final Activity activity) {
        if (null == viewGroup) {
            return;
        }
        int count = viewGroup.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = viewGroup.getChildAt(i);
            if (view instanceof ScrollView) {
                ScrollView newDtv = (ScrollView) view;
                newDtv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        dispatchTouchEvent(activity, motionEvent);

                        return false;
                    }
                });
            } else if (view instanceof AbsListView) {
                AbsListView newDtv = (AbsListView) view;
                newDtv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        dispatchTouchEvent(activity, motionEvent);

                        return false;
                    }
                });
            } else if (view instanceof RecyclerView) {
                RecyclerView newDtv = (RecyclerView) view;
                newDtv.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        dispatchTouchEvent(activity, motionEvent);

                        return false;
                    }
                });
            } else if (view instanceof ViewGroup) {

                getScrollView((ViewGroup) view, activity);
            }
        }
    }

    /**
     * @param activity
     * @param ev
     * @return
     */
    private static boolean dispatchTouchEvent(Activity activity, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = activity.getCurrentFocus();
            if (null != v && isShouldHideInput(v, ev)) {
                hideSoftInput(activity, v.getWindowToken());
            }
        }
        return false;
    }
    /**
     * @param dialog
     * @param ev
     * @return
     */
    public static boolean dispatchTouchEvent(Dialog dialog, MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = dialog.getCurrentFocus();
            if (null != v && isShouldHideInput(v, ev)) {
                hideSoftInput(dialog.getContext(), v.getWindowToken());
            }
        }
        return false;
    }
    /**
     * @param v
     * @param event
     * @return
     */
    private static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v instanceof EditText) {
            Rect rect = new Rect();
            v.getHitRect(rect);
            if (rect.contains((int) event.getX(), (int) event.getY())) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param mActivity
     * @param token
     */
    private static void hideSoftInput(Activity mActivity, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
    /**
     * @param context
     * @param token
     */
    private static void hideSoftInput(Context context, IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
