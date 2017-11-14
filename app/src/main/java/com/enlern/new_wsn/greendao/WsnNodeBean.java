package com.enlern.new_wsn.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by oc on 2017/7/11.
 */

@Entity
public class WsnNodeBean {

    /**
     * ID号，自增长
     */
    @Id(autoincrement = true)
    private Long id;
    /**
     * 系统用户名
     */
    private String username;
    /**
     * 系统板号
     */
    private String sys_board_num;
    /**
     * 芯片类型
     */
    private String chip_type;
    /**
     * 传感器名称
     */
    private String node_name;
    /**
     * 传感器协议16进制编号
     */
    private String node_wsn;
    /**
     * 传感器数据
     */
    private String node_data;
    /**
     * 数据插入时间
     */
    private String insert_date;
    /**
     * 数据更新时间
     */
    private String update_date;
    /**
     * 协议长度
     */
    private String wsn_length;
    /**
     * 传感器编号
     */
    private String node_number;
    /**
     * 传感器设置临界值
     */
    private String node_setting;

    public String getNode_setting() {
        return this.node_setting;
    }

    public void setNode_setting(String node_setting) {
        this.node_setting = node_setting;
    }

    public String getNode_number() {
        return this.node_number;
    }

    public void setNode_number(String node_number) {
        this.node_number = node_number;
    }

    public String getWsn_length() {
        return this.wsn_length;
    }

    public void setWsn_length(String wsn_length) {
        this.wsn_length = wsn_length;
    }

    public String getUpdate_date() {
        return this.update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getInsert_date() {
        return this.insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getNode_data() {
        return this.node_data;
    }

    public void setNode_data(String node_data) {
        this.node_data = node_data;
    }

    public String getNode_wsn() {
        return this.node_wsn;
    }

    public void setNode_wsn(String node_wsn) {
        this.node_wsn = node_wsn;
    }

    public String getNode_name() {
        return this.node_name;
    }

    public void setNode_name(String node_name) {
        this.node_name = node_name;
    }

    public String getChip_type() {
        return this.chip_type;
    }

    public void setChip_type(String chip_type) {
        this.chip_type = chip_type;
    }

    public String getSys_board_num() {
        return this.sys_board_num;
    }

    public void setSys_board_num(String sys_board_num) {
        this.sys_board_num = sys_board_num;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1112392226)
    public WsnNodeBean(Long id, String username, String sys_board_num,
                       String chip_type, String node_name, String node_wsn, String node_data,
                       String insert_date, String update_date, String wsn_length,
                       String node_number, String node_setting) {
        this.id = id;
        this.username = username;
        this.sys_board_num = sys_board_num;
        this.chip_type = chip_type;
        this.node_name = node_name;
        this.node_wsn = node_wsn;
        this.node_data = node_data;
        this.insert_date = insert_date;
        this.update_date = update_date;
        this.wsn_length = wsn_length;
        this.node_number = node_number;
        this.node_setting = node_setting;
    }

    @Generated(hash = 2018094028)
    public WsnNodeBean() {
    }
}
