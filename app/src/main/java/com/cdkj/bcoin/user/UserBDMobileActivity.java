package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserBdmobileBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class UserBDMobileActivity extends AbsBaseActivity {

    private ActivityUserBdmobileBinding mBinding;


    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UserEmailActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_user_bdmobile, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);

        setTopTitle(getStrRes(R.string.user_title_moblie_bind));

        initListener();
    }

    private void initListener() {
        mBinding.btnSend.setOnClickListener(view -> {

            if (check("code"))
                sendEmailCode();

        });

        mBinding.btnConfirm.setOnClickListener(v -> {

            if (check("all")) {
                bindMoblie();
            }
        });
    }


    private boolean check(String type) {
        if (TextUtils.isEmpty(mBinding.edtMobile.getText().toString())) {
            showToast(getStrRes(R.string.user_sign_up_mobile_hint));
            return false;
        }

        if (type.equals("all")) {
            if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
                showToast(getStrRes(R.string.sms_code_hint));
                return false;
            }
        }

        return true;
    }

    /**
     * 修改邮箱
     */
    public void sendEmailCode() {
        Map<String, String> map = new HashMap<>();
        map.put("bizType", "805081");
        map.put("email", mBinding.edtMobile.getText().toString());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805952", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    if (data.isSuccess()) {
                        showToast(getString(R.string.email_code_send_success));

                        //启动倒计时
                        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));
                        //改变ui
                        mBinding.btnSend.setBackground(ContextCompat.getDrawable(UserBDMobileActivity.this, R.drawable.corner_sign_btn_gray));
                    } else {
                        showToast(getString(R.string.moblie_code_send_failure));
                    }
                }
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void bindMoblie() {
        Map<String, String> map = new HashMap<>();
        map.put("bizType", "805081");
        map.put("email", mBinding.edtMobile.getText().toString());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805952", StringUtils.getJsonToString(map));
    }

}
