package com.cdkj.bcoin.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baseim.util.Foreground;
import com.cdkj.baselibrary.BaseApplication;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.bcoin.R;
import com.tencent.imsdk.TIMManager;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.zendesk.sdk.network.impl.ZendeskConfig;
import com.zopim.android.sdk.api.ZopimChat;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

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
        initLitePal();
        initZenDesk();

        Foreground.init(this);
        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(notification -> {

                notification.doNotify(getApplicationContext(), R.mipmap.app_icon);
                notification.setTitle("Bcoin");
            });
        }

    }

    private void initLitePal() {
        LitePal.initialize(this);
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
