package com.holderzone.intelligencepos.utils.print.local;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.print.AbsPrinter;
import com.holderzone.intelligencepos.utils.print.PrintException;

import java.util.List;

/**
 * Created by tcw on 2017/10/16.
 */

public class LocalPrinter implements AbsPrinter {

    private AbsPrinter mAbsPrinterDelegate;

    public LocalPrinter() {
        mAbsPrinterDelegate = LocalPrinterDelegate.getInstance();
    }

    @Override
    public void print(PrinterE printerE, List<String> imgs) throws PrintException {
        mAbsPrinterDelegate.print(printerE,imgs);
    }
}
