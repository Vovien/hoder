package com.holderzone.intelligencepos.utils.print.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.holderzone.intelligencepos.mvp.model.bean.PrintRowE;
import com.holderzone.intelligencepos.mvp.model.bean.PrinterE;
import com.holderzone.intelligencepos.utils.CollectionUtils;
import com.holderzone.intelligencepos.utils.print.AbsPrinter;
import com.holderzone.intelligencepos.utils.print.PrintException;
import com.holderzone.intelligencepos.utils.print.PrinterTest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.List;
import java.util.Vector;

/**
 * Created by tcw on 2017/10/16.
 */

public class NetPrinterDelegate implements AbsPrinter {

    private static final String TAG = "NetPrinterDelegate";

    private volatile static NetPrinterDelegate sInstance;// 使用volatile变量以保证是在主内存上取值

    private String mEncoding;

    private Socket mSocket;

    private OutputStreamWriter mOutputStreamWriter;

    public static NetPrinterDelegate getInstance() {
        if (sInstance == null) {
            synchronized (NetPrinterDelegate.class) {
                if (sInstance == null) {
                    sInstance = new NetPrinterDelegate();
                }
            }
        }
        return sInstance;
    }

    @Override
    public void print(PrinterE printerE, List<String> imgs) throws PrintException {
        try {
            // 开启Socket连接，初始化输出流
            openSocketConnect(printerE.getIP(), printerE.getPort(), "gbk");
            // 设置打印属性
            StringBuffer stringBuffer = new StringBuffer();
            List<PrintRowE> arrayOfPrintRowE = printerE.getArrayOfPrintRowE();
            int size = arrayOfPrintRowE.size();
            for (int i = 0; i < size; i++) {
                // 当前待处理打印内容
                PrintRowE printRowE = arrayOfPrintRowE.get(i);
                // 设置对齐方式
                switch (printRowE.getAlign()) {
                    case "L":
                        cmdPrintLocation(stringBuffer, 0);
                        break;
                    case "R":
                        cmdPrintLocation(stringBuffer, 2);
                        break;
                    default:
                        cmdPrintLocation(stringBuffer, 1);
                        break;
                }
                // 设置打印类型
                switch (printRowE.getContentType()) {
                    case "IMG":
                        if (CollectionUtils.isNotEmpty(imgs)) {
                            print(stringBuffer.toString());
                            stringBuffer.delete(0, stringBuffer.length());
                            String printLogoPath = null;
                            for (String s : imgs) {
                                if (s.endsWith(printRowE.getContent())) {
                                    printLogoPath = s;
                                    break;
                                }
                            }
                            if (printLogoPath != null) {
                                Vector<Byte> vector = new PrinterTest().CMD_Image(BitmapFactory.decodeFile(printLogoPath));
                                print(vector);
                            }
                        }

                        break;
                    case "QRCode":
                        cmdQRCode(stringBuffer, printRowE.getContent());
                        break;
                    default:
                        cmdBold(stringBuffer, "加粗".equals(printRowE.getBlod()));
                        cmdSetFontSize(stringBuffer, printRowE.getXM(), printRowE.getYM());
                        cmdPrintText(stringBuffer, printRowE.getContent());
                        break;
                }
                cmdPrintLine(stringBuffer);
            }
            cmdPrintLine(stringBuffer, 3);
            // 没有蜂鸣器和切刀  所以注释掉
            if ("80".equals(printerE.getPaper())) {
                cmdFeedAndCut(stringBuffer);
                SetPrintBuzzerAndAlarmLight(stringBuffer, 3, 2, 3);
            } else {
                cmdPrintLine(stringBuffer, 3);
            }
            // 打印
            print(stringBuffer.toString());
            // 关闭输出流、套接字
            closeIOAndSocket();
        } catch (IOException e) {
            throw new PrintException("局域网小票打印机连接未成功，请检查打印设备。", PrintException.MESSAGE_TYPE_NET_PRINT_FAILED);
        }
    }

    /**
     * 打开一个Socket连接
     *
     * @param ip       打印机IP
     * @param port     打印机端口号
     * @param encoding 编码
     * @return
     */
    private void openSocketConnect(String ip, int port, String encoding) throws IOException {
        mSocket = new Socket();
        SocketAddress address = new InetSocketAddress(ip, port);
        mSocket.connect(address, 2000);
        DataOutputStream socketOut = new DataOutputStream(mSocket.getOutputStream());
        mOutputStreamWriter = new OutputStreamWriter(socketOut, encoding);
        mEncoding = encoding;
    }

    /**
     * 关闭IO流和Socket
     *
     * @throws IOException
     */
    private void closeIOAndSocket() throws IOException {
        mOutputStreamWriter.close();
        mSocket.close();
    }

    /**
     * 打印二维码
     *
     * @param qrData 二维码的内容
     * @throws IOException
     */
    private void cmdQRCode(StringBuffer sb, String qrData) throws IOException {
        int moduleSize = 8;
        int length = qrData.getBytes(mEncoding).length;
        //打印二维码矩阵
        sb.append((char) 0x1D);// init
        sb.append("(k");// adjust height of barcode
        sb.append((char) (length + 3)); // pl
        sb.append((char) 0); // ph
        sb.append((char) 49); // cn
        sb.append((char) 80); // fn
        sb.append((char) 48); //
        sb.append(qrData);
        sb.append((char) 0x1D);
        sb.append("(k");
        sb.append((char) 3);
        sb.append((char) 0);
        sb.append((char) 49);
        sb.append((char) 69);
        sb.append((char) 48);
        sb.append((char) 0x1D);
        sb.append("(k");
        sb.append((char) 3);
        sb.append((char) 0);
        sb.append((char) 49);
        sb.append((char) 67);
        sb.append((char) moduleSize);
        sb.append((char) 0x1D);
        sb.append("(k");
        sb.append((char) 3); // pl
        sb.append((char) 0); // ph
        sb.append((char) 49); // cn
        sb.append((char) 81); // fn
        sb.append((char) 48); // m
    }

    /**
     * 进纸并全部切割
     *
     * @return
     * @throws IOException
     */
    private void cmdFeedAndCut(StringBuffer sb) {
        sb.append((char) 0x1D);
        sb.append((char) 86);
        sb.append((char) 65);
        //        mOutputStreamWriter.write(0);
        //切纸前走纸多少
        sb.append((char) 100);
        //另外一种切纸的方式
        //        byte[] bytes = {29, 86, 0};
        //        socketOut.write(bytes);
    }

    /**
     * 打开钱箱
     *
     * @return
     */
    private String openCashbox() {
        return String.valueOf((char) 27) + (char) 112 + (char) 0 + (char) 60 + (char) 255;
    }

    /**
     * 打印换行
     *
     * @return length 需要打印的空行数
     * @throws IOException
     */
    private void cmdPrintLine(StringBuffer sb, int lineNum) {
        for (int i = 0; i < lineNum; i++) {
            sb.append("\n");
        }
    }

    /**
     * 58 80
     *
     * @param sb
     * @param size
     */
    private void cmdPrintStar(StringBuffer sb, PaperSize size) {
        sb.append("\n");
        for (int i = 0; i < size.getSize(); i++) {
            sb.append("-");
        }
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    private void cmdPrintLine(StringBuffer sb) {
        sb.append("\n");
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    private static void printTabSpace(StringBuffer sb, int length) {
        for (int i = 0; i < length; i++) {
            sb.append("\t");
        }
    }

    /**
     * 打印空白（一个汉字的位置）
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    private void cmdPrintWordSpace(StringBuffer sb, int length) {
        for (int i = 0; i < length; i++) {
            sb.append("  ");
        }
    }

    /**
     * @param sb
     * @param str
     * @param maxLength
     */
    private void cmdPatchSpace(StringBuffer sb, String str, int maxLength) {
        int length = 0;
        try {
            length = str.getBytes("gbk").length;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < maxLength - length; i++) {
            sb.append(" ");
        }
    }

    /**
     * 打印位置调整
     *
     * @param position 打印位置  0：居左(默认) 1：居中 2：居右
     * @throws IOException
     */
    private void cmdPrintLocation(StringBuffer sb, int position) {
        sb.append((char) 0x1B);
        sb.append((char) 97);
        sb.append(position);
    }

    /**
     * 绝对打印位置
     *
     * @throws IOException
     */
    private void cmdPrintLocation(StringBuffer sb, int light, int weight) {
        sb.append((char) 0x1B);
        sb.append((char) 0x24);
        sb.append((char) light);
        sb.append((char) weight);
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    private void cmdPrintText(StringBuffer sb, String text) {
        sb.append(text);
    }

    /**
     * 新起一行，打印文字
     *
     * @param text
     * @throws IOException
     */
    private void cmdPrintTextNewLine(StringBuffer sb, String text) {
        //换行
        sb.append("\n");
        sb.append(text);
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    private void cmdInitPos(StringBuffer sb) {
        sb.append((char) 0x1B).append((char) 0x40);
    }

    /**
     * 加粗
     *
     * @param flag false为不加粗
     * @return
     * @throws IOException
     */
    private void cmdBold(StringBuffer sb, boolean flag) {
        if (flag) {
            //常规粗细
            sb.append((char) 0x1B);
            sb.append((char) 69);
            sb.append((char) 0xF);
        } else {
            //加粗
            sb.append((char) 0x1B);
            sb.append((char) 69);
            sb.append((char) 0);
        }
    }

    /**
     * 设置字体大小
     *
     * @param sb
     * @param fontXSize
     * @param fontYSize
     */
    private void cmdSetFontSize(StringBuffer sb, int fontXSize, int fontYSize) {
        String _cmdstr = "";
        // 设置字体大小
        _cmdstr = new StringBuffer().append((char) 29).append((char) 33)
                .append((char) (GetParaByXMultiple(fontXSize) + GetParaByYMultiple(fontYSize)))
                .toString();// 29
        // 传输的命令集
        sb.append(_cmdstr);
    }

    /**
     * 打印机来单打印 蜂鸣提示及报警灯闪烁（适用于 GP-80250  系列）
     * 格式：ESC C m t n (m和t的范围 1~20  n的范围：0~3)
     * m指报警灯闪烁次数或蜂鸣器鸣叫次数；
     * t指报警灯闪烁间隔时间为 t*50 ms 或蜂鸣器鸣叫间隔时间为(t × 50)毫秒；
     * 当 n=0 时，蜂鸣器不鸣叫，同时报警灯不闪烁；当 n=1 时，蜂鸣器鸣叫；当 n=2 时，报警灯闪烁；当 n=3 时，蜂鸣器鸣叫，同时报警灯闪烁；
     *
     * @param sb
     * @param m
     * @param t
     * @param n
     */
    private void SetPrintBuzzerAndAlarmLight(StringBuffer sb, int m, int t, int n) {
        sb.append((char) 27);
        sb.append((char) 67);
        sb.append((char) m);
        sb.append((char) t);
        sb.append((byte) n);
    }

    /**
     * 字体大小
     *
     * @param fontsize 0:正常大小 1:两倍高 2:两倍宽 3:两倍大小 4:三倍高 5:三倍宽 6:三倍大小
     *                 7:四倍高 8:四倍宽 9:四倍大小 10:五倍高 11:五倍宽 12:五倍大小
     * @return
     * @throws IOException
     */
    private void cmdSetFontSize(StringBuffer sb, int fontsize) {
        String _cmdstr = "";
        // 设置字体大小
        switch (fontsize) {
            case -1:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 0).toString();// 29
                // 33
                break;
            case 0:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 0).toString();// 29
                // 33
                break;
            case 1:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 1).toString();
                break;
            case 2:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 16).toString();
                break;
            case 3:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 17).toString();
                break;
            case 4:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 2).toString();
                break;
            case 5:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 32).toString();
                break;
            case 6:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 34).toString();
                break;
            case 7:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 3).toString();
                break;
            case 8:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 48).toString();
                break;
            case 9:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 51).toString();
                break;
            case 10:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 4).toString();
                break;
            case 11:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 64).toString();
                break;
            case 12:
                _cmdstr = new StringBuffer().append((char) 29).append((char) 33).append((char) 68).toString();
                break;
            default:
                break;
        }
        // 传输的命令集
        sb.append(_cmdstr);
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    private void print(String text) throws IOException {
        mOutputStreamWriter.write(text, 0, text.length());
        mOutputStreamWriter.flush();
    }

    /**
     * 打印数组
     *
     * @param vector
     * @throws IOException
     */
    private void print(Vector<Byte> vector) throws IOException {
        for (Byte b : vector) {
            mOutputStreamWriter.write(b);
        }
        mOutputStreamWriter.flush();
    }

    /**
     * 宽度放大倍数对应的指令值
     *
     * @param xMultiple 宽度放大倍数
     * @return
     */
    private int GetParaByXMultiple(int xMultiple) {
        switch (xMultiple) {
            case 1:
                return 0;
            case 2:
                return 16;
            case 3:
                return 32;
            case 4:
                return 48;
            case 5:
                return 64;
            case 6:
                return 80;
            case 7:
                return 96;
            case 8:
                return 112;
            default:
                return 0;
        }
    }

    /**
     * 高度放大倍数对应的指令值
     *
     * @param yMultiple 宽度放大倍数
     * @return
     */
    private int GetParaByYMultiple(int yMultiple) {
        switch (yMultiple) {
            case 1:
                return 0;
            case 2:
                return 1;
            case 3:
                return 2;
            case 4:
                return 3;
            case 5:
                return 4;
            case 6:
                return 5;
            case 7:
                return 6;
            case 8:
                return 7;
            default:
                return 0;
        }
    }

    /***图片打印*/
    public byte[] BitmapToByte(Bitmap b) {
        int ww = b.getWidth();
        int h = b.getHeight();
        int w = (ww - 1) / 8 + 1;
        byte[] data = new byte[h * w + 8];
        data[0] = 0x1D;
        data[1] = 0x76;
        data[2] = 0x30;
        data[3] = 0x00;
        data[4] = (byte) w;// xL
        data[5] = (byte) (w >> 8);// xH
        data[6] = (byte) h;
        data[7] = (byte) (h >> 8);
        getAllPixels_gh(b, data);
        return data;
    }

    /**
     * @param bit
     * @param gh
     */
    private void getAllPixels_gh(Bitmap bit, byte[] gh) {
        int k = bit.getWidth() * bit.getHeight();
        int[] pixels = new int[k];
        bit.getPixels(pixels, 0, bit.getWidth(), 0, 0, bit.getWidth(), bit.getHeight());
        int j = 7;
        int index = 8;
        for (int i = 0; i < pixels.length; i++) {
            int clr = pixels[i];
            int red = (clr & 0x00ff0000) >> 16;
            int green = (clr & 0x0000ff00) >> 8;
            int blue = clr & 0x000000ff;
            if (j == -1) {
                j = 7;
                index++;
            }
            gh[index] = (byte) (gh[index] | (RGB2Gray(red, green, blue) << j));
            j--;
        }
    }

    /**
     * @param r
     * @param g
     * @param b
     * @return
     */
    private byte RGB2Gray(int r, int g, int b) {
        return (int) (0.29900 * r + 0.58700 * g + 0.11400 * b) < 150 ? (byte) 1 : (byte) 0;
    }

    /**
     *
     */
    private enum PaperSize {
        Size58(32), Size80(48);

        public int getSize() {
            return size;
        }

        private int size;

        PaperSize(int i) {
            size = i;
        }

        public static PaperSize getValue(int value) {
            switch (value) {
                case 58:
                    return PaperSize.Size58;
                case 80:
                    return PaperSize.Size80;
                default:
                    return PaperSize.Size80;
            }
        }
    }
}
