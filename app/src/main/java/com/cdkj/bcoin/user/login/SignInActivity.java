package com.cdkj.bcoin.user.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baseim.event.GroupEvent;
import com.cdkj.baseim.event.MessageEvent;
import com.cdkj.baseim.event.RefreshEvent;
import com.cdkj.baseim.interfaces.TxImLoginInterface;
import com.cdkj.baseim.interfaces.TxImLoginPresenter;
import com.cdkj.baseim.ui.NotifyDialog;
import com.cdkj.baseim.util.PushUtil;
import com.cdkj.baselibrary.activitys.FindPwdActivity;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivitySignInBinding;
import com.cdkj.bcoin.main.MainActivity;
import com.huawei.android.pushagent.PushManager;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMUserConfig;
import com.tencent.imsdk.TIMUserStatusListener;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.greenrobot.eventbus.EventBus;

import java.util.Locale;

import static com.cdkj.bcoin.util.ZenDeskUtil.initZenDeskIdentity;

@Route(path = "/user/login")
public class SignInActivity extends AbsBaseActivity implements LoginInterface,TxImLoginInterface {

    private boolean canOpenMain;

    private LoginPresenter mPresenter;
    private ActivitySignInBinding mBinding;

    private TxImLoginPresenter txImLoginPresenter;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, boolean canOpenMain) {
        if (context == null) {
            return;
        }
        Intent intent= new Intent(context, SignInActivity.class);
        intent.putExtra("canOpenMain",canOpenMain);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_sign_in, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_sign_in));
        setTopLineState(true);
        setSubLeftImgState(true);
        setSubRightTitleAndClick(getStrRes(R.string.user_title_sign_up),v -> {
            SignUpActivity.open(SignInActivity.this);
        });


        mPresenter = new LoginPresenter(this);

        init();
        initListener();
        initSignInBtn();
    }

    private void init() {
        if (getIntent() == null)
            return;

        canOpenMain = getIntent().getBooleanExtra("canOpenMain",false);
    }

    private void initListener() {
        //登录
        mBinding.btnConfirm.setOnClickListener(v -> {
            if (check()){
                mPresenter.login(mBinding.edtUsername.getText().toString(), mBinding.edtPassword.getText().toString(), this);
            }

        });

        //找回密码
        mBinding.tvForget.setOnClickListener(v -> {
            FindPwdActivity.open(this, mBinding.edtUsername.getText().toString().trim());
        });
    }

    private boolean check(){
        if (TextUtils.isEmpty(mBinding.edtUsername.getText().toString().trim())){
            showToast(getStrRes(R.string.user_mobile_hint));
            return false;
        }
        if (mBinding.edtUsername.getText().toString().trim().length() != 11){
            showToast(getStrRes(R.string.user_mobile_format_hint));
            return false;
        }
        if (mBinding.edtPassword.getText().toString().trim().length() < 6){
            showToast(getStrRes(R.string.user_password_format_hint));
            return false;
        }

        return true;
    }

    private void initSignInBtn() {

        mBinding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBinding.edtUsername.getText().toString().length() == 11){
                    if (mBinding.edtPassword.getText().toString().length() >= 6){
                        // 可以登录
                        mBinding.btnConfirm.setTextColor(ContextCompat.getColor(SignInActivity.this,R.color.white));
                    }else {
                        // 不可登录
                        mBinding.btnConfirm.setTextColor(ContextCompat.getColor(SignInActivity.this,R.color.white_80));
                    }
                }else {
                    // 不可登录
                    mBinding.btnConfirm.setTextColor(ContextCompat.getColor(SignInActivity.this,R.color.white_80));
                }
            }
        });

        mBinding.edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (mBinding.edtUsername.getText().toString().length() == 11){
                    if (mBinding.edtPassword.getText().toString().length() >= 6){
                        // 可以登录
                        mBinding.btnConfirm.setTextColor(ContextCompat.getColor(SignInActivity.this,R.color.white));
                    }else {
                        // 不可登录
                        mBinding.btnConfirm.setTextColor(ContextCompat.getColor(SignInActivity.this,R.color.white_80));
                    }
                }else {
                    // 不可登录
                    mBinding.btnConfirm.setTextColor(ContextCompat.getColor(SignInActivity.this,R.color.white_80));
                }
            }
        });

    }

    @Override
    public void LoginSuccess(UserLoginModel user, String msg) {

        Log.e("user.getToken()",user.getToken());

//        SPUtilHelper.saveUserName(user.getN);
        SPUtilHelper.saveUserId(user.getUserId());
        SPUtilHelper.saveUserToken(user.getToken());
        SPUtilHelper.saveUserPhoneNum(mBinding.edtUsername.getText().toString().trim());

        initZenDeskIdentity(SPUtilHelper.getUserName(), SPUtilHelper.getUserEmail());
        initTencent();


    }

    @Override
    public void LoginFailed(String code, String msg) {
        showToast(msg);
    }

    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
        disMissLoading();
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

                        SignInActivity.open(SignInActivity.this,true);
                        finish();
                    }

                    @Override
                    public void onUserSigExpired() {
                        //用户签名过期了，需要刷新userSig重新登录SDK
                        showToast(getString(R.string.tls_expire));
                        SPUtilHelper.logOutClear();
                        EventBus.getDefault().post(EventTags.AllFINISH);

                        SignInActivity.open(SignInActivity.this,true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }

    @Override
    protected boolean canFinish() {
        if(canOpenMain){
            MainActivity.open(this);
            finish();
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        if(canOpenMain){
            MainActivity.open(this);
            finish();
        }else{
            super.onBackPressed();
        }
    }



}
