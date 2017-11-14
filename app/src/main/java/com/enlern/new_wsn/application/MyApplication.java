package com.enlern.new_wsn.application;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.greendao.DaoMaster;
import com.enlern.new_wsn.greendao.DaoSession;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by oc on 2017/6/22.
 */

public class MyApplication extends Application {
    /**
     * 定义全局上下文
     */
    private static MyApplication instance;
    private static Context context;

    /**
     * 设置green dao
     */
    private DaoMaster.DevOpenHelper mHelper;
    private SQLiteDatabase db;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = getApplicationContext();
        setDatabase();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/consola.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static MyApplication getInstance() {
        return instance;

    }

    public abstract class Content {

    }

    public static Context getContext() {
        return context;
    }


    /**
     * 设置Greendao数据库使用
     */
    private void setDatabase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "new_wsn_db", null);
        db = mHelper.getWritableDatabase();
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getDb() {
        return db;
    }
}
