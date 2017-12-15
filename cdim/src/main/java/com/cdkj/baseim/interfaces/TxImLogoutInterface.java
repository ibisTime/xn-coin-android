package com.cdkj.baseim.interfaces;

/**
 * Created by lq on 2017/11/27.
 */

public interface TxImLogoutInterface {

    void emptyLoginUser(); // 当前没有用户登陆腾讯云

    void onError(int code, String desc); // 登出失败

    void onSuccess(); // 登出成功

}
