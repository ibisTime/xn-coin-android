package com.cdkj.bcoin.util;


import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.huawei.android.pushagent.api.PushEventReceiver;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushToken;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * 华为推送接收
 */
public class HwPushMessageReceiver extends PushEventReceiver {
    private final String TAG = "HwPushMessageReceiver";

    private long mBussId = 3122;

    @Override
    public void onToken(Context context, String token, Bundle extras){
        String belongId = extras.getString("belongId");
        String content = "获取token和belongId成功，token = " + token + ",belongId = " + belongId;
        Log.e(TAG, content);
        TIMOfflinePushToken param = new TIMOfflinePushToken(mBussId, token);
        TIMManager.getInstance().setOfflinePushToken(param, null);
    }


    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
            String content = "收到一条Push消息： " + new String(msg, "UTF-8");
            Log.e(TAG, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onEvent(Context context, Event event, Bundle extras) {
        Log.e(TAG, extras.toString());

        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                manager.cancel(notifyId);
            }
            String content = "收到通知附加消息： " + extras.getString(BOUND_KEY.pushMsgKey);

            try {
                JSONArray j = new JSONArray(extras.getString(BOUND_KEY.pushMsgKey));
                String ext = j.getJSONObject(0).getString("ext");

                if (ext==null)
                    return;
                SPUtilHelper.savePushOrder(ext);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e(TAG, content);

        } else if (Event.PLUGINRSP.equals(event)) {
            final int TYPE_LBS = 1;
            final int TYPE_TAG = 2;
            int reportType = extras.getInt(BOUND_KEY.PLUGINREPORTTYPE, -1);
            boolean isSuccess = extras.getBoolean(BOUND_KEY.PLUGINREPORTRESULT, false);
            String message = "";
            if (TYPE_LBS == reportType) {
                message = "LBS report result :";
            } else if(TYPE_TAG == reportType) {
                message = "TAG report result :";
            }
            Log.e(TAG, message + isSuccess);
        }
        super.onEvent(context, event, extras);
    }
}
