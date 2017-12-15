package com.cdkj.baselibrary.utils;

import android.util.Log;


/**
 * log工具类
 */
public class LogUtil {
    public static Boolean isLog = true;

    private static final String TAG = "LOG_YITU";

    public static void I(String msg) {
        if (isLog) {
            Log.i(TAG, msg);
        }
    }

    public static void E(String msg) {
        if (isLog) {
            Log.e(TAG, msg);
        }
    }

    public static void BIGLOG(String responseInfo) {
        if (!isLog) {
            return;
        }
        if (responseInfo.length() > 4000) {
            int chunkCount = responseInfo.length() / 4000;
            for (int i = 0; i <= chunkCount; i++) {
                int max = 4000 * (i + 1);
                if (max >= responseInfo.length()) {
                    Log.e(TAG, responseInfo.substring(4000 * i));
                } else {
                    Log.e(TAG, responseInfo.substring(4000 * i, max));
                }
            }
        } else {
            Log.e(TAG, responseInfo.toString());
        }
    }
}