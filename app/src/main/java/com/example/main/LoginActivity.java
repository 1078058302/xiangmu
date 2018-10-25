package com.example.main;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.example.HttpHelper;
import com.example.R;
import com.example.Utils.NetworkUtils;
import com.example.Utils.SharedPreUtils;
import com.example.base.BaseActivity;
import com.example.bean.LoginBean;
import com.google.gson.Gson;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class LoginActivity extends BaseActivity {


    private EditText user_login;
    private EditText user_pass;

    @Override
    public void setTheme(boolean openBool) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        boolean b = NetworkUtils.isConnected(this);
        setTitles("登录");
        user_login = findViewById(R.id.user_login);
        user_pass = findViewById(R.id.user_pass);
        findViewById(R.id.go_login).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });
    }

    private void Login() {
        String user = user_login.getText().toString().trim();
        String pass = user_pass.getText().toString().trim();
        if (TextUtils.isEmpty(user)) {
            toast("请输入邮箱账号");
            return;
        }
//        if (!DataValidator.isEmail(user)) {
//            toast("请输入正确的邮箱账号");
//            return;
//        }
        if (TextUtils.isEmpty(pass)) {
            toast("请输入密码");
            return;
        }
        pass = stringToMD5(pass);
        String url = "http://www.vipandroid.cn/cert/LoginUser.php?user_name=" + user + "&user_pass=" + pass;
        new HttpHelper().get(url).result(new HttpHelper.HttpListener() {
            @Override
            public void success(String data) {
                LoginBean loginBean = new Gson().fromJson(data, LoginBean.class);
                if (0 == loginBean.getStaus()) {
                    SharedPreUtils.put(LoginActivity.this, "user_name", loginBean.getUser_name());
                    SharedPreUtils.put(LoginActivity.this, "user_pass", loginBean.getUser_pass());
                    SharedPreUtils.put(LoginActivity.this, "user_nickname", loginBean.getUser_nickname());
                    SharedPreUtils.put(LoginActivity.this, "user_for", loginBean.getUser_for());
                } else {
                    toast("账号或密码输入错误");
                }
                finish();

            }

            @Override
            public void fail() {
                toast("请求失败");
            }
        });
    }

    public String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个md5算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }

}

