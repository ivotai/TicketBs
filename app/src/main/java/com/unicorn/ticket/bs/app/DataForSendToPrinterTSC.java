package com.unicorn.ticket.bs.app;

import android.graphics.Bitmap;

import net.posprinter.posprintersdk.utils.BitmapToByteData;
import net.posprinter.posprintersdk.utils.BitmapToByteData.BmpType;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;


/**
 * @author chensong<br>
 */
public class DataForSendToPrinterTSC {
    private static String charsetName = "gbk";

    /**
     *
     * */
    public static void setCharsetName(String charset) {
        charsetName = charset;
    }

    /**
     * This command defines the label width and length.<br>
     * SIZE m mm,n mm<br>
     *
     * @param m ,Label width (inch/ mm/ dot)
     * @param n ,Label length (inch/ mm/ dot)
     * @return byte[]
     */
    public static byte[] sizeBymm(double m, double n) {
        String str = "SIZE " + m + " mm," + n + " mm\n";

        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command defines the label width and length.<br>
     * SIZE m mm,n mm<br>
     *
     * @param m ,Label width (inch/ mm/ dot)
     * @param n ,Label length (inch/ mm/ dot)
     * @return byte[]
     */
    public static byte[] sizeByinch(double m, double n) {
        String str = "SIZE " + m + "," + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command defines the label width and length.<br>
     * SIZE m mm,n mm<br>
     *
     * @param m ,Label width (inch/ mm/ dot)
     * @param n ,Label length (inch/ mm/ dot)
     * @return byte[]
     */
    public static byte[] sizeBydot(int m, int n) {
        String str = "SIZE " + m + " dot," + n + " dot\n";

        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Defines the gap distance between two labels.<br>
     * GAP m,n<br>
     *
     * @param m,The gap distance between two labels<br>
     *              0 ?? m ??1 (inch), 0 ?? m ?? 25.4 (mm)<br>
     *              0 ?? m ??5 (inch), 0 ?? m ?? 127 (mm) / since V6.21 EZ and later firmware<br>
     * @param n,The offset distance of the gap<br>
     *              n ?? label length (inch or mm)	<br>
     * @return byte[]
     */
    public static byte[] gapByinch(double m, double n) {
        String str = "GAP " + m + "," + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Defines the gap distance between two labels<br>.
     * GAP m,n<br>
     *
     * @param m,The gap distance between two labels<br>
     * @param n,The offset distance of the gap<br>
     *              n ?? label length (inch or mm)		<br>
     * @return byte[]
     */
    public static byte[] gapBymm(double m, double n) {
        String str = "GAP " + m + " mm," + n + " mm\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Defines the gap distance between two labels.<br>
     * GAP m,n<br>
     *
     * @param n The offset distance of the gap<br>
     *          n ?? label length (inch or mm)
     * @return byte[]
     */
    public static byte[] gapBydot(int m, int n) {
        String str = "GAP " + m + " dot," + n + " dot\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command feeds the paper through the gap sensor in an effort to determine the paper and gap
     * sizes, respectively. This command references the user??s approximate measurements. If the
     * measurements conflict with the actual size, the GAPDETECT command will not work properly. This
     * calibration method can be applied to the labels with pre-printed logos or texts.<br>
     *
     * @param x Paper length (in dots)
     * @param y Gap length (in dots)
     * @return byte[]
     */
    public static byte[] gapDetect(int x, int y) {
        String str = "GAPDETECT " + x + "," + y + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command feeds the paper through the gap sensor in an effort to determine the paper and gap
     * sizes, respectively. This command references the user??s approximate measurements. If the
     * measurements conflict with the actual size, the GAPDETECT command will not work properly. This
     * calibration method can be applied to the labels with pre-printed logos or texts.<br>
     *
     * @return byte[]
     */
    public static byte[] gapDetect() {
        String str = "GAPDETECT\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command feeds the paper through the black mark sensor in an effort to determine the paper and
     * black mark sizes, respectively. This command references the user??s approximate measurements. If the
     * measurements conflict with the actual size, the BLINEDETECT command will not work properly. This
     * calibration method can be applied to the labels with pre-printed logos or texts.<br>
     *
     * @return byte[]
     */
    public static byte[] blineDetect(int x, int y) {
        String str = "BLINEDETECT " + x + "," + y + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command feeds the paper through the gap/black mark sensor in an effort to determine the paper
     * and gap/black mark sizes, respectively. This command references the user??s approximate
     * measurements. If the measurements conflict with the actual size, the AUTODETECT command will not
     * work properly. This calibration method can be applied to the labels with pre-printed logos or texts.
     *
     * @return byte[]
     */
    public static byte[] autoDetect(int x, int y) {
        String str = "AUTODETECT " + x + "," + y + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command sets the height of the black line and the user-defined extra label feeding length each
     * form feed takes.<br>
     * BLINE m,n <br>
     *
     * @param m The height of black line either in inch or mm<br>
     *          0 ?? m ?? 1 (inch), 0 ?? m ?? 25.4 (mm)
     *          0 ?? m ??5 (inch), 0 ?? m ?? 127 (mm) / since V6.21 EZ and later firmware<br>
     * @param n The extra label feeding length<br>
     *          0 ?? n ?? label length<br>
     * @return byte[]
     */
    public static byte[] blineByinch(double m, double n) {
        String str = "BLINE " + m + "," + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????O????????????λ??<br>
     * This command sets the height of the black line and the user-defined extra label feeding length each
     * form feed takes.<br>
     * BLINE m dot,n dot <br>
     *
     * @param m The height of black line either in inch or mm<br>
     *          0 ?? m ?? 1 (inch), 0 ?? m ?? 25.4 (mm)<br>
     *          0 ?? m ??5 (inch), 0 ?? m ?? 127 (mm) / since V6.21 EZ and later firmware<br>
     * @param n The extra label feeding length<br>
     *          0 ?? n ?? label length<br>
     * @return byte[]
     */
    public static byte[] blineBymm(double m, double n) {
        String str = "BLINE " + m + " mm," + n + " mm\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????O????????????λ??<br>
     * This command sets the height of the black line and the user-defined extra label feeding length each
     * form feed takes.<br>
     * BLINE m mm,n mm <br>
     *
     * @param m The height of black line either in inch or mm<br>
     *          0 ?? m ?? 1 (inch), 0 ?? m ?? 25.4 (mm)<br>
     *          0 ?? m ??5 (inch), 0 ?? m ?? 127 (mm) / since V6.21 EZ and later firmware<br>
     * @param n The extra label feeding length<br>
     *          0 ?? n ?? label length<br>
     * @return byte[]
     */
    public static byte[] blineBydot(int m, int n) {
        String str = "BLINE " + m + " dot," + n + " dot\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????????????x???r(pee-off mode)?????`????λ???????H?m?????x????<br>
     * OFFSET m<br>
     *
     * @param mThe offset distance (inch or mm)<br>
     *             -1 ?? m ?? 1 (inch)<br>
     * @return byte[]
     */
    public static byte[] offSetByinch(double m) {
        String str = "OFFSET " + m + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????????????x???r(pee-off mode)?????`????λ???????H?m?????x????<br>
     * OFFSET m<br>
     *
     * @param mThe offset distance (inch or mm)<br>
     *             -1 ?? m ?? 1 (inch)<br>
     * @return byte[]
     */
    public static byte[] offSetBymm(double m) {
        String str = "OFFSET " + m + " mm\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????????????x???r(pee-off mode)?????`????λ???????H?m?????x????<br>
     * OFFSET m<br>
     *
     * @param mThe offset distance (inch or mm)<br>
     *             -1 ?? m ?? 1 (inch)<br>
     * @return byte[]
     */
    public static byte[] offSetBydot(int m) {
        String str = "OFFSET " + m + " dot\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????????????x???r(pee-off mode)?????`????λ???????H?m?????x??<br>
     * This command defines the print speed<br>
     * SPEED n<br>
     *
     * @param n Printing speed in inch per second<br>
     * @return byte[]
     */
    public static byte[] speed(double n) {
        String str = "SPEED " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command sets the printing darkness<br>
     * DENSITY n<br>
     *
     * @param n 0~15<br>
     *          0: specifies the lightest level<br>
     *          15: specifies the darkest level<br>
     * @return byte[]
     */
    public static byte[] density(int n) {
        String str = "DENSITY " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ?????????x????r?????????<br>
     * This command defines the printout direction and mirror image. This will be stored in the printermemory.<br>
     * DIRECTION n[,m]<br>
     *
     * @param n 0 or 1. Please refer to the illustrations below<br>
     * @return byte[]
     */
    public static byte[] direction(int n) {
        String str = "DIRECTION " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ?????????x??`???????????c<br>
     * This command defines the reference point of the label. The reference (origin) point varies with the
     * print direction, as shown:<br>
     * REFERENCE x, y<br>
     *
     * @param x Horizontal coordinate (in dots)
     * @param y Vertical coordinate (in dots)
     * @return byte[]
     */
    public static byte[] reference(int x, int y) {
        String str = "REFERENCE " + x + ", " + y + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command moves the label??s vertical position. A positive value moves the label further from the
     * printing direction; a negative value moves the label towards the printing direction. For a visual
     * representation, see next page.<br>
     * SHIFT n<br>
     *
     * @param n The maximum value is 1 inch. For 200 dpi printers, the range is ?C203 to 203; for
     *          300 dpi printers, the range is ?C300 to 300. The unit is dot.<br>
     * @return byte[]
     */
    public static byte[] shift(int n) {
        String str = "SHIFT " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????O????y??I?P(?x??KP-200)LCD ????????????@?????????<br>
     * This command orients the keyboard for use in different countries via defining special characters on the
     * KP-200 series portable LCD keyboard (option).<br>
     * COUNTRY n<br>
     *
     * @param n 001: USA<br>
     *          002: Canadian-French<br>
     *          003: Spanish (Latin America)<br>
     *          031: Dutch<br>
     *          032: Belgian<br>
     *          033: French (France)<br>
     *          034: Spanish (Spain)<br>
     *          036: Hungarian<br>
     *          038: Yugoslavian<br>
     *          039: Italian<br>
     *          041: Switzerland<br>
     *          042: Slovak<br>
     *          044: United Kingdom<br>
     *          045: Danish<br>
     *          046: Swedish<br>
     *          047: Norwegian<br>
     *          048: Polish<br>
     *          049: German<br>
     *          055: Brazil<br>
     *          061: English (International)<br>
     *          351: Portuguese<br>
     *          358: Finnish<br>
     * @return byte[]
     */
    public static byte[] country(String n) {
        String str = "COUNTRY " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ?????????x??y??I?P(?x??KP-200)?????????I??????????H?????<br>
     * CODEPAGE n<br>
     *
     * @param n ?????????????????????M????^??? 7-bit ?? 8-bit<br>\
     * @return byte[]
     */
    public static byte[] codePage(String n) {
        String str = "CODEPAGE " + n + "\n";

        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????????????n?^(image buffer)???Y??<br>
     * This command clears the image buffer.<br>
     * CLS<br>
     *
     * @return byte[]
     */
    public static byte[] cls() {
        String str = "CLS\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ???????????M??????x?????c(dot)???<br>
     * This command feeds label with the specified length. The length is specified by dot<br>
     * FEED n<br>
     *
     * @param n unit: dot<br>
     *          1 ?? n ?? 9999<br>
     * @return byte[]
     */
    public static byte[] feed(int n) {
        String str = "FEED " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command feeds the label in reverse. The length is specified by dot<br>
     * BACKFEED n <br>
     *
     * @param n unit: dot,1 ?? n ?? 9999<br>
     * @return byte[]
     */
    public static byte[] backFeed(int n) {
        String str = "BACKFEED " + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????????C?M?????<br>
     * This command feeds label to the beginning of next label<br>
     *
     * @return byte[]
     */
    public static byte[] formFeed() {
        String str = "FORMFEED\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????_?C???????????`?r?????λ?????????_????????????`?r???λ???????_??????_?C????????`???????????_??λ????????????<br>
     * This command will feed label until the internal sensor has determined the origin. Size and gap of the
     * label should be defined before using this command.<br>
     * HOME<br>
     *
     * @return byte[]
     */
    public static byte[] home() {
        String str = "HOME\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????????????????n?^?????Y??<br>
     * This command prints the label format currently stored in the image buffer.<br>
     * PRINT m [,n]<br>
     *
     * @param m Specifies how many sets of labels will be printed.1 ?? m ?? 999999999
     * @param n Specifies how many copies should be printed for each particular label set.1 ?? n ?? 999999999
     * @return byte[]
     */
    public static byte[] print(int m, int n) {
        String str = "PRINT " + m + "," + n + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????????????????n?^?????Y??<br>
     * This command prints the label format currently stored in the image buffer.<br>
     * PRINT m [,n]<br>
     *
     * @param m Specifies how many sets of labels will be printed.1 ?? m ?? 999999999<br>
     * @return byte[]
     */
    public static byte[] print(int m) {
        String str = "PRINT " + m + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????????Q?????l??????O?? 10 ?A?????????A?????L????????????????<br>
     * This command controls the sound frequency of the beeper. There are 10 levels of sounds. The timingcontrol can be set by the ??interval?? parameter.<br>
     * SOUND level,interval<br>
     *
     * @param level    Sound level: 0~9<br>
     * @param interval Sound interval: 1~4095<br>
     * @return byte[]
     */
    public static byte[] sound(int level, int interval) {
        String str = "SOUND " + level + "," + interval + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ???????r??????C??????????<br>
     * This command activates the cutter to immediately cut the labels without back feeding the label.<br>
     * CUT<br>
     *
     * @return byte[]
     */
    public static byte[] cut() {
        String str = "CUT\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????O??????C?M???r???????^???O?????L????o????y??????g???t????C?l???e?`?????M??<br>
     * LIMITFEED n<br>
     *
     * @param n ??????????<br>
     * @return byte[]
     */
    public static byte[] limitFeedByinch(double n) {
        String str = "LIMITFEED n\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????O??????C?M???r???????^???O?????L????o????y??????g???t????C?l???e?`?????M??<br>
     * LIMITFEED n<br>
     *
     * @param n ??????????<br>
     * @return byte[]
     */
    public static byte[] limitFeedBymm(double n) {
        String str = "LIMITFEED n mm\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????O??????C?M???r???????^???O?????L????o????y??????g???t????C?l???e?`?????M??<br>
     * LIMITFEED n<br>
     *
     * @param n ??????????<br>
     * @return byte[]
     */
    public static byte[] limitFeedBydot(int n) {
        String str = "LIMITFEED n dot\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * At this command, the printer will print out the printer information<br>
     * SELFTEST [page]<br>
     *
     * @return byte[]
     */
    public static byte[] selfTest() {
        String str = "SELFTEST\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * At this command, the printer will print out the printer information<br>
     * SELFTEST [page]<br>
     *
     * @param page omitted: Print a self-test page with whole printer information.<br>
     *             PATTERN: Print a pattern to check the status of print head heat line.<br>
     *             ETHERNET: Print a self-test page with Ethernet settings.<br>
     *             WLAN: Print a self-test page with Wi-Fi settings.<br>
     *             RS232: Print a self-test page with RS-232 settings.<br>
     *             SYSTEM: Print a self-test page with printer settings.<br>
     *             Z: Print a self-test page with emulated language settings.<br>
     * @return byte[]
     */
    public static byte[] selfTest(String page) {
        String str = "SELFTEST " + page + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Let the printer wait until process of commands (before EOJ) be finished then go on the next command.<br>
     * EOJ<br>
     * This command has been supported since V6.39 EZ and later firmware.<br>
     *
     * @return byte[]
     */
    public static byte[] eoj() {
        String str = "EOJ\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Let the printer wait specific period of time then go on next command.<br>
     * DELAY ms<br>
     *
     * @param ms The specific period of time. Unit is millisecond. 1000 ms = 1 second<br>
     * @return byte[]
     */
    public static byte[] delay(int ms) {
        String str = "DELAY " + ms + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command can show the image, which is in printer??s image buffer, on LCD panel<br>
     * DISPLAY IMAGE/OFF<br>
     *
     * @param s IMAGE  Show the image in printer??s image buffer on LCD panel.<br>
     *          OFF  Disable this function.<br>
     * @return byte[]
     */
    public static byte[] disPlay(String s) {
        String str = "DISPLAY " + s + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command can restore printer settings to defaults.<br>
     * INITIALPRINTER<br>
     *
     * @return byte[]
     */
    public static byte[] initialPrinter() {
        String str = "INITIALPRINTER\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ???`?????<br>
     * This command draws a bar on the label format.<br>
     * BAR x,y,width,height<br>
     *
     * @param x      The upper left corner x-coordinate (in dots)<br>
     * @param y      The upper left corner y-coordinate (in dots)<br>
     * @param width  Bar width (in dots)<br>
     * @return byte[]
     */
    public static byte[] bar(int x, int y, int width, int heigth) {
        String str = "BAR " + x + "," + y + "," + width + "," + heigth + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ?????????S?lΛ????? 23 ?N<br>
     * ???????????????<br>
     * This command prints 1D barcodes. The available barcodes are listed below:<br>
     * BARCODE X,Y,??codetype??,height,humanreadable,rotation,narrow,wide,[alignment,]??content ??<br>
     *
     * @return byte[]
     */
    public static byte[] barCode(int x, int y, String codeType, int heigth, int human, int rotation, int narrow, int wide, String content) {
        String str = "BARCODE " + x + "," + y + ",\"" + codeType + "\"," + heigth + "," + human + "," + rotation + "," + narrow + "," + wide + ",\"" + content + "\"\n";

        byte[] data = strTobytes(str);
        return data;
    }

    //add cjk 读取打印机状态
    public static byte[] checkStatus() {
        byte[] data = {0X1B, 0X21, 0X3F, 0X0A};
        return data;
    }
	/*
	public static byte[] tlc39(int x,int y,int rotation,int height,int narrow,int wide,int cellwidth,int cellheight,String eclnumber,String additionaldata){
		String str="TLC39 "+x+","+y+","+rotation+","+height+","+narrow+","+wide+","+cellwidth+","+cellheight+",\""+eclnumber+","+additionaldata+"\"\n";
		byte[] data=strTobytes(str);
		return data;}*/

    /**
     * ??????????`???L?u?c?D(?? BMP ????D?n)<br>
     * This command draws bitmap images (as opposed to BMP graphic files).<br>
     * BITMAP X,Y,width,height,mode,bitmap data??<br>
     *
     * @param x       Specify the x-coordinate<br>
     * @param y       Specify the y-coordinate<br>
     * @param mode    Graphic modes listed below:<br>
     *                0: OVERWRITE<br>
     *                1: OR<br>
     *                2: XOR<br>
     * @param bitmap  data Bitmap data<br>
     * @param bmpType ??????????
     * @return byte[]
     */
    public static byte[] bitmap(int x, int y, int mode, Bitmap bitmap, BmpType bmpType) {
        int width, heigth;
        width = (bitmap.getWidth() + 7) / 8;
        heigth = bitmap.getHeight();
        String str = "BITMAP " + x + "," + y + "," + width + "," + heigth + "," + mode + ",";
        String end = "\n";
        byte[] ended = strTobytes(end);
        byte[] head = strTobytes(str);
        byte[] data = BitmapToByteData.downLoadBmpToSendTSCData(bitmap, bmpType);
        data = byteMerger(head, data);
        data = byteMerger(data, ended);
        return data;
    }

    /**
     * ??????????`???L?u????<br>
     * This command draws rectangles on the labe<br>
     * BOX x,y,x_end,y_end,line thickness[,radius]<br>
     * ?????????ο???????<br>
     *
     * @param x Specify x-coordinate of upper left corner (in dots)
     * @return byte[]
     * @param        y Specify y-coordinate of upper left corner (in dots)
     * @param        x_end Specify x-coordinate of lower right corner (in dots)
     * @param        y_end Specify y-coordinate of lower right corner (in dots)
     * @param    thickness
     */
    public static byte[] box(int x, int y, int x_end, int y_end, int thickness) {
        String str = "BOX " + x + "," + y + "," + x_end + "," + y_end + "," + thickness + "\n";
        byte[] data = strTobytes(str);
        return data;
    }
    /**
     * This command draws a circle on the label<br>
     * CIRCLE X_start,Y_start,diameter,thickness<br>
     * @param    x_start  Specify x-coordinate of upper left corner (in dots)
     @param        y_start  Specify y-coordinate of upper left corner (in dots)
     @param        diameter  Specify the diameter of the circle (in dots)
     @param        thickness  Thickness of the circle (in dots)
      * *//*
	public static byte[] circle(int x_start,int y_start,int diameter,int thickness){
		String str="CIRCLE "+x_start+","+y_start+","+diameter+","+thickness+"\n";
		byte[] data=strTobytes(str);
		return data;}*/

    /**
     * This command draws an ellipse on the label.<br>
     * ELLIPSE x,y,width,height,thickness<br>
     *
     * @param x Specify x-coordinate of upper left corner (in dots)
     * @return byte[]
     * @param        y Specify y-coordinate of upper left corner (in dots)
     * @param        width Specify the width of the ellipse (in dots)
     * @param        height Specify the height of the ellipse (in dots)
     * @param        thickness Thickness of the ellipse (in dots)
     */
    public static byte[] ellipse(int x, int y, int width, int height, int thickness) {
        String str = "ELLIPSE " + x + "," + y + "," + width + "," + height + "," + thickness + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command draws CODABLOCK F mode barcode.<br>
     * CODABLOCK x,y,rotation,[row height,]module width,]??content??<br>
     *
     * @param x Specify the x-coordinate<br>
     * @return byte[]
     * @param            y Specify the y-coordinate<br>
     * @param            rotation 0  : No rotation<br>
     * 90 : Rotate 90 degrees clockwise<br>
     * 180 : Rotate 180 degrees clockwise<br>
     * 270 : Rotate 270 degrees clockwise<br>
     * @param            row_height The height of individual row equals to row height x module width (Default is
     * 8)
     * @param            module_width Width of narrow element of CODABLOCK in dots (Default is 2)
     * @param        content content of CODABLOCK bar code
     */
    public static byte[] codeBlockFMode(int x, int y, int rotation, int row_height, int module_width, String content) {
        String str = "CODABLOCK " + x + "," + y + "," + rotation + "," + row_height + "," + module_width + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????L?u DataMatrix ???S?l?a<br>
     * DMATRIX  x, y, width, height, [xm, row, col], expression<br>
     *
     * @param x ?l?a???????λ??????c(dot)???	<br>
     * @return byte[]
     * @param        y ?l?a????????λ??????c(dot)???<br>
     * @param        width ?l?a????????c(dot)???<br>
     * @param        height ?l?a???????c(dot)???<br>
     * @param        xm ??M??磬???c(dot)???<br>
     * @param        row ?l?a?Д? Symbol size of row: 10 to 144<br>
     * @param        col ?l?a??? Symbol size of col: 10 to 144<br>
     * @param    content Content of DataMatrix 2D bar code<br>
     */
    public static byte[] dmatrix(int x, int y, int width, int height, int xm, int row, int col, String content) {
        String str = "DMATRIX " + x + "," + y + "," + width + "," + height + "," + xm + "," + row + "," + col + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????????????n?^??????^????Y??<br>
     * This command clears a specified region in the image buffer.<br>
     *
     * @param x The x-coordinate of the starting point (in dots)<br>
     * @return byte[]
     * @param        y The y-coordinate of the starting point (in dots)<br>
     */
    public static byte[] erase(int x, int y, int width, int height) {
        String str = "ERASE " + x + "," + y + "," + width + "," + height + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ????????? PDF417 ???S?l?a<br>
     * This command defines a PDF417 2D bar code.<br>
     * PDF417 x,y,width,height,rotate,[option],??content??<br>
     *
     * @return byte[]
     * @param    x X-coordinate of starting point (in dot)<br>
     * @param        y Y-coordinate of starting point (in dot)<br>
     * @param        width Expected width (in dots)<br>
     * @param        height Expected height (in dots)<br>
     * @param        rotate Rotation counterclockwise<br>
     * 0  : No rotation<br>
     * 90 : Rotate 90 degrees<br>
     * 180 : Rotate 180 degrees<br>
     * 270 : Rotate 270 degrees<br>
     * @param        option ????????????????????????????????"E4,W4,T1";<br>
     * @param        content Content of PDF417 2D bar code<br>
     */
    public static byte[] pdf417(int x, int y, int width, int height, int rotate, String option, String content) {
        String str = "PDF417 " + x + "," + y + "," + width + "," + height + "," + rotate + "," + option + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints BMP format images. The grayscale printing is for direct thermal mode only.<br>
     * Support 1-bit (monochrome) and 8-bit (256-color) BMP graphic only.<br>
     * PUTBMP x,y,??filename??[, bpp][, contract]<br>
     *
     * @return byte[]
     * @param    x The x-coordinate of the BMP format image<br>
     * @param        y The y-coordinate of the BMP format image<br>
     * @param        filename The downloaded BMP filename<br>
     * @param        bpp Optional. Bits per pixel of grayscale graphic. Default is 1. *Since V6.91EZ.<br>
     * 1: 1-bit (monochrome) graphic<br>
     * 8: 8-bit (256-color) graphic<br>
     * @param        contrast Optional. Contrast of grayscale graphic. Default is 80. Suggested range is<br>
     * from 60 to 100. *Since V6.91EZ.<br>
     */
    public static byte[] putBmp(int x, int y, String filename, int bpp, int contrast) {
        String str = "PUTBMP " + x + "," + y + ",\"" + filename + "\", " + bpp + ", " + contrast + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints BMP format images. The grayscale printing is for direct thermal mode only.<br>
     * Support 1-bit (monochrome) and 8-bit (256-color) BMP graphic only.<br>
     * PUTBMP x,y,??filename??[, bpp][, contract]<br>
     *
     * @return byte[]
     * @param    x The x-coordinate of the BMP format image<br>
     * @param        y The y-coordinate of the BMP format image<br>
     * @param        filename The downloaded BMP filename<br>
     * 1: 1-bit (monochrome) graphic<br>
     * 8: 8-bit (256-color) graphic<br>
     * from 60 to 100. *Since V6.91EZ.<br>
     */
    public static byte[] putBmp(int x, int y, String filename) {
        String str = "PUTBMP " + x + "," + y + ",\"" + filename + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints PCX format images. TSPL language supports 2-color PCX format graphics. TSPL2 language supports 256-color PCX format graphics. <br>
     * PUTPCX x,y,??filename??<br>
     *
     * @return byte[]
     * @param    x The x-coordinate of the BMP format image<br>
     * @param        y The y-coordinate of the BMP format image<br>
     * @param        filename The downloaded pcx filename<br>
     */
    public static byte[] putpcx(int x, int y, String filename) {
        String str = "PUTPCX " + x + "," + y + ",\"" + filename + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints QR code.<br>
     * QRCODE x,y,ECC Level,cell width,mode,rotation,[model,mask,]??content??<br>
     *
     * @param    x The upper left corner x-coordinate of the QR code<br>
     * @param        y The upper left corner y-coordinate of the QR code<br>
     * @param        eccLevel Error correction recovery level<br>
     * L  : 7%<br>
     * M  : 15%<br>
     * Q  : 25%<br>
     * H  : 30%<br>
     * @param            cellWidth 1~10<br>
     * @param        mode Auto / manual encode<br>
     * A : Auto<br>
     * M : Manual<br>
     * @param        rotation 0  :<br>
     * 0 degree<br>
     * 90 : 90 degree<br>
     * 180 : 180 degree<br>
     * 270 : 270 degree<br>
     * @param        model M1: (default), original version<br>
     * M2: enhanced version (Almost smart phone is supported by this version.)<br>
     * @param        mask S0~S8, default is S7<br>
     * @param        content The encodable character set is described as below,<br>
     */
    public static byte[] qrCode(int x, int y, String eccLevel, int cellWidth, String mode, int rotation, String model, String mask, String content) {
        String str = "QRCODE " + x + "," + y + "," + eccLevel + "," + cellWidth + "," + mode + "," + rotation + "," + model + "," + mask + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints QR code.<br>
     * QRCODE x,y,ECC Level,cell width,mode,rotation,[model,mask,]??content??<br>
     *
     * @param    x The upper left corner x-coordinate of the QR code<br>
     * @param        y The upper left corner y-coordinate of the QR code<br>
     * @param        eccLevel Error correction recovery level<br>
     * L  : 7%<br>
     * M  : 15%<br>
     * Q  : 25%<br>
     * H  : 30%<br>
     * @param        cellWidth 1~10<br>
     * @param        mode Auto / manual encode<br>
     * A : Auto<br>
     * M : Manual<br>
     * @param        rotation 0  :<br> 0 degree<br>
     * 90 : 90 degree<br>
     * 180 : 180 degree<br>
     * 270 : 270 degree<br>
     * M2: enhanced version (Almost smart phone is supported by this version.)<br>
     * @param    content The encodable character set is described as below,<br>
     * @return byte[]
     */
    public static byte[] qrCode(int x, int y, String eccLevel, int cellWidth, String mode, int rotation, String content) {
        String str = "QRCODE " + x + "," + y + "," + eccLevel + "," + cellWidth + "," + mode + "," + rotation + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command reverses a region in image buffer.<br>
     * ???????^??????<br>
     * REVERSE x_start,y_start,x_width,y_height<br>
     *
     * @return byte[]
     * @param    x The x-coordinate of the starting point (in dots)<br>
     * @param        y The y-coordinate of the starting point (in dots)<br>
     * @param        width X-axis region width (in dots)<br>
     * @param        height Y-axis region height (in dots)<br>
     */
    public static byte[] reverse(int x, int y, int width, int height) {
        String str = "REVERSE " + x + "," + y + "," + width + "," + height + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints text on label.<br>
     * ??????C????????(???)???????<br>
     * TEXT x,y,??font??,rotation,x-multiplication,y-multiplication,??content??<br>
     *
     * @param x The x-coordinate of the text<br>
     * @return byte[]
     * @param        y The y-coordinate of the text<br>
     * @param        font Font name??ο???????<br>
     * @param        rotation The rotation angle of text<br>
     * 0 : No rotation<br>
     * 90 : degrees, in clockwise direction<br>
     * 180 : degrees, in clockwise direction<br>
     * 270 : degrees, in clockwise direction<br>
     * @param        x_multiplication Horizontal multiplication, up to 10x<br>
     * @param        y_multiplication Vertical multiplication, up to 10x<br>
     * @param        content Content of text string<br>
     */
    public static byte[] text(int x, int y, String font, int rotation, int x_multiplication, int y_multiplication, String content) {
        String str = "TEXT " + x + "," + y + ",\"" + font + "\"," + rotation + "," + x_multiplication + "," + y_multiplication + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints paragraph on label.<br>
     * BLOCK x,y,width,height,??font??,rotation,x-multiplication,y-multiplication,[space,]alignment,]??content??<br>
     *
     * @return byte[]
     * @param    x The x-coordinate of the text<br>
     * @param        y The y-coordinate of the text<br>
     * @param        width The width of block for the paragraph in dots<br>
     * @param        height The height of block for the paragraph in dots<br>
     * @param        font Font name<br>
     * @param        rotation The rotation angle of text<br>
     * 0  : No rotation<br>
     * 90 : degrees, in clockwise direction<br>
     * 180 : degrees, in clockwise direction<br>
     * 270 : degrees, in clockwise direction<br>
     * @param        x_multiplication Horizontal multiplication, up to 10x<br>
     * @param        y_multiplication Vertical multiplication, up to 10x<br>
     * @param        space Add or delete the space between lines in dot.<br>
     * @param        alignment Text alignment. 0 : default (Left);1 : Left;2 : Center;	3 : Right<br>
     * @param        content Data in block. The maximum data length is 4092 bytes.<br>
     */
    public static byte[] block(int x, int y, int width, int height, String font, int rotation, int x_multiplication, int y_multiplication, int space, int alignment, String content) {
        String str = "BLOCK " + x + "," + y + "," + width + "," + height + ",\"" + font + "\"," + rotation + "," + x_multiplication + "," + y_multiplication + "," + space + "," + alignment + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints paragraph on label.<br>
     * BLOCK x,y,width,height,??font??,rotation,x-multiplication,y-multiplication,??content??<br>
     *
     * @param    x The x-coordinate of the text<br>
     * @param        y The y-coordinate of the text<br>
     * @param        width The width of block for the paragraph in dots<br>
     * @param        height The height of block for the paragraph in dots<br>
     * @param        font Font name<br>
     * @param        rotation The rotation angle of text<br>
     * 0  : No rotation<br>
     * 90 : degrees, in clockwise direction<br>
     * 180 : degrees, in clockwise direction<br>
     * 270 : degrees, in clockwise direction<br>
     * @param        x_multiplication Horizontal multiplication, up to 10x<br>
     * @param        y_multiplication Vertical multiplication, up to 10x<br>
     * @param        content Data in block. The maximum data length is 4092 bytes.<br>
     */
    public static byte[] block(int x, int y, int width, int height, String font, int rotation, int x_multiplication, int y_multiplication, String content) {
        String str = "BLOCK " + x + "," + y + "," + width + "," + height + ",\"" + font + "\"," + rotation + "," + x_multiplication + "," + y_multiplication + ",\"" + content + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command obtains the printer status at any time, even in the event of printer error. An inquiry<br>
     * request is solicited by sending an <ESC> (ASCII 27, escape character) as the beginning control character<br>
     * to the printer. A one byte character is returned, flagging the printer status. A 0 signifies the printer is<br>
     * ready to print labels.<br>
     * < ESC >!?<br>
     *
     * @returnbyte[]
     */
    public static byte[] checkPrinterStateByPort9100() {
        byte[] data = {0x1D, 0x61, 0x1F};
        return data;
    }

    /**
     *
     * @return byte[]
     */
    public static byte[] checkPrinterStateByPort4000() {
        byte[] data = {0x1B, 0x76, 0x00};


        return data;
    }

    /**
     * Download ???????<br>
     * DOWNLOAD ??EXAMPLE.BAS??<br>
     *
     * @param filename FILENAME  The name of data file that will remain resident in the printer memory
     * @return byte[]
     */
    public static byte[] downLoad(String filename) {
        String str = "DOWNLOAD \"" + filename + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Download ??????<br>
     * <p>
     * DOWNLOAD ??FILENAME?? ,DATA SIZE,DATA??CONTENT...<br>
     *
     * @param filename FILENAME  The name of data file that will remain resident in the printer memory
     * @param size     ?????С
     * @param content  ?????content???洢????????????????
     * @return byte[]
     */
    public static byte[] downLoad(String filename, int size, String content) {
        String str = "DOWNLOAD \"" + filename + "\"," + size + "," + content + "\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * Download ??????<br>
     * DOWNLOAD ??FILENAME?? ,FILE SIZE,DATA??CONTENT...<br>
     *
     * @param filename FILENAME  The name of data file that will remain resident in the printer memory
     * @param filepath filepath??洢??????????????????????·??
     * @return byte[]
     */
    public static byte[] downLoad(String filename, String filepath) {
        byte[] data = null;
        try {
            File f = new File(filepath);
            FileInputStream fIn = new FileInputStream(f);
            int size = fIn.available();
            String str = "DOWNLOAD \"" + filename + "\"," + size + ",";
            data = strTobytes(str);
            byte[] b = new byte[size];
            int c = -1;
            while ((c = fIn.read(b)) != -1) {
                data = byteMerger(data, b);
            }
            fIn.close();
            String end = "\n";
            byte[] endata = strTobytes(end);
            data = byteMerger(data, endata);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

        }
        return data;
    }

    /**
     * Download ???<br>
     * DOWNLOAD ??FILENAME.PCX?? ,FILE SIZE,DATA??CONTENT...<br>
     *
     * @param filename FILENAME  The name of data file that will remain resident in the printer memory
     * @param bitmap   ??洢???????????????????bitmap????,?????????<br>
     * @return byte[]
     */
    public static byte[] downLoad(String filename, Bitmap bitmap) {
        Bitmap bitmap2 = bitmap;
        byte[] data = BitmapToByteData.downLoadBmpToSendTSCdownloadcommand(bitmap2);
        int size = data.length;
        String str = "DOWNLOAD \"" + filename + "\"," + size + ",";
        byte[] head = strTobytes(str);
        data = byteMerger(head, data);
        byte[] end = strTobytes("\n");
        data = byteMerger(data, end);
        return data;
    }


    /**
     * End of program. To declare the start and end of BASIC language commands used in a program,
     * DOWNLOAD ??FILENAME.BAS ?? must be added in the first line of the program, and ??EOP ?? statement at
     * the last line of program.<br>
     * EOP<br>
     *
     * @return byte[]
     */
    public static byte[] eop() {
        String str = "EOP\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command prints out the total memory size, available memory size and files lists (or lists the files
     * through RS-232) in the printer memory (both FLASH memory and DRAM).<br>
     * FILES<br>
     *
     * @return byte[]
     */
    public static byte[] files() {
        String str = "FILES\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command deletes a file in the printer memory. The wild card (*) will delete all files resident in
     * specified DRAM or FLASH memory.<br>
     * KILL ??FILENAME??<br>
     *
     * @return byte[]
     * @param    filename The name of data file that will delete in the printer memory (case
     * sensitive)
     */
    public static byte[] kill(String filename) {
        String str = "KILL \"" + filename + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command moves downloaded files from DRAM to FLASH memory.<br>
     * MOVE<br>
     *
     * @return byte[]
     */
    public static byte[] move() {
        String str = "MOVE\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * This command executes a program resident in the printer memory. It is available for TSPL2 language
     * printers only.<br>
     * RUN ??FILENAME.BAS??<br>
     * This command can be replaced to filename that without typing ??.BAS??.<br>
     *
     * @param filename ?????????е????????<br>
     * @return byte[]
     */
    public static byte[] run(String filename) {
        String str = "RUN \"" + filename + "\"\n";
        byte[] data = strTobytes(str);
        return data;
    }

    /**
     * ??????byte????<br>
     *
     * @param ???????
     * @return byte[]
     */
    private static byte[] strTobytes(String str) {
        byte[] b = null, data = null;
        try {
            b = str.getBytes("utf-8");
            if (charsetName == null | charsetName == "") {
                charsetName = "gbk";
            }
            data = new String(b, "utf-8").getBytes(charsetName);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return data;
    }

    /**
     * byte???????
     */
    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
