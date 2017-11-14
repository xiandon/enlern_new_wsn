package com.enlern.new_wsn.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.bean.SerialWsn;
import com.enlern.new_wsn.bean.WsnDataBaen;
import com.enlern.new_wsn.sharedPreferences.SPUtils;
import com.enlern.new_wsn.utils.ConversionSystem;
import com.enlern.new_wsn.utils.StaticData;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by oc on 2017/7/3.
 */

public class MainWsnAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private LayoutInflater layoutInflater;
    private List<SerialWsn> serialWsns;
    private String tcp_ip;
    private int tcp_port;

    public MainWsnAdapter(Context context, List<SerialWsn> wsnList) {
        mContext = context;
        serialWsns = wsnList;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_node_item, parent, false);
        RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(view) {
        };
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int pos = holder.getLayoutPosition();
//        ImageView iv_surroundings_sensor = (ImageView) holder.itemView.findViewById(R.id.iv_surroundings_sensor);
//        TextView tv_surroundings_sensor = (TextView) holder.itemView.findViewById(R.id.tv_surroundings_sensor);
//        TextView tv_surroundings_data = (TextView) holder.itemView.findViewById(R.id.tv_surroundings_data);
//
//        iv_surroundings_sensor.setImageResource(serialWsns.get(position).getImageUrl());
//        tv_surroundings_sensor.setText(serialWsns.get(position).getNode_name() + serialWsns.get(position).getChip_type());
//        tv_surroundings_data.setText(serialWsns.get(position).getNode_data());

        ImageView image_a_node_chip = holder.itemView.findViewById(R.id.image_a_node_chip);
        ImageView image_a_node_wsn = holder.itemView.findViewById(R.id.image_a_node_wsn);

        TextView text_a_node_name = holder.itemView.findViewById(R.id.text_a_node_name);
        TextView text_a_node_time = holder.itemView.findViewById(R.id.text_a_node_time);
        TextView text_a_node_data = holder.itemView.findViewById(R.id.text_a_node_data);

        image_a_node_chip.setImageResource(chipType(serialWsns.get(position).getChip_type()));
        image_a_node_wsn.setImageResource(serialWsns.get(position).getImageUrl());
        text_a_node_name.setText(serialWsns.get(position).getNode_name());
        text_a_node_data.setText(serialWsns.get(position).getNode_data());
        text_a_node_time.setText(serialWsns.get(position).getInsert_date());


        final String strwsnCrcOpen = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000001" + "0101" + "00";
        final String strwsnCrcClose = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000001" + "0000" + "00";

        //继电器控制
        final String strwsnOpen = "36" +
                serialWsns.get(position).getChip_type() +
                serialWsns.get(position).getLength() +
                strwsnCrcOpen + ConversionSystem.CRC_Count(strwsnCrcOpen);
        final String strwsnClose = "36" +
                serialWsns.get(position).getChip_type() +
                serialWsns.get(position).getLength() +
                strwsnCrcClose + ConversionSystem.CRC_Count(strwsnCrcClose);


        final String strwsnCrcZheng = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000001" + "000001" + "00";
        final String strwsnCrcFan = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000001" + "000002" + "00";
        final String strwsnCrcStop = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000001" + "000003" + "00";

        final String strwsnZheng = "36" +
                serialWsns.get(position).getChip_type() +
                serialWsns.get(position).getLength() +
                strwsnCrcZheng + ConversionSystem.CRC_Count(strwsnCrcZheng);

        final String strwsnFan = "36" +
                serialWsns.get(position).getChip_type() +
                serialWsns.get(position).getLength() +
                strwsnCrcFan + ConversionSystem.CRC_Count(strwsnCrcFan);

        final String strwsnStop = "36" +
                serialWsns.get(position).getChip_type() +
                serialWsns.get(position).getLength() +
                strwsnCrcStop + ConversionSystem.CRC_Count(strwsnCrcStop);

        final String strTubeCRC = "";


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String node = serialWsns.get(position).getNode_id();
                switch (node) {
                    case "0011":
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setIcon(R.mipmap.enlern_logo);
                        builder.setTitle("请选择操作");
                        builder.setMessage("打开继电器或者关闭继电器");

                        builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(strwsnOpen);
                            }
                        });

                        builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(strwsnClose);
                            }
                        });
                        builder.show();
                        break;
                    case "0016":
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(mContext);
                        builder2.setIcon(R.mipmap.enlern_logo);
                        builder2.setTitle("请选择电机操作方式");
                        String[] comm_type = {"控制正转", "控制反转", "控制停止"};
                        final String[] wsn = {strwsnZheng, strwsnFan, strwsnStop};


                        final ChoiceOnClickListener choiceListener = new ChoiceOnClickListener();
                        builder2.setSingleChoiceItems(comm_type, 0, choiceListener);

                        builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(wsn[choiceListener.getWhich()]);
                            }
                        });

                        builder2.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builder2.show();
                        break;
                    case "0017":
                        AlertDialog.Builder builderBU = new AlertDialog.Builder(mContext);
                        builderBU.setIcon(R.mipmap.enlern_logo);
                        builderBU.setTitle("请选择电机操作方式");
                        String[] comm_typeBu = {"控制正转", "控制反转", "控制停止"};
                        final String[] wsnBU = {strwsnZheng, strwsnFan, strwsnStop};


                        final ChoiceOnClickListener choiceListenerBu = new ChoiceOnClickListener();
                        builderBU.setSingleChoiceItems(comm_typeBu, 0, choiceListenerBu);

                        builderBU.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(wsnBU[choiceListenerBu.getWhich()]);
                            }
                        });

                        builderBU.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builderBU.show();
                        break;

                    case "001b":
                        //数码管
                        String strTube0 = CrcTube(position, "00");
                        String strTube1 = CrcTube(position, "01");
                        String strTube2 = CrcTube(position, "02");
                        String strTube3 = CrcTube(position, "03");
                        String strTube4 = CrcTube(position, "04");
                        String strTube5 = CrcTube(position, "05");
                        String strTube6 = CrcTube(position, "06");
                        String strTube7 = CrcTube(position, "07");
                        String strTube8 = CrcTube(position, "08");
                        String strTube9 = CrcTube(position, "09");

                        AlertDialog.Builder builderTube = new AlertDialog.Builder(mContext);
                        builderTube.setIcon(R.mipmap.enlern_logo);
                        builderTube.setTitle("请选择需要数码管显示的数字");
                        String[] comm_type_tube = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
                        final String[] wsnTube = {strTube0, strTube1, strTube2, strTube3, strTube4, strTube5, strTube6, strTube7, strTube8, strTube9};


                        final ChoiceOnClickListener choiceListenerTube = new ChoiceOnClickListener();
                        builderTube.setSingleChoiceItems(comm_type_tube, 0, choiceListenerTube);

                        builderTube.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(wsnTube[choiceListenerTube.getWhich()]);
                            }
                        });

                        builderTube.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                        builderTube.show();
                        break;
                    case "001c":
                        //声光报警

                        String strSoundOpen0 = CrcSound(position, "00", "01");
                        String strSoundOpen1 = CrcSound(position, "01", "01");
                        String strSoundOpen2 = CrcSound(position, "02", "01");
                        String strSoundOpen3 = CrcSound(position, "03", "01");
                        String strSoundOpen4 = CrcSound(position, "04", "01");
                        String strSoundOpen5 = CrcSound(position, "05", "01");
                        String strSoundOpen6 = CrcSound(position, "06", "01");
                        String strSoundOpen7 = CrcSound(position, "07", "01");

                        String strSoundClose0 = CrcSound(position, "00", "00");
                        String strSoundClose1 = CrcSound(position, "01", "00");
                        String strSoundClose2 = CrcSound(position, "02", "00");
                        String strSoundClose3 = CrcSound(position, "03", "00");
                        String strSoundClose4 = CrcSound(position, "04", "00");
                        String strSoundClose5 = CrcSound(position, "05", "00");
                        String strSoundClose6 = CrcSound(position, "06", "00");
                        String strSoundClose7 = CrcSound(position, "07", "00");

                        AlertDialog.Builder builderSound = new AlertDialog.Builder(mContext);
                        builderSound.setIcon(R.mipmap.enlern_logo);
                        builderSound.setTitle("请选择需要显示的颜色");
                        String[] comm_type_sound = {"关灯", "白色灯", "红色灯", "绿色灯", "蓝色灯", "黄色灯", "紫色灯", "青色灯"};
                        final String[] wsnSoundOpen = {
                                strSoundOpen0,
                                strSoundOpen1,
                                strSoundOpen2,
                                strSoundOpen3,
                                strSoundOpen4,
                                strSoundOpen5,
                                strSoundOpen6,
                                strSoundOpen7,};
                        final String[] wsnSoundClose = {
                                strSoundClose0,
                                strSoundClose1,
                                strSoundClose2,
                                strSoundClose3,
                                strSoundClose4,
                                strSoundClose5,
                                strSoundClose6,
                                strSoundClose7,};

                        final ChoiceOnClickListener choiceListenerSound = new ChoiceOnClickListener();
                        builderSound.setSingleChoiceItems(comm_type_sound, 0, choiceListenerSound);

                        builderSound.setPositiveButton("打开", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(wsnSoundOpen[choiceListenerSound.getWhich()]);
                            }
                        });

                        builderSound.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                download(wsnSoundClose[choiceListenerSound.getWhich()]);
                            }
                        });


                        builderSound.show();


                        break;
                    default:
                        break;
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return serialWsns.size();
    }

    private void dealWsn(String wsn, String control) {
    }

    private void download(final String wsn) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                connTcpServer("1502" + wsn);
                System.out.println("1502" + wsn);
            }
        };

        new Thread(runnable).start();
    }


    private void connTcpServer(String string) {
        try {
            tcp_ip = SPUtils.get(mContext, "TCP_IP", "").toString();
            tcp_port = (int) SPUtils.get(mContext, "TCP_PORT", 0);
            byte[] addData = ConversionSystem.string2byteArrays(string);
            Socket socket = new Socket(tcp_ip, tcp_port);
            OutputStream os = socket.getOutputStream();
            // 输出流包装为打印流
            DataOutputStream dos = new DataOutputStream(os);
            PrintWriter pw = new PrintWriter(os);
            // 向服务器端发送信息
            dos.write(addData);// 写入内存缓冲区
            dos.flush();// 刷新缓存，向服务器端输出信息
            socket.shutdownOutput();// 关闭输出流

            // 获取输入流，接收服务器端响应信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data = null;
            while ((data = br.readLine()) != null) {
            }
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("数据加载失败");
        }
    }


    private class ChoiceOnClickListener implements DialogInterface.OnClickListener {

        private int which = 0;

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            this.which = i;
        }

        public int getWhich() {
            return which;
        }
    }

    private String CrcTube(int position, String code) {
        String strTube = null;
        String strCrc = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000000" + code + "00";
        strTube = "36" + serialWsns.get(position).getChip_type() + serialWsns.get(position).getLength() +
                strCrc + ConversionSystem.CRC_Count(strCrc);
        return strTube;
    }

    private String CrcSound(int position, String code, String light) {
        String strSound = null;
        String strCrc = serialWsns.get(position).getNode_id() +
                serialWsns.get(position).getNode_num() +
                serialWsns.get(position).getSys_board() +
                "000000" + code + light + "00";
        strSound = "36" + serialWsns.get(position).getChip_type() + serialWsns.get(position).getLength() +
                strCrc + ConversionSystem.CRC_Count(strCrc);
        return strSound;
    }

    private int chipType(String chip) {
        int i = 0;
        switch (chip) {
            case "2530":
                i = R.mipmap.chip_serial_icon;
                break;
            case "8266":
                i = R.mipmap.chip_wifi_icon;
                break;
            case "2540":
                i = R.mipmap.chip_bluetooth_icon;
                break;
        }
        return i;
    }


}
