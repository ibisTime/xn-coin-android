package com.cdkj.baseim.interfaces;

import android.content.Context;
import android.util.Log;

import com.cdkj.baseim.api.MyApiServer;
import com.cdkj.baseim.maneger.TXImManager;
import com.cdkj.baseim.model.TencentSignModel;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lq on 2017/11/27.
 */

public class TxImLoginPresenter {

    private TxImLoginInterface mListener;
    private Context mContext;
    private Call call;

    public TxImLoginPresenter(TxImLoginInterface mInterface) {
        this.mListener = mInterface;
    }

    //处理登录逻辑
    public void login(Context context) {
        this.mContext = context;
        getTxKeyRequest();
    }

    /**
     * 获取腾讯签名
     */
    private void getTxKeyRequest() {

        Map map = new HashMap<>();

        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        call = RetrofitUtils.createApi(MyApiServer.class).getTencentSign("625000", StringUtils.getJsonToString(map));

        call.enqueue(new BaseResponseModelCallBack<TencentSignModel>(mContext) {
            @Override
            protected void onSuccess(TencentSignModel data, String SucMessage) {
                TXImManager.getInstance().init(Integer.parseInt(data.getTxAppCode()));

                // 登录用小写userId去登录
                TXImManager.getInstance().login(SPUtilHelper.getUserId(), data.getSign(), new TXImManager.LoginBallBack() {
                    @Override
                    public void onError(int i, String s) {
                        mListener.onError(i,s);
                    }

                    @Override
                    public void onSuccess() {
                        txLoginSuccess();
                    }
                });
            }

            @Override
            protected void onReqFailure(int code, String error) {
                super.onReqFailure(code, error);
                mListener.onError(6000, error);
            }

            @Override
            protected void onFinish() {
                mListener.onFinish();
            }
        });
    }

    private void txLoginSuccess() {
        TXImManager.getInstance().setUserNickName(SPUtilHelper.getUserName(), new TXImManager.ChangeInfoBallBack() {
            @Override
            public void onError(int i, String s) {
                mListener.onError(i,s);
            }

            @Override
            public void onSuccess() {
                mListener.onSuccess();
            }
        });
    }

    private void setLogo() {
        TXImManager.getInstance().setUserLogo(SPUtilHelper.getUserPhoto(), new TXImManager.ChangeInfoBallBack() {
            @Override
            public void onError(int i, String s) {
            }

            @Override
            public void onSuccess() {
            }
        });
    }

    //处理持有对象
    public void clear() {
        if(this.call!=null){
            this.call.cancel();
            this.call = null;
        }
        this.mListener = null;
        this.mContext = null;
    }


}
