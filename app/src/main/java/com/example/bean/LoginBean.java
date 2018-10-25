package com.example.bean;

public class LoginBean {

    private String user_nickname;
    private String user_name;
    private String user_pass;
    private String user_for;
    private int staus;

    public int getStaus() {
        return staus;
    }

    public void setStaus(int staus) {
        this.staus = staus;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pass() {
        return user_pass;
    }

    public void setUser_pass(String user_pass) {
        this.user_pass = user_pass;
    }

    public String getUser_for() {
        return user_for;
    }

    public void setUser_for(String user_for) {
        this.user_for = user_for;
    }
}
