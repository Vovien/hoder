package com.holderzone.intelligencepos.utils.print;

import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;

import java.util.List;

/**
 * Created by tcw on 2017/10/16.
 */

public interface AbsPrinter {

    void print(PrinterE printerE, List<String> imgs) throws PrintException;
}
