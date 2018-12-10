package com.holderzone.intelligencepos.mvp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.holderzone.intelligencepos.R;
import com.holderzone.intelligencepos.base.BaseActivity;
import com.holderzone.intelligencepos.mvp.contract.BillDetailContract;
import com.holderzone.intelligencepos.mvp.fragment.BillDetailCollectionRecordFragment;
import com.holderzone.intelligencepos.mvp.fragment.BillDetailDiscountFragment;
import com.holderzone.intelligencepos.mvp.fragment.BillDetailInfoFragemnt;
import com.holderzone.intelligencepos.mvp.fragment.BillDetailSurchagreFragment;
import com.holderzone.intelligencepos.mvp.fragment.OperationRecordFragment;
import com.holderzone.intelligencepos.mvp.fragment.OrderInformationFragment;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTable;
import com.holderzone.intelligencepos.mvp.model.bean.DiningTableE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderBatchE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderDishesE;
import com.holderzone.intelligencepos.mvp.model.bean.SalesOrderE;
import com.holderzone.intelligencepos.mvp.presenter.BillDetailPresenter;
import com.holderzone.intelligencepos.widget.popupwindow.base.RelativePopupWindow;
import com.holderzone.intelligencepos.widget.popupwindow.impl.RePrintOrderPopup;
import com.jakewharton.rxbinding2.view.RxView;
import com.kennyc.view.MultiStateView;
import com.zaaach.toprightmenu.TopRightMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 账单详情页面
 * Created by chencao on 2017/6/5.
 */

public class BillDetailActivity extends BaseActivity<BillDetailContract.Presenter>
        implements BillDetailContract.View, RePrintOrderPopup.OnItemClickListener {

    @BindView(R.id.back_image)
    ImageView billTitleBackImage;
    @BindView(R.id.title_text)
    TextView billTitleText;
    @BindView(R.id.title_more_image)
    ImageView billTitleChoiceImage;
    @BindView(R.id.title_refresh)
    TextView refresh;
    @BindView(R.id.bill_detail_tab_layout)
    TabLayout billDetailTabLayout;
    @BindView(R.id.bill_detail_viewpager)
    ViewPager billDetailViewpager;
    @BindView(R.id.bill_recode_title)
    LinearLayout titleLayout;
    @BindView(R.id.bill_detail_multistateview)
    MultiStateView multiStateView;

    private SalesOrderE salesOrderE = null;
    private List<String> titleList;
    private TopRightMenu topRightMenu;
    private boolean useMemberPrice;

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        refresh.setVisibility(View.GONE);
        if (getIntent().getParcelableExtra("salesOrderE") == null) {
            return;
        }
        billTitleText.setText("账单详情");
        salesOrderE = getIntent().getParcelableExtra("salesOrderE");
        if (salesOrderE.getOrderStat() == -1) {
            billTitleChoiceImage.setVisibility(View.GONE);
        } else {
            billTitleChoiceImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        salesOrderE.setReturnBatch(10);
        salesOrderE.setReturnBatchDishesRemark(1);
        salesOrderE.setReturnBatchDishesPractice(1);
        salesOrderE.setReturnBatchDishesReturnReason(1);
        salesOrderE.setReturnBatchDishes(1);
        salesOrderE.setReturnSalesOrderPayment(1);
        salesOrderE.setReturnSalesOrderAdditionalFees(1);
        salesOrderE.setReturnSalesOrderDiscount(1);
        salesOrderE.setReturnSalesOrderDishes(1);
        mPresenter.setRequestBillInfomation(salesOrderE);
    }

    @Override
    protected void onSubscribe(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected int getContentViewLayoutId() {
        return R.layout.activity_bill_detail;
    }

    @Override
    protected BillDetailContract.Presenter initPresenter() {
        return new BillDetailPresenter(this);
    }

    @Override
    public void getError(String message) {
        showMessage(message);
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
    }

    private List<SalesOrderE> salesOrderEList;
    private static final String BILL_INFORMATION = "账单信息";
    private static final String ORDER_INFORMATION = "点单信息";
    private static final String OPERATION_RECORD = "操作记录";
    private static final String DISCOUNT_RECORD = "折扣明细";
    private static final String SURCHAGRE = "附加费";
    private static final String COLLECTION_RECORD = "收款记录";

    @Override
    public void getResponsSalesOrderEInfomation(List<SalesOrderE> salesOrderEList) {
        this.salesOrderEList = salesOrderEList;
        titleList = new ArrayList<>();
        titleList.add(BILL_INFORMATION);
        titleList.add(ORDER_INFORMATION);
        titleList.add(OPERATION_RECORD);
        titleList.add(DISCOUNT_RECORD);
        titleList.add(SURCHAGRE);
        titleList.add(COLLECTION_RECORD);
        if (salesOrderEList != null) {
            for (SalesOrderE salesOrderE : salesOrderEList) {
                if (salesOrderE.getUpperState() == 1 || salesOrderEList.size() == 1) {
                    useMemberPrice = salesOrderE.isMemberPrice();
                    break;
                }
            }
        }
        multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
        if (salesOrderEList == null || salesOrderEList.size() == 0) {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_EMPTY);
        } else {
            multiStateView.setViewState(MultiStateView.VIEW_STATE_CONTENT);
            MyPagerAdapter pagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), titleList);
            billDetailViewpager.setAdapter(pagerAdapter);
            billDetailTabLayout.setupWithViewPager(billDetailViewpager);
//            billDetailViewpager.setOffscreenPageLimit(0);
        }
    }

    @Override
    public void getResponsByReturnCount(String message) {
    }

    @Override
    public void getResopnsPrint(String message) {
        showMessage(message);
    }

    @Override
    public void showNetworkError() {
        multiStateView.setViewState(MultiStateView.VIEW_STATE_ERROR);
        View reflash = multiStateView.getView(MultiStateView.VIEW_STATE_ERROR).findViewById(R.id.btn_retry);
        RxView.clicks(reflash).throttleFirst(1L, TimeUnit.SECONDS).subscribe(o -> {
            initData(null);
        });
    }

    @Override
    protected void handleBundleExtras(@NonNull Bundle extras) {
    }

    @Override
    protected void handleSavedInstanceState(@NonNull Bundle savedInstanceState) {
    }

    @OnClick({R.id.back_image, R.id.title_text, R.id.title_more_image})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_image:
            case R.id.title_text:
                finish();
                break;
            case R.id.title_more_image:
                RePrintOrderPopup rePrintOrderPopup = new RePrintOrderPopup(BillDetailActivity.this);
                rePrintOrderPopup.showOnAnchor(billTitleChoiceImage,
                        RelativePopupWindow.VerticalPosition.BELOW, RelativePopupWindow.HorizontalPosition.ALIGN_RIGHT, -24, -6);
                break;
            default:
                break;
        }
    }

    @Override
    public void onDispose() {
    }

    @Override
    public void onItemClick() {
        salesOrderE.setCheckPrintType(1);
        mPresenter.setRequestPrint(salesOrderE);
    }

    class MyPagerAdapter extends FragmentPagerAdapter {
        private List<String> titleList;

        MyPagerAdapter(FragmentManager fm, List<String> titleList) {
            super(fm);
            this.titleList = titleList;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (titleList.get(position)) {
                case BILL_INFORMATION:
                    fragment = BillDetailInfoFragemnt.getInstanse(salesOrderE);
                    break;
                case ORDER_INFORMATION:
                    setOrderDetailsData();
                    fragment = OrderInformationFragment.newInstance(salesOrderDishesEList, useMemberPrice);
                    break;
                case OPERATION_RECORD:
                    List<SalesOrderBatchE> allSalesOrderBatchE = new ArrayList<>();
                    for (SalesOrderE salesOrderE : salesOrderEList) {
                        List<SalesOrderBatchE> arrayOfSalesOrderE = salesOrderE.getArrayOfSalesOrderBatchE();
                        if (arrayOfSalesOrderE != null) {
                            if (salesOrderE.getTradeMode() == 0) {
                                for (SalesOrderBatchE salesOrderBatchE : arrayOfSalesOrderE) {
                                    DiningTableE diningTableE = salesOrderBatchE.getDiningTableE();
                                    if (diningTableE == null) {
                                        salesOrderBatchE.setDiningTableE(salesOrderE.getDiningTableE());
                                    }
                                }
                            }
                            allSalesOrderBatchE.addAll(arrayOfSalesOrderE);
                        }
                    }
                    fragment = OperationRecordFragment.newInstance(allSalesOrderBatchE, useMemberPrice);
                    break;
                case DISCOUNT_RECORD:
                    fragment = BillDetailDiscountFragment.getInstanse(salesOrderEList);
                    break;
                case SURCHAGRE:
                    fragment = BillDetailSurchagreFragment.getInstanse(salesOrderEList);
                    break;
                case COLLECTION_RECORD:
                    fragment = BillDetailCollectionRecordFragment.getInstanse(salesOrderEList);
                    break;
                default:
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return titleList.size();
        }

        /**
         * FragmentPager的标题,如果不重写这个方法就显示不出tab的标题内容
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }
    }

    /**
     * 点单信息
     */
    private void setOrderDetailsData() {
        for (SalesOrderE salesOrderE : salesOrderEList) {
            List<SalesOrderDishesE> arrayOfSalesOrderDishesE = salesOrderE.getArrayOfSalesOrderDishesE();
            if (arrayOfSalesOrderDishesE != null && arrayOfSalesOrderDishesE.size() != 0) {
                if (salesOrderE.getTradeMode() == 0) {
                    for (SalesOrderDishesE salesOrderDishesE : arrayOfSalesOrderDishesE) {
                        DiningTable diningTable = salesOrderDishesE.getDiningTable();
                        if (diningTable == null) {
                            salesOrderDishesE.setDiningTable(salesOrderE.getDiningTableE());
                        }
                    }
                }
                salesOrderDishesEList.addAll(arrayOfSalesOrderDishesE);
            } else {
                /**
                 * 如果桌台没有菜品 仍需要显示桌台名称
                 */
                SalesOrderDishesE salesOrderDishesE = new SalesOrderDishesE();
                salesOrderDishesE.setSalesOrderGUID(salesOrderE.getSalesOrderGUID());
                DiningTable diningTable = salesOrderDishesE.getDiningTable();
                if (diningTable == null) {
                    salesOrderDishesE.setDiningTable(salesOrderE.getDiningTableE());
                }
                salesOrderDishesEList.add(salesOrderDishesE);
            }
        }
    }

    private List<SalesOrderBatchE> salesOrderBatchEList;
    private List<SalesOrderDishesE> salesOrderDishesEList = new ArrayList<>();
}
