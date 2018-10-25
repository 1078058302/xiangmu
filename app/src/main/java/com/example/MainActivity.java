package com.example;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.Utils.NetworkUtils;
import com.example.Utils.SharedPreUtils;
import com.example.base.BaseActivity;
import com.example.bean.NewsItemBean;
import com.example.fragment.NewsFragment;
import com.example.service.SuspensionWindowService;
import com.example.topgridlibrary.topgrid.ChannelActivity;
import com.example.topgridlibrary.topgrid.app.AppApplication;
import com.example.topgridlibrary.topgrid.bean.ChannelItem;
import com.example.topgridlibrary.topgrid.bean.ChannelManage;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {


    private String HTTP_NEWS_ITEM = "http://www.vipandroid.cn/week/news_xl.txt";
    private List<NewsItemBean.NewsBean> items = new ArrayList<>();
    private List<NewsFragment> fragments = new ArrayList<>();
    private MAdapter mAdapter;
    private DialogUtils dialogUtils;
    private List<ChannelItem> userList = new ArrayList<>();
    private ViewPager viewPager;
    private boolean openBool;
    private TabLayout tabLayout;
    private RelativeLayout layout_rea;

    @Override
    public void setTheme(boolean openBool) {
        this.openBool = openBool;
        Fragment fragment = mAdapter.getItem(viewPager.getCurrentItem());
        if (fragment instanceof NewsFragment) {
            ((NewsFragment) fragment).setTheme(openBool);
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }


    @Override
    public void initData() {
        showHideBar(false);
        tabLayout = findViewById(R.id.tab);
        viewPager = findViewById(R.id.main_vp);
        mAdapter = new MAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);
        dialogUtils = new DialogUtils(this);
        doNewsItem();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Fragment fragment = mAdapter.getItem(position);
                if (fragment instanceof NewsFragment) {
                    ((NewsFragment) fragment).setTheme(openBool);
                }
                if (openBool) {
                    tabLayout.setBackgroundColor(Color.parseColor("#222222"));
                    layout_rea.setBackgroundColor(Color.parseColor("#222222"));
                } else {
                    tabLayout.setBackgroundColor(Color.parseColor("#ffffff"));
                    layout_rea.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        layout_rea = findViewById(R.id.layout_manger);
        layout_rea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChannelActivity.class));
            }
        });
    }

    private void doNewsItem() {
        if (!NetworkUtils.isConnected(MainActivity.this)) {
            String tab = SharedPreUtils.getString(MainActivity.this, "tab");
            Gson gson = new Gson();
            NewsItemBean newsItemBean = gson.fromJson(tab, NewsItemBean.class);
            items = newsItemBean.getItems();
            for (int i = 0; i < items.size(); i++) {
                fragments.add(new NewsFragment());
            }
            mAdapter.notifyDataSetChanged();
        } else {
            dialogUtils.show();
            new HttpHelper().get(HTTP_NEWS_ITEM).result(new HttpHelper.HttpListener() {

                @Override
                public void success(String data) {
                    SharedPreUtils.put(MainActivity.this, "tab", data);
                    Gson gson = new Gson();
                    NewsItemBean newsItemBean = gson.fromJson(data, NewsItemBean.class);
                    items = newsItemBean.getItems();
                    for (int i = 0; i < items.size(); i++) {
                        fragments.add(new NewsFragment());
                    }
                    mAdapter.notifyDataSetChanged();
                    dialogUtils.dismiss();
                }

                @Override
                public void fail() {

                }
            });
        }
    }

    private class MAdapter extends FragmentPagerAdapter {

        public MAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            NewsFragment newsFragment = fragments.get(position);
            Bundle bundle = new Bundle();
            bundle.putString("lid", items.get(position).getId());
            newsFragment.setArguments(bundle);
            return newsFragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return items.get(position).getName();
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }

    private Intent intentService;

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                toast("请找到浪八资讯应用，把悬浮窗权限打开");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivityForResult(intent, 1);
                    }
                }, 5000);
            } else {
                //TODO do something you need
                intentService = new Intent(this, SuspensionWindowService.class);
                startService(intentService);
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                //判断是否需要 向用户解释，为什么要申请该权限
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }
        createItem();
    }


    private boolean createItem() {
        try {
            String channel = SharedPreUtils.getString(this, "channel");
            if (channel != null && "1".equals(channel)) {
                items.clear();
                fragments.clear();
                List<ChannelItem> channelList = ChannelManage.getManage(AppApplication.getApp().getSQLHelper()).getUserChannel();

                for (int a = 0; a < channelList.size(); a++) {
                    NewsItemBean.NewsBean bean = new NewsItemBean.NewsBean();

                    String itemBean = channelList.get(a).getName();

                    String[] itemArr = itemBean.split("=");

                    bean.setName(itemArr[0]);
                    bean.setId(itemArr[1]);
                    items.add(bean);
                    fragments.add(new NewsFragment());
                }
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }

                return true;
            }
        } catch (Exception e) {
        }

        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (intentService != null) {
            stopService(intentService);
        }
    }

}
