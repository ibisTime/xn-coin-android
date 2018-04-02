package com.cdkj.bcoin.user.login;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baseim.event.GroupEvent;
import com.cdkj.baseim.event.MessageEvent;
import com.cdkj.baseim.event.RefreshEvent;
import com.cdkj.baseim.interfaces.TxImLoginInterface;
import com.cdkj.baseim.interfaces.TxImLoginPresenter;
import com.cdkj.baseim.maneger.TXImManager;
import com.cdkj.baseim.util.PushUtil;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.model.BaseCoinModel;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
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
import org.litepal.crud.DataSupport;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;

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

        clearNotification();

        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity
        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        setContentView(R.layout.activity_start);

//        getQiniu();

        getCoinList();
    }

    private void open(){

        mSubscription.add(Observable.timer(0, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {//延迟两秒进行跳转
                    initTencent();

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

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                MyConfig.IMGURL = "http://"+data.getCvalue()+"/";

                open();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void getCoinList(){
        Map<String, String> map = new HashMap<>();
        map.put("type", "");
        map.put("ename", "");
        map.put("cname", "");
        map.put("symbol", "");
        map.put("status", "0"); // 0已发布，1已撤下
        map.put("contractAddress", "");

        Call call = RetrofitUtils.createApi(MyApi.class).getCoinList("802267", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseListCallBack<BaseCoinModel>(this) {

            @Override
            protected void onSuccess(List<BaseCoinModel> data, String SucMessage) {
                if (data == null)
                    return;

                // 如果数据库已有数据，清空重新加载
                if(DataSupport.isExist(BaseCoinModel.class))
                    DataSupport.deleteAll(BaseCoinModel.class);

                // 初始化交易界面默认所选择的币
                data.get(0).setChoose(true);
                DataSupport.saveAll(data);

                open();
            }

            @Override
            protected void onReqFailure(int errorCode, String errorMessage) {
                super.onReqFailure(errorCode, errorMessage);

                // 如果数据库已有数据，直接加载数据库
                if(DataSupport.isExist(BaseCoinModel.class)){
                    open();
                }else {
                    ToastUtil.show(StartActivity.this,"无法连接服务器，请检查网络");
                }

            }

            @Override
            protected void onFinish() {
            }
        });
    }


    /**
     * 设置腾讯云监听,登录腾讯云
     */
    public void initTencent(){
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig()
                .setUserStatusListener(new TIMUserStatusListener() {
                    @Override
                    public void onForceOffline() {
                        //被其他终端踢下线
                        KickActivity.open(StartActivity.this);
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


        if (!SPUtilHelper.isLoginNoStart()) {
            MainActivity.open(this);
            finish();
        }else { //如果已经登录，判断腾讯云是否登录
            if (TXImManager.getInstance().isLogin()) { //如果腾讯云已经登录 直接打开主页
                MainActivity.open(this);
                finish();
            }else {
                loginTencent();
            }
        }

    }

    /**
     * 登录腾讯云
     */
    private void loginTencent() {
        // 登录腾讯云
        txImLoginPresenter = new TxImLoginPresenter(this);
        txImLoginPresenter.login(this);
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

    @Override
    public void onError(int i, String s) {
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
//                NotifyDialog dialog = new NotifyDialog();
//                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), (dialog1, which) -> initTencent());

                // 重新登录
                loginTencent();
                break;

            case 6200:
                showToast(getString(R.string.login_error_timeout));
                SignInActivity.open(this,true);
                StartActivity.this.finish();
                break;

            case 6000:
                // 获取腾讯云签名失败，去到主页，打开聊天时再登录腾讯云
                MainActivity.open(this);
                StartActivity.this.finish();
                break;

            default:
                // 腾讯云登录失败，提醒用户重新登录
                SignInActivity.open(this,true);
                StartActivity.this.finish();
                break;
        }
    }

    @Override
    public void onFinish() {
    }

    /**
     * 清楚所有通知栏通知
     */
    private void clearNotification(){
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        MiPushClient.clearNotification(getApplicationContext());
    }

}
