package com.example.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.example.R;


/**
 * Created by xiaoming.li on 16/11/24.
 * 悬浮窗
 */

public class SuspensionWindowService extends Service {
    private WindowManager wm;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();
    private View rocket;
    private int width_phone, height_phone;
    private int WINDOWLEFT = 111;
    private int WINDOWRIGHT = 112;
    private int PERMISSION = 101;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        width_phone = display.getWidth(); //获取屏幕的宽度
        height_phone = display.getHeight();//获取屏幕的高度
        touch();
    }

    private void touch() {
        int x = width_phone;
        int y = height_phone / 3 * 2;
        // params 默认是居中对齐的，屏幕中心点是0,0点
        params.gravity = Gravity.LEFT + Gravity.TOP; // 改为左上角对齐
        params.x = x;
        params.y = y;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        // 高优先级的窗体，可以显示在锁屏页面之上,但要添加权限
        // android.permission.SYSTEM_ALERT_WINDOW
        if (Build.VERSION.SDK_INT > 25) {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
//        params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
        rocket = View.inflate(this, R.layout.window_service, null);
        regTouch();
        wm.addView(rocket, params);
    }

    private int downX;
    private int downY;

    private void regTouch() {
        rocket.setOnTouchListener(new View.OnTouchListener() {
            private int lastX;
            private int lastY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 按下 事件
                        downX = lastX = (int) event.getRawX();
                        downY = lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:// 移动 事件
                        int disX = (int) (event.getRawX() - lastX);
                        int disY = (int) (event.getRawY() - lastY);
                        params.x += disX;
                        params.y += disY;
                        wm.updateViewLayout(rocket, params);
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:// 抬起 事件
                        int x = (int) event.getRawX();
                        int y = (int) event.getRawY();
                        int upX = x - downX;
                        int upY = y - downY;
                        upX = Math.abs(upX);
                        upY = Math.abs(upY);

                        if (upX < 20 && upY < 20) {
                            //点击进入指定页面
                            mClick();

                        }
                        handler.sendEmptyMessage(WINDOWLEFT);
                        break;
                }
                return true;
            }
        });
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int type = msg.what;
            if (type == WINDOWLEFT) {
                params.x = width_phone;
                wm.updateViewLayout(rocket, params);
            } else if (type == WINDOWRIGHT) {
                params.x = 0;
                wm.updateViewLayout(rocket, params);
            }


        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        wm.removeView(rocket);

    }


    private void mClick() {
        Intent intent = new Intent("com.abner.ming.mian");
        sendBroadcast(intent);
    }


}
/*

 private Intent intentService;
    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {
                toast("请找到时时头条应用，把权限打开");
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent, 1);
            } else {
                //TODO do something you need
                intentService = new Intent(this, SuspensionWindowService.class);
                startService(intentService);
            }
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                //判断是否需要 向用户解释，为什么要申请该权限
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (intentService != null) {
            stopService(intentService);
        }

    }



* */