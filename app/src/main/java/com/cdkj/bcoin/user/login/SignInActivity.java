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
import com.cdkj.baselibrary.activitys.FindPwdActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.LoginInterface;
import com.cdkj.baselibrary.interfaces.LoginPresenter;
import com.cdkj.baselibrary.model.UserLoginModel;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivitySignInBinding;
import com.cdkj.bcoin.main.MainActivity;

import static com.cdkj.bcoin.util.ZenDeskUtil.initZenDeskIdentity;

@Route(path = "/user/login")
public class SignInActivity extends AbsBaseActivity implements LoginInterface {

    private boolean canOpenMain;

    private LoginPresenter mPresenter;
    private ActivitySignInBinding mBinding;

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

        initZenDeskIdentity(SPUtilHelper.getUserName(), SPUtilHelper.getUserEmail());

        MainActivity.open(this);
        finish();
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
