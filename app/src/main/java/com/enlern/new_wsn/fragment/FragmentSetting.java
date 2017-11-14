package com.enlern.new_wsn.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.network.NetworkUtils;
import com.enlern.new_wsn.sharedPreferences.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by pen on 2017/8/8.
 */

public class FragmentSetting extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.textView_fragment_title)
    TextView textViewFragmentTitle;
    @BindView(R.id.local_ip)
    TextView localIp;
    @BindView(R.id.tcp_server_ip)
    TextView tcpServerIp;
    @BindView(R.id.tcp_server_port)
    TextView tcpServerPort;
    @BindView(R.id.srl_node_container)
    SwipeRefreshLayout srlNodeContainer;
    @BindView(R.id.tcp_connect)
    TextView tcpConnect;
    private Context context;
    private View view;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_settings, container, false);
        context = getActivity();
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        textViewFragmentTitle.setText(R.string.title_node);
        localIp.setText(NetworkUtils.getHostIP());

        tcpServerIp.setText(SPUtils.get(getActivity(), "TCP_IP", "192.168.250.10").toString());
        tcpServerPort.setText(SPUtils.get(getActivity(), "TCP_PORT", 9191).toString());

        handler.sendEmptyMessage(1);

        srlNodeContainer.setOnRefreshListener(this);
        srlNodeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorEnlern, R.color.colorPrimaryDark, R.color.yellow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tcp_server_ip, R.id.tcp_server_port, R.id.local_ip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tcp_server_ip:
                String serverIP = tcpServerIp.getText().toString();
                final EditText editText = new EditText(getActivity());
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                editText.setText(serverIP);
                builder.setIcon(R.mipmap.enlern_logo);
                builder.setTitle("请输入服务器IP地址");
                builder.setView(editText);

                builder.setNegativeButton("取消", null);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tcpServerIp.setText(editText.getText().toString());
                        SPUtils.put(getActivity(), "TCP_IP", editText.getText().toString());
                        handler.sendEmptyMessage(1);
                    }
                });
                builder.show();
                break;
            case R.id.tcp_server_port:
                String serverPort = tcpServerPort.getText().toString();
                final EditText editText2 = new EditText(getActivity());
                editText2.setInputType(InputType.TYPE_CLASS_PHONE);
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                editText2.setText(serverPort);
                builder2.setTitle("请输入服务器端口号");
                builder2.setIcon(R.mipmap.enlern_logo);
                builder2.setView(editText2);

                builder2.setNegativeButton("取消", null);
                builder2.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        tcpServerPort.setText(editText2.getText().toString());
                        SPUtils.put(getActivity(), "TCP_PORT", Integer.parseInt(editText2.getText().toString()));
                        handler.sendEmptyMessage(1);
                    }
                });
                builder2.show();
                break;

            case R.id.local_ip:
                Intent intent2 = new Intent("android.settings.WIFI_SETTINGS");
                startActivity(intent2);
                break;

            default:

                break;
        }
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initViews();
                srlNodeContainer.setRefreshing(false);
            }
        }, 1000);

    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    tcpConnect.setText(SPUtils.get(getActivity(), "TCP_IP", "192.168.250.10").toString() + ":" + SPUtils.get(getActivity(), "TCP_PORT", 9191));
                    break;

            }
            return false;
        }
    });
}
