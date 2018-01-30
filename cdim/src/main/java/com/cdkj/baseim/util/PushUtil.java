package com.cdkj.baseim.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.cdkj.baseim.R;
import com.cdkj.baseim.activity.TxImPushActivity;
import com.cdkj.baseim.event.MessageEvent;
import com.cdkj.baseim.model.CustomMessage;
import com.cdkj.baseim.model.Message;
import com.cdkj.baseim.model.MessageFactory;
import com.cdkj.baselibrary.BaseApplication;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMMessage;

import org.greenrobot.eventbus.EventBus;

import java.net.URLDecoder;
import java.util.Observable;
import java.util.Observer;

/**
 * 在线消息通知展示
 */

public class PushUtil implements Observer {

    private static final String TAG = PushUtil.class.getSimpleName();

    private static int pushNum = 0;

    private final int pushId = 1;

    private static PushUtil instance = new PushUtil();

    private PushUtil() {
        MessageEvent.getInstance().addObserver(this);
    }

    public static PushUtil getInstance() {
        return instance;
    }


    private void PushNotify(TIMMessage msg) {

        String peer;

        Log.e("msg == null",(msg == null)+"");
        Log.e("isForeground()",(Foreground.get().isForeground())+"");
        Log.e("Group",(msg.getConversation().getType() != TIMConversationType.Group)+"");
        Log.e("C2C",(msg.getConversation().getType() != TIMConversationType.C2C)+"");
        Log.e("isSelf",(msg.isSelf())+"");
        Log.e("getRecvFlag",(msg.getRecvFlag() == TIMGroupReceiveMessageOpt.ReceiveNotNotify)+"");
        Log.e("getMessage",(MessageFactory.getMessage(msg) instanceof CustomMessage)+"");

//        if (Foreground.get().isForeground()){
//            String senderStr;
//
//            Message message = MessageFactory.getMessage(msg);
//
//            if (message == null)
//                return;
//
//            senderStr = message.getSender();
//
//            if (senderStr.equals("admin")){ // 系统消息
//                // 更新订单详情数据
//                EventBus.getDefault().post(EventTags.IM_MSG_UPDATE_ORDER);
//            }else {
//                // 更新订单列表消息状态
//                EventBus.getDefault().post(EventTags.IM_MSG_UPDATE);
//            }
//        }

        //系统消息，自己发的消息，程序在前台的时候不通知
        if (msg == null ||
                (msg.getConversation().getType() != TIMConversationType.Group &&
                        msg.getConversation().getType() != TIMConversationType.C2C) ||
                msg.isSelf() ||
                MessageFactory.getMessage(msg) instanceof CustomMessage) return;

        //会话Id
        peer = msg.getConversation().getPeer();

        // 聊天界面在前台
        if (ChatForeground.get().isForeground()){

            // 来自当前会话的消息
            if (ChatForeground.get().getIdentify().equals(peer)){
                // 震动提醒
                EventBus.getDefault().post(EventTags.IM_MSG_VIBRATOR);
                return;
            }else {
                notifi(msg);
            }

        }else {
            notifi(msg);
        }

    }

    public void notifi(TIMMessage msg){

        String senderStr, contentStr, senderNick;

        Message message = MessageFactory.getMessage(msg);

        if (message == null)
            return;

        Log.e("getSender", message.getSender());
        Log.e("getSummary", message.getSummary());

        senderStr = message.getSender();

        if (senderStr.equals("admin")){ // 系统消息，需解码
            contentStr = URLDecoder.decode(message.getSummary());
            senderNick = "系统消息";

            // 更新订单详情数据
            EventBus.getDefault().post(EventTags.IM_MSG_UPDATE_ORDER);
        }else {
            contentStr = message.getSummary();
            senderNick = URLDecoder.decode(message.getMessage().getSenderProfile().getNickName());
        }

        // 更新订单列表消息状态
        EventBus.getDefault().post(EventTags.IM_MSG_UPDATE);

        Intent notificationIntent = new Intent(BaseApplication.getContext(), TxImPushActivity.class);
        notificationIntent.putExtra("toUserId", senderStr);
        // 状态消息打开type
        notificationIntent.putExtra("openType", TxImPushActivity.OPEN_TYPE_ORDER);

        NotificationManager mNotificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BaseApplication.getContext());

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(BaseApplication.getContext(), 0,
                notificationIntent, 0);

        Log.e("senderNick", senderNick);
        Log.e("contentStr", contentStr);

        mBuilder.setContentTitle(senderNick)//设置通知栏标题
                .setContentText(contentStr)
                .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(senderNick + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.mipmap.app_icon);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        mNotificationManager.notify(pushId, notify);
    }

    public static void resetPushNum() {
        pushNum = 0;
    }

    public void reset() {
        NotificationManager notificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().NOTIFICATION_SERVICE);
        notificationManager.cancel(pushId);
    }

    /**
     * This method is called if the specified {@code Observable} object's
     * {@code notifyObservers} method is called (because the {@code Observable}
     * object has been updated.
     *
     * @param observable the {@link Observable} object.
     * @param data       the data passed to {@link Observable#notifyObservers(Object)}.
     */
    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof MessageEvent) {
            if (data instanceof TIMMessage) {
                TIMMessage msg = (TIMMessage) data;
                if (msg != null) {
                    PushNotify(msg);
                }
            }
        }
    }
}
