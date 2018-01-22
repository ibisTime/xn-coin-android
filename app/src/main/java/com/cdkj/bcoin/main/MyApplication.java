package com.cdkj.bcoin.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.BaseApplication;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.bcoin.R;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMOfflinePushListener;
import com.tencent.imsdk.TIMOfflinePushNotification;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zopim.android.sdk.api.ZopimChat;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lei on 2017/10/20.
 */

public class MyApplication extends Application {

    private final int pushId = 1;

    public static Application application;



    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        BaseApplication.initialize(this);

        EventBus.builder().throwSubscriberException(LogUtil.isLog).installDefaultEventBus();
        if (MyConfig.IS_DEBUG) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化

        initZXing();
        initZenDesk();

        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    Log.e("setOfflinePushListener","setOfflinePushListener");
                    Log.e("getTitle",notification.getTitle());
                    Log.e("getContent",notification.getContent());
                    Log.e("getConversationId",notification.getConversationId());

                    notification.doNotify(getApplicationContext(), R.mipmap.app_icon);
                    notification.setTitle("Bcoin");
                }

            });
        }

//        if(MsfSdkUtils.isMainProcess(this)) {
//
//            TIMManager.getInstance().setOfflinePushListener(notification -> {
//
//
//                Log.e("getConversationType",notification.getConversationType()+"");
//
//                if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                    //消息被设置为需要提醒
//                    notification.doNotify(getApplicationContext(), R.mipmap.app_icon);
//                    notification.setTitle(StringUtil.getString(R.string.app_name));

//                    Intent notificationIntent = new Intent(BaseApplication.getContext(), TxImPushActivity.class);
//                    notificationIntent.putExtra("toUserId", notification.getConversationId());
//                    // 状态消息打开type
//                    notificationIntent.putExtra("openType", TxImPushActivity.OPEN_TYPE_ORDER);
//
//                    NotificationManager mNotificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(BaseApplication.getContext().NOTIFICATION_SERVICE);
//                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(BaseApplication.getContext());
//
//                    notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
//                            | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    PendingIntent intent = PendingIntent.getActivity(BaseApplication.getContext(), 0,
//                            notificationIntent, 0);
//
//                    mBuilder.setContentTitle("倍可盈")//设置通知栏标题
//                            .setContentText(notification.getContent())
//                            .setContentIntent(intent) //设置通知栏点击意图
////                            .setNumber(++pushNum) //设置通知集合的数量
////                            .setTicker(senderNick + ":" + contentStr) //通知首次出现在通知栏，带上升动画效果的
//                            .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//                            .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//                            .setSmallIcon(com.cdkj.baseim.R.mipmap.app_icon);//设置通知小ICON
//                    Notification notify = mBuilder.build();
//                    notify.flags |= Notification.FLAG_AUTO_CANCEL;
//                    mNotificationManager.notify(pushId, notify);
//                }
//            });
//        }

    }

    public void initZenDesk(){
        ZendeskConfig.INSTANCE.init(this,
                "https://beicoin.zendesk.com",
                "f9ab448e1dfdb93e3b4ff1f2c2d4fb3a72140cbfd6ee10e0",
                "mobile_sdk_client_b388fa777945f99314b7");

        // 登录时设置Identity
//        Identity identity = new AnonymousIdentity.Builder().build();
//        ZendeskConfig.INSTANCE.setIdentity(identity);

        ZopimChat.init("MvxwoT6827HylJtr6360QQQ5yve4Z2Ny");
    }

    private void initZXing() {
        ZXingLibrary.initDisplayOpinion(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getInstance(){
        return application;
    }

}
