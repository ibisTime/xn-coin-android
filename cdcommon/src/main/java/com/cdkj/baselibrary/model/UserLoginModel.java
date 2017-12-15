package com.cdkj.baselibrary.model;

/**
 * Created by 李先俊 on 2017/6/9.
 */

public class UserLoginModel {

    private String userId;

    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
