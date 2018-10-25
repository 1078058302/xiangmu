package com.example.mvp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public interface LayoutInterface {
    //初始化的方法
    void initData();

    //获取View的方法
    View getRootView();

    //设置layout
    void create(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle);
}
