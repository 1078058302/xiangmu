package com.example.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.R;
import com.tapadoo.alerter.Alerter;

public abstract class BaseFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = View.inflate(getActivity(), R.layout.fragment_base, null);
        View view1 = View.inflate(getActivity(), getLayoutId(), null);
        RelativeLayout mLayout = view.findViewById(R.id.fragment_layout);
        mLayout.addView(view1);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    public abstract int getLayoutId();

    public abstract void initData();

    private Alerter alert;

    public void toast(String content) {
        if (alert == null) {
            alert = Alerter.create(getActivity());
        }
        alert.create(getActivity()).
                setText(content).
                setDuration(3000).
                setBackgroundColor(R.color.colorPrimary).
                show();
    }
}
