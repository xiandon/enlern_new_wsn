package com.enlern.new_wsn.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.enlern.new_wsn.R;
import com.enlern.new_wsn.application.CustomActivityManager;
import com.enlern.new_wsn.fragment.FragmentData;
import com.enlern.new_wsn.fragment.FragmentHome;
import com.enlern.new_wsn.fragment.FragmentNode;
import com.enlern.new_wsn.fragment.FragmentSetting;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * 新物联网，手机客户端
 */
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    @BindView(R.id.viewPager_main)
    ViewPager viewPagerMain;
    @BindView(R.id.navigation)
    BottomNavigationView navigation;
    private Context context;

    private FragmentHome fragmentHome = new FragmentHome();
    private FragmentData fragmentData = new FragmentData();
    private FragmentSetting fragmentNode = new FragmentSetting();


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            viewPagerMain.setCurrentItem(item.getOrder());
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    break;
                case R.id.navigation_dashboard:
                    break;
                case R.id.navigation_notifications:
                    break;
            }
            return true;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = MainActivity.this;
        initViews();

    }


    private void initViews() {
        CustomActivityManager.addActivity(MainActivity.this);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPagerMain.addOnPageChangeListener(this);
        viewPagerMain.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragmentHome;
                    case 1:
                        return fragmentNode;
                    case 2:
                        return fragmentData;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        navigation.getMenu().getItem(position).setChecked(true);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void showDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("您确定要退出本程序！").setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomActivityManager.finishAllActivity();
                    }
                }).setNegativeButton("取消", null);
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 主页后退键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                showDialog(this);
                break;

            default:
                break;
        }
        return false;
    }

}
