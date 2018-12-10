package com.holderzone.intelligencepos.mvp.hall.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.dialog.base.BaseDialogFragment;
import com.holderzone.intelligencepos.utils.permission.PermissionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by LiTao on 2017-8-4.
 * 点单菜品操作dialog（修改数量、赠送、不打印厨单、备注信息、删除）
 */

public class DineDishesOperationDialogFragment extends BaseDialogFragment<DineDishesOperationDialogFragment.SetOnOperationClickListener> {
    public static final String OPERATION_KEY = "OPERATION_KEY";
    public static final String OPERATION_GIFT_KEY = "OPERATION_GIFT_KEY";
    public static final String OPERATION_PRINT_KEY = "OPERATION_PRINT_KEY";
    public static final String OPERATION_POSITION_KEY = "OPERATION_POSITION_KEY";
    public static final String OPERATION_HANG_KEY = "OPERATION_HANG_KEY";
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.close_iv)
    ImageView closeIv;
    @BindView(R.id.change_number)
    TextView changeNumber;
    @BindView(R.id.gift)
    TextView gift;
    @BindView(R.id.no_print)
    TextView noPrint;
    @BindView(R.id.remark)
    TextView remark;
    @BindView(R.id.delete)
    TextView delete;
    @BindView(R.id.hang)
    TextView hang;
    Unbinder unbinder;
    /**
     * 标题
     */
    private String mTitle;
    /**
     * 回调接口
     */
    private SetOnOperationClickListener mOnOperationClick;
    /**
     * 赠送状态 0:不赠送 1：赠送
     */
    private int mGiftTag;
    /**
     * 打印状态 0：不打印 1：打印
     */
    private int mPrintTag;
    /**
     * 点击RecyclerView中的position
     */
    private int mPosition;
    /**
     * 挂起状态 0：挂起 1：不挂起
     */
    private int mHang;

    public static DineDishesOperationDialogFragment getInstance(String title, int hangTag, int giftTag, int printTag, int position) {
        Bundle args = new Bundle();
        args.putString(OPERATION_KEY, title);
        args.putInt(OPERATION_GIFT_KEY, giftTag);
        args.putInt(OPERATION_PRINT_KEY, printTag);
        args.putInt(OPERATION_POSITION_KEY, position);
        args.putInt(OPERATION_HANG_KEY, hangTag);
        DineDishesOperationDialogFragment fragment = new DineDishesOperationDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //接口回调
    public interface SetOnOperationClickListener {
        //修改数量
        void onChangeNumberClick(int position);

        //赠送
        void onGiftClick(int position, int giftState);

        //不打印
        void onNoPrintClick(int position, int printState);

        //备注信息
        void onRemarkClick(int position);

        //删除
        void onDeleteClick(int position);

        //是否挂起
        void onHang(int position, int hangState);
    }

    @Override
    protected void parseArgumentExtra(Bundle args) {
        mTitle = args.getString(OPERATION_KEY);
        mGiftTag = args.getInt(OPERATION_GIFT_KEY);
        mPrintTag = args.getInt(OPERATION_PRINT_KEY);
        mPosition = args.getInt(OPERATION_POSITION_KEY);
        mHang = args.getInt(OPERATION_HANG_KEY);
    }

    @Override
    protected void setAttributesOnceCreate() {
        setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle);
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.dialog_fragment_dine_dishes_operation;
    }

    @Override
    protected void setAttributesBefore() {

    }

    @Override
    protected void setAttributesAfter() {
        // 设置不可取消
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
        // 设置宽度为dialog_width*dialog_height，居中显示
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(R.color.dialog_bg);
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        wlp.width = getResources().getDimensionPixelSize(R.dimen.dishes_operation_width);
        wlp.height = getResources().getDimensionPixelSize(R.dimen.dishes_operation_height);
        window.setAttributes(wlp);
    }

    @Override
    protected void initView() {
        //设置标题
        title.setText(mTitle);
        //设置界面显示
        switch (mHang) {
            case 0:
                //原状态是不挂起
                hang.setText("不挂起");
                break;
            case 1:
                //原状态是挂起
                hang.setText("挂起");
                break;
            default:
                break;
        }
        switch (mGiftTag) {
            case 0:
                //原状态是不赠送  改为赠送
                gift.setText("赠送");
                break;
            case 1:
                //原状态是赠送  改为不赠送
                gift.setText("不赠送");
                break;
            default:
                break;
        }
        switch (mPrintTag) {
            case 0:
                //原状态是不打印 改为打印
                noPrint.setText("打印厨单");
                break;
            case 1:
                //原状态是打印 改为不打印
                noPrint.setText("不打印厨单");
                break;
            default:
                break;
        }
    }

    @Override
    protected void initData() {

    }

    @Override
    public void setDialogListener(SetOnOperationClickListener setOnOperationClickListener) {
        mOnOperationClick = setOnOperationClickListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.close_iv, R.id.change_number, R.id.gift, R.id.no_print, R.id.remark, R.id.delete, R.id.hang})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.close_iv:
                dismiss();
                break;
            case R.id.change_number:
                mOnOperationClick.onChangeNumberClick(mPosition);
                dismiss();
                break;
            case R.id.hang:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_HANG_UP,
                        () -> mOnOperationClick.onHang(mPosition, mHang == 0 ? 1 : 0));
                dismiss();
                break;
            case R.id.gift:
                PermissionManager.checkPermission(PermissionManager.PERMISSION_GIFT_DISHES,
                        () -> mOnOperationClick.onGiftClick(mPosition, mGiftTag == 0 ? 1 : 0));
                dismiss();
                break;
            case R.id.no_print:
                mOnOperationClick.onNoPrintClick(mPosition, mPrintTag == 0 ? 1 : 0);
                dismiss();
                break;
            case R.id.remark:
                mOnOperationClick.onRemarkClick(mPosition);
                dismiss();
                break;
            case R.id.delete:
                mOnOperationClick.onDeleteClick(mPosition);
                dismiss();
                break;
            default:
                break;
        }
    }
}
