package com.holderzone.intelligencepos.utils.print;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.print.label.LabelPrinter;
import com.holderzone.intelligencepos.utils.print.local.LocalPrinter;
import com.holderzone.intelligencepos.utils.print.net.NetPrinter;

/**
 * Created by tcw on 2017/10/16.
 */

public class PrinterFactory {

    public static AbsPrinter produce(PrinterE printerE) {
        int onLocal = printerE.getOnLocal();
        if (0 == onLocal) {
            return new NetPrinter();
        } else {
            String printerTypeUID = printerE.getPrinterTypeUID();
            if (!"50".equals(printerTypeUID)) {
                return new LocalPrinter();
            } else {
                return new LabelPrinter();
            }
        }
    }
}
