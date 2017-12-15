package com.cdkj.ethereumdemo.main;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.BaseApplication;
import com.cdkj.baselibrary.utils.LogUtil;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lei on 2017/10/20.
 */

public class MyApplication extends Application {

    public static Application application;

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        BaseApplication.initialize(this);

        EventBus.builder().throwSubscriberException(LogUtil.isLog).installDefaultEventBus();
        if (true) {
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(application); // 尽可能早，推荐在Application中初始化

        initZXing();

    }

    private void initZXing() {
        ZXingLibrary.initDisplayOpinion(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
