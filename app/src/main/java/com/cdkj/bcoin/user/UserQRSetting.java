package com.cdkj.bcoin.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.activitys.ImageSelectActivity;
import com.cdkj.baselibrary.api.BaseResponseModel;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserQrsettingBinding;
import com.cdkj.bcoin.util.AliOssUtils;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 收款款设置  支付宝二维码  账号的设置
 */
public class UserQRSetting extends AbsBaseActivity {

    private ActivityUserQrsettingBinding mBinding;
    private int REQUEST_CODE = 1000;
    private String QRName;
    private String name;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserQRSetting.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_qrsetting, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_qr));
        setTopLineState(true);
        setSubLeftImgState(true);

        initListener();

        initData();

    }

    /**
     * 复现数据
     */
    private void initData() {

        if (!TextUtils.isEmpty(SPUtilHelper.getZfbAccount())) {
            mBinding.etName.setText(SPUtilHelper.getZfbAccount());
        }
        if (!TextUtils.isEmpty(SPUtilHelper.getZfbQr())) {
            ImgUtils.loadActImg(this, SPUtilHelper.getZfbQr(), mBinding.ivQr);
        }
    }

    private void initListener() {
        mBinding.btnConfirm.setOnClickListener(v -> {
            if (check()) {
                //提交
                submit();
            }
        });
        mBinding.ivQr.setOnClickListener(v -> {
            ImageSelectActivity.launch(UserQRSetting.this, REQUEST_CODE);
        });
    }

    private Boolean check() {
        name = mBinding.etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.show(this, "请填写收款账号");
            return false;
        }
        if (TextUtils.isEmpty(QRName)) {
            ToastUtil.show(this, "请上传收款码");
            return false;
        }

        return true;
    }

    //二维码设置接口
    private void submit() {
        HashMap<String, String> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("zfbAccount", name);//账号
        map.put("zfbQr", QRName);//二维码
        Call<BaseResponseModel<IsSuccessModes>> baseResponseModelCall = RetrofitUtils.getBaseAPiService().successRequest("805097", StringUtils.getJsonToString(map));
        showLoadingDialog();
        baseResponseModelCall.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    ToastUtil.show(UserQRSetting.this, "成功");
                    SPUtilHelper.saveZfbAccount(mBinding.etName.getText().toString());
                    SPUtilHelper.saveZfbQr(QRName);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_CODE) {
            String path = data.getStringExtra(ImageSelectActivity.staticPath);

            new AliOssUtils(this).getAliURL(new AliOssUtils.AliUpLoadBack() {
                @Override
                public void onSuccess(String name, String etag, String requestId) {
                    ImgUtils.loadImage(UserQRSetting.this, name, mBinding.ivQr);
                    QRName = name;
                }

                @Override
                public void onFal(String info) {
                    ToastUtil.show(UserQRSetting.this, "出错了" + info);
                }
            }, path);
        }
    }
}
