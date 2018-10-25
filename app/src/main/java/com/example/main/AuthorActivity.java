package com.example.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.R;
import com.example.base.BaseActivity;

public class AuthorActivity extends BaseActivity {


    @Override
    public void setTheme(boolean openBool) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_author;
    }

    @Override
    public void initData() {
        setTitles("关于作者");
    }
}
