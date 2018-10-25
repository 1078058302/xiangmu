package com.example.base;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.R;
import com.example.Utils.SharedPreUtils;
import com.example.adapter.BaseListAdapter;
import com.example.main.AppActivity;
import com.example.main.AuthorActivity;
import com.example.main.LoginActivity;
import com.example.service.SuspensionWindowService;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public abstract class BaseActivity extends AppCompatActivity {

    private TextView textView;
    private RelativeLayout bar;
    private ListView listView;
    private String[] baseDesc = {
            "应用说明",
            "关于作者",
            "设置"
    };
    private int[] imageArr = {
            R.drawable.base_0,
            R.drawable.base_2,
            R.drawable.base_1
    };
    private CircleImageView login_pic;
    private DrawerLayout mDrawerLayout;
    private LinearLayout mLayoutLeft;
    private ImageView mImage;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        listView = findViewById(R.id.base_list);
        RelativeLayout mLayout = findViewById(R.id.activity_layout);
        ListView listView = findViewById(R.id.base_list);
        textView = findViewById(R.id.txt_title);
        bar = findViewById(R.id.layout_bar);
        mDrawerLayout = findViewById(R.id.drawlayout);
        login_pic = findViewById(R.id.user_login_pic);
        mLayoutLeft = findViewById(R.id.layout_left);
        View v = View.inflate(this, getLayoutId(), null);
        mLayout.addView(v);
        findViewById(R.id.back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
        BaseListAdapter baseListAdapter = new BaseListAdapter(this, baseDesc, imageArr);
        listView.setAdapter(baseListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://应用说明
                        startActivity(new Intent(BaseActivity.this, AppActivity.class));
                        break;
                    case 1://关于作者
                        startActivity(new Intent(BaseActivity.this, AuthorActivity.class));
                        break;
                    case 2://设置
                        clearC();
                        break;
                }
            }
        });
        findViewById(R.id.layout_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = SharedPreUtils.getString(BaseActivity.this, "user_name");
                if (TextUtils.isEmpty(user_name)) {
                    startActivity(new Intent(BaseActivity.this, LoginActivity.class));
                } else {
                    toast("您已经登陆过了");
                }

            }
        });
        mImage = findViewById(R.id.image_theme);
        findViewById(R.id.layout_theme).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (openBool) {
                    mImage.setImageResource(R.drawable.profile_day);
                    openBool = false;
                } else {
                    mImage.setImageResource(R.drawable.profile_night);
                    openBool = true;
                }
                setTheme(!openBool);
            }
        });
    }

    public abstract void setTheme(boolean openBool);

    private boolean openBool = true;

    private void clearC() {
        SharedPreUtils.put(this, "user_name", "");
        SharedPreUtils.put(this, "user_pass", "");
        SharedPreUtils.put(this, "user_nickname", "");
        SharedPreUtils.put(this, "user_for", "");
        login_pic.setImageResource(R.drawable.user_logo);
        toast("退出成功");
    }

    ;

    public abstract int getLayoutId();

    public abstract void initData();

    public void setTitles(String title) {
        textView.setText(title);
    }

    public void showHideBar(boolean bool) {
        if (bool) {
            bar.setVisibility(View.VISIBLE);
        } else {
            bar.setVisibility(View.GONE);
        }
    }

    private Alerter alerter;

    public void toast(String content) {

        if (alerter == null) {
            alerter = Alerter.create(this);
        }
        alerter.
                setText(content).
                setDuration(3000).
                setBackgroundColor(R.color.colorPrimary).
                show();

    }

    public void openLayout() {
        mDrawerLayout.openDrawer(mLayoutLeft);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String user_for = SharedPreUtils.getString(this, "user_for");
        if (!TextUtils.isEmpty(user_for)) {
            String url = "http://www.vipandroid.cn/cert/upload/" + user_for + ".png";
            ImageLoader.getInstance().displayImage(url, login_pic);
        }
        resBroadcast();

    }


    //注册广播
    private void resBroadcast() {
        mClickBroadcast = new ClickBroadcast();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.abner.ming.mian");
        registerReceiver(mClickBroadcast, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mClickBroadcast);
    }

    private ClickBroadcast mClickBroadcast;
    private boolean showHide = true;

    private class ClickBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("com.abner.ming.mian".equals(action)) {
                //点击事件
                if (showHide) {
                    mDrawerLayout.openDrawer(mLayoutLeft);

                    showHide = false;
                } else {
                    mDrawerLayout.closeDrawer(mLayoutLeft);
                    showHide = true;
                }

            }
        }
    }


}
