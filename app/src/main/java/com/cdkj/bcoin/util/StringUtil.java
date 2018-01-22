package com.cdkj.bcoin.util;

import com.cdkj.bcoin.main.MyApplication;

/**
 * Created by lei on 2017/11/25.
 */

public class StringUtil {

    public static String getString(int resources){
        return MyApplication.application.getString(resources);
    }
}
