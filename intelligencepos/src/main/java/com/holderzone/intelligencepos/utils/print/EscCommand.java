package com.holderzone.intelligencepos.utils.print;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Vector;

public class EscCommand {
    private static final String DEBUG_TAG = "EscCommand";
    Vector<Byte> Command = null;

    public enum STATUS {
        PRINTER_STATUS(1),
        PRINTER_OFFLINE(2),
        PRINTER_ERROR(3),
        PRINTER_PAPER(4);

        private final int value;

        STATUS(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum ENABLE {
        OFF(0),
        ON(1);

        private final int value;

        ENABLE(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum UNDERLINE_MODE {
        OFF(0),
        UNDERLINE_1DOT(1),
        UNDERLINE_2DOT(2);

        private final int value;

        UNDERLINE_MODE(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum FONT {
        FONTA(0),
        FONTB(1);

        private final int value;

        FONT(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum CHARACTER_SET {
        USA(0),
        FRANCE(1),
        GERMANY(2),
        UK(3),
        DENMARK_I(4),
        SWEDEN(5),
        ITALY(6),
        SPAIN_I(7),
        JAPAN(8),
        NORWAY(9),
        DENMARK_II(10),
        SPAIN_II(11),
        LATIN_AMERCIA(12),
        KOREAN(13),
        SLOVENIA(14),
        CHINA(15);

        private final int value;

        CHARACTER_SET(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum JUSTIFICATION {
        LEFT(0),
        CENTER(1),
        RIGHT(2);

        private final int value;

        JUSTIFICATION(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum CODEPAGE {
        PC437(0),
        KATAKANA(1),
        PC850(2),
        PC860(3),
        PC863(4),
        PC865(5),
        WEST_EUROPE(6),
        GREEK(7),
        HEBREW(8),
        EAST_EUROPE(9),
        IRAN(10),
        WPC1252(16),
        PC866(17),
        PC852(18),
        PC858(19),
        IRANII(20),
        LATVIAN(21),
        ARABIC(22),
        PT151(23),
        PC747(24),
        WPC1257(25),
        VIETNAM(27),
        PC864(28),
        PC1001(29),
        UYGUR(30),
        THAI(255);

        private final int value;

        CODEPAGE(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum WIDTH_ZOOM {
        MUL_1(0), MUL_2(16), MUL_3(32), MUL_4(48), MUL_5(64), MUL_6(80), MUL_7(96), MUL_8(112);

        private final int value;

        WIDTH_ZOOM(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum HEIGHT_ZOOM {
        MUL_1(0), MUL_2(1), MUL_3(2), MUL_4(3), MUL_5(4), MUL_6(5), MUL_7(6), MUL_8(7);

        private final int value;

        HEIGHT_ZOOM(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public enum HRI_POSITION {
        NO_PRINT(0), ABOVE(1), BELOW(2), ABOVE_AND_BELOW(3);

        private final int value;

        HRI_POSITION(int value) {
            this.value = value;
        }

        public byte getValue() {
            return (byte) this.value;
        }
    }

    public EscCommand() {
        this.Command = new Vector(4096, 1024);
    }

    private void addArrayToCommand(byte[] array) {
        for (int i = 0; i < array.length; i++) {
            this.Command.add(Byte.valueOf(array[i]));
        }
    }

    private void addStrToCommand(String str, String charset) {
        byte[] bs = null;
        if (!str.equals("")) {
            try {
                bs = str.getBytes(charset);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < bs.length; i++) {
                this.Command.add(Byte.valueOf(bs[i]));
            }
        }
    }

    private void addStrToCommand(String str, int length) {
        byte[] bs = null;
        if (!str.equals("")) {
            try {
                bs = str.getBytes("GB2312");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.d("EscCommand", "bs.length" + bs.length);
            if (length > bs.length) {
                length = bs.length;
            }
            Log.d("EscCommand", "length" + length);
            for (int i = 0; i < length; i++) {
                this.Command.add(Byte.valueOf(bs[i]));
            }
        }
    }

    public void addText(String text, String charsetName) {
        addStrToCommand(text, charsetName);
    }

    public void addPrintAndLineFeed() {
        byte[] command = {10};
        addArrayToCommand(command);
    }

    public Vector<Byte> getCommand() {
        return this.Command;
    }

    public void addInitializePrinter() {
        byte[] command = {27, 64};
        addArrayToCommand(command);
    }

    public void addTurnEmphasizedModeOnOrOff(ENABLE enabel) {
        byte[] command = new byte[]{(byte) 27, (byte) 32, enabel.getValue()};
        addArrayToCommand(command);
    }

    /**
     * addSelectJustification(JUSTIFICATION just)
     * 功能：选择对齐方式
     * 参数： enum JUSTIFICATION{
     * LEFT(0), // 左对齐
     * CENTER(1),// 居中对齐
     * RIGHT(2);//右对齐
     * }
     *
     * @param just
     */
    public void addSelectJustification(JUSTIFICATION just) {
        byte[] command = new byte[]{(byte) 27, (byte) 97, just.getValue()};
        addArrayToCommand(command);
    }

    /**
     * 添加打印行的指令
     *
     * @param n
     */
    public void addPrintAndFeedLines(byte n) {
        byte[] command = new byte[]{(byte) 27, (byte) 100, n};
        addArrayToCommand(command);
    }


    public void addSelectCodePage(CODEPAGE page) {
        byte[] command = {27, 116, 0};
        command[2] = page.getValue();
        addArrayToCommand(command);
    }

    public void addTurnUpsideDownModeOnOrOff(ENABLE enable) {
        byte[] command = {27, 123, 0};
        command[2] = enable.getValue();
        addArrayToCommand(command);
    }

    public void addSetCharcterSize(WIDTH_ZOOM width, HEIGHT_ZOOM height) {
        byte[] command = {29, 33, 0};
        byte temp = 0;
        temp = (byte) (temp | width.getValue());
        temp = (byte) (temp | height.getValue());
        command[2] = temp;
        addArrayToCommand(command);
    }

    public void addTurnReverseModeOnOrOff(ENABLE enable) {
        byte[] command = {29, 66, 0};
        command[2] = enable.getValue();
        addArrayToCommand(command);
    }

    public void addSelectPrintingPositionForHRICharacters(HRI_POSITION position) {
        byte[] command = {29, 72, 0};
        command[2] = position.getValue();
        addArrayToCommand(command);
    }

    public void addSetBarcodeHeight(byte height) {
        byte[] command = new byte[]{(byte) 29, (byte) 104, height};
        this.addArrayToCommand(command);
    }

    public void addSetBarcodeWidth(byte width) {
        byte[] command = new byte[]{(byte) 29, (byte) 119, (byte) 0};
        if (width > 6) {
            width = 6;
        }
        if (width < 2) {
            width = 2;
        }
        command[2] = width;
        this.addArrayToCommand(command);
    }

    public void addSetKanjiFontMode(ENABLE DoubleWidth, ENABLE DoubleHeight, ENABLE Underline) {
        byte[] command = {28, 33, 0};
        byte temp = 0;
        if (DoubleWidth == ENABLE.ON) {
            temp = (byte) (temp | 0x4);
        }
        if (DoubleHeight == ENABLE.ON) {
            temp = (byte) (temp | 0x8);
        }
        if (Underline == ENABLE.ON) {
            temp = (byte) (temp | 0x80);
        }
        command[2] = temp;
        addArrayToCommand(command);
    }

    /**
     * @param bitmap 位图
     * @param nWidth 打印宽度（可以用于缩放图片）
     * @param nMode  打印模式 0： 正常 1：倍宽 2：倍高 3：倍宽 倍高
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

    public void addDownloadNvBitImage(Bitmap[] bitmap) {
        if (bitmap != null) {
            Log.d("BMP", "bitmap.length " + bitmap.length);
            int n = bitmap.length;
            if (n > 0) {
                byte[] command = new byte[3];
                command[0] = 28;
                command[1] = 113;
                command[2] = ((byte) n);
                addArrayToCommand(command);
                for (int i = 0; i < n; i++) {
                    int height = (bitmap[i].getHeight() + 7) / 8 * 8;
                    int width = bitmap[i].getWidth() * height / bitmap[i].getHeight();
                    Bitmap grayBitmap = GpUtils.toGrayscale(bitmap[i]);
                    Bitmap rszBitmap = GpUtils.resizeImage(grayBitmap, width, height);
                    byte[] src = GpUtils.bitmapToBWPix(rszBitmap);
                    height = src.length / width;
                    Log.d("BMP", "bmp  Width " + width);
                    Log.d("BMP", "bmp  height " + height);
                    byte[] codecontent = GpUtils.pixToEscNvBitImageCmd(src, width, height);
                    for (int k = 0; k < codecontent.length; k++) {
                        this.Command.add(Byte.valueOf(codecontent[k]));
                    }
                }
            }
        } else {
            Log.d("BMP", "bmp.  null ");
            return;
        }
    }

    public void addPrintNvBitmap(byte n, byte mode) {
        byte[] command = {28, 112, 0, 0};
        command[2] = n;
        command[3] = mode;
        addArrayToCommand(command);
    }

    public void addUPCA(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 65;
        command[3] = 11;
        if (content.length() < command[3]) {
            return;
        }
        addArrayToCommand(command);
        addStrToCommand(content, 11);
    }

    public void addUPCE(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 66;
        command[3] = 11;
        if (content.length() < command[3]) {
            return;
        }
        addArrayToCommand(command);
        addStrToCommand(content, command[3]);
    }

    public void addEAN13(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 67;
        command[3] = 12;
        if (content.length() < command[3]) {
            return;
        }
        addArrayToCommand(command);
        Log.d("EscCommand", "content.length" + content.length());
        addStrToCommand(content, command[3]);
    }

    public void addEAN8(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 68;
        command[3] = 7;
        if (content.length() < command[3]) {
            return;
        }
        addArrayToCommand(command);
        addStrToCommand(content, command[3]);
    }

    @SuppressLint({"DefaultLocale"})
    public void addCODE39(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 69;
        command[3] = ((byte) content.length());
        content = content.toUpperCase();
        addArrayToCommand(command);
        addStrToCommand(content, command[3]);
    }

    public void addITF(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 70;
        command[3] = ((byte) content.length());
        addArrayToCommand(command);
        addStrToCommand(content, command[3]);
    }

    public void addCODABAR(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 71;
        command[3] = ((byte) content.length());
        addArrayToCommand(command);
        addStrToCommand(content, command[3]);
    }

    public void addCODE93(String content) {
        byte[] command = new byte[4];
        command[0] = 29;
        command[1] = 107;
        command[2] = 72;
        command[3] = ((byte) content.length());
        addArrayToCommand(command);
        addStrToCommand(content, command[3]);
    }

    public void addCODE128(String content) {
        byte[] data = content.getBytes();
        if (data[0] != 123) {
            byte[] command = new byte[]{(byte) 29, (byte) 107, (byte) 73, (byte) (content.length() + 2), (byte) 123, (byte) 66};
            this.addArrayToCommand(command);
        } else {
            byte[] command = new byte[]{(byte) 29, (byte) 107, (byte) 73, (byte) content.length()};
            this.addArrayToCommand(command);
        }
        this.addStrToCommand(content, content.length());
    }

    /**
     * 功能：设置QRCode的单元模块大小
     * 参数：n： 单元模块为n点 默认为3点
     * 返回值：无
     * 相关指令：GP58编程手册 GS ( k <Function 167>
     *
     * @param n
     */
    public void addSelectSizeOfModuleForQRCode(byte n) {
        byte[] command = {29, 40, 107, 3, 0, 49, 67, 3};
        command[7] = n;
        addArrayToCommand(command);
    }

    public void addSelectErrorCorrectionLevelForQRCode(byte n) {
        byte[] command = new byte[]{29, 40, 107, 3, 0, 49, 69, n};
        addArrayToCommand(command);
    }

    public void addStoreQRCodeData(String content) {
        byte[] command = {29, 40, 107, 0, 0, 49, 80, 48};
        command[3] = ((byte) ((content.length() + 3) % 256));
        command[4] = ((byte) ((content.length() + 3) / 256));
        addArrayToCommand(command);
        addStrToCommand(content, content.length());
    }

    public void addPrintQRCode() {
        byte[] command = {29, 40, 107, 3, 0, 49, 81, 48};
        addArrayToCommand(command);
    }

    public void addUserCommand(byte[] command) {
        addArrayToCommand(command);
    }
}
