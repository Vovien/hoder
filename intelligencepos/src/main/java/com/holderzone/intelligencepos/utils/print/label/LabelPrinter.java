package com.holderzone.intelligencepos.utils.print.label;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.print.AbsPrinter;
import com.holderzone.intelligencepos.utils.print.PrintException;

import java.util.List;

/**
 * Created by tcw on 2017/10/16.
 */

public class LabelPrinter implements AbsPrinter {

    @Override
    public void print(PrinterE printerE, List<String>imgs) throws PrintException {
        // do nothing by purpose
    }
}
