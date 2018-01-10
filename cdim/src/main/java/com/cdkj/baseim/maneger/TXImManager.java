package com.cdkj.baseim.maneger;

import android.text.TextUtils;

import com.cdkj.baselibrary.BaseApplication;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.utils.LogUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMLogLevel;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMSdkConfig;
import com.tencent.qalsdk.sdk.MsfSdkUtils;

/**
 * 腾讯云Im管理
 * Created by cdkj on 2017/10/27.
 */

public class TXImManager {

    private static TXImManager instance = null;

    private static boolean isInit;//用于判断有没有初始化 防止多次初始化

    private TXImManager() {
    }

    public static TXImManager getInstance() {
        if (instance == null) {
            instance = new TXImManager();
        }
        return instance;
    }


    /**
     * SDK初始化
     *
     * @param appid
     */
    public void init(int appid) {
        if (isInit) {
            return;
        }
        if (MsfSdkUtils.isMainProcess(BaseApplication.getContext())) {
            //初始化SDK基本配置
            TIMSdkConfig config = new TIMSdkConfig(appid)
                    .enableCrashReport(false)//是否开启错误上报
                    .enableLogPrint(MyConfig.IS_DEBUG)//是否开启日志
                    .setLogLevel(MyConfig.IS_DEBUG ? TIMLogLevel.ERROR : TIMLogLevel.OFF);

            //初始化SDK
            TIMManager.getInstance().init(BaseApplication.getContext(), config);
            isInit = true;
        }

    }

    /**
     * 用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        if (instance == null) return false;
        return !TextUtils.isEmpty(TIMManager.getInstance().getLoginUser());
    }

    /**
     * 登录
     *
     * @param id
     * @param sig
     * @param loCallBack
     */
    public void login(String id, String sig, final LoginBallBack loCallBack) {
        TIMManager.getInstance().login(id, sig, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                LogUtil.E("腾讯云登录失败" + i + s);
                if (loCallBack == null) return;

                switch (i) {
                    case 6208:
                        //离线状态下被其他终端踢下线
                        loCallBack.onError(i, "您的账号已在其他终端登录,请重新登录");
                        break;
                    case 6200:
                        loCallBack.onError(i, "暂无网络");
                        break;
                    default:
                        loCallBack.onError(i, "登录失败");
                        break;
                }

            }

            @Override
            public void onSuccess() {
                LogUtil.E("腾讯云登录成功");
                //注册小米推送
//                if (DeviceHelper.isXiaomi() || SystemUtils.isMIUI()) {//判断是不是小米设备
//                    if (MsfSdkUtils.isMainProcess(BaseApplication.getContext())) {
//                        Log.d("MyApplication", "main process");
//                        MiPushClient.registerPush(BaseApplication.getContext(), "2882303761517633048", "5201763375048");
//                    }
//                }

                //全局推送开启离线推送
//                TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
//                settings.setEnabled(true);
//                TIMManager.getInstance().setOfflinePushSettings(settings);

                if (loCallBack == null) return;
                loCallBack.onSuccess();
            }
        });
    }


    /**
     * 设置昵称
     *
     * @param name               要修改的姓名
     * @param changeInfoBallBack
     */
    public void setUserNickName(String name, final changeInfoBallBack changeInfoBallBack) {
        if (!isLogin()) {
            if (changeInfoBallBack != null) {
                changeInfoBallBack.onError(0, "");
            }
            return;
        }
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setNickname(name);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                LogUtil.E("modifyProfile failed: " + code + " desc" + desc);
                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onError(code, desc);
                }
            }

            @Override
            public void onSuccess() {
                LogUtil.E("modifyProfile succ");

                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onSuccess();
                }
            }
        });
    }

    /**
     * 设置昵称
     *
     * @param changeInfoBallBack
     */
    public void setUserLogo(String url, final changeInfoBallBack changeInfoBallBack) {
        if (!isLogin()) {
            if (changeInfoBallBack != null) {
                changeInfoBallBack.onError(0, "");
            }
            return;
        }
        TIMFriendshipManager.ModifyUserProfileParam param = new TIMFriendshipManager.ModifyUserProfileParam();
        param.setFaceUrl(url);

        TIMFriendshipManager.getInstance().modifyProfile(param, new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                //错误码code和错误描述desc，可用于定位请求失败原因
                //错误码code列表请参见错误码表
                LogUtil.E("modifyProfile failed: " + code + " desc" + desc);
                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onError(code, desc);
                }
            }

            @Override
            public void onSuccess() {
                LogUtil.E("modifyProfile succ");

                if (changeInfoBallBack != null) {
                    changeInfoBallBack.onSuccess();
                }
            }
        });
    }


    /**
     * 登出imsdk
     *
     * @param callBack 登出后回调
     */
    public void logout(TIMCallBack callBack) {
        if (!isLogin()) {
            callBack.onSuccess();
            return;
        }
        try {
            TIMManager.getInstance().logout(callBack);
        } catch (Exception e) {

        }

    }

    /**
     * 登出imsdk
     *
     * @param
     */
    public void logout() {
        if (!isLogin()) return;
        try {
            TIMManager.getInstance().logout(new TIMCallBack() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess() {

                }
            });
        } catch (Exception e) {

        }
    }


    public interface LoginBallBack {

        void onError(int i, String s);

        void onSuccess();
    }

    public interface changeInfoBallBack {

        void onError(int i, String s);

        void onSuccess();
    }

}
