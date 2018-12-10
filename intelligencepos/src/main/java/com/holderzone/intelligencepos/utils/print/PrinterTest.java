package com.holderzone.intelligencepos.utils.print;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Vector;

public class PrinterTest
{

    Vector<Byte> Command = null;
    public PrinterTest() {
        this.Command = new Vector(4096, 1024);
    }

    /**
     * 初始化打印机,主要清空打印机缓存数据，这步很重要，要不打印机容易内存溢出，打印出乱码
     */
    public void printInit()
    {
        byte[] command = {27, 64};
        addArrayToCommand(command);
    }

    private void addArrayToCommand(byte[] array) {
        for (int i = 0; i < array.length; i++) {
            this.Command.add(Byte.valueOf(array[i]));
        }
    }

    /**
     * 换行回车
     */
    public void printEnter()
    {
        byte[] command = {10};
        addArrayToCommand(command);
    }



    /**
     * 功能：选择对齐方式
     * @param align:左对齐 1:居中对齐 2:右对齐
     */
    public void printTextAlign(int align) {
        byte[] command = {27,97,(byte) align};
        addArrayToCommand(command);
    }

    /**
     *
     * @param bitmap  位图
     * @param nWidth  打印宽度（可以用于缩放图片）
     * @param nMode 打印模式 0： 正常 1：倍宽 2：倍高 3：倍宽 倍高
     */
    public void addRastBitImage(Bitmap bitmap, int nWidth, int nMode) {
        if (bitmap != null) {
            int width = (nWidth + 7) / 8 * 8;
            int height = bitmap.getHeight() * width / bitmap.getWidth();
            Bitmap grayBitmap = GpUtils.toGrayscale(bitmap);
            Bitmap rszBitmap = GpUtils.resizeImage(grayBitmap, width, height);
            byte[] src = GpUtils.bitmapToBWPix(rszBitmap);
            byte[] command = new byte[8];
            height = src.length / width;
            command[0] = 29;
            command[1] = 118;
            command[2] = 48;
            command[3] = ((byte) (nMode & 0x1));
            command[4] = ((byte) (width / 8 % 256));
            command[5] = ((byte) (width / 8 / 256));
            command[6] = ((byte) (height % 256));
            command[7] = ((byte) (height / 256));
            addArrayToCommand(command);
            byte[] codecontent = GpUtils.pixToEscRastBitImageCmd(src);
            for (int k = 0; k < codecontent.length; k++) {
                this.Command.add(Byte.valueOf(codecontent[k]));
            }
        } else {
            Log.d("BMP", "bmp.  null ");
        }
    }
    /**
     * 打印图片
     * @param mbitmap  对象bitmap
     */
    public Vector<Byte> CMD_Image(Bitmap mbitmap) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            byte[] decodedByte = Base64.decode(bitmapToBase64(mbitmap), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
            final int imgWidth = bitmap.getWidth();
            final int imgHeight = bitmap.getHeight();
            final int[] argb = new int[imgWidth * imgHeight];
            bitmap.getPixels(argb, 0, imgWidth, 0, 0, imgWidth, imgHeight);

            EscCommand esc = new EscCommand();

            esc.addSelectJustification(EscCommand.JUSTIFICATION.RIGHT);
            esc.addRastBitImage(bitmap, imgWidth, 0);
            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
            Vector<Byte> Command  = esc.getCommand();
            Vector<Byte> data = new Vector<>(Command.size());
            for (int i = 0; i < Command.size(); i++) {
                data.add(Command.get(i));
            }
            bitmap.recycle();
        return  data;
    }

    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    /**
     * 打印二维条形码
     * @param printData  打印的内容
     */
    public Vector<Byte> CMD_Qrcode(String printData) {
        EscCommand esc = new EscCommand();
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectErrorCorrectionLevelForQRCode((byte) 0x31); //设置纠错等级
        esc.addSelectSizeOfModuleForQRCode((byte) 3);//设置qrcode模块大小
        esc.addStoreQRCodeData(printData);//设置qrcode内容
        esc.addPrintQRCode();//打印QRCode
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addPrintAndLineFeed();
        return esc.getCommand();
    }
    /**
     * 打印的一维码
     * @param printData  需要打印的数据
     */
    public Vector<Byte> CMD_Barcode(String printData, Context context) {

         EscCommand esc = new EscCommand();
//        Bitmap b = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        px2Byte(0,0,b);
//        esc.addRastBitImage(b, 384, 0); //打印图片
//            esc.addText("printData");
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
//             /*打印一维条码*/
            esc.addSetBarcodeWidth((byte) 2);
            esc.addSetBarcodeHeight((byte) 60); //设置条码高度为60点
                esc.addSelectPrintingPositionForHRICharacters(EscCommand.HRI_POSITION.BELOW);//设置条码可识别字符位置在条码下方
            esc.addCODE128(printData);  //打印Code128码
            esc.addPrintAndLineFeed();
//            esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
//            esc.addPrintAndLineFeed();
            return  esc.getCommand();
    }


    /**
     * 走纸
     * @param line  走纸的行数
     */
    public void printLine(int line)
    {
        byte[] command = {27,100,(byte) line};
        addArrayToCommand(command);
    }



    /**
     * 切纸
     */
    public void printCutPage()
    {
        byte[] command = {27,109};
        addArrayToCommand(command);
    }

    /**
     * 打开钱箱
     * @return
     */
    public void printQianXiang() {
        byte[] command = {27,112,0,60,(byte) 255};
        addArrayToCommand(command);
    }
}