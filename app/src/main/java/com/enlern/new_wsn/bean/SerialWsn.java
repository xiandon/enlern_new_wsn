package com.enlern.new_wsn.bean;

/**
 * 节点实体类
 * Created by oc on 2017/6/28.
 */

public class SerialWsn {
    private String id;//ID号
    private String sys_board;//系统板号
    private String chip_type;//芯片类型
    private String node_id;//传感器id
    private String node_name;//传感器类型
    private String node_num;//传感器编号
    private String node_data;
    private String insert_date;//插入时间
    private int imageUrl;//图片编号
    private String length;//数据长度

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(int imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNode_data() {
        return node_data;
    }

    public void setNode_data(String node_data) {
        this.node_data = node_data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSys_board() {
        return sys_board;
    }

    public void setSys_board(String sys_board) {
        this.sys_board = sys_board;
    }

    public String getChip_type() {
        return chip_type;
    }

    public void setChip_type(String chip_type) {
        this.chip_type = chip_type;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getNode_name() {
        return node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getNode_num() {
        return node_num;
    }

    public void setNode_num(String node_num) {
        this.node_num = node_num;
    }


    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

}
