package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPasswordBinding;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;

/**
 * 找回密码
 */
public class FindPwdActivity extends AbsBaseActivity implements SendCodeInterface {

    private ActivityModifyPasswordBinding mBinding;

    private String mPhoneNumber;

    private SendPhoneCodePresenter mSendCOdePresenter;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String mPhoneNumber) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, FindPwdActivity.class);
        intent.putExtra("phonenumber", mPhoneNumber);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_password, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.activity_find_title));
        setSubLeftImgState(true);
        mSendCOdePresenter = new SendPhoneCodePresenter(this);
        if (getIntent() != null) {
            mPhoneNumber = getIntent().getStringExtra("phonenumber");
        }

        mBinding.llGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);
        mBinding.lineGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);

        if (!TextUtils.isEmpty(mPhoneNumber)) {
            mBinding.edtPhone.setText(mPhoneNumber);
            mBinding.edtPhone.setSelection(mBinding.edtPhone.getText().toString().length());
        }

        initListener();
    }

    /**
     *
     */
    private void initListener() {

        //发送验证码
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mBinding.edtPhone.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    ToastUtil.show(FindPwdActivity.this, getStrRes(R.string.activity_mobile_mobile_hint));
                    return;
                }
                    mSendCOdePresenter.sendCodeRequest(name, "805063", MyConfig.USERTYPE, FindPwdActivity.this);

            }
        });


        //确定
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhone.getText().toString())) {
                    showToast(getString(R.string.activity_find_mobile_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                    showToast(getString(R.string.activity_find_code_hint));
                    return;
                }

                if (SPUtilHelper.getGoogleAuthFlag()) {
                    if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
                        showToast(getString(R.string.activity_find_google_hint));
                        return;
                    }
                }

                if (TextUtils.isEmpty(mBinding.edtPassword.getText().toString())) {
                    showToast(getString(R.string.activity_find_password_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtRepassword.getText().toString())) {
                    showToast(getString(R.string.activity_find_repassword_hint));
                    return;
                }

                if (mBinding.edtPassword.getText().length() < 6) {
                    showToast(getString(R.string.activity_find_password_format_hint));
                    return;
                }

                if (!mBinding.edtPassword.getText().toString().equals(mBinding.edtRepassword.getText().toString())) {
                    showToast(getString(R.string.activity_find_repassword_format_hint));
                    return;
                }

                findPwdReqeust();
            }
        });
    }


    /**
     * 找回密码请求
     */
    private void findPwdReqeust() {

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("mobile", mBinding.edtPhone.getText().toString());
        hashMap.put("newLoginPwd", mBinding.edtPassword.getText().toString());
        hashMap.put("smsCaptcha", mBinding.edtCode.getText().toString());
        hashMap.put("kind", MyConfig.USERTYPE);
        hashMap.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        hashMap.put("systemCode", MyConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805063", StringUtils.getJsonToString(hashMap));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(FindPwdActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast(getString(R.string.activity_find_success));
                    finish();
                } else {
                    showToast(getString(R.string.activity_find_failure));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }


    @Override
    public void CodeSuccess(String msg) {
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
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

        if (mSendCOdePresenter != null) {
            mSendCOdePresenter.clear();
            mSendCOdePresenter = null;
        }
    }
}
