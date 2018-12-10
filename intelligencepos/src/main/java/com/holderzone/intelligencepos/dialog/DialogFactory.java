package com.holderzone.intelligencepos.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.dialog.impl.AddDishesRemarkDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ChangeDishesInnerDishesPracticeDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ChangeOrderDishesCountDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmChangeDishesDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ConfirmDialogFragment;
import com.holderzone.intelligencepos.mvp.hall.dialog.DineDishesOperationDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.DiningNumberDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.DishesOperationDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ForceLogOutDialog;
import com.holderzone.intelligencepos.dialog.impl.InputPasswordDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.IntervalDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.MsgDialog;
import com.holderzone.intelligencepos.dialog.impl.PaymentOfAllinpayDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.PaymentOfAllinpayReturnDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.PrintErrorDialog;
import com.holderzone.intelligencepos.dialog.impl.ProgressDialog;
import com.holderzone.intelligencepos.dialog.impl.ProgressDialogFragment;
import com.holderzone.intelligencepos.dialog.impl.ResetPasswordDialogFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DishesPracticeE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchDishesE;
import com.holderzone.intelligencepos.utils.AppManager;

import java.util.List;

public class DialogFactory {

    /**
     * 对话框的tag
     */
    private static final String DIALOG_PROGRESS_TAG = ProgressDialogFragment.class.getSimpleName();
    private static final String DIALOG_CONFIRM_TAG = ConfirmDialogFragment.class.getSimpleName();
    private static final String DIALOG_INPUT_PASSWORD_TAG = InputPasswordDialogFragment.class.getSimpleName();
    private static final String DIALOG_RESET_PASSWORD_TAG = ResetPasswordDialogFragment.class.getSimpleName();
    private static final String DIALOG_INTERVAL_TAG = IntervalDialogFragment.class.getSimpleName();
    private static final String DIALOG_DISHES_OPERATION_TAG = DishesOperationDialogFragment.class.getSimpleName();
    private static final String DIALOG_DINE_DISHES_OPERATION_TAG = DineDishesOperationDialogFragment.class.getSimpleName();
    private static final String DIALOG_ADD_REMARK_TAG = AddDishesRemarkDialogFragment.class.getSimpleName();
    private static final String DIALOG_PAYMET_ALLIN_TAG = PaymentOfAllinpayDialogFragment.class.getSimpleName();
    private static final String DIALOG_PAYMET_ALLIN_RETURN_TAG = PaymentOfAllinpayReturnDialogFragment.class.getSimpleName();
    private static final String DIALOG_CHANGE_PRACTICE_TAG = ChangeDishesInnerDishesPracticeDialogFragment.class.getSimpleName();
    private static final String DIALOG_CONFIRM_CHANGE_DIAHES_TAG = ConfirmChangeDishesDialogFragment.class.getSimpleName();
    private static final String DIALOG_CHANGE_ORDER_DISHES_COUNT_TAG = ChangeOrderDishesCountDialogFragment.class.getSimpleName();
    private static final String DIALOG_DINING_NUMBER_TAG = DiningNumberDialogFragment.class.getSimpleName();

    /**
     * fragment manager
     */
    private FragmentManager mFragmentManager;

    // dialog
    private ProgressDialog mProgressDialog;

    // system dialog
    private static PrintErrorDialog sPrintErrorDialog;
    private static ForceLogOutDialog sForceLogOutDialog;

    public DialogFactory(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    /**
     * 显示强制退出系统级对话框，仅退出
     *
     * @param context 上下文
     * @param msg     强制退出提示消息
     */
    public static void showForceLogoutDialog(Context context, String msg) {
        if (sForceLogOutDialog == null || !sForceLogOutDialog.isShowing()) {
            sForceLogOutDialog = new ForceLogOutDialog(context, msg);
            sForceLogOutDialog.setOnClickListener(() -> AppManager.getInstance().AppExit(context));
            sForceLogOutDialog.show();
        }
    }

    /**
     * 显示强制退出系统级对话框，清除临时数据
     *
     * @param context 上下文
     * @param msg     强制退出提示消息
     */
    public static void showForceLogoutDialogResultDataClear(Context context, String msg) {
        if (sForceLogOutDialog == null || !sForceLogOutDialog.isShowing()) {
            sForceLogOutDialog = new ForceLogOutDialog(context, msg);
            sForceLogOutDialog.setOnClickListener(() -> AppManager.getInstance().AppExitWithDataClear(context));
            sForceLogOutDialog.show();
        }
    }

    /**
     * 显示强制退出系统级对话框，关闭服务，清除临时数据
     *
     * @param context 上下文
     * @param msg     强制退出提示消息
     */
    public static void showForceLogoutDialogWithServiceShutDownResultDataClear(Context context, String msg) {
        dissmissPrintErrorDialog();
        BaseApplication.getApplication().stopMqttService();
        showForceLogoutDialogResultDataClear(context, msg);
    }

    /**
     * 显示打印错误系统级对话框
     *
     * @param msg             打印错误提示消息
     * @param onClickListener
     */
    public static void showPrintErrorDialog(Context context, String msg, PrintErrorDialog.OnClickListener onClickListener) {
        sPrintErrorDialog = new PrintErrorDialog(context, msg);
        sPrintErrorDialog.setOnClickListener(onClickListener);
        sPrintErrorDialog.show();
    }

    /**
     * 关闭打印错误系统级对话框
     */
    private static void dissmissPrintErrorDialog() {
        if (sPrintErrorDialog != null && sPrintErrorDialog.isShowing()) {
            sPrintErrorDialog.dismiss();
        }
    }

    /**
     * 显示进度对话框
     */
    public void showProgressDialog(Context context) {
        showProgressDialog(context, "加载中，请稍后...");
    }

    /**
     * 显示进度对话框
     */
    public void showProgressDialog(Context context, String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context, msg);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 隐藏进度对话框
     */
    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * 显示进度对话框碎片
     */
    public void showProgressDialogFragment() {
        showProgressDialogFragment("加载中，请稍后...");
    }

    /**
     * 显示进度对话框碎片
     */
    public void showProgressDialogFragment(String message) {
        if (mFragmentManager != null) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PROGRESS_TAG);
            if (null != fragment) {
                ft.remove(fragment).commit();
            }
            ProgressDialogFragment progressDialogFragment = ProgressDialogFragment.newInstance(message);
            progressDialogFragment.show(mFragmentManager, DIALOG_PROGRESS_TAG);
            mFragmentManager.executePendingTransactions();
        }
    }

    /**
     * 隐藏进度对话框碎片
     */
    public void dismissProgressDialogFragment() {
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PROGRESS_TAG);
        if (null != fragment) {
            ((ProgressDialogFragment) fragment).dismiss();
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    /**
     * 确认对话框
     */
    public void showConfirmDialog(String message, String negText, String posText, ConfirmDialogFragment.ConfirmDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CONFIRM_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ConfirmDialogFragment df = ConfirmDialogFragment.newInstance(message, negText, posText);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_CONFIRM_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 确认对话框
     */
    public void showConfirmDialog(String tableName, boolean isMoreColor, String negText, String posText, ConfirmDialogFragment.ConfirmDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CONFIRM_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ConfirmDialogFragment df = ConfirmDialogFragment.newInstance(tableName, isMoreColor, negText, posText);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_CONFIRM_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 确认对话框
     */
    public void showConfirmDialog(String message, boolean negVisibility, String negText, @DrawableRes int negDrawableRes, boolean posVisibility, String posText, @DrawableRes int posDrawableRes, ConfirmDialogFragment.ConfirmDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CONFIRM_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ConfirmDialogFragment df = ConfirmDialogFragment.newInstance(message, negVisibility, negText, negDrawableRes, posVisibility, posText, posDrawableRes);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_CONFIRM_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 密码输入对话框
     */
    public void showInputPasswordDialog(InputPasswordDialogFragment.ConfirmPasswordDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_INPUT_PASSWORD_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        InputPasswordDialogFragment df = InputPasswordDialogFragment.newInstance();
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_INPUT_PASSWORD_TAG);
        mFragmentManager.executePendingTransactions();
    }
    /**
     * 密码输入对话框
     */
    public void showInputPasswordDialog(boolean isHesMember,InputPasswordDialogFragment.ConfirmPasswordDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_INPUT_PASSWORD_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        InputPasswordDialogFragment df = InputPasswordDialogFragment.newInstance(isHesMember);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_INPUT_PASSWORD_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 显示重置密码对话框，需要和updateResetPasswordDialog配合使用
     *
     * @param listener
     */
    public void showResetPasswordDialog(ResetPasswordDialogFragment.ResetPasswordDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_RESET_PASSWORD_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ResetPasswordDialogFragment df = ResetPasswordDialogFragment.newInstance();
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_RESET_PASSWORD_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 更新重置密码对话框，更新对话框里的VerCodeUID值
     *
     * @param verCodeUID
     */
    public void updateResetPasswordDialog(String verCodeUID) {
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_RESET_PASSWORD_TAG);
        if (fragment instanceof ResetPasswordDialogFragment) {
            ResetPasswordDialogFragment resetPasswordDialogFragment = (ResetPasswordDialogFragment) fragment;
            resetPasswordDialogFragment.update(verCodeUID);
        }
    }

    /**
     * 显示轮询对话框
     *
     * @param msg
     * @param btnText
     * @param listener
     */
    public void showIntervalDialog(String msg, String btnText, IntervalDialogFragment.IntervalDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_INTERVAL_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        IntervalDialogFragment df = IntervalDialogFragment.newInstance(msg, btnText);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_INTERVAL_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 取消轮询对话框
     */
    public void dismissIntervalDialog() {
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_INTERVAL_TAG);
        if (null != fragment) {
            ((IntervalDialogFragment) fragment).dismiss();
            mFragmentManager.beginTransaction().remove(fragment).commit();
        }
    }

    /**
     * 菜品操作对话框
     */
    public void showDishesOperationDialog(String title, int giftTag, int printTag, int position, DishesOperationDialogFragment.SetOnOperationClickListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_DISHES_OPERATION_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        DishesOperationDialogFragment operationDialogFragment = DishesOperationDialogFragment.getInstance(title, giftTag, printTag, position);
        operationDialogFragment.setDialogListener(listener);
        operationDialogFragment.show(mFragmentManager, DIALOG_DISHES_OPERATION_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 堂食菜品操作对话框
     */
    public void showDineDishesOperationDialog(String title, int hangTag, int giftTag, int printTag, int position, DineDishesOperationDialogFragment.SetOnOperationClickListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_DINE_DISHES_OPERATION_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        DineDishesOperationDialogFragment operationDialogFragment = DineDishesOperationDialogFragment.getInstance(title, hangTag, giftTag, printTag, position);
        operationDialogFragment.setDialogListener(listener);
        operationDialogFragment.show(mFragmentManager, DIALOG_DINE_DISHES_OPERATION_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 添加菜品备注对话框
     */
    public void showAddDishesRemarkDialog(String remark, String hint, AddDishesRemarkDialogFragment.AddRemarkClickListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_ADD_REMARK_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        AddDishesRemarkDialogFragment addDishesRemarkDialogFragment = AddDishesRemarkDialogFragment.getInstance(remark, hint);
        addDishesRemarkDialogFragment.setDialogListener(listener);
        addDishesRemarkDialogFragment.show(mFragmentManager, DIALOG_ADD_REMARK_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 显示普通msg对话框
     *
     * @param context 上下文
     * @param msg     提示消息
     */
    public void showMsgDialog(Context context, String msg) {
        Dialog dialog = new MsgDialog(context, msg);
        dialog.show();
    }

    /**
     * 通联支付异常  对话框
     */
    public void showPaymentAllinDialog(PaymentOfAllinpayDialogFragment.SetPaymentNumberListener numberListener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PAYMET_ALLIN_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        PaymentOfAllinpayDialogFragment paymentOfAllinpayDialogFragment = PaymentOfAllinpayDialogFragment.getInstance();
        paymentOfAllinpayDialogFragment.setDialogListener(numberListener);
        paymentOfAllinpayDialogFragment.show(mFragmentManager, DIALOG_PAYMET_ALLIN_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 通联撤销异常处理 对话框
     */
    public void showPaymentAllinReturnDialog(PaymentOfAllinpayReturnDialogFragment.SetAllinPaymentReturnListener allinPaymentReturnListener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_PAYMET_ALLIN_RETURN_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        PaymentOfAllinpayReturnDialogFragment returnDialogFragment = PaymentOfAllinpayReturnDialogFragment.newInstance();
        returnDialogFragment.setDialogListener(allinPaymentReturnListener);
        returnDialogFragment.show(mFragmentManager, DIALOG_PAYMET_ALLIN_RETURN_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 外卖 选择做法弹框
     */
    public void showChangeDishesInnerChoosePracticeDialog(List<DishesPracticeE> dishesPracticeEs, SalesOrderBatchDishesE salesOrderBatchDishesE
            , ChangeDishesInnerDishesPracticeDialogFragment.ConfirmListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CHANGE_PRACTICE_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ChangeDishesInnerDishesPracticeDialogFragment df = ChangeDishesInnerDishesPracticeDialogFragment.newInstance(dishesPracticeEs, salesOrderBatchDishesE);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_CHANGE_PRACTICE_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 换菜对话框
     */
    public void showConfirmChangeDishesDialog(String title, ConfirmChangeDishesDialogFragment.ConfirmChangeDishesListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CONFIRM_CHANGE_DIAHES_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ConfirmChangeDishesDialogFragment df = ConfirmChangeDishesDialogFragment.newInstance(title);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_CONFIRM_CHANGE_DIAHES_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 修改点菜数量对话框
     */
    public void showChangeOrderDishesCountDialog(SalesOrderBatchDishesE salesOrderBatchDishesE, double currentCount, double maxCount, String type
            , String showType, ChangeOrderDishesCountDialogFragment.ChangeOrderDishesCountListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_CHANGE_ORDER_DISHES_COUNT_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        ChangeOrderDishesCountDialogFragment df = ChangeOrderDishesCountDialogFragment.newInstance(salesOrderBatchDishesE
                , currentCount, maxCount, type, showType);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_CHANGE_ORDER_DISHES_COUNT_TAG);
        mFragmentManager.executePendingTransactions();
    }

    /**
     * 修改就餐人数对话框
     *
     * @param title
     * @param number
     * @param minNumber
     * @param maxNumber
     * @param step
     * @param leftBtnText
     * @param rightBtnText
     */
    public void showDiningNumberDialog(String title, int number, int minNumber, int maxNumber, int step, String leftBtnText, String rightBtnText, DiningNumberDialogFragment.DiningNumberDialogListener listener) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        Fragment fragment = mFragmentManager.findFragmentByTag(DIALOG_DINING_NUMBER_TAG);
        if (null != fragment) {
            ft.remove(fragment).commit();
        }
        DiningNumberDialogFragment df = DiningNumberDialogFragment.newInstance(title, number, minNumber, maxNumber, step, leftBtnText, rightBtnText);
        df.setDialogListener(listener);
        df.show(mFragmentManager, DIALOG_DINING_NUMBER_TAG);
        mFragmentManager.executePendingTransactions();
    }
}
