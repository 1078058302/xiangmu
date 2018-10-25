package com.example;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.common.io.CharStreams;
import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpHelper {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = (String) msg.obj;
            listener.success(s);
        }
    };
    private HttpListener listener;

    public void result(HttpListener listener) {
        this.listener = listener;
    }

    public interface HttpListener {
        void success(String data);

        void fail();
    }

    public HttpHelper() {

    }

    public HttpHelper get(String url) {
        doHttp(url, "GET");
        return this;
    }

    public HttpHelper post(String url) {
        doHttp(url, "POST");
        return this;
    }


    private void doHttp(final String url, final String method) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL mUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) mUrl.openConnection();
                    connection.setRequestMethod(method);
                    connection.setConnectTimeout(3000);
                    int code = connection.getResponseCode();
                    if (code == HttpURLConnection.HTTP_OK) {
                        Message message = Message.obtain();
                        InputStream is = connection.getInputStream();
                        String s = CharStreams.toString(new InputStreamReader(is));
                        message.obj = s;
                        handler.sendMessage(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }
}
