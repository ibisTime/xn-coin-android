package com.cdkj.baseim.activity;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baseim.R;
import com.cdkj.baseim.api.MyApiServer;
import com.cdkj.baseim.databinding.ActivityTxLogingBinding;
import com.cdkj.baseim.maneger.TXImManager;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baseim.model.TencentSignModel;
import com.cdkj.baseim.util.PushUtil;
import com.cdkj.baselibrary.api.BaseApiServer;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 腾讯im聊天登录
 * Created by cdkj on 2017/10/30.
 */

public class TxImLogingActivity extends AbsBaseActivity {

    public static final String CHAT = "chat";
    public static final String DEAL = "deal";

    // 待下单订单
    public static final String ORDER_DNS_NEW = "order_dns_new";
    public static final String ORDER_NEW = "order_new";

    public static final String ORDER_DNS_DONE = "order_dns_done";
    public static final String ORDER_DONE = "order_done";

    private String openFlag;
    private String orderCode;
    private boolean canOpenMain;
    private boolean isStartChat;

    private ActivityTxLogingBinding binding;
    private ImUserInfo imUserInfo;


    /**
     * @param context
     * @param canOpenMain 如果未登录，登录后能否打开主页
     * @param isStartChat 是否启动聊天
     */
    public static void open(Activity context, ImUserInfo imUserInfo, boolean canOpenMain, boolean isStartChat, String openFlag) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, TxImLogingActivity.class);
        intent.putExtra("openFlag", openFlag);
        intent.putExtra("canOpenMain", canOpenMain);
        intent.putExtra("isStartChat", isStartChat);
        intent.putExtra("imUserInfo", imUserInfo);
        context.startActivity(intent);
        context.overridePendingTransition(0, 0);
    }


    @Override
    public View addMainView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_tx_loging, null, false);
        return binding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTitle("");
        if (getIntent() != null) {
            openFlag = getIntent().getStringExtra("openFlag");
            orderCode = getIntent().getStringExtra("orderCode");
            canOpenMain = getIntent().getBooleanExtra("canOpenMain", false);
            isStartChat = getIntent().getBooleanExtra("isStartChat", false);
            imUserInfo = getIntent().getParcelableExtra("imUserInfo");
        }

        if (!SPUtilHelper.isLoginNoStart()) { //如果没有登录的话先登录
            // 路由跳转登录页面
            ARouter.getInstance().build("/user/login")
                    .withBoolean("canOpenMain",canOpenMain)
                    .navigation();
            finishNoTransition();
        } else if (TXImManager.getInstance().isLogin()) {//如果腾讯云已经登录 直接打开聊天界面
            Log.e("afterCreate",openFlag);
            switch (openFlag){
                case CHAT:
                    ChatC2CActivity.open(TxImLogingActivity.this, imUserInfo);
                    break;

                case DEAL:
                    imUserInfo.setEventTag(DEAL);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_NEW:
                    imUserInfo.setEventTag(ORDER_NEW);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_DNS_NEW:
                    imUserInfo.setEventTag(ORDER_DNS_NEW);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_DONE:
                    imUserInfo.setEventTag(ORDER_DONE);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_DNS_DONE:
                    imUserInfo.setEventTag(ORDER_DNS_DONE);
                    EventBus.getDefault().post(imUserInfo);
                    break;
            }
            finishNoTransition();
        } else {
            getUserInfoRequest(false); //登录--> 获取用户信息 -->获取腾讯签名-->登录腾讯--->登录成功
        }


    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        Map<String, String> map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(BaseApiServer.class).getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
                SPUtilHelper.saveTradePwdFlag(data.isTradepwdFlag());
                SPUtilHelper.saveUserPhoneNum(data.getMobile());
                SPUtilHelper.saveRealName(data.getRealName());
                SPUtilHelper.saveUserName(data.getNickname());
                SPUtilHelper.saveUserPhoto(data.getPhoto());
                getTxKeyRequest();
            }

            @Override
            protected void onNoNet(String msg) {
                disMissLoading();
                finishNoTransition();
            }

            @Override
            protected void onNull() {
                disMissLoading();
                finishNoTransition();
            }

            @Override
            protected void onFinish() {
                if (isShowdialog) disMissLoading();
            }
        });
    }


    /**
     * 获取腾讯签名
     */
    public void getTxKeyRequest() {


        Map map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call<BaseResponseModel<TencentSignModel>> call = RetrofitUtils.createApi(MyApiServer.class).getTencentSign("625000", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<TencentSignModel>(this) {
            @Override
            protected void onSuccess(TencentSignModel data, String SucMessage) {

                TXImManager.getInstance().init(Integer.parseInt(data.getTxAppCode()));

                TXImManager.getInstance().login(SPUtilHelper.getUserId(), data.getSign(), new TXImManager.LoginBallBack() {
                    @Override
                    public void onError(int i, String s) {
                        Log.e("onError","onError");
                        ToastUtil.show(TxImLogingActivity.this, s);
                        disMissLoading();
                        finishNoTransition();
                        //初始化程序后台后消息推送
                        PushUtil.getInstance();
                    }

                    @Override
                    public void onSuccess() {
                        Log.e("onSuccess","onSuccess");
                        txLoginSucc();
                    }
                });
            }

            @Override
            protected void onNoNet(String msg) {
                disMissLoading();
                finishNoTransition();
            }

            @Override
            protected void onNull() {
                disMissLoading();
                finishNoTransition();
            }


            @Override
            protected void onFinish() {
                disMissLoading();
                finishNoTransition();
            }
        });
    }

    private void txLoginSucc() {
        Log.e("txLoginSucc","txLoginSucc");
        TXImManager.getInstance().setUserNickName(SPUtilHelper.getUserName(), new TXImManager.changeInfoBallBack() {
            @Override
            public void onError(int i, String s) {
                setLogo();
            }

            @Override
            public void onSuccess() {
                setLogo();
            }
        });
    }

    private void setLogo() {
        Log.e("setLogo","setLogo");
        TXImManager.getInstance().setUserLogo(SPUtilHelper.getUserPhoto(), new TXImManager.changeInfoBallBack() {
            @Override
            public void onError(int i, String s) {
                startChat();
            }

            @Override
            public void onSuccess() {
                startChat();
            }
        });
    }

    /**
     * 启动聊天
     */
    private void startChat() {
        Log.e("startChat",openFlag);
        disMissLoading();
        if (isStartChat) {
            switch (openFlag){
                case CHAT:
                    ChatC2CActivity.open(TxImLogingActivity.this, imUserInfo);
                    break;

                case DEAL:
                    imUserInfo.setEventTag(DEAL);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_NEW:
                    imUserInfo.setEventTag(ORDER_NEW);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_DNS_NEW:
                    imUserInfo.setEventTag(ORDER_DNS_NEW);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_DONE:
                    imUserInfo.setEventTag(ORDER_DONE);
                    EventBus.getDefault().post(imUserInfo);
                    break;

                case ORDER_DNS_DONE:
                    imUserInfo.setEventTag(ORDER_DNS_DONE);
                    EventBus.getDefault().post(imUserInfo);
                    break;
            }

        }
        finishNoTransition();
    }

    private void finishNoTransition() {
        finish();
        overridePendingTransition(0, 0);
    }


    @Override
    public void onBackPressed() {

    }

}
