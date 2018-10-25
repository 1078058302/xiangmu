package com.example.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class BaseListAdapter extends BaseAdapter {
    private String[] baseDesc;
    private int[] imageArr;
    private Context context;

    public BaseListAdapter(Context context, String[] baseDesc, int[] imageArr) {
        this.context = context;
        this.baseDesc = baseDesc;
        this.imageArr = imageArr;
    }

    @Override
    public int getCount() {
        return imageArr.length;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.base_list_item, null);
            holder = new ViewHolder();
            holder.imageView = convertView.findViewById(R.id.image_base);
            holder.textView = convertView.findViewById(R.id.base_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.imageView.setImageResource(imageArr[position]);
        holder.textView.setText(baseDesc[position]);
        return convertView;
    }

    class ViewHolder {
        public ImageView imageView;
        public TextView textView;
    }
}
