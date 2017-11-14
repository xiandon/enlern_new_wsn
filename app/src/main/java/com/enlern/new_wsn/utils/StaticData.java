package com.enlern.new_wsn.utils;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.bean.IpBean;


/**
 * Created by oc on 2017/6/26.
 */

public class StaticData {
    public static String IP2 = "10.10.100.100";
    public static String IP = "192.168.250.10";
    public static int PORT = 9191;

    private static String ipWeb = "58.246.105.210:6100";
    private static String ipLocal = "192.168.1.11:8080";


    public static IpBean selectIP(String common) {
        IpBean ipBean = new IpBean();
        switch (common) {
            case "web":
                ipBean.setInstant("http://" + ipWeb + "/EnlernWsn/instantdata");
                ipBean.setHistorical("http://" + ipWeb + "/EnlernWsn/historicaldata");
                break;
            case "local":
                ipBean.setInstant("http://" + ipLocal + "/EnlernWsn/instantdata");
                ipBean.setHistorical("http://" + ipLocal + "/EnlernWsn/historicaldata");
                break;
            default:
                break;
        }
        return ipBean;
    }

    public static String[] strNodeName =

            {
                    "霍尔传感器", "雨滴传感器", "流量传感器", "火焰传感器", "温度传感器"
            };
    public static String[] strNodeData =

            {
                    "磁场正常", "无雨滴", "16.06lux", "有火焰", "26.54℃"
            };
    public static int[] strNodeImage =

            {
                    R.mipmap.serial06, R.mipmap.serial07, R.mipmap.serial08, R.mipmap.serial09, R.mipmap.serial10
            };

}
