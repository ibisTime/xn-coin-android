package com.cdkj.baseim.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baseim.R;
import com.cdkj.baseim.databinding.ActivityTxLogingBinding;
import com.cdkj.baseim.maneger.TXImManager;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.model.UserInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 腾讯im推送信息获取
 * Created by cdkj on 2017/10/30.
 */

public class TxImPushActivity extends AbsBaseActivity {

    public static final String OPEN_TYPE_ORDER = "order"; // 订单
    public static final String OPEN_TYPE_OTHER = "other"; // 其他

    private ActivityTxLogingBinding binding;
    private String toUserId;
    private String openType;

    @Override
    public View addMainView() {
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_tx_loging, null, false);
        return binding.getRoot();
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }


    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTitle("");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent() != null) {
            toUserId = getIntent().getStringExtra("toUserId");
            openType = getIntent().getStringExtra("openType");
        }

        if (openType.equals(OPEN_TYPE_ORDER)){

            ARouter.getInstance().build("/main/page").navigation();

            EventBusModel model;
            model = new EventBusModel();
            model.setTag(EventTags.MAIN_CHANGE_SHOW_INDEX);
            model.setEvInt(1);
            EventBus.getDefault().post(model);

        }else {
            if (!SPUtilHelper.isLoginNoStart() || !TXImManager.getInstance().isLogin()) { //如果没有登录的话先登录
                startLogin();
            } else {
                getUserInfoRequest(false); //登录--> 获取用户信息 -->获取腾讯签名-->登录腾讯--->登录成功
            }
        }


    }

    public void startLogin() {
        EventBus.getDefault().post(EventTags.MAINFINISH);
        EventBus.getDefault().post(EventTags.AllFINISH);
        // 路由跳转登录页面
        ARouter.getInstance().build("/user/login")
                .withBoolean("canOpenMain",true)
                .navigation();
        finish();
    }

    /**
     * 获取用户信息
     */
    public void getUserInfoRequest(final boolean isShowdialog) {

        if (TextUtils.isEmpty(toUserId)) {
            finish();
        }

        Map<String, String> map = new HashMap<>();

        map.put("userId", toUserId);
        map.put("token", SPUtilHelper.getUserToken());
        Call call = RetrofitUtils.getBaseAPiService().getUserInfoDetails("805121", StringUtils.getJsonToString(map));

        addCall(call);

        if (isShowdialog) showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<UserInfoModel>(this) {
            @Override
            protected void onSuccess(UserInfoModel data, String SucMessage) {
//                ImUserInfo info = new ImUserInfo();
//                info.setLeftImg(data.getPhoto());
//                info.setLeftName(data.getNickname());
//                info.setLeftUserId(data.getUserId());
//                info.setRightImg(SPUtilHelper.getUserPhoto());
//                info.setRightName(SPUtilHelper.getUserName());
//
//                ChatC2CActivity.open(TxImPushActivity.this, info);
//                finish();
            }

            @Override
            protected void onNoNet(String msg) {
                disMissLoading();
                finish();
            }

            @Override
            protected void onNull() {
                disMissLoading();
                finish();
            }

            @Override
            protected void onFinish() {
                if (isShowdialog)
                    disMissLoading();
            }
        });
    }


    @Override
    public void onBackPressed() {

    }
}
