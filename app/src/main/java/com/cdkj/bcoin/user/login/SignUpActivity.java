package com.cdkj.bcoin.user.login;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cdkj.baseim.event.GroupEvent;
import com.cdkj.baseim.event.MessageEvent;
import com.cdkj.baseim.event.RefreshEvent;
import com.cdkj.baseim.interfaces.TxImLoginInterface;
import com.cdkj.baseim.interfaces.TxImLoginPresenter;
import com.cdkj.baseim.ui.NotifyDialog;
import com.cdkj.baseim.util.PushUtil;
import com.cdkj.baselibrary.activitys.AppBuildTypeActivity;
import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivitySignUpBinding;
import com.cdkj.bcoin.main.MainActivity;
import com.huawei.android.pushagent.PushManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.bcoin.util.ZenDeskUtil.initZenDeskIdentity;

public class SignUpActivity extends AbsBaseActivity implements SendCodeInterface,TxImLoginInterface {

    private boolean agreeState = true;

    private SendPhoneCodePresenter mPresenter;
    private ActivitySignUpBinding mBinding;

    private TxImLoginPresenter txImLoginPresenter;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent= new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return true;
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_up, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_sign_up));
        setSubLeftImgState(true);
        if (MyConfig.IS_DEBUG){
            setSubRightTitleAndClick(getStrRes(R.string.build_type),v -> {
                AppBuildTypeActivity.open(this);
            });
        }

        mPresenter = new SendPhoneCodePresenter(this);

        initListener();
    }

    private void initListener() {

        mBinding.btnSend.setOnClickListener(view -> {
            if (check("code")){
                mPresenter.sendCodeRequest(mBinding.edtMobile.getText().toString().trim(),"805041","C",this);
            }
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check("all")){
                signUp();
            }
        });

        mBinding.llAgree.setOnClickListener(v -> {
            if (agreeState){
                agreeState = false;
                mBinding.ivAgree.setBackgroundResource(R.mipmap.user_sign_unagree);
            }else {
                agreeState = true;
                mBinding.ivAgree.setBackgroundResource(R.mipmap.user_sign_agree);
            }
        });

        mBinding.tvClause.setOnClickListener(v -> {
            WebViewActivity.openkey(this, getStrRes(R.string.user_sign_up_protocol),"reg_protocol");
        });


    }

    private boolean check(String type){
        if (TextUtils.isEmpty(mBinding.edtNick.getText().toString().trim())){
            showToast(getStrRes(R.string.user_nick_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString().trim())){
            showToast(getStrRes(R.string.user_mobile_hint));
            return false;
        }

        if(type.equals("all")){
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString().trim())){
                showToast(getStrRes(R.string.user_code_hint));
                return false;
            }
            if (mBinding.edtCode.getText().toString().trim().length() != 4){
                showToast(getStrRes(R.string.user_code_format_hint));
                return false;
            }

            if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString().trim())){
                showToast(getStrRes(R.string.user_password_hint));
                return false;
            }
            if (TextUtils.isEmpty(mBinding.edtRePassword.getText().toString().trim())){
                showToast(getStrRes(R.string.user_repassword_hint));
                return false;
            }
            if (!mBinding.edtRePassword.getText().toString().trim().equals(mBinding.edtPassword.getText().toString().trim())){
                showToast(getStrRes(R.string.user_repassword_two_hint));
                return false;
            }

            if (!agreeState){
                showToast(getStrRes(R.string.user_protocol_hint));
                return false;
            }
        }



        return true;
    }

    private void signUp(){
        Map<String, Object> map = new HashMap<>();
        map.put("nickname", mBinding.edtNick.getText().toString().trim());
        map.put("kind", "C");
        map.put("userRefereeKind", "C");
        map.put("userReferee", mBinding.edtReferee.getText().toString().trim());
        map.put("mobile", mBinding.edtMobile.getText().toString().trim());
        map.put("loginPwd", mBinding.edtPassword.getText().toString().trim());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString().trim());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).signUp("805041", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserLoginModel>(this) {

            @Override
            protected void onSuccess(UserLoginModel data, String SucMessage) {
                if (!TextUtils.isEmpty(data.getToken()) || !TextUtils.isEmpty(data.getUserId())) {
                    showToast(getStrRes(R.string.user_sign_up_success));


                    SPUtilHelper.saveUserId(data.getUserId());
                    SPUtilHelper.saveUserToken(data.getToken());
                    SPUtilHelper.saveUserName(mBinding.edtNick.getText().toString().trim());
                    SPUtilHelper.saveUserPhoneNum(mBinding.edtMobile.getText().toString().trim());

                    initZenDeskIdentity(SPUtilHelper.getUserName(), SPUtilHelper.getUserEmail());

                    initTencent();

                } else {
                    showToast(getStrRes(R.string.user_sign_up_failure));
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    @Subscribe
    public void changeUi(String tag){
        if (tag == null)
            return;

        if (tag.equals(EventTags.CHANGE_CODE_BTN)){
            mBinding.btnSend.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.corner_sign_btn));
        }
    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));

        //改变ui
        mBinding.btnSend.setBackground(ContextCompat.getDrawable(SignUpActivity.this,R.drawable.corner_sign_btn_gray));
    }

    @Override
    public void CodeFailed(String code, String msg) {
        showToast(msg);
    }

    @Override
    public void StartSend() {
        showLoadingDialog();
    }

    @Override
    public void EndSend() {
        disMissLoading();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
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
        toMianPage();
    }

    private void toMianPage(){
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

        EventBus.getDefault().post(EventTags.AllFINISH);
        EventBus.getDefault().post(EventTags.MAINFINISH);

        MainActivity.open(SignUpActivity.this);
        finish();
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

                        SignInActivity.open(SignUpActivity.this,true);
                        finish();
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        showToast(getString(R.string.tls_expire));
                        SPUtilHelper.logOutClear();
                        EventBus.getDefault().post(EventTags.AllFINISH);

                        SignInActivity.open(SignUpActivity.this,true);
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
}
