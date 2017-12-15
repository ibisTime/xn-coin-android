package com.cdkj.ethereumdemo.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.api.MyApi;
import com.cdkj.ethereumdemo.databinding.ActivityUserTipBinding;
import com.cdkj.ethereumdemo.model.UserSettingModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/12/5.
 */

public class UserTipActivity extends AbsBaseActivity {

    private ActivityUserTipBinding mBinding;

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserTipActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_tip, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getStrRes(R.string.user_title_tip));
        setTopLineState(true);
        setSubLeftImgState(true);

        getSetting();
    }

    private void initListener() {
        mBinding.sbAutoGood.setOnCheckedChangeListener((compoundButton, b) -> {
            set("1",b == true ? "0":"1");
        });

        mBinding.sbAutoTrust.setOnCheckedChangeListener((compoundButton, b) -> {
            set("2",b == true ? "0":"1");
        });

        mBinding.llLanguage.setOnClickListener(view -> {
            UserLanguageActivity.open(this);
        });
    }

    private void getSetting() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.createApi(MyApi.class).getUserSetting("625301", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<UserSettingModel>(this) {

            @Override
            protected void onSuccess(List<UserSettingModel> data, String SucMessage) {
                if (data == null)
                    return;

                setView(data);
                initListener();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void setView(List<UserSettingModel> data) {

        for (UserSettingModel model : data){

            switch (model.getType()){

                case "1": // 自动好评

                    // 1已设置
                    mBinding.sbAutoGood.setCheckedNoEvent(model.getValue().equals("1"));

                    break;

                case "2": // 自动信任

                    // 1已设置
                    mBinding.sbAutoTrust.setCheckedNoEvent(model.getValue().equals("1"));

                    break;

            }
        }

    }

    private void set(String location, String value) {
        Map<String, Object> map = new HashMap<>();
        map.put("type", location); // ("1", "设置自动好评"), ("2",设置自动信任）
        map.put("opType", value); // 0=未设置，添加设置，1=已设置，取消设置
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest("625300", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess())
                    getSetting();

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
