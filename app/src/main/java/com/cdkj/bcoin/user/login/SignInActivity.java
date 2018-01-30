package com.cdkj.bcoin.user.login;


import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.tencent.imsdk.TIMOfflinePushSettings;
import com.tencent.imsdk.TIMUserConfig;
import com.xiaomi.mipush.sdk.MiPushClient;

import java.util.Locale;

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

        SPUtilHelper.saveUserId(user.getUserId());
        SPUtilHelper.saveUserToken(user.getToken());
        SPUtilHelper.saveUserPhoneNum(mBinding.edtUsername.getText().toString().trim());

        initTencent();

    }

    @Override
    public void LoginFailed(String code, String msg) {
        disMissLoading();
        showToast(msg);
    }

    @Override
    public void StartLogin() {
        showLoadingDialog();
    }

    @Override
    public void EndLogin() {
        // 不隐藏Dialog，避免出现 腾讯云登录Dialog隐藏再二次打开的效果，腾讯云登录回调隐藏
//        disMissLoading();
    }

    /**
     * 登录腾讯云
     */
    private void initTencent() {
        // 登录腾讯云
        txImLoginPresenter = new TxImLoginPresenter(this);
        txImLoginPresenter.login(this);
    }

    @Override
    public void onError(int i, String s) {
        switch (i) {
            case 6208:
                //离线状态下被其他终端踢下线
                NotifyDialog dialog = new NotifyDialog();
                dialog.show(getString(R.string.kick_logout), getSupportFragmentManager(), (dialog1, which) -> groupEvent());
                break;
            case 6200:
                showToast(getString(R.string.login_error_timeout));
                finish();
                break;
            default:
                showToast(getString(R.string.login_error));
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

        //全局推送开启离线推送
        TIMOfflinePushSettings settings = new TIMOfflinePushSettings();
        settings.setEnabled(true);
        TIMManager.getInstance().setOfflinePushSettings(settings);
    }

    @Override
    public void onFinish() {
        disMissLoading();
    }

    /**
     * 设置腾讯云监听,登录腾讯云
     */
    public void groupEvent(){
        //登录之前要初始化群和好友关系链缓存
        TIMUserConfig userConfig = new TIMUserConfig();

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
