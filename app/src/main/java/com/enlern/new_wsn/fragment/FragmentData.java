package com.enlern.new_wsn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enlern.new_wsn.utils.JsonUtils;
import com.enlern.new_wsn.R;
import com.enlern.new_wsn.adapter.WsnHistoryAdapter;
import com.enlern.new_wsn.application.MyApplication;
import com.enlern.new_wsn.bean.SerialWsn;
import com.enlern.new_wsn.sharedPreferences.SPUtils;
import com.enlern.new_wsn.utils.ConversionSystem;
import com.enlern.new_wsn.utils.StaticData;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 历史数据查询，节点简介等
 * Created by oc on 2017/6/22.
 */

public class FragmentData extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private static String TAG = "FragmentData";
    @BindView(R.id.textView_fragment_title)
    TextView textViewFragmentTitle;
    @BindView(R.id.recyclerViewHistory)
    RecyclerView recyclerViewHistory;
    @BindView(R.id.srl_data)
    SwipeRefreshLayout srlData;
    @BindView(R.id.tv_data_loading)
    TextView tvDataLoading;
    private Context context;
    private View view;
    private Unbinder unbinder;

    private GridLayoutManager gridLayoutManager;
    private WsnHistoryAdapter wsnHistoryAdapter;
    private ArrayList<SerialWsn> dataList;

    private boolean bServer = false;

    private String tcp_ip;
    private int tcp_port;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data, container, false);
        context = MyApplication.getInstance();
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        textViewFragmentTitle.setText(R.string.title_data);
        setRecyclerView();
        swipeRefresh();
        handler.sendEmptyMessage(2);

    }

    private void swipeRefresh() {
        srlData.setOnRefreshListener(this);
        srlData.setColorSchemeResources(R.color.colorAccent, R.color.colorEnlern, R.color.colorPrimaryDark, R.color.yellow);

    }

    private void setRecyclerView() {
        gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewHistory.setLayoutManager(gridLayoutManager);

    }

    //绑定数据
    private void binderListData(List<SerialWsn> serialWsns) {
        if (serialWsns.size() != 0) {
            tvDataLoading.setVisibility(View.GONE);
        } else {
            tvDataLoading.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(3);
        }
        wsnHistoryAdapter = new WsnHistoryAdapter(getActivity(), serialWsns);
        recyclerViewHistory.setAdapter(wsnHistoryAdapter);
    }

    private void startGet() {
        int conn = (Integer) SPUtils.get(getActivity(), "int_dialog_common_type", 0);
        System.out.println("DATA访问模式 = " + conn);
        switch (conn) {
            case 0:
                new Thread(runnable).start();
                break;
            case 1:
                doOkHttpGet(StaticData.selectIP("local").getHistorical());
                break;
            case 2:
                doOkHttpGet(StaticData.selectIP("web").getHistorical());
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }
    }

    private void doOkHttpGet(String url) {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ConversionSystem.toastDeal(getActivity(), "网络通信错误");
                bServer = false;
            }

            @Override
            public void onResponse(String response, int id) {
                bServer = true;
                dataList = new ArrayList<SerialWsn>();
                dataList = JsonUtils.jsonDealWeb(response, "result");
                if (dataList != null) {
                    binderListData(dataList);
                }
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            connTcpServer("1503");
        }
    };


    private void connTcpServer(String string) {
        try {

            tcp_ip = SPUtils.get(getActivity(), "TCP_IP", "").toString();
            tcp_port = (int) SPUtils.get(getActivity(), "TCP_PORT", 0);

            System.out.println("22222 = " + tcp_ip + ":" + tcp_port);

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
                dataList = new ArrayList<>();
                dataList = JsonUtils.jsonDeal(data, "Historical_data_data");
                handler.sendMessage(handler.obtainMessage(4, dataList));
            }
            bServer = true;
            br.close();
            is.close();
            pw.close();
            os.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            bServer = false;
            handler.sendEmptyMessage(1);
        }
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    ConversionSystem.toastDeal(getActivity(), "DATA服务器异常");
                    break;
                case 2:
                    startGet();
                    break;
                case 3:
                    ConversionSystem.toastDeal(getActivity(), "DATA服务器暂无数据");
                    break;
                case 4:
                    ArrayList<SerialWsn> serialWsns = (ArrayList<SerialWsn>) message.obj;
                    if (serialWsns != null) {
                        binderListData(serialWsns);
                    }

                    break;

                case 5:
                    srlData.setRefreshing(false);
                    break;
            }
            return false;
        }
    });


    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(2);
                handler.sendEmptyMessage(5);
            }
        }, 1000);
    }

    @OnClick(R.id.tv_data_loading)
    public void onViewClicked() {
        srlData.setRefreshing(true);
        onRefresh();
    }
}
