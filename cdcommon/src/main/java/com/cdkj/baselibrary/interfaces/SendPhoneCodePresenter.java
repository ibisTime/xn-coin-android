package com.cdkj.baselibrary.interfaces;

import android.content.Context;
import android.text.TextUtils;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.util.HashMap;

import retrofit2.Call;

/**
 * 发送验证码
 * Created by 李先俊 on 2017/8/8.
 */
public class SendPhoneCodePresenter {

    private SendCodeInterface mListener;
    private Context mContext;
    private Call call;

    public SendPhoneCodePresenter(SendCodeInterface view) {
        this.mListener = view;
    }

    //处理登录逻辑
    public void sendCodeRequest(String phone, String bizType, String kind, Context context) {
        this.mContext = context;
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.show(context, mContext.getString(R.string.activity_mobile_mobile_hint));
            return;
        }

        request(phone, bizType, kind);
    }

    /**
     * 请求
     */
    private void request(String phone, String bizType, String kind) {

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("systemCode", MyConfig.SYSTEMCODE);
        hashMap.put("companyCode", MyConfig.COMPANYCODE);
        hashMap.put("mobile", phone);
        hashMap.put("email", phone);
        hashMap.put("bizType", bizType);
        hashMap.put("kind", kind);

        call = RetrofitUtils.getBaseAPiService().successRequest("805950", StringUtils.getJsonToString(hashMap));

        mListener.StartSend();
        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(mContext) {
            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data.isSuccess()) {
                    ToastUtil.show(mContext, mContext.getString(R.string.smscode_send_success));
                    mListener.CodeSuccess(mContext.getString(R.string.smscode_send_success));
                } else {
                    mListener.CodeFailed("", mContext.getString(R.string.smscode_send_success));
                }
            }

            @Override
            protected void onBuinessFailure(String code, String error) {
                mListener.CodeFailed(code, error);
            }

            @Override
            protected void onFinish() {
                mListener.EndSend();
            }
        });
    }

    //处理持有对象
    public void clear() {
        if (this.call != null) {
            this.call.cancel();
            this.call = null;
        }
        this.mListener = null;
        this.mContext = null;
    }


}
