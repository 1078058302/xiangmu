package com.example.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.R;
import com.example.bean.NewsBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<NewsBean.ResultBean.DataBean> data = new ArrayList<>();
    private DisplayImageOptions options;

    public MyAdapter(Context context) {
        this.context = context;
        options = new DisplayImageOptions.
                Builder().
                cacheOnDisk(true).//开启磁盘缓存
                cacheInMemory(true).//内存缓存
                build();
    }

    public MyAdapter(List<NewsBean.ResultBean.DataBean> data) {
        this.data = data;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<NewsBean.ResultBean.DataBean> getData() {
        return data;
    }

    public void setData(List<NewsBean.ResultBean.DataBean> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (data.get(position).getImages() != null && data.get(position).getImages().size() >= 3) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                ViewHolder1 holder1;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.news_item1, null);
                    holder1 = new ViewHolder1();
                    holder1.image1 = convertView.findViewById(R.id.image1);
                    holder1.image2 = convertView.findViewById(R.id.image2);
                    holder1.image3 = convertView.findViewById(R.id.image3);
                    holder1.text = convertView.findViewById(R.id.news1_text);
                    convertView.setTag(holder1);
                } else {
                    holder1 = (ViewHolder1) convertView.getTag();
                }
                NewsBean.ResultBean.DataBean dataBean = data.get(position);
                ImageLoader.getInstance().displayImage(dataBean.getImages().get(0).getU(), holder1.image1, options);
                ImageLoader.getInstance().displayImage(dataBean.getImages().get(1).getU(), holder1.image2, options);
                ImageLoader.getInstance().displayImage(dataBean.getImages().get(2).getU(), holder1.image3, options);
                holder1.text.setText(dataBean.getTitle());
                if (theme) {
                    convertView.setBackgroundColor(Color.parseColor("#222222"));
                    holder1.text.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                    holder1.text.setTextColor(Color.parseColor("#222222"));
                }
                break;
            case 1:
                ViewHolder2 holder2;
                if (convertView == null) {
                    convertView = View.inflate(context, R.layout.news_item2, null);
                    holder2 = new ViewHolder2();
                    holder2.image1 = convertView.findViewById(R.id.news_image1);
                    holder2.text1 = convertView.findViewById(R.id.news_text1);
                    holder2.text2 = convertView.findViewById(R.id.news_text2);
                    holder2.time = convertView.findViewById(R.id.time);
                    convertView.setTag(holder2);
                } else {
                    holder2 = (ViewHolder2) convertView.getTag();
                }
                NewsBean.ResultBean.DataBean bean = data.get(position);
                holder2.text1.setText(bean.getTitle());
                holder2.text2.setText(bean.getIntro());
                holder2.time.setText(bean.getMedia_name() + "" + stampToTime(Long.parseLong(bean.getCtime())));
                if (bean.getImages() != null && bean.getImages().size() > 0) {
                    ImageLoader.getInstance().displayImage(bean.getImages().get(0).getU(), holder2.image1, options);
                } else {
                    holder2.image1.setImageResource(R.mipmap.logo);
                }
                if (theme) {
                    convertView.setBackgroundColor(Color.parseColor("#222222"));
                    holder2.text1.setTextColor(Color.parseColor("#ffffff"));
                    holder2.text2.setTextColor(Color.parseColor("#ffffff"));
                    holder2.time.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    convertView.setBackgroundColor(Color.parseColor("#ffffff"));
                    holder2.text1.setTextColor(Color.parseColor("#222222"));
                    holder2.text2.setTextColor(Color.parseColor("#222222"));
                    holder2.time.setTextColor(Color.parseColor("#222222"));
                }
                break;
        }
        return convertView;
    }

    private boolean theme;

    public void setTheme(boolean theme) {
        this.theme = theme;
        notifyDataSetChanged();
    }

    public class ViewHolder1 {
        public ImageView image1;
        public ImageView image2;
        public ImageView image3;
        public TextView text;
    }

    public class ViewHolder2 {
        public ImageView image1;
        public TextView text2;
        public TextView time;
        public TextView text1;
    }

    public String stampToTime(long stamp) {
        String time;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(stamp * 1000);
        time = simpleDateFormat.format(date);
        return time;
    }
}
