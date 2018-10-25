package com.example.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.HttpHelper;
import com.example.R;
import com.example.Utils.NetworkUtils;
import com.example.Utils.SharedPreUtils;
import com.example.WebViewActivity;
import com.example.adapter.MyAdapter;
import com.example.bean.NewsBean;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    private PullToRefreshListView pullToRefreshListView;
    private List<NewsBean.ResultBean.DataBean> newsDataAll = new ArrayList<>();
    private MyAdapter myAdapter;
    private int page = 1;
    private int total;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_news, container, false);
        initData(v);
        return v;
    }

    public void initData(View v) {
        pullToRefreshListView = v.findViewById(R.id.pull);
        myAdapter = new MyAdapter(getActivity());
        pullToRefreshListView.setMode(PullToRefreshBase.Mode.BOTH);
        pullToRefreshListView.setAdapter(myAdapter);
        Bundle bundle = getArguments();
        final String id = bundle.getString("lid");
        doHttp(id, page);
        pullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                newsDataAll.clear();
                doHttp(id, page);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (newsDataAll.size() >= total) {
                    toast("没有更多了");
                } else {
                    page++;
                    doHttp(id, page);
                }
            }
        });
        pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = newsDataAll.get(position).getUrl();
//                toast(url);
                WebViewActivity.startWebViewActivity(getActivity(), newsDataAll.get(position - 1).getUrl());

            }
        });
    }

    private void doHttp(final String id, final int page) {
        String url = "https://feed.mix.sina.com.cn/api/roll/get?pageid=153&lid=" + id + "&k=&num=20&page=" + page;
        if (!NetworkUtils.isConnected(getActivity())) {
            String data1 = SharedPreUtils.getString(getActivity(), "data" + id);
            Gson gson = new Gson();
            NewsBean newsBean = gson.fromJson(data1, NewsBean.class);
            total = newsBean.getResult().getTotal();
            List<NewsBean.ResultBean.DataBean> data2 = newsBean.getResult().getData();
            newsDataAll.addAll(data2);
            myAdapter.setData(newsDataAll);
            myAdapter.notifyDataSetChanged();
        } else {
            new HttpHelper().get(url).result(new HttpHelper.HttpListener() {
                @Override
                public void success(String data) {
                    if (page == 1) {
                        SharedPreUtils.put(getActivity(), "data" + id, data);
                    }
                    boolean b = NetworkUtils.isConnected(getActivity());
                    Gson gson = new Gson();
                    NewsBean newsBean = gson.fromJson(data, NewsBean.class);
                    total = newsBean.getResult().getTotal();
                    List<NewsBean.ResultBean.DataBean> data1 = newsBean.getResult().getData();
                    newsDataAll.addAll(data1);
                    myAdapter.setData(newsDataAll);
                    myAdapter.notifyDataSetChanged();
                    pullToRefreshListView.onRefreshComplete();
                }

                @Override
                public void fail() {

                }
            });
        }
    }

    private Alerter alert;

    public void toast(String content) {
        if (alert == null) {
            alert = Alerter.create(getActivity());
        }
        alert.setText(content).
                setDuration(3000).
                setBackgroundColor(R.color.colorPrimary).
                show();
    }


    public void setTheme(boolean theme) {
        if(myAdapter!=null){
            myAdapter.setTheme(theme);
        }
    }
}
