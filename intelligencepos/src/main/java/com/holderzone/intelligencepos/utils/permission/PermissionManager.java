package com.holderzone.intelligencepos.utils.permission;

import com.holderzone.intelligencepos.base.BaseApplication;
import com.holderzone.intelligencepos.mvp.model.RepositoryImpl;
import com.holderzone.intelligencepos.mvp.model.bean.UserGroupRightE;
import com.holderzone.intelligencepos.mvp.model.bean.UsersE;

import java.util.List;

/**
 * Created by tcw on 2017/9/27.
 */

public class PermissionManager {

    public static final String PERMISSION_OPEN_TABLE = "101001";// 开台点餐 done
    public static final String PERMISSION_CLOSE_TABLE = "101002";// 关台 done
    public static final String PERMISSION_GIFT_DISHES = "101004";// 赠送 done
    public static final String PERMISSION_RETURN_DISHES = "101005";// 退菜 done
    public static final String PERMISSION_CHANGE_TABLE = "101006";// 换台 done
    public static final String PERMISSION_MERGE_TABLE = "101007";// 并单 done
    public static final String PERMISSION_SPLIT_TABLE = "101008";// 拆单 done
    public static final String PERMISSION_HANG_UP = "101009";// 挂起 done
    public static final String PERMISSION_CALL_UP = "101010";// 叫起 done

    public static final String PERMISSION_ADDITION_FEE = "102001";// 附加费 done
    public static final String PERMISSION_CHANGE_PRICE = "102002";// 收款改价 done
    public static final String PERMISSION_SINGLE_DISH_DISCOUNT = "102003";// 单品折扣 not nec
    public static final String PERMISSION_SALES_ORDER_DISCOUNT = "102004";// 整单折扣 done
    public static final String PERMISSION_DISABLE_SALES_ORDER = "102005";// 作废账单 done
    public static final String PERMISSION_CHECK_SALES_ORDER = "102006";// 对单 not nec

    public static final String PERMISSION_OPEN_MONEY_BOX = "103001";// 开钱箱 not nec
    public static final String PERMISSION_REFUND_MONEY = "103002";// 退款 done
    public static final String PERMISSION_JIE_ZHANG = "103004";// 结账 done
    public static final String PERMISSION_FAN_JIE_ZHANG = "103005";// 反结账 not nec

    public static final String PERMISSION_QUERY_MEMBER = "104001";// 查询会员 not nec
    public static final String PERMISSION_REGIST_MEMBER = "104002";// 注册会员 not nec
    public static final String PERMISSION_MAKE_CARD = "104003";// 办卡 not nec
    public static final String PERMISSION_RECHARGE_MONEY = "104004";// 充值 not nec
    public static final String PERMISSION_RESET_PSD = "104005";// 重置密码 done

    public static final String PERMISSION_CREATE_BUSSINESS_DAY = "105001";// 开营业日 not nec
    public static final String PERMISSION_PREDICT = "105002";// 预订 not nec
    public static final String PERMISSION_QUEUE = "105003";// 排队 not nec
    public static final String PERMISSION_GU_QING = "105004";// 估清 not nec
    public static final String PERMISSION_ZHA_ZHANG = "105005";// 扎帐 not nec
    public static final String PERMISSION_SETTING = "105006";// 设置 done
    public static final String PERMISSION_YINYE_DATA = "105007";// 营业数据 done

    public interface OnCheckPermissionListener {

        void onPermissionGranted();// 已授权

//        void onPermissionDenied();// 未授权
    }

    /**
     * Map权限List到String
     * @param usersE
     */
    public static void mapArrayOfUserGroupRightE2UserRight(UsersE usersE) {
        StringBuilder userRight = new StringBuilder();
        if (0 == usersE.getUserGroupE().getGroupRightEnable()) {// 停用权限
            userRight.append("false");
        } else {// 启用权限
            userRight.append("true");
            List<UserGroupRightE> arrayofUserGroupRightE = usersE.getArrayofUserGroupRightE();
            for (UserGroupRightE userGroupRightE : arrayofUserGroupRightE) {
                userRight.append("&").append(userGroupRightE.getUserRightUID());
            }
        }
        usersE.setUserRight(userRight.toString());
    }

    /**
     * 校验用户权限
     * @param permission
     * @param onCheckPermissionListener
     */
    public static void checkPermission(String permission, OnCheckPermissionListener onCheckPermissionListener) {
        // TODO: 2017/10/13 加入后台线程控制
        RepositoryImpl.getInstance().getUsers()
                .subscribe(users -> {
                    List<String> userRightList = users.getUserRightList();
                    if (userRightList.contains("false")) {
                        onCheckPermissionListener.onPermissionGranted();
                    } else {
                        if (userRightList.contains(permission)) {
                            onCheckPermissionListener.onPermissionGranted();
                        } else {
                            BaseApplication.showMessage("您没有该操作权限");
                        }
                    }
                });
    }

    private static String translate(String permission) {
        switch (permission) {
            case PERMISSION_OPEN_TABLE:
                return "您没有开台权限，请联系管理员。";
            case PERMISSION_CLOSE_TABLE:
                return "您没有关台权限，请联系管理员。";
            case PERMISSION_GIFT_DISHES:
                return "您没有赠送权限，请联系管理员";
            case PERMISSION_RETURN_DISHES:
                return "您没有退菜权限，请联系管理员";
            case PERMISSION_CHANGE_TABLE:
                return "您没有换台权限，请联系管理员";
            case PERMISSION_MERGE_TABLE:
                return "您没有并单权限，请联系管理员";
            case PERMISSION_SPLIT_TABLE:
                return "您没有拆单权限，请联系管理员";
            case PERMISSION_HANG_UP:
                return "您没有挂起权限，请联系管理员";
            case PERMISSION_CALL_UP:
                return "您没有叫起权限，请联系管理员";
            case PERMISSION_ADDITION_FEE:
                return "您没有附加费权限，请联系管理员";
            case PERMISSION_CHANGE_PRICE:
                return "您没有收款改价权限，请联系管理员";
            case PERMISSION_SINGLE_DISH_DISCOUNT:
                return "您没有单品折扣权限，请联系管理员";
            case PERMISSION_SALES_ORDER_DISCOUNT:
                return "您没有整单折扣权限，请联系管理员";
            case PERMISSION_DISABLE_SALES_ORDER:
                return "您没有作废账单权限，请联系管理员";
            case PERMISSION_CHECK_SALES_ORDER:
                return "您没有对单权限，请联系管理员";
            case PERMISSION_OPEN_MONEY_BOX:
                return "您没有开钱箱权限，请联系管理员";
            case PERMISSION_REFUND_MONEY:
                return "您没有退款权限，请联系管理员";
            case PERMISSION_JIE_ZHANG:
                return "您没有结账收款权限，请联系管理员";
            case PERMISSION_FAN_JIE_ZHANG:
                return "您没有反结账权限，请联系管理员";
            case PERMISSION_QUERY_MEMBER:
                return "您没有查询会员权限，请联系管理员";
            case PERMISSION_REGIST_MEMBER:
                return "您没有注册会员权限，请联系管理员";
            case PERMISSION_MAKE_CARD:
                return "您没有办卡权限，请联系管理员";
            case PERMISSION_RECHARGE_MONEY:
                return "您没有充值权限，请联系管理员";
            case PERMISSION_RESET_PSD:
                return "您没有重置密码权限，请联系管理员";
            case PERMISSION_CREATE_BUSSINESS_DAY:
                return "您没有开营业日权限，请联系管理员";
            case PERMISSION_PREDICT:
                return "您没有预订权限，请联系管理员";
            case PERMISSION_QUEUE:
                return "您没有排队权限，请联系管理员";
            case PERMISSION_GU_QING:
                return "您没有估清权限，请联系管理员";
            case PERMISSION_YINYE_DATA:
                return "您没有营业数据权限，请联系管理员";
            case PERMISSION_ZHA_ZHANG:
                return "您没有扎帐权限，请联系管理员";
            case PERMISSION_SETTING:
                return "您没有设置权限，请联系管理员";
            default:
                return "您没有相关权限，请联系管理员";
        }
    }
}
