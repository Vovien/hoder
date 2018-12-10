package com.holderzone.intelligencepos.mvp.model.db;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by tcw on 2017/7/26.
 */

public interface DbPrintModule {

    Observable<Boolean> save(final List<PrinterE> list);

    void updateSuccessPrintBean(PrinterE printerE);

    Observable<Boolean> updateSuccessPrintBean(List<PrinterE> list);

    Observable<List<PrinterE>> getCleanPrintData();

    Observable<Boolean> updatePrintBeanUploadStatus(List<Long> list);
}
