package com.enlern.new_wsn.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.bean.SerialWsn;

import java.util.List;

/**
 * Created by oc on 2017/6/29.
 */

public class WsnHistoryAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<SerialWsn> serialWsns;


    public WsnHistoryAdapter(Context context, List<SerialWsn> wsnList) {
        mContext = context;
        serialWsns = wsnList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_data_item, parent, false);
        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
        };
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView history_name = holder.itemView.findViewById(R.id.tv_history_name);
        TextView history_sys_board = holder.itemView.findViewById(R.id.tv_history_sys_board);
        TextView history_chip = holder.itemView.findViewById(R.id.tv_history_chip);
        TextView tv_history_date = holder.itemView.findViewById(R.id.tv_history_date);
        TextView history_data = holder.itemView.findViewById(R.id.tv_history_data);


        history_name.setText(serialWsns.get(position).getNode_name() + serialWsns.get(position).getNode_num());
        history_sys_board.setText("系统板号：" + serialWsns.get(position).getSys_board());
        history_chip.setText(chipType(serialWsns.get(position).getChip_type()));
        tv_history_date.setText("时间：" + serialWsns.get(position).getInsert_date());
        history_data.setText("数据值：" + serialWsns.get(position).getNode_data());


    }

    @Override
    public int getItemCount() {
        return serialWsns.size();
    }


    private String dataOneDeal(String one) {
        if (one != null) {
            return "数据：" + one;
        }
        return one;
    }

    private String dataTwoDeal(String two) {
        if (two.length() > 0) {
            return "数据2：" + two;
        }
        return null;
    }

    /**
     * 开关
     *
     * @param strSwitch
     * @return
     */

    private String switchDeal(String strSwitch) {
        if (strSwitch != null) {
            if (strSwitch.equals("00")) {
                return "状态灯：" + "开";
            } else if (strSwitch.equals("01")) {
                return "状态灯：" + "关";
            }
        }
        return "";
    }

    /**
     * 芯片类型
     *
     * @param chip
     * @return
     */
    private String chipType(String chip) {
        if (chip != null) {
            if (chip.equals("8266")) {
                return "通信类型：" + "wifi节点";
            }
        }
        return "串口数据";
    }
}
