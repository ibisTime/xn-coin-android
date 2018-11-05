package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserPasswordBinding;

import java.util.HashMap;
import java.util.LinkedHashMap;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.SystemUtils.paste;

/**
 * 找回密码
 */
public class UserPasswordActivity extends AbsBaseActivity {

    private ActivityUserPasswordBinding mBinding;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_password, null, false);
        return mBinding.getRoot();
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.user_password_title));
        setSubLeftImgState(true);

        mBinding.llGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);
        mBinding.lineGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);

        initListener();
    }

    /**
     *
     */
    private void initListener() {

        mBinding.btnPaste.setOnClickListener(view -> {
            mBinding.edtGoogle.setText(paste(this));
        });

        //确定
        mBinding.btnConfirm.setOnClickListener(view -> {
            if (TextUtils.isEmpty(mBinding.edtPasswordOld.getText().toString())) {
                showToast(getString(R.string.user_password_pwdold_hint));
                return;
            }

            if (SPUtilHelper.getGoogleAuthFlag()) {
                if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
                    showToast(getString(R.string.user_password_google_hint));
                    return;
                }
            }

            if (TextUtils.isEmpty(mBinding.edtPasswordNew.getText().toString())) {
                showToast(getString(R.string.user_password_pwdnew_hint));
                return;
            }
            if (TextUtils.isEmpty(mBinding.edtRepassword.getText().toString())) {
                showToast(getString(R.string.user_password_repwd_hint));
                return;
            }

            if (mBinding.edtPasswordNew.getText().length() < 6) {
                showToast(getString(R.string.user_password_pwdnew_format_hint));
                return;
            }

            if (!mBinding.edtPasswordNew.getText().toString().equals(mBinding.edtRepassword.getText().toString())) {
                showToast(getString(R.string.user_password_repwd_format_hint));
                return;
            }

            findPwdReqeust();
        });
    }


    /**
     * 找回密码请求
     */
    private void findPwdReqeust() {

        HashMap<String, String> hashMap = new LinkedHashMap<String, String>();

        hashMap.put("oldLoginPwd", mBinding.edtPasswordOld.getText().toString());
        hashMap.put("newLoginPwd", mBinding.edtPasswordNew.getText().toString());
        hashMap.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        hashMap.put("userId", SPUtilHelper.getUserId());
        hashMap.put("token", SPUtilHelper.getUserToken());
        hashMap.put("systemCode", MyConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805064", StringUtils.getJsonToString(hashMap));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(UserPasswordActivity.this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    showToast(getString(R.string.user_password_success));
                    finish();
                } else {
                    showToast(getString(R.string.user_password_failure));
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

}
