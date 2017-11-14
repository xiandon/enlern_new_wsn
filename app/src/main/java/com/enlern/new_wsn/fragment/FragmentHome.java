package com.enlern.new_wsn.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.enlern.new_wsn.utils.JsonUtils;
import com.enlern.new_wsn.R;
import com.enlern.new_wsn.adapter.MainWsnAdapter;
import com.enlern.new_wsn.application.MyApplication;
import com.enlern.new_wsn.bean.SerialWsn;
import com.enlern.new_wsn.glide.GlideImageLoader;
import com.enlern.new_wsn.sharedPreferences.SPUtils;
import com.enlern.new_wsn.utils.ConversionSystem;
import com.enlern.new_wsn.utils.StaticData;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
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
 * 显示节点即时数据
 * Created by oc on 2017/6/22.
 */

public class FragmentHome extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    private static String TAG = "FragmentHome";
    @BindView(R.id.textView_fragment_title)
    TextView textViewFragmentTitle;
    @BindView(R.id.bannerHome)
    Banner bannerHome;
    @BindView(R.id.recyclerViewMain)
    RecyclerView recyclerViewMain;
    @BindView(R.id.srl_home)
    SwipeRefreshLayout srlHome;
    @BindView(R.id.tv_loading)
    TextView tvLoading;
    private Context context;
    private View view;
    private Unbinder unbinder;

    private GridLayoutManager layoutManager;
    private MainWsnAdapter wsnAdapter;

    private String tcp_ip;
    private int tcp_port;


    private ArrayList<SerialWsn> dataList;
    private boolean bServer = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = MyApplication.getInstance();
        unbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    private void initViews() {
        if (!SPUtils.contains(getActivity(), "TCP_IP")) {
            SPUtils.put(getActivity(), "TCP_IP", "192.168.250.10");
            SPUtils.put(getActivity(), "TCP_PORT", 9191);
        }


        textViewFragmentTitle.setText(R.string.title_home);
        // loadWebImage();
        loadHostImage();//banner
        swipeRefresh();//刷新
        recyclerView();//recyclerView
        handler.sendEmptyMessage(2);
    }

    private void swipeRefresh() {
        srlHome.setOnRefreshListener(this);
        srlHome.setColorSchemeResources(R.color.colorAccent, R.color.colorEnlern, R.color.colorPrimaryDark, R.color.yellow);
    }


    private void recyclerView() {
        layoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        layoutManager.setOrientation(GridLayoutManager.VERTICAL);
        recyclerViewMain.setLayoutManager(layoutManager);
        recyclerViewMain.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));


    }


    //绑定数据
    private void binderListData(List<SerialWsn> serialWsns) {
        if (serialWsns.size() != 0) {
            tvLoading.setVisibility(View.GONE);
            wsnAdapter = new MainWsnAdapter(getActivity(), serialWsns);
            recyclerViewMain.setAdapter(wsnAdapter);
        } else {
            tvLoading.setVisibility(View.VISIBLE);
            handler.sendEmptyMessage(3);
        }
        wsnAdapter = new MainWsnAdapter(getActivity(), serialWsns);
        recyclerViewMain.setAdapter(wsnAdapter);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void startGet() {
        int conn = (Integer) SPUtils.get(getActivity(), "int_dialog_common_type", 0);
        System.out.println("HOME访问模式 = " + conn);
        switch (conn) {
            case 0:
                new Thread(runnable).start();
                break;
            case 1:
                doOkHttpGet(StaticData.selectIP("local").getInstant());
                break;
            case 2:
                doOkHttpGet(StaticData.selectIP("web").getInstant());
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
                binderListData(dataList);

            }
        });

    }


    private void loadWebImage() {
        List<Object> listUrl = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();
        String Url1 = "http://58.246.105.210:6100/LoginWeb/image/serial06.png";
        String Url2 = "http://58.246.105.210:6100/LoginWeb/image/serial07.png";
        String Url3 = "http://58.246.105.210:6100/LoginWeb/image/serial08.png";
        String Url4 = "http://58.246.105.210:6100/LoginWeb/image/serial09.png";
        String Url5 = "http://58.246.105.210:6100/LoginWeb/image/serial0a.png";
        listUrl.add(Url1);
        listUrl.add(Url2);
        listUrl.add(Url3);
        listUrl.add(Url4);
        listUrl.add(Url5);
        listTitle.add("霍尔传感器");
        listTitle.add("雨滴传感器");
        listTitle.add("流量传感器");
        listTitle.add("火焰传感器");
        listTitle.add("温度传感器");

        addBanner(listUrl, listTitle);
    }


    private void loadHostImage() {
        List<Object> fileList = new ArrayList<>();
        List<String> listTitle = new ArrayList<>();

        String Url1 = "file:///android_asset/image/serial01.png";
        String Url2 = "file:///android_asset/image/serial02.png";
        String Url3 = "file:///android_asset/image/serial03.png";
        String Url4 = "file:///android_asset/image/serial04.png";
        String Url5 = "file:///android_asset/image/serial05.png";

        fileList.add(Url1);
        fileList.add(Url2);
        fileList.add(Url3);
        fileList.add(Url4);
        fileList.add(Url5);
        listTitle.add("光照传感器");
        listTitle.add("温湿度传感器");
        listTitle.add("远红外传感器");
        listTitle.add("气压传感器");
        listTitle.add("血压传感器");
        addBanner(fileList, listTitle);
    }

    private void addBanner(List<Object> listUrl, List<String> listTitle) {
        bannerHome.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        bannerHome.setImageLoader(new GlideImageLoader());
        bannerHome.setImages(listUrl);
        bannerHome.setBannerTitles(listTitle);
        bannerHome.setDelayTime(3000);
        bannerHome.setBannerAnimation(Transformer.DepthPage);
        bannerHome.setIndicatorGravity(BannerConfig.CENTER);
        bannerHome.start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            connTcpServer("1501");
        }
    };

    private void connTcpServer(String string) {
        try {

            tcp_ip = SPUtils.get(getActivity(), "TCP_IP", "").toString();
            tcp_port = (int) SPUtils.get(getActivity(), "TCP_PORT", 9191);


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
                dataList = new ArrayList<SerialWsn>();
                dataList = JsonUtils.jsonDeal(data, "Real_time_data");
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
                    ConversionSystem.toastDeal(getActivity(), "HOME服务器异常");
                    break;
                case 2:
                    startGet();
                    break;
                case 3:
                    ConversionSystem.toastDeal(getActivity(), "HOME服务器暂无数据");
                    break;
                case 4:
                    ArrayList<SerialWsn> serialWsns = (ArrayList<SerialWsn>) message.obj;
                    binderListData(serialWsns);
                    break;
            }
            return false;
        }
    });


    @Override
    public void onRefresh() {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(2);
                srlHome.setRefreshing(false);
            }
        });

    }

    @OnClick({R.id.textView_fragment_title, R.id.tv_loading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView_fragment_title:
                break;
            case R.id.tv_loading:
                srlHome.setRefreshing(true);
                onRefresh();
                break;
        }
    }


}
