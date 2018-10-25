package com.example.mvp;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.R;

public abstract class AppLayoutInterface implements LayoutInterface {

    private View rootView;
    private SparseArray<View> views = new SparseArray<>();

    @Override
    public void initData() {

    }

    /*
     * 获取控件
     * */
    public <T extends View> T get(int id) {
        T view = (T) this.views.get(id);
        if (view == null) {
            view = rootView.findViewById(id);
            views.put(id, view);
        }
        return view;
    }

    public void setClick(View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        } else {
            for (int id : ids) {
                get(id).setOnClickListener(listener);
            }
        }
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public void create(LayoutInflater inflater, ViewGroup viewGroup, Bundle bundle) {
        rootView = inflater.inflate(getLayoutId(), viewGroup, false);
    }


    public abstract int getLayoutId();
}
