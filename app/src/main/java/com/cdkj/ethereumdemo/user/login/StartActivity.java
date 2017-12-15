package com.cdkj.ethereumdemo.user.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.BaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.api.MyApi;
import com.cdkj.ethereumdemo.main.MainActivity;
import com.cdkj.ethereumdemo.model.SystemParameterModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import retrofit2.Call;

@Route(path = "/user/start")
public class StartActivity extends BaseActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("getAPPBuildType()", SPUtilHelper.getAPPBuildType());

        // 用于第一次安装APP，进入到除这个启动activity的其他activity，点击home键，再点击桌面启动图标时，
        // 系统会重启此activty，而不是直接打开之前已经打开过的activity，因此需要关闭此activity

        try {
            if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
                finish();
                return;
            }
        } catch (Exception e) {

        }
        setContentView(R.layout.activity_start);

        open();
//        getQiniu();

    }

    private void open(){

        mSubscription.add(Observable.timer(2, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {//延迟两秒进行跳转
//                    if (!SPUtilHelper.isLogin(StartActivity.this, false)) {
//                        finish();
//                        return;
//                    }
                    MainActivity.open(this);
                    finish();
                }, Throwable::printStackTrace));
    }

    /**
     * 获取七牛服务器链接
     */
    public void getQiniu() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "qiniu_domain");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("805917", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                MyConfig.IMGURL = data.getDkey();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
