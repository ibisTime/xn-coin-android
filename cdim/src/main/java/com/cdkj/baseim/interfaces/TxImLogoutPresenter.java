package com.cdkj.baseim.interfaces;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMManager;

/**
 * Created by lq on 2017/11/27.
 */

public class TxImLogoutPresenter {

    private TxImLogoutInterface mListener;
    private Context mContext;

    public TxImLogoutPresenter(TxImLogoutInterface view) {
        this.mListener = view;
    }

    //处理登出逻辑
    public void logout() {

        if (TextUtils.isEmpty(TIMManager.getInstance().getLoginUser())){
            mListener.emptyLoginUser();
        }else {
            //登出
            TIMManager.getInstance().logout(new TIMCallBack() {
                @Override
                public void onError(int code, String desc) {

                    mListener.onError(code, desc);
                }

                @Override
                public void onSuccess() {
                    //登出成功
                    mListener.onSuccess();
                }
            });
        }

    }

    //处理持有对象
    public void clear() {
        this.mListener = null;
        this.mContext = null;
    }


}
