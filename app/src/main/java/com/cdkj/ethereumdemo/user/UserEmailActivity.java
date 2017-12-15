package com.cdkj.ethereumdemo.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.databinding.ActivityUserEmailBinding;

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
        mBinding.btnConfirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(mBinding.edtEmail.getText().toString())) {
                showToast(getStrRes(R.string.user_email_hint));
                return;
            }
            modifyEmail();
        });
    }

    /**
     * 修改邮箱
     */
    public void modifyEmail() {
        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("email", mBinding.edtEmail.getText().toString());
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
