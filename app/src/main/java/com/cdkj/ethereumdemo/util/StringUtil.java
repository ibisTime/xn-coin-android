package com.cdkj.ethereumdemo.util;

import com.cdkj.ethereumdemo.main.MyApplication;

/**
 * Created by lei on 2017/11/25.
 */

public class StringUtil {

    public static String getStirng(int resources){
        return MyApplication.application.getString(resources);
    }
}
