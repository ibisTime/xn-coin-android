package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityModifyPhoneBinding;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * 更换手机号码
 * Created by 李先俊 on 2017/6/16.
 */

public class UpdatePhoneActivity extends AbsBaseActivity implements SendCodeInterface {

    private ActivityModifyPhoneBinding mBinding;

    private SendPhoneCodePresenter mSendCodePresenter;

    /**
     * 打开当前页面
     *
     * @param context
     */
    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, UpdatePhoneActivity.class);

        context.startActivity(intent);
    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_modify_phone, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {

        setTopTitle(getString(R.string.activity_mobile_title));
        setSubLeftImgState(true);

        mSendCodePresenter = new SendPhoneCodePresenter(this);

        initListener();
    }

    private void initListener() {
        //发送验证码
        mBinding.btnSendNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSendCodePresenter.sendCodeRequest(mBinding.edtPhoneNew.getText().toString(), "805061", MyConfig.USERTYPE, UpdatePhoneActivity.this);
            }
        });

        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mBinding.edtPhoneNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_mobile_hint));
                    return;
                }

                if (TextUtils.isEmpty(mBinding.edtCodeNew.getText().toString())) {
                    showToast(getString(R.string.activity_mobile_code_hint));
                    return;
                }

                updatePhone();

            }
        });

    }


    /**
     * 更换手机号
     */
    private void updatePhone() {

        Map<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("newMobile", mBinding.edtPhoneNew.getText().toString());
        map.put("smsCaptcha", mBinding.edtCodeNew.getText().toString());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805061", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {

                    showToast(getString(R.string.activity_mobile_modify_success));

                    EventBusModel eventBusModel=new EventBusModel();      //刷新上一页数据
                    eventBusModel.setTag(EventTags.CHANGEPHONENUMBER_REFRESH);
                    eventBusModel.setEvInfo(mBinding.edtPhoneNew.getText().toString());
                   finish();
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
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSendNew));
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
        if(mSendCodePresenter!=null){
            mSendCodePresenter.clear();
            mSendCodePresenter=null;
        }
    }
}
