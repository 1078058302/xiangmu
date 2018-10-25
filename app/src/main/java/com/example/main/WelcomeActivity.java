package com.example.main;

import android.content.Intent;
import android.media.Image;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.MainActivity;
import com.example.R;
import com.example.Utils.SharedPreUtils;
import com.example.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {


    private ViewPager viewPager;
    private int[] imageArr = {
            R.drawable.welcome_1,
            R.drawable.welcome_2,
            R.drawable.welcome_3
    };
    private MAdapter mAdapter;
    private LinearLayout linearLayout;

    @Override
    public void setTheme(boolean openBool) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initData() {
        showHideBar(false);
        viewPager = findViewById(R.id.vp);
        mAdapter = new MAdapter();
        viewPager.setAdapter(mAdapter);
        SharedPreUtils.put(this, "boolean", true);
        linearLayout = findViewById(R.id.point_layout);
        showPoint(0);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                showPoint(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void showPoint(int position) {
        linearLayout.removeAllViews();
        for (int i = 0; i < imageArr.length; i++) {
            ImageView imageView = new ImageView(this);
            if (position == i) {
                imageView.setBackgroundResource(R.drawable.point);
            } else {
                imageView.setBackgroundResource(R.drawable.point_e8);
            }
            linearLayout.addView(imageView);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = 16;
            layoutParams.height = 16;
            layoutParams.leftMargin = 5;
            imageView.setLayoutParams(layoutParams);
        }
    }

    private class MAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageArr.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = View.inflate(WelcomeActivity.this, R.layout.welcome_page, null);
            ImageView imageView = v.findViewById(R.id.welcom_img);
            imageView.setImageResource(imageArr[position]);
            ImageView image = v.findViewById(R.id.image);
            Button tiyan = v.findViewById(R.id.tiyan);

            if (position == imageArr.length - 1) {
                image.setVisibility(View.GONE);
                tiyan.setVisibility(View.VISIBLE);
                tiyan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                });
            } else {
                image.setVisibility(View.VISIBLE);
                tiyan.setVisibility(View.GONE);
            }

            container.addView(v);
            return v;
        }
    }
}
