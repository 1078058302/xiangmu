package com.example.mvp;

import com.example.base.BaseActivity;

public abstract class ActivityPresenter<T extends LayoutInterface> extends BaseActivity {

    private T layoutInterface;

    private Class<T> getClassLayoutInterface;

    public ActivityPresenter() {
        try {
            layoutInterface = getClassLayoutInterface.newInstance();

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    public void initData() {

    }
}
