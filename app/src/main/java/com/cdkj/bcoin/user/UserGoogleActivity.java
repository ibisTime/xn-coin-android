package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.interfaces.SendCodeInterface;
import com.cdkj.baselibrary.interfaces.SendPhoneCodePresenter;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.databinding.ActivityUserGoogleBinding;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;

import static com.cdkj.baselibrary.utils.SystemUtils.copy;
import static com.cdkj.baselibrary.utils.SystemUtils.paste;

/**
 * Created by lei on 2017/12/6.
 */

public class UserGoogleActivity extends AbsBaseActivity implements SendCodeInterface {

    private SendPhoneCodePresenter mPresenter;

    private ActivityUserGoogleBinding mBinding;

    private String status;
    private String bizType;

    public static void open(Context context, String status) {
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserGoogleActivity.class).putExtra("status", status));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_google, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_google));
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();

    }

    @Subscribe
    public void changeUi(String tag) {
        if (tag == null)
            return;

        if (tag.equals(EventTags.CHANGE_CODE_BTN)) {
            mBinding.btnSend.setBackground(ContextCompat.getDrawable(UserGoogleActivity.this, R.drawable.corner_sign_btn));
        }
    }

    //获取验证码相关
    @Override
    public void CodeSuccess(String msg) {
        //启动倒计时
        mSubscription.add(AppUtils.startCodeDown(60, mBinding.btnSend));

        //改变ui
        mBinding.btnSend.setBackground(ContextCompat.getDrawable(this, R.drawable.corner_sign_btn_gray));
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
        if (mPresenter != null) {
            mPresenter.clear();
            mPresenter = null;
        }
    }

    private void init() {
        if (getIntent() == null)
            return;

        status = getIntent().getStringExtra("status");
        if (status.equals("close")) { // 关闭谷歌验证

            mBinding.llKey.setVisibility(View.GONE);
            mBinding.lineKey.setVisibility(View.GONE);

            mBinding.btnConfirm.setText(getStrRes(R.string.user_google_btn_close));

        } else if (status.equals("modify")) { // 修改谷歌验证
            // 获取密钥
            getGoogleKey();

            mBinding.btnConfirm.setText(getStrRes(R.string.user_google_btn_modify));
        } else { // 打开修改谷歌验证
            // 获取密钥
            getGoogleKey();
        }


        mPresenter = new SendPhoneCodePresenter(this);
    }

    private void initListener() {
        mBinding.btnCopy.setOnClickListener(view -> {
            copy(this, mBinding.tvKey.getText().toString());
        });

        mBinding.btnPaste.setOnClickListener(view -> {
            mBinding.edtGoogle.setText(paste(this));
        });

        mBinding.btnSend.setOnClickListener(view -> {

            if (status.equals("close")) {
                bizType = "805072";
            } else {
                bizType = "805071";
            }
            //优先使用手机号,没有手机号就使用邮箱  两个至少会有一个  不存在全部没有的情况
//            TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum()) ? SPUtilHelper.getUserEmail() : SPUtilHelper.getUserPhoneNum()
            mPresenter.sendCodeRequest(SPUtilHelper.getUserEmail(), bizType, "C", this);
        });

        mBinding.btnConfirm.setOnClickListener(view -> {
            if (check())
                if (status.equals("close")) { // 关闭谷歌验证
                    closeGoogleKey();
                } else { // 打开或者修改谷歌验证
                    openGoogleKey();
                }

        });
    }

    private void getGoogleKey() {

        JSONObject object = new JSONObject();

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805070", object.toString());

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.tvKey.setText(data.getSecret());

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private boolean check() {
        if (TextUtils.isEmpty(mBinding.edtGoogle.getText().toString())) {
            showToast(getStrRes(R.string.google_code_hint));
            return false;
        }

        if (!isNumeric(mBinding.edtGoogle.getText().toString())) {
            showToast(getStrRes(R.string.google_code_format_hint));
            return false;
        }

        if (TextUtils.isEmpty(mBinding.edtCode.getText().toString())) {
            showToast(getStrRes(R.string.sms_code_hint));
            return false;
        }

        return true;
    }

    /**
     * 判断是否为纯数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void openGoogleKey() {

        Map<String, String> map = new HashMap<>();
        map.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        map.put("secret", mBinding.tvKey.getText().toString());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());

        String zhanghao = TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum()) ? SPUtilHelper.getUserEmail() : SPUtilHelper.getUserPhoneNum();
        if (zhanghao.contains("@")) {
            //判断是不是 手机号
            map.put("type", "2");
        } else {
            map.put("type", "1");
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805071", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()) {
                    SPUtilHelper.saveGoogleAuthFlag(true);
                    finish();
                }

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void closeGoogleKey() {

        Map<String, String> map = new HashMap<>();
        map.put("googleCaptcha", mBinding.edtGoogle.getText().toString());
        map.put("smsCaptcha", mBinding.edtCode.getText().toString());
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        String zhanghao = TextUtils.isEmpty(SPUtilHelper.getUserPhoneNum()) ? SPUtilHelper.getUserEmail() : SPUtilHelper.getUserPhoneNum();
        if (zhanghao.contains("@")) {
            //判断是不是 手机号
            map.put("type", "2");
        } else {
            map.put("type", "1");
        }

        Call call = RetrofitUtils.getBaseAPiService().successRequest("805072", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess()) {
                    SPUtilHelper.saveGoogleAuthFlag(false);
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
