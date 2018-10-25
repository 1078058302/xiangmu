package com.example.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.R;
import com.example.base.BaseActivity;

public class AppActivity extends BaseActivity {


    @Override
    public void setTheme(boolean openBool) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_app;
    }

    @Override
    public void initData() {
        setTitles("应用说明");
    }
}
