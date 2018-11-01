package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPayPasswordBinding;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 修改 设置 资金密码
 * Created by 李先俊 on 2017/6/29.
 */
public class PayPwdModifyActivity extends AbsBaseActivity implements SendCodeInterface {

    private ActivityModifyPayPasswordBinding mBinding;

    private boolean mIsSetPwd;//是否设置过密码

    private SendPhoneCodePresenter mSendCoodePresenter;


    /**
     * @param context
     * @param isSetPwd//是否设置过资金密码
     */
    public static void open(Context context, boolean isSetPwd, String mobile) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, PayPwdModifyActivity.class);
        intent.putExtra("isSetPwd", isSetPwd);
        intent.putExtra("mobile", mobile);
        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_pay_password, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);

        mBinding.llGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);
        mBinding.lineGoogle.setVisibility(SPUtilHelper.getGoogleAuthFlag() ? View.VISIBLE : View.GONE);

        if (getIntent() != null) {
            mIsSetPwd = getIntent().getBooleanExtra("isSetPwd", false);
            mBinding.edtPhone.setText(getIntent().getStringExtra("mobile"));
            mBinding.edtPhone.setSelection(mBinding.edtPhone.getText().length());
        }

        if (mIsSetPwd) {
            setTopTitle(getString(R.string.activity_paypwd_title));
        } else {
            setTopTitle(getString(R.string.activity_paypwd_title_set));
        }
        mSendCoodePresenter = new SendPhoneCodePresenter(this);
        setListener();
    }

    /**
     * 设置事件
     */
    private void setListener() {
//发送验证码
        mBinding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bizType = "";
                if (mIsSetPwd) {

                    String name = mBinding.edtPhone.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtil.show(PayPwdModifyActivity.this, getString(R.string.activity_mobile_mobile_hint));
                        return;
                    }
                    bizType = "805067";//修改

                } else {
                    String name = mBinding.edtPhone.getText().toString();
                    if (TextUtils.isEmpty(name)) {
                        ToastUtil.show(PayPwdModifyActivity.this, getString(R.string.activity_mobile_mobile_hint));
                        return;
                    }
                    bizType = "805066";
                }

                mSendCoodePresenter.sendCodeRequest(mBinding.edtPhone.getText().toString(), bizType, MyConfig.USERTYPE, PayPwdModifyActivity.this);
            }
        });
//确认
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhone.getText())) {
                    showToast(getString(R.string.activity_paypwd_mobile_hint));
                    return;
                }
                if (TextUtils.isEmpty(mBinding.edtCode.getText())) {
                    showToast(getString(R.string.activity_paypwd_code_hint));
                    return;
                }
                if (SPUtilHelper.getGoogleAuthFlag()) {
                    if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
                        showToast(getString(R.string.activity_paypwd_google_hint));
                        return;
                    }
                }
                if (TextUtils.isEmpty(mBinding.edtRepassword.getText())) {
                    showToast(getString(R.string.activity_paypwd_pwd_hint));
                    return;
                }
                if (mBinding.edtRepassword.getText().length() < 6) {
                    showToast(getString(R.string.activity_paypwd_pwd_format_hint));
                    return;
                }


                setPwd();

            }
        });
    }


    private void setPwd() {

        Map<String, String> object = new HashMap<>();

        object.put("userId", SPUtilHelper.getUserId());
        object.put("token", SPUtilHelper.getUserToken());
        object.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        if (mIsSetPwd) {
            object.put("newTradePwd", mBinding.edtRepassword.getText().toString().trim());
        } else {
            object.put("tradePwd", mBinding.edtRepassword.getText().toString().trim());
        }
        if (mBinding.edtPhone.getText().toString().trim().contains("@")) {
            //邮箱
            object.put("type", "2");
        } else {
            //手机号
            object.put("type", "1");
        }

        object.put("smsCaptcha", mBinding.edtCode.getText().toString().toString());

        String code = "";
        if (mIsSetPwd) {  //修改
            code = "805067";
        } else {
            code = "805066";
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest(code, StringUtils.getJsonToString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (!data.isSuccess()) {
                    return;
                }

                if (mIsSetPwd) {
                    showToast(getString(R.string.activity_paypwd_modify_sucess));
                } else {
                    showToast(getString(R.string.activity_paypwd_set_success));
                    EventBusModel eventBusModel = new EventBusModel();
                    eventBusModel.setTag(EventTags.CHANGE_PAY_PWD_REFRESH);
                    EventBus.getDefault().post(eventBusModel);

                }

                SPUtilHelper.saveTradePwdFlag(true);

                finish();
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
        if (mSendCoodePresenter != null) {
            mSendCoodePresenter.clear();
            mSendCoodePresenter = null;
        }
    }
}
