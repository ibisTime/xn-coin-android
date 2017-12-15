package com.cdkj.baselibrary.nets;

import android.content.Context;

import com.cdkj.baselibrary.BaseApplication;
import com.cdkj.baselibrary.api.BaseResponseListModel;
import com.cdkj.baselibrary.utils.LogUtil;
import com.cdkj.baselibrary.utils.ToastUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 网络请求回调
 * Created by Administrator on 2016/9/3.
 */
public abstract class BaseResponseListCallBack<T> implements Callback<BaseResponseListModel<T>> {


    /*0=成功；1=权限错误；2=参数错误；3=业务错误；9=未知错误*/

    public static final String REQUESTOK = "0";   //请求后台成功

    public static final String REQUESTFECODE3 = "3";
    public static final String REQUESTFECODE2 = "2";

    public static final String REQUESTFECODE4 = "4";//重新登录

    public static final String REQUESTFECODE9 = "9";

    public static final String NET_ERROR = "-1";


    /**
     * 网络异常状态错误码
     */
    public static final int NETERRORCODE0 = 0;  //请求成功，但是服务器返回除1000外错误码
    public static final int NETERRORCODE1 = 1;  //网络异常
    public static final int NETERRORCODE2 = 2;  //响应超时
    public static final int NETERRORCODE3 = 3;  //连接超时
    public static final int NETERRORCODE4 = 4;  //其它错误

    private Context context;

    public BaseResponseListCallBack(Context context) {
        this.context = context;
    }

    @Override
    public void onResponse(Call<BaseResponseListModel<T>> call, Response<BaseResponseListModel<T>> response) {

        onFinish();

        if (response == null || response.body() == null) {
            onNull();
            this.context = null;
            return;
        }

        if (response.isSuccessful()) {

            try {
                BaseResponseListModel t = response.body();
                checkState(t);      //根据返回错误的状态码实现相应的操作
            } catch (Exception e) {
                if (LogUtil.isLog) {
                    onReqFailure(NETERRORCODE4, "未知错误" + e.toString());
                }else{
                    onReqFailure(NETERRORCODE4, "未知错误");
                }
            }

        } else {
            onReqFailure(NETERRORCODE4, "网络请求失败");
        }

        this.context = null;
    }

    @Override
    public void onFailure(Call<BaseResponseListModel<T>> call, Throwable t) {

        if (call.isCanceled()) {                //如果是主动请求取消的就不执行
            return;
        }
        onFinish();
        if (!NetUtils.isNetworkConnected(BaseApplication.getContext())) {
            onNoNet("暂无网络");
            return;
        }

        String errorString = "";

        int errorCode = 0;

        if (t instanceof UnknownHostException) { // 网络错误
            errorString = "网络加载异常";
            errorCode = NETERRORCODE1;
        } else if (t instanceof SocketTimeoutException) {//响应超时
            errorString = "服务器响应超时";
            errorCode = NETERRORCODE2;
        } else if (t instanceof ConnectException) {//请求超时
            errorString = "网络请求超时";
            errorCode = NETERRORCODE3;
        } else {
            errorString = "未知错误";
            errorCode = NETERRORCODE4;
        }

        if (LogUtil.isLog) {
            errorString += t.toString();
        }

        onReqFailure(errorCode, errorString);
        this.context = null;
    }

    /**
     * 检查错误码
     *
     * @param baseModelNew 根据返回错误的状态码实现相应的操作
     */
    protected void checkState(BaseResponseListModel baseModelNew) {

        String state = baseModelNew.getErrorCode();

        if (REQUESTOK.equals(state)) { //请求成功

            List<T> t = (List<T>) baseModelNew.getData();

            if (t == null) {
                onFinish();
                onNull();
                this.context = null;
                return;
            }

            onSuccess(t, baseModelNew.getErrorInfo());

        } else if (REQUESTFECODE4.equals(state)) {
            OnOkFailure.StartDoFailure(context, baseModelNew.getErrorInfo());
//        } else if (REQUESTFECODE2.equals(state) || REQUESTFECODE3.equals(state) || REQUESTFECODE9.equals(state)) {
//            onBuinessFailure(state, baseModelNew.getErrorInfo());
        } else {
            onBuinessFailure(state, baseModelNew.getErrorInfo());
        }

    }


    /**
     * 请求成功
     *
     * @param data
     */
    protected abstract void onSuccess(List<T> data, String SucMessage);

    /**
     * 请求失败
     *
     * @param errorCode
     * @param errorMessage
     */
    protected void onReqFailure(int errorCode, String errorMessage) {
        LogUtil.E("数据  错误"+errorMessage);
        ToastUtil.show(context, errorMessage);
    }

    /**
     * 业务逻辑错误
     *
     * @param error
     */
    protected void onBuinessFailure(String code, String error) {
        LogUtil.E("数据  错误"+error);
        ToastUtil.show(context, error);
    }


    /**
     * 请求数据为空
     */
    protected  void onNull(){
        LogUtil.E("数据  空");
    }

    /**
     * 请求结束 无论请求成功或者失败都会被调用
     */
    protected abstract void onFinish();

    /**
     * 无网络
     */
    protected void onNoNet(String msg) {
        ToastUtil.show(context, msg);
    }

}
