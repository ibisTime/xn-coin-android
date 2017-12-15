package com.cdkj.baselibrary.interfaces;

import com.cdkj.baselibrary.model.UserLoginModel;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public interface SendCodeInterface {

    void CodeSuccess(String msg);    //成功

    void CodeFailed(String code, String msg);   //失败

    void StartSend();   //开始

    void EndSend();   //结束


}
