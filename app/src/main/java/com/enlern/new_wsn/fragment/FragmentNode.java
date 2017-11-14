package com.enlern.new_wsn.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.activity.SingleBluetoothActivity;
import com.enlern.new_wsn.application.MyApplication;
import com.enlern.new_wsn.network.NetworkUtils;
import com.enlern.new_wsn.sharedPreferences.SPUtils;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 配置Wi-Fi
 * 配置接口
 * Created by oc on 2017/6/22.
 */

public class FragmentNode extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "FragmentNode";
    @BindView(R.id.textView_fragment_title)
    TextView textViewFragmentTitle;
    @BindView(R.id.textView_config_commune)
    TextView textViewConfigCommune;
    @BindView(R.id.textView_config_access_web)
    TextView textViewConfigAccessWeb;
    @BindView(R.id.textView_config_bluetooth)
    TextView textViewConfigBluetooth;
    @BindView(R.id.textView_config_wifi_ssid)
    TextView textViewConfigWifiSsid;
    @BindView(R.id.textView_config_wifi_id_address)
    TextView textViewConfigWifiIdAddress;
    @BindView(R.id.textView_config_type_setting)
    TextView textViewConfigTypeSetting;
    @BindView(R.id.swipeRefreshLayout_node_container)
    SwipeRefreshLayout swipeRefreshLayoutNodeContainer;
    private Context context;
    private View view;
    private Unbinder unbinder;
    private int iWhiched;
    private String[] comm_type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_node, container, false);
        context = MyApplication.getInstance();
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        swipeRefreshLayoutNodeContainer.setOnRefreshListener(this);

        swipeRefreshLayoutNodeContainer.setColorSchemeResources(R.color.colorAccent, R.color.colorEnlern, R.color.colorPrimaryDark, R.color.yellow);

        textViewFragmentTitle.setText(R.string.title_node);
        int iNetworkType = NetworkUtils.getNetworkType(getActivity().getApplicationContext());
        String strSSID = NetworkUtils.wifiHostSSID(getActivity().getApplicationContext());
        System.out.println("Wi-Fi网络 = " + iNetworkType);
        if (iNetworkType == 1) {
            //wifi连接
            textViewConfigCommune.setText(R.string.network_WIFI);
            textViewConfigWifiSsid.setText(strSSID.replaceAll("\"", ""));
            textViewConfigWifiIdAddress.setText(NetworkUtils.getHostIP());
        } else if (iNetworkType == 2) {
            //2G
            textViewConfigCommune.setText(R.string.network_2G);
            textViewConfigWifiSsid.setText(R.string.no_wifi);
            textViewConfigWifiIdAddress.setText(R.string.network_2G);
        } else if (iNetworkType == 3) {
            //3G
            textViewConfigCommune.setText(R.string.network_3G);
            textViewConfigWifiIdAddress.setText(R.string.network_3G);
            textViewConfigWifiSsid.setText(R.string.no_wifi);
        } else if (iNetworkType == 4) {
            //4G
            textViewConfigCommune.setText(R.string.network_4G);
            textViewConfigWifiSsid.setText(R.string.no_wifi);
            textViewConfigWifiIdAddress.setText(R.string.network_4G);
        } else if (iNetworkType == 5) {
            //no network
            textViewConfigCommune.setText(R.string.no_network);
            textViewConfigWifiSsid.setText(R.string.no_wifi);
            textViewConfigWifiIdAddress.setText(R.string.no_network);
        } else {
            textViewConfigCommune.setText(R.string.network_unknown);

        }


        handler.sendEmptyMessage(0);
        boolean bBlueTooth = NetworkUtils.blueBoothStatus(getActivity().getApplicationContext());
        if (bBlueTooth) {
            textViewConfigBluetooth.setText(R.string.bluetooth_open);
        } else {
            textViewConfigBluetooth.setText(R.string.bluetooth_close);
        }


        comm_type = getActivity().getApplicationContext().getResources().getStringArray(R.array.common_type);

        if (SPUtils.get(context, "int_dialog_common_type", 0) != null) {
            iWhiched = (int) SPUtils.get(context, "int_dialog_common_type", 0);
            textViewConfigTypeSetting.setText(comm_type[iWhiched]);
        } else {
            textViewConfigTypeSetting.setText(comm_type[0]);
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void accessTheNetwork() {
        String url = "https://www.baidu.com";
        NetworkUtils.isNetWorkAvailableOfGet(url, new Comparable<Boolean>() {
            @Override
            public int compareTo(@NonNull Boolean aBoolean) {
                if (aBoolean) {
                    // 网络正常
                    SPUtils.put(getActivity().getApplicationContext(), "NetworkRun", 1);
                } else {
                    // 网络异常
                    SPUtils.put(getActivity().getApplicationContext(), "NetworkRun", 0);
                }
                return 0;
            }
        });
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    accessTheNetwork();
                    int i = (int) SPUtils.get(getActivity().getApplicationContext(), "NetworkRun", 1);
                    if (i == 1) {
                        textViewConfigAccessWeb.setText(R.string.network_normal);
                    } else {
                        textViewConfigAccessWeb.setText(R.string.network_abnormal);
                    }
                    break;
                case 1:
                    break;
            }
            return false;
        }
    });


    @OnClick({R.id.ll_node_bluetooth_setting,
            R.id.ll_node_wifi_setting,
            R.id.ll_node_open_wsn,
            R.id.ll_node_open_intelligent,
            R.id.ll_node_open_smart_transportation,
            R.id.ll_node_open_rf_reader,
            R.id.ll_node_open_medical_care,
            R.id.ll_node_setting_type,
            R.id.ll_node_open_single_bluetooth})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_node_bluetooth_setting:
                Intent intent = new Intent("android.settings.BLUETOOTH_SETTINGS");
                startActivity(intent);
                break;
            case R.id.ll_node_wifi_setting:
                Intent intent2 = new Intent("android.settings.WIFI_SETTINGS");
                startActivity(intent2);
                break;
            case R.id.ll_node_open_wsn:
                // 新物联网
                showDialogExample(context, "wsn");
                break;
            case R.id.ll_node_open_intelligent:
                // 智能农业
                break;
            case R.id.ll_node_open_smart_transportation:
                // 智能交通
                break;
            case R.id.ll_node_open_rf_reader:
                // RFID
                break;
            case R.id.ll_node_open_medical_care:
                // 智能医疗
                break;
            case R.id.ll_node_setting_type:
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.mipmap.enlern_logo);
                builder.setTitle("请选择通信模式");

                final int[] iWhich = new int[1];
                builder.setSingleChoiceItems(comm_type, iWhiched, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        iWhich[0] = i;
                        SPUtils.put(context, "int_dialog_common_type", i);
                    }
                });

                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        initViews();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
                break;

            case R.id.ll_node_open_single_bluetooth:
                Intent intentSingleBluetooth = new Intent(context, SingleBluetoothActivity.class);
                startActivity(intentSingleBluetooth);
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initViews();
    }

    private void showDialogExample(Context context, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setIcon(R.mipmap.enlern_logo);
        builder.setTitle("请确定通信模式");
        builder.setMessage("当前选泽的通信模式是：" + comm_type[iWhiched] + "; 准备访问" + type + "实例");

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.show();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                initViews();
                swipeRefreshLayoutNodeContainer.setRefreshing(false);
            }
        }, 2000);
    }

    public void onDownloadRefresh(Context context) {
        onRefresh();
    }
}
