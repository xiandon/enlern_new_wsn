package com.enlern.new_wsn.utils;

import com.enlern.new_wsn.bean.SerialWsn;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by oc on 2017/7/6.
 */

public class JsonUtils {

    /**
     * json web处理得到的数据
     *
     * @param json
     * @return
     */
    public static ArrayList<SerialWsn> jsonDealWeb(String json, String type) {
        try {
            ArrayList<SerialWsn> listSerial = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            System.out.println("PC  = " + jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(type);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                SerialWsn serial = new SerialWsn();
                serial.setId(jsonItem.getString("id"));//id
                serial.setSys_board(jsonItem.getString("Address"));//系统板号
                serial.setChip_type(jsonItem.getString("IC_Type"));//芯片类型
                serial.setNode_id(jsonItem.getString("Node_Type"));//传感器id
                serial.setNode_name(jsonItem.getString("name"));//传感器类型
                serial.setNode_num(jsonItem.getString("Node_number"));//传感器编号
                serial.setInsert_date(jsonItem.getString("Time_standard"));//插入时间
                serial.setNode_data(jsonItem.getString("srcState"));
                serial.setImageUrl(ConversionSystem.nodeNameDeal(jsonItem.getString("Node_Type")));
                serial.setLength(jsonItem.getString("length"));

                listSerial.add(serial);
            }
            return listSerial;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }


    /**
     * json local处理得到的数据
     *
     * @param json
     * @return
     */
    public static ArrayList<SerialWsn> jsonDeal(String json, String type) {
        System.out.println(json);
        try {
            ArrayList<SerialWsn> listSerial = new ArrayList<>();
            JSONObject jsonObject = new JSONObject(json);
            System.out.println("Tablet = " + jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONArray(type);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                SerialWsn serial = new SerialWsn();
                serial.setId(jsonItem.getString("id"));//id
                serial.setSys_board(jsonItem.getString("Address"));//系统板号
                serial.setChip_type(jsonItem.getString("IC_Type"));//芯片类型
                serial.setNode_id(jsonItem.getString("Node_Type"));//传感器id
                serial.setNode_name(jsonItem.getString("name"));//传感器类型
                serial.setNode_num(jsonItem.getString("Node_number"));//传感器编号
                serial.setInsert_date(DateUtils.stampToDate(jsonItem.getString("Time_standard")));//插入时间
                serial.setNode_data(jsonItem.getString("srcState"));
                serial.setImageUrl(ConversionSystem.nodeNameDeal(jsonItem.getString("Node_Type")));
                serial.setLength(jsonItem.getString("length"));

                listSerial.add(serial);
            }
            return listSerial;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
