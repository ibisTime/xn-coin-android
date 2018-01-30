package com.cdkj.baselibrary.activitys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityAuthenticateBinding;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class AuthenticateActivity extends AbsBaseActivity {

    private ActivityAuthenticateBinding mBinding;

    private String bizNo;

    public static void open(Context context) {
        if (context == null) {
            return;
        }
        Intent intent = new Intent(context, AuthenticateActivity.class);
        context.startActivity(intent);
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_authenticate, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.activity_authenticate_title));
        setTopLineState(true);
        setSubLeftImgState(true);

        initListener();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Uri uri = intent.getData();
        if (uri != null) {
            check();
        }
    }

    private void initListener() {
        mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBinding.edtName.getText().toString().equals("")) {
                    showToast(getString(R.string.activity_authenticate_name_hint));
                    return;
                }
                if (mBinding.edtIdentity.getText().toString().length() == 15 || mBinding.edtIdentity.getText().toString().length() == 18) {

                } else {
                    showToast(getString(R.string.activity_authenticate_id_format_hint));
                    return;
                }

                authenticate();
            }
        });
    }


    /**
     * 实名认证
     */
    private void authenticate() {

        Map<String, String> object = new HashMap<>();
        object.put("returnUrl", "bcoin://certi.back");
        object.put("userId", SPUtilHelper.getUserId());
        object.put("realName", mBinding.edtName.getText().toString().trim());
        object.put("localCheck", "0");
        object.put("idNo", mBinding.edtIdentity.getText().toString().trim());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805195", StringUtils.getJsonToString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if(data.isSuccess()){
                    SPUtilHelper.saveRealName(mBinding.edtName.getText().toString().trim());
                    showToast(getString(R.string.activity_authenticate_success));
                    finish();
                }else{
                    bizNo = data.getBizNo();
                    doVerify(data.getUrl());
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 实名认证结果查询
     */
    private void check() {
        Map<String, String> object = new HashMap<>();
        object.put("userId", SPUtilHelper.getUserId());
        object.put("bizNo", bizNo);


        Call call = RetrofitUtils.getBaseAPiService().successRequest("805196", StringUtils.getJsonToString(object));
        addCall(call);
        showLoadingDialog();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {

                if (data.isSuccess()) {
                    SPUtilHelper.saveRealName(mBinding.edtName.getText().toString().trim());
                    showToast(getString(R.string.activity_authenticate_success));
                    finish();
                } else {
                    showToast(getString(R.string.activity_authenticate_failure));
                }


            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }

        });

    }


    /**
     * 启动支付宝进行认证
     *
     * @param url 开放平台返回的URL
     */
    private void doVerify(String url) {
        if (hasApplication()) {
            Intent action = new Intent(Intent.ACTION_VIEW);
            StringBuilder builder = new StringBuilder();
            builder.append("alipays://platformapi/startapp?appId=20000067&url=");
            builder.append(URLEncoder.encode(url));
            action.setData(Uri.parse(builder.toString()));
            startActivity(action);
        } else {
            //处理没有安装支付宝的情况
            new AlertDialog.Builder(this)
                    .setMessage(getStrRes(R.string.activity_authenticate_download_title))
                    .setPositiveButton(getStrRes(R.string.activity_authenticate_download_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse("https://m.alipay.com"));
                            startActivity(action);
                        }
                    }).setNegativeButton(getStrRes(R.string.activity_authenticate_download_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).show();
        }
    }

    /**
     * 判断是否安装了支付宝
     *
     * @return true 为已经安装
     */
    private boolean hasApplication() {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse("alipays://"));
        List<ResolveInfo> list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }


}
