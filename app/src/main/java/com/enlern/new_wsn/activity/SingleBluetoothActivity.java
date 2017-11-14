package com.enlern.new_wsn.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.application.CustomActivityManager;
import com.inuker.bluetooth.library.BluetoothClient;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oc on 2017/7/17.
 */

public class SingleBluetoothActivity extends BaseActivity {
    @BindView(R.id.textView_activity_title)
    TextView textViewActivityTitle;
    @BindView(R.id.recyclerViewBluetooth)
    RecyclerView recyclerViewBluetooth;
    @BindView(R.id.textView_connected_bluetooth)
    TextView textViewConnectedBluetooth;
    @BindView(R.id.textView_send_bluetooth)
    EditText textViewSendBluetooth;

    /**
     * 蓝牙
     */
    private BluetoothClient bluetoothClient;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_single_bluetooth);
        context = SingleBluetoothActivity.this;
        ButterKnife.bind(this);
        CustomActivityManager.addActivity(SingleBluetoothActivity.this);
        initViews();
    }

    private void initViews() {
        textViewActivityTitle.setText("蓝牙通信");
        bluetoothClient = new BluetoothClient(context);
        textViewConnectedBluetooth.setText(connectedBlueTooth());
        boolean a = textViewSendBluetooth.requestFocus();
        if (a){
            System.out.println(a);
        }



    }

    @OnClick({R.id.imageView_activity_reply, R.id.open_bluetooth, R.id.close_bluetooth, R.id.scan_bluetooth, R.id.textView_send_bluetooth, R.id.send_bluetooth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView_activity_reply:
                finish();
                break;
            case R.id.open_bluetooth:
                bluetoothClient.openBluetooth();
                break;
            case R.id.close_bluetooth:
                bluetoothClient.closeBluetooth();
                break;
            case R.id.scan_bluetooth:
                Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
                startActivity(intent);
                break;
            case R.id.textView_send_bluetooth:

                //showCustomizeDialog();
                break;
            case R.id.send_bluetooth:
                break;
        }
    }

    private void showCustomizeDialog() {
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(SingleBluetoothActivity.this);
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_dialog_customize, null);
        customizeDialog.setIcon(R.mipmap.enlern_logo);
        customizeDialog.setTitle("上海因仑");
        customizeDialog.setView(dialogView);
        customizeDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取EditView中的输入内容
                        EditText edit_text = (EditText) dialogView.findViewById(R.id.editText_send_bluetooth);
                        textViewSendBluetooth.setText(edit_text.getText().toString());
                    }
                });
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        customizeDialog.show();
    }

    private String connectedBlueTooth() {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                return device.getName() + " 已连接";
            }
        } else {
            return "没有找到已匹对的设备";
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViews();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        }
    };


}
