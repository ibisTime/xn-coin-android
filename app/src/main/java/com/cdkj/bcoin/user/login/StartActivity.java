package com.cdkj.bcoin.user.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baseim.api.MyApiServer;
import com.cdkj.baseim.event.GroupEvent;
import com.cdkj.baseim.event.MessageEvent;
import com.cdkj.baseim.event.RefreshEvent;
import com.cdkj.baseim.interfaces.TxImLoginInterface;
import com.cdkj.baseim.interfaces.TxImLoginPresenter;
import com.cdkj.baseim.ui.NotifyDialog;
import com.cdkj.baseim.util.PushUtil;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.main.MainActivity;
import com.cdkj.bcoin.model.SystemParameterModel;
import com.huawei.android.pushagent.PushManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = "/user/start")
public class StartActivity extends BaseActivity implements TxImLoginInterface {

    private TxImLoginPresenter txImLoginPresenter;

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, StartActivity.class));
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("getAPPBuildType()", SPUtilHelper.getAPPBuildType());

        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity

        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {

        }
        setContentView(R.layout.activity_start);

//        getLoginCode();
        open();

//        getQiniu();

    }

    private void open(){

        mSubscription.add(Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {//延迟两秒进行跳转
                    if (!SPUtilHelper.isLoginNoStart()) {
                        MainActivity.open(this);
                        finish();
                    }else {
                        groupEvent();
                    }

                }, Throwable::printStackTrace));
    }

    /**
     * 获取七牛服务器链接
     */
    public void getQiniu() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "qiniu_domain");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("805917", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                MyConfig.IMGURL = data.getDkey();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }


    /**
     * 设置腾讯云监听,登录腾讯云
     */
    public void groupEvent(){
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        showToast("该账号在其他设备登录，请重新登录");
                        SPUtilHelper.logOutClear();
                        EventBus.getDefault().post(EventTags.AllFINISH);

                        SignInActivity.open(StartActivity.this,true);
                        finish();
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        showToast(getString(R.string.tls_expire));
                        SPUtilHelper.logOutClear();
                        EventBus.getDefault().post(EventTags.AllFINISH);

                        SignInActivity.open(StartActivity.this,true);
                        finish();
                    }
                });

        //设置刷新监听
        RefreshEvent.getInstance().init(userConfig);
        userConfig = GroupEvent.getInstance().init(userConfig);
        userConfig = MessageEvent.getInstance().init(userConfig);
        TIMManager.getInstance().setUserConfig(userConfig);

        initTencent();
    }

    /**
     * 登录腾讯云
     */
    private void initTencent() {
        // 登录腾讯云
        txImLoginPresenter = new TxImLoginPresenter(this);
        txImLoginPresenter.login();
    }

    @Override
    public void onError(int i, String s) {
        Log.e("StartActivity", "login error : code " + i + " " + s);
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), (dialog1, which) -> groupEvent());
                break;
            case 6200:
                showToast(getString(R.string.login_error_timeout));
                SignInActivity.open(this,true);
                finish();
                break;
            default:
                showToast(getString(R.string.login_error));
                SignInActivity.open(this,true);
                finish();
                break;
        }
    }

    @Override
    public void onSuccess() {

        MainActivity.open(this);
        finish();

        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String vendor = android.os.Build.MANUFACTURER;
        //注册小米和华为推送
        if(vendor.toLowerCase(Locale.ENGLISH).contains("xiaomi")) {
            //注册小米推送服务
            MiPushClient.registerPush(this, "2882303761517705483", "5941770524483");
        }else if(vendor.toLowerCase(Locale.ENGLISH).contains("huawei")) {

            //请求华为推送设备token
            PushManager.requestToken(this);
        }
    }

    /**
     * 获取登录验证码
     */
    private void getLoginCode() {

        Call<ResponseBody> call = RetrofitUtils.createApi(MyApiServer.class).rhLoginCode(new Date().getTime() + "");

        showLoadingDialog();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("onResponse","onResponse");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure","onFailure");
            }
        });

    }
}
