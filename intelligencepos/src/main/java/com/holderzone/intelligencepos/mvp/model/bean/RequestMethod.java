package com.holderzone.intelligencepos.mvp.model.bean;

/**
 * Created by tcw on 2017/3/15.
 */

public class RequestMethod {
    public static final class EquipmentsB {
        public static final String GetBinding = "EquipmentsB.GetBinding";
        public static final String AuthorizeCheck = "EquipmentsB.AuthorizeCheck";
        public static final String GetInfo = "EquipmentsB.GetInfo";
    }

    public static final class HPMemberB {
        public static final String GetListPreferential = "HPMemberB.GetListPreferential";
        public static final String GetMemberPayCard = "HPMemberB.GetMemberPayCard";
    }

    public static final class DinnerB {
        public static final String RefreshInfo = "DinnerB.RefreshInfo";
        public static final String RefreshInfoPos = "DinnerB.RefreshInfoPos";
        public static final String GetCanExchangeTable = "DinnerB.GetCanExchangeTable";
        public static final String GetCanMergeTable = "DinnerB.GetCanMergeTable";
    }

    public static final class CardsB {
        public static final String GetSingle = "CardsB.GetSingle";
        public static final String GetListByMember = "CardsB.GetListByMember";
        public static final String GetBusinessRecord = "CardsB.GetBusinessRecord";
        public static final String GetListElectronicCard = "CardsB.GetListElectronicCard";
        public static final String GetECardListByStoreSell = "CardsB.GetECardListByStoreSell";
        public static final String GetSingleByUse = "CardsB.GetSingleByUse";
        public static final String GetListByMemberUse = "CardsB.GetListByMemberUse";
        public static final String CardsBCheckAmount = "CardsB.CheckAmount";
    }

    public static final class CouponsB {
        public static final String GetSingle = "CouponsB.GetSingle";
        public static final String GetListByMember = "CouponsB.GetListByMember";
        public static final String UseCoupons = "CouponsB.UseCoupons";
    }

    public static final class DeviceB {
        public static final String GetBinding = "DeviceB.GetBinding";
        public static final String EnterpriseBinding = "DeviceB.EnterpriseBinding";
        public static final String StoreBinding = "DeviceB.StoreBinding";
        public static final String SendActivationMsg = "DeviceB.SendActivationMsg";
        public static final String CheckActivationMsg = "DeviceB.CheckActivationMsg";
    }

    public static final class EnterpriseB {
        public static final String GetSingle = "EnterpriseB.GetSingle";
    }

    public static final class StoreB {
        public static final String GetSingle = "StoreB.GetSingle";
        public static final String GetList = "StoreB.GetList";
    }

    public static final class UsersB {
        public static final String GetSingle = "UsersB.GetSingle";
        public static final String GetList = "UsersB.GetList";
        public static final String Login = "UsersB.Login";
        public static final String NewLogin = "UsersB.NewLogin";
        public static final String PasswordReset = "UsersB.PasswordReset";
        public static final String PasswordModify = "UsersB.PasswordModify";
    }

    public static final class DiningTableAreaB {
        public static final String GetSingle = "DiningTableAreaB.GetSingle";
        public static final String GetList = "DiningTableAreaB.GetList";
    }

    public static final class DiningTableB {
        public static final String GetSingle = "DiningTableB.GetSingle";
        public static final String GetList = "DiningTableB.GetList";
        public static final String GetListByArea = "DiningTableB.GetListByArea";
    }

    public static final class DishesTypeB {
        public static final String GetSingle = "DishesTypeB.GetSingle";
        public static final String GetList = "DishesTypeB.GetList";
    }

    public static final class PrinterConfigB {
        public static final String GetPrintLogoList = "PrinterConfigB.GetPrintLogoList";
    }

    public static final class DishesB {
        public static final String GetSingle = "DishesB.GetSingle";
        public static final String GetList = "DishesB.GetList";
        public static final String GetListByType = "DishesB.GetListByType";
    }

    public static final class DishesRemarkB {
        public static final String GetSingle = "DishesRemarkB.GetSingle";
        public static final String GetList = "DishesRemarkB.GetList";
        public static final String Insert = "DishesRemarkB.Insert";
        public static final String Delete = "DishesRemarkB.Delete";
        public static final String BatchDelete = "DishesRemarkB.BatchDelete";
    }

    public static final class DishesPracticeB {
        public static final String GetSingle = "DishesPracticeB.GetSingle";
        public static final String GetList = "DishesPracticeB.GetList";
    }

    public static final class DishesReturnReasonB {
        public static final String GetList = "DishesReturnReasonB.GetList";
        public static final String Insert = "DishesReturnReasonB.Insert";
        public static final String Delete = "DishesReturnReasonB.Delete";
        public static final String BatchDelete = "DishesReturnReasonB.BatchDelete";
    }

    public static final class PaymentItemB {
        public static final String GetSingle = "PaymentItemB.GetSingle";
        public static final String GetList = "PaymentItemB.GetList";
    }

    public static final class SalesOrderAdditionalFeesB {
        public static final String GetSingle = "SalesOrderAdditionalFeesB.GetSingle";
        public static final String GetList = "SalesOrderAdditionalFeesB.GetList";
        public static final String Insert = "SalesOrderAdditionalFeesB.Insert";
        public static final String Delete = "SalesOrderAdditionalFeesB.Delete";
    }

    public static final class OrderRecordRemarkItemB {
        public static final String GetList = "OrderRecordRemarkItemB.GetList";
        public static final String Insert = "OrderRecordRemarkItemB.Insert";
        public static final String BatchDelete = "OrderRecordRemarkItemB.BatchDelete";
    }

    public static final class OrderRecordSectionB {
        public static final String GetList = "OrderRecordSectionB.GetList";
        public static final String GetListCanUse = "OrderRecordSectionB.GetListCanUse";
    }

    public static final class OrderRecordB {
        public static final String GetSingle = "OrderRecordB.GetSingle";
        public static final String GetTableOrder = "OrderRecordB.GetTableOrder";
        public static final String GetOtherOrder = "OrderRecordB.GetOtherOrder";
        public static final String Insert = "OrderRecordB.Insert";
        public static final String GetListByCurrent = "OrderRecordB.GetListByCurrent";
        public static final String Cancel = "OrderRecordB.Cancel";
        public static final String Update = "OrderRecordB.Update";
        public static final String GetStatisticsData = "OrderRecordB.GetStatisticsData";
        public static final String SendSMS = "OrderRecordB.SendSMS";
    }

    public static final class SalesOrderB {
        public static final String GetListNotCheckOut = "SalesOrderB.GetListNotCheckOut";
        public static final String SetFees = "SalesOrderB.SetFees";
        public static final String MemberLoginOut = "SalesOrderB.MemberLoginOut";
        public static final String VipCardDiscount = "SalesOrderB.VipCardDiscount";
        public static final String SetDiscountRatio = "SalesOrderB.SetDiscountRatio";
        public static final String SetActually = "SalesOrderB.SetActually";
        public static final String GetSingle = "SalesOrderB.GetSingle";
        public static final String Create = "SalesOrderB.Create";
        public static final String Invalid = "SalesOrderB.Invalid";
        public static final String Merge = "SalesOrderB.Merge";
        public static final String Split = "SalesOrderB.Split";
        public static final String ChangeTable = "SalesOrderB.ChangeTable";
        public static final String Check = "SalesOrderB.Check";
        public static final String CheckPrint = "SalesOrderB.CheckPrint";
        public static final String CheckOut = "SalesOrderB.CheckOut";
        public static final String UpdateGuestCount = "SalesOrderB.UpdateGuestCount";
        public static final String MemberLogin = "SalesOrderB.MemberLogin";
        public static final String GetListByBusinessDay = "SalesOrderB.GetListByBusinessDay";
        public static final String GetBusinessDaySummary = "SalesOrderB.GetBusinessDaySummary";
        public static final String Recovery = "SalesOrderB.Recovery";
        public static final String GetBusinessDaySummaryDishes = "SalesOrderB.GetBusinessDaySummaryDishes";
        public static final String GetBusinessDaySummaryDishesGift = "SalesOrderB.GetBusinessDaySummaryDishesGift";
        public static final String GetBusinessDaySummaryDishesBack = "SalesOrderB.GetBusinessDaySummaryDishesBack";
        public static final String GetBusinessDaySummaryPayment = "SalesOrderB.GetBusinessDaySummaryPayment";
        public static final String GetBusinessDaySummaryDiscount = "SalesOrderB.GetBusinessDaySummaryDiscount";
        public static final String GetBusinessDaySummaryCoupons = "SalesOrderB.GetBusinessDaySummaryCoupons";
        public static final String GetBusinessDaySummaryCard = "SalesOrderB.GetBusinessDaySummaryCard";
        public static final String GetBusinessDaySummaryCardPayment = "SalesOrderB.GetBusinessDaySummaryCardPayment";
        public static final String GetBusinessDaySummaryCardSell = "SalesOrderB.GetBusinessDaySummaryCardSell";
        public static final String GetBusinessDaySummaryCardRecharge = "SalesOrderB.GetBusinessDaySummaryCardRecharge";
        public static final String GetCanUsePoints = "SalesOrderB.GetCanUsePoints";
        public static final String UsePoints = "SalesOrderB.UsePoints";
        public static final String CancelPoints = "SalesOrderB.CancelPoints";
        public static final String PlatCardsDiscount = "SalesOrderB.PlatCardsDiscount";
        public static final String PlatCoupon = "SalesOrderB.PlatCoupon";
        public static final String UseRedPacket = "SalesOrderB.UseRedPacket";
        public static final String GetListBySalesOrderGuid_V1 = "SalesOrderB.GetListBySalesOrderGuid_V1";
    }

    public static final class SalesOrderBatchB {
        public static final String SetPrice = "SalesOrderBatchB.SetPrice";
        public static final String GetSingle = "SalesOrderBatchB.GetSingle";
        public static final String ReviewDishes = "SalesOrderBatchB.ReviewDishes";
        public static final String GetListByOrder = "SalesOrderBatchB.GetListByOrder";
        public static final String GetListByTable = "SalesOrderBatchB.GetListByTable";
        public static final String GetListByTable_V1 = "SalesOrderBatchB.GetListByTable_V1";
        public static final String Create = "SalesOrderBatchB.Create";
        public static final String AddDishes = "SalesOrderBatchB.AddDishes";
        public static final String GetHangDishes = "SalesOrderBatchB.GetHangDishes";
        public static final String WakeUp = "SalesOrderBatchB.WakeUp";
        public static final String ModifiedQuantity = "SalesOrderBatchB.ModifiedQuantity";
        public static final String Urge = "SalesOrderBatchB.Urge";
        public static final String ReturnDishes = "SalesOrderBatchB.ReturnDishes";
        public static final String GetDTDishListByNoServing = "SalesOrderBatchB.GetDTDishListByNoServing";
        public static final String ServingDishesWholeDT = "SalesOrderBatchB.ServingDishesWholeDT";
        public static final String ServingDishes = "SalesOrderBatchB.ServingDishes";
        public static final String SetDishesGift = "SalesOrderBatchB.SetDishesGift";

    }

    public static final class SalesOrderPaymentB {
        public static final String GetSingle = "SalesOrderPaymentB.GetSingle";
        public static final String GetList = "SalesOrderPaymentB.GetList";
        public static final String Refund = "SalesOrderPaymentB.Refund";
        public static final String AddOrdinary = "SalesOrderPaymentB.AddOrdinary";
        public static final String AddWeChat = "SalesOrderPaymentB.AddWeChat";
        public static final String AddAlipay = "SalesOrderPaymentB.AddAlipay";
        public static final String AddJhPay = "SalesOrderPaymentB.AddJhPay";
        public static final String AddVipCard = "SalesOrderPaymentB.AddVipCard";
        public static final String CheckPayStatus = "SalesOrderPaymentB.CheckPayStatus";
        public static final String MeituanPrepare = "SalesOrderPaymentB.MeituanPrepare";
        public static final String AddMeituanCoupon = "SalesOrderPaymentB.AddMeituanCoupon";
        public static final String CancelMeituanCoupon = "SalesOrderPaymentB.CancelMeituanCoupon";
        public static final String QueryMeituanCoupon = "SalesOrderPaymentB.QueryMeituanCoupon";
        public static final String CheckRefundStatus = "SalesOrderPaymentB.CheckRefundStatus";
        public static final String AddMemberPlat = "SalesOrderPaymentB.AddMemberPlat";
    }

    public static final class PrinterTypeB {
        public static final String GetList = "PrinterTypeB.GetList";
    }

    public static final class PrinterB {
        public static final String GetSingle = "PrinterB.GetSingle";
        public static final String GetList = "PrinterB.GetList";
        public static final String Insert = "PrinterB.Insert";
        public static final String Update = "PrinterB.Update";
        public static final String Delete = "PrinterB.Delete";
        public static final String SetDishes = "PrinterB.SetDishes";
        public static final String SetDishesCancel = "PrinterB.SetDishesCancel";
        public static final String GetPrintData = "PrinterB.GetPrintData";
        public static final String ConfirmPrintData = "PrinterB.ConfirmPrintData";
    }

    public static final class DishesEstimateRecordB {
        public static final String GetSingle = "DishesEstimateRecordB.GetSingle";
        public static final String GetList = "DishesEstimateRecordB.GetList";
        public static final String GetCurrentInfo = "DishesEstimateRecordB.GetCurrentInfo";
        public static final String SetEstimate = "DishesEstimateRecordB.SetEstimate";
        public static final String GetSingleByBusinessDay = "DishesEstimateRecordB.GetSingleByBusinessDay";
        public static final String Insert = "DishesEstimateRecordB.Insert";
        public static final String Update = "DishesEstimateRecordB.Update";
        public static final String Delete = "DishesEstimateRecordB.Delete";
    }

    public static final class AccountRecordB {
        public static final String GetSingle = "AccountRecordB.GetSingle";
        public static final String GetList = "AccountRecordB.GetList";
        public static final String Confirm = "AccountRecordB.Confirm";
        public static final String Confirm2 = "AccountRecordB.Confirm2";
        public static final String GetCurrent = "AccountRecordB.GetCurrent";
        public static final String SetCurrent = "AccountRecordB.SetCurrent";
        public static final String PrintReport = "AccountRecordB.PrintReport";
        public static final String GetSingleByBusinessDay = "AccountRecordB.GetSingleByBusinessDay";
        public static final String GetOrderCount = "AccountRecordB.GetOrderCount";
    }

    public static final class ShiftConfigB {
        public static final String GetSingle = "ShiftConfigB.GetSingle";
        public static final String GetList = "ShiftConfigB.GetList";
        public static final String Insert = "ShiftConfigB.Insert";
        public static final String Update = "ShiftConfigB.Update";
        public static final String Delete = "ShiftConfigB.Delete";
    }

    public static final class ShiftRecordB {
        public static final String GetSingle = "ShiftRecordB.GetSingle";
        public static final String GetList = "ShiftRecordB.GetList";
        public static final String GetListByBusinessDay = "ShiftRecordB.GetListByBusinessDay";
        public static final String Confirm = "ShiftRecordB.Confirm";
    }

    public static final class AdditionalFeesB {
        public static final String GetList = "AdditionalFeesB.GetList";
    }

    public static final class MemberInfoB {
        public static final String GetRegVerCode = "MemberInfoB.GetRegVerCode";
        public static final String CheckRegVerCode = "MemberInfoB.CheckRegVerCode";
        public static final String GetSingle = "MemberInfoB.GetSingle";
        public static final String Update = "MemberInfoB.Update";
        public static final String ResetPassWord = "MemberInfoB.ResetPassWord";
        public static final String GetVerCode = "MemberInfoB.GetVerCode";
        public static final String Login = "MemberInfoB.Login";
    }

    public static final class CardBusinessOrderB {
        public static final String Recharge = "CardBusinessOrderB.Recharge";
        public static final String GetSingle = "CardBusinessOrderB.GetSingle";
        public static final String CheckPayStatus = "CardBusinessOrderB.CheckPayStatus";
    }

    public static final class CardSellOrderB {
        public static final String EntitySellCard = "CardSellOrderB.EntitySellCard";
        public static final String VirtualSellCard = "CardSellOrderB.VirtualSellCard";
        public static final String CheckPayStatus = "CardSellOrderB.CheckPayStatus";
    }

    public static final class CardTypeB {
        public static final String GetSingle = "CardTypeB.GetSingle";
    }

    public static final class QueueUpRecordB {
        public static final String GetList = "QueueUpRecordB.GetList";
        public static final String GetStatisticalReport = "QueueUpRecordB.GetStatisticalReport";
        public static final String Add = "QueueUpRecordB.Add";
        public static final String Call = "QueueUpRecordB.Call";
        public static final String UpdateCustomerCount = "QueueUpRecordB.UpdateCustomerCount";
        public static final String Confirm = "QueueUpRecordB.Confirm";
        public static final String Delete = "QueueUpRecordB.Delete";
        public static final String Skip = "QueueUpRecordB.Skip";
        public static final String Recover = "QueueUpRecordB.Recover";
        public static final String ReportPrint = "QueueUpRecordB.ReportPrint";
    }

    public static final class QueueUpTypeB {
        public static final String GetList = "QueueUpTypeB.GetList";
    }

    public static final class SysParametersB {
        public static final String GetParameters = "SysParametersB.GetParameters";
        public static final String getSystemConfig = "SysParametersB.getSystemConfig";
    }

    public static final class SalesOrderServingDishesB {
        public static final String CheckOrderServing = "SalesOrderServingDishesB.CheckOrderServing";
    }

    public static final class UnOrderB {
        public static final String NewMessage = "UnOrderB.NewMessage";
        public static final String UntreatedMessageList = "UnOrderB.UntreatedMessageList";
        public static final String GetHistoryList = "UnOrderB.GetHistoryList";
        public static final String GetSingle = "UnOrderB.GetSingle";
        public static final String GetDaySummary = "UnOrderB.GetDaySummary";
        public static final String ConfirmOrder = "UnOrderB.ConfirmOrder";
        public static final String RefuseOrder = "UnOrderB.RefuseOrder";
        public static final String ReplyReminder = "UnOrderB.ReplyReminder";
        public static final String ConfirmChargeback = "UnOrderB.ConfirmChargeback";
        public static final String RefuseChargeback = "UnOrderB.RefuseChargeback";
        public static final String MsgReceiveConfirm = "UnOrderB.MsgReceiveConfirm";
        public static final String TakeOutPrint = "UnOrderB.TakeOutPrint";
    }

    public static final class UnOrderDishesB {
        public static final String DishesChange = "UnOrderDishesB.DishesChange";
    }

    public static final class UnReminderReplyContentB {
        public static final String GetList = "UnReminderReplyContentB.GetList";
        public static final String Insert = "UnReminderReplyContentB.Insert";
    }
}
