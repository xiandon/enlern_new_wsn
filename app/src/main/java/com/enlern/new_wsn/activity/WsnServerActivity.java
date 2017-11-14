package com.enlern.new_wsn.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.network.NetworkUtils;
import com.enlern.new_wsn.tcp.TcpServer;

import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oc on 2017/7/4.
 */

public class WsnServerActivity extends BaseActivity {
    @BindView(R.id.tv_local_ip)
    TextView tvLocalIp;
    @BindView(R.id.tv_local_port)
    EditText tvLocalPort;
    @BindView(R.id.et_return_data)
    EditText etReturnData;
    @BindView(R.id.et_download_data)
    EditText etDownloadData;

    private static TcpServer tcpServer = null;
    private final MyHandler myHandler = new MyHandler(this);

    private static String TAG = "ActivityNodeServer";
    public static Context context;
    private MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();
    private ExecutorService exec = Executors.newCachedThreadPool();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_wsn_server);
        context = WsnServerActivity.this;
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        tvLocalIp.setText(NetworkUtils.getHostIP());
        bindReceiver();
    }

    private void bindReceiver() {
        IntentFilter intentFilter = new IntentFilter("tcpServerReceiver");
        registerReceiver(myBroadcastReceiver, intentFilter);
    }

    @OnClick({R.id.btn_open_server, R.id.btn_close_server, R.id.btn_download_server})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open_server:
                System.out.println("***开启服务器***");
                tcpServer = new TcpServer(getHost(tvLocalPort.getText().toString()));
                exec.execute(tcpServer);
                break;
            case R.id.btn_close_server:
                System.out.println("***关闭服务器***");
                tcpServer.closeSelf();
                break;
            case R.id.btn_download_server:
                Message message = Message.obtain();
                message.what = 2;
                message.obj = etDownloadData.getText().toString();
                myHandler.sendMessage(message);
                exec.execute(new Runnable() {
                    @Override
                    public void run() {
                        tcpServer.SST.get(0).send(etDownloadData.getText().toString());
                    }
                });
                break;
        }
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String mAction = intent.getAction();
            switch (mAction) {
                case "tcpServerReceiver":
                    String msg = intent.getStringExtra("tcpServerReceiver");
                    Message message = Message.obtain();
                    message.what = 1;
                    message.obj = msg;
                    myHandler.sendMessage(message);
                    break;
            }
        }
    }

    private class MyHandler extends Handler {
        private final WeakReference<WsnServerActivity> mActivity;

        MyHandler(WsnServerActivity activity) {
            mActivity = new WeakReference<WsnServerActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            WsnServerActivity activity = mActivity.get();
            if (activity != null) {
                switch (msg.what) {
                    case 1:
                        etReturnData.setText(msg.obj.toString());
                        //执行添加数据库的方法
                        // insertDataBase(msg.obj.toString());
                        break;
                    case 2:
                        etDownloadData.setText(msg.obj.toString());
                        break;
                }
            }
        }
    }

    private int getHost(String msg) {
        if (msg.equals("")) {
            msg = "9191";
        }
        return Integer.parseInt(msg);
    }
}
