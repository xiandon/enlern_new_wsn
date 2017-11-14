package com.enlern.new_wsn.utils;

import android.content.Context;
import android.widget.Toast;

import com.enlern.new_wsn.R;

/**
 * Created by oc on 2017/6/26.
 */

public class ConversionSystem {

    private static ConversionSystem instance;

    private ConversionSystem() {
    }

    public static ConversionSystem getIstance() {
        if (instance == null) {
            synchronized (ConversionSystem.class) {
                if (instance == null) {
                    instance = new ConversionSystem();
                }
            }
        }
        return instance;
    }

    /**
     * 16进制格式字符串 变byte字节数组
     */
    public static byte[] string2byteArrays(String s) {
        String ss = s.replace(" ", "");
        int string_len = ss.length();
        int len = string_len / 2;
        if (string_len % 2 == 1) {
            ss = "0" + ss;
            string_len++;
            len++;
        }
        byte[] a = new byte[len];
        for (int i = 0; i < len; i++) {
            a[i] = (byte) Integer.parseInt(ss.substring(2 * i, 2 * i + 2), 16);
        }
        return a;
    }

    /**
     * 异或 效验 输入16进制字符串，得到16进制string 效验位 11 11 11 得到11
     */
    public static String CRC_Count(String validation) {

        byte crcValue;
        int i;
        byte[] CRCData = string2byteArrays(validation);
        int len = CRCData.length;
        crcValue = CRCData[0];
        for (i = 1; i < len; i++) {
            crcValue ^= CRCData[i];
        }
        // System.out.println(crcValue);
        String crc = Integer.toHexString(crcValue);// 16进制字符串
        if (crc.length() > 2) {
            crc = crc.substring(crc.length() - 2, crc.length());

        }
        if (crc.length() < 2) {// 一位时 补0
            crc = addZeroForNum(crc, 2, 0);
        }

        return crc;
    }

    /**
     * 0为左补0 1为右补0
     * 输入总长度补0
     */
    public static String addZeroForNum(String str, int strLength, int status) {
        int strLen = str.length();
        if (strLen < strLength) {
            while (strLen < strLength) {
                StringBuffer sb = new StringBuffer();
                if (status == 0) {// status状态
                    sb.append("0").append(str);// 左补0
                } else if (status == 1) {
                    sb.append(str).append("0");// 右补0
                }
                str = sb.toString();
                strLen = str.length();
            }
        }
        return str;
    }


    /**
     * 图片ID
     *
     * @param node
     * @return
     */
    public static int nodeNameDeal(String node) {
        int i = 0;
        switch (node) {
            case "0001":
                i = R.mipmap.serial01;
                break;
            case "0002":
                i = R.mipmap.serial02;
                break;
            case "0003":
                i = R.mipmap.serial03;
                break;
            case "0004":
                i = R.mipmap.serial04;
                break;
            case "0005":
                i = R.mipmap.serial05;
                break;
            case "0006":
                i = R.mipmap.serial06;
                break;
            case "0007":
                i = R.mipmap.serial07;
                break;
            case "0008":
                i = R.mipmap.serial08;
                break;
            case "0009":
                i = R.mipmap.serial09;
                break;
            case "000a":
                i = R.mipmap.serial0a;
                break;
            case "000b":
                i = R.mipmap.serial0b;
                break;
            case "000c":
                i = R.mipmap.serial0c;
                break;
            case "000d":
                i = R.mipmap.serial0d;
                break;
            case "000e":
                i = R.mipmap.serial0e;
                break;
            case "000f":
                i = R.mipmap.serial0f;
                break;
            case "0010":
                i = R.mipmap.serial10;
                break;
            case "0011":
                i = R.mipmap.serial11;
                break;
            case "0012":
                i = R.mipmap.serial12;
                break;
            case "0013":
                i = R.mipmap.serial13;
                break;
            case "0014":
                i = R.mipmap.serial14;
                break;
            case "0015":
                i = R.mipmap.serial15;
                break;
            case "0016":
                i = R.mipmap.serial16;
                break;
            case "0017":
                i = R.mipmap.serial17;
                break;
            case "0018":
                i = R.mipmap.serial18;
                break;
            case "0019":
                i = R.mipmap.serial19;
                break;
            case "001a":
                i = R.mipmap.serial0a;
                break;
            case "001b":
                i = R.mipmap.serial1b;
                break;
            case "001c":
                i = R.mipmap.serial1c;
                break;
            case "0020":
                i = R.mipmap.serial21;
                break;
            case "0021":
                i = R.mipmap.serial21;
                break;
            case "0022":
                i = R.mipmap.serial22;
                break;
            case "0023":
                i = R.mipmap.serial23;
                break;
            case "0024":
                i = R.mipmap.serial24;
                break;
            case "0030":
                i = R.mipmap.serial30;
                break;
            case "0040":
                i = R.mipmap.serial30;
                break;
            case "0043":
                i = R.mipmap.serial30;
                break;
            case "0080":
                i = R.mipmap.serial30;
                break;
            case "0081":
                i = R.mipmap.serial30;
                break;
            case "0082":
                i = R.mipmap.serial30;
                break;
            case "0083":
                i = R.mipmap.serial30;
                break;
            case "0084":
                i = R.mipmap.serial30;
                break;
            case "0085":
                i = R.mipmap.serial30;
                break;

            default:
                break;
        }
        return i;
    }

    public static void toastDeal(Context context, String toast) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
        //数码管001b，声光001c
    }

}
