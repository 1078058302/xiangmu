package com.example.main;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.example.Utils.SharedPreUtils;
import com.example.base.BaseActivity;
import com.example.MainActivity;
import com.example.R;

public class StartActivity extends BaseActivity {


    @Override
    public void setTheme(boolean openBool) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_start;
    }

    @Override
    public void initData() {
        showHideBar(false);
        boolean b = SharedPreUtils.getBoolean(this, "boolean");
        if (b) {
            handler.sendEmptyMessageDelayed(723, 3000);
        } else {
            handler.sendEmptyMessageDelayed(722, 3000);
        }

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 723) {
                doActivity(MainActivity.class);
            } else {
                doActivity(WelcomeActivity.class);
            }
        }
    };

    private void doActivity(Class cls) {
        startActivity(new Intent(this, cls));
        finish();
    }
}
