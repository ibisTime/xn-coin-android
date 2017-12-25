package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserEmailBinding;

import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/25.
 */

public class UserEmailActivity extends AbsBaseActivity {

    private ActivityUserEmailBinding mBinding;

    private String email;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context, String email) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserEmailActivity.class);
        intent.putExtra("email",email);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_email, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);

        if (getIntent() != null) {
            email = getIntent().getStringExtra("email");
            if (email == null || email.equals("")){
                setTopTitle(getStrRes(R.string.user_title_email_bind));
            }else {
                setTopTitle(getStrRes(R.string.user_title_email_modify));
                mBinding.edtEmail.setText(email);
                mBinding.edtEmail.setHint(email);
            }
        }

        initListener();
    }

    private void initListener() {
        mBinding.btnSend.setOnClickListener(view -> {

            if (check("code"))
                sendEmailCode();

        });

        mBinding.btnConfirm.setOnClickListener(v -> {

            if (check("all")){
                modifyEmail();
            }
        });
    }

    private boolean check(String type){
        if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString())) {
            showToast(getStrRes(R.string.user_email_hint));
            return false;
        }

        if (type.equals("all")){
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                showToast(getStrRes(R.string.user_email_code_hint));
                return false;
            }
        }

        return true;
    }

    @Subscribe
    public void changeUi(String tag){
        if (tag == null)
            return;

        if (tag.equals(EventTags.CHANGE_CODE_BTN)){
            mBinding.btnSend.setBackground(ContextCompat.getDrawable(UserEmailActivity.this,R.drawable.corner_sign_btn));
        }
    }

    /**
     * 修改邮箱
     */
    public void sendEmailCode() {
        Map<String, String> map = new HashMap<>();
        map.put("bizType", "805081");
        map.put("email", mBinding.edtEmail.getText().toString());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805952", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    if(data.isSuccess()){
                        showToast(getString(R.string.email_code_send_success));

                        //启动倒计时
                        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
                        //改变ui
                        mBinding.btnSend.setBackground(ContextCompat.getDrawable(UserEmailActivity.this,R.drawable.corner_sign_btn_gray));
                    }else{
                        showToast(getString(R.string.email_code_send_failure));
                    }
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    /**
     * 修改邮箱
     */
    public void modifyEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("captcha", mBinding.edtCode.getText().toString());
        map.put("email", mBinding.edtEmail.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805081", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    if (email == null || email.equals("")){
                        showToast(getStrRes(R.string.user_email_bind_success));
                    }else {
                        showToast(getStrRes(R.string.user_email_modify_success));
                    }
                    SPUtilHelper.saveUserEmail(mBinding.edtEmail.getText().toString().trim());

                    finish();
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }



}
