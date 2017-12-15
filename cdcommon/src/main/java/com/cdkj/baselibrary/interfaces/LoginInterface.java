package com.cdkj.baselibrary.interfaces;

import com.cdkj.baselibrary.model.UserLoginModel;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public interface LoginInterface {

    void LoginSuccess(UserLoginModel user, String msg);    //登录成功

    void LoginFailed(String code, String msg);   //登录失败

    void StartLogin();   //开始登录

    void EndLogin();   //结束登录


}
