package com.holderzone.intelligencepos.utils.print.net;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.print.AbsPrinter;
import com.holderzone.intelligencepos.utils.print.PrintException;

import java.util.List;

/**
 * Created by tcw on 2017/10/16.
 */

public class NetPrinter implements AbsPrinter {

    private AbsPrinter mNetPrinterDelegate;

    public NetPrinter() {
        mNetPrinterDelegate = NetPrinterDelegate.getInstance();
    }

    @Override
    public void print(PrinterE printerE, List<String> imgs) throws PrintException {
        mNetPrinterDelegate.print(printerE,imgs);
    }
}
