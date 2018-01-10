package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.model.IsSuccessModes;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityUserPersonBinding;
import com.cdkj.bcoin.model.DealUserDataModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.StringUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/12/5.
 */

public class UserPersonActivity extends AbsBaseActivity {

    private ActivityUserPersonBinding mBinding;

    private String userId;
    private String nickName;
    private String photo;

    public static void open(Context context, String userId, String nickName, String photo){
        if (context == null) {
            return;
        }

        // 自己不能打开个人主页
       if (!SPUtilHelper.getUserId().equals(userId)){
            context.startActivity(new Intent(context, UserPersonActivity.class)
                    .putExtra("userId", userId)
                    .putExtra("photo", photo)
                    .putExtra("nickName", nickName));
        }

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_user_person, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initListener();

        if (getIntent() == null)
            return;

        userId = getIntent().getStringExtra("userId");
        photo = getIntent().getStringExtra("photo");
        nickName = getIntent().getStringExtra("nickName");

        getUserData();
    }

    private void initListener() {
        mBinding.flBack.setOnClickListener(view -> {
            finish();
        });

        mBinding.btnTrust.setOnClickListener(view -> {
            if (mBinding.btnTrust.getText().equals(StringUtil.getStirng(R.string.get_trust))){
                trustOrList("1", "805110");
            }else {
                trustOrList("1", "805111");
            }
        });

        mBinding.btnBlackList.setOnClickListener(view -> {
            if (mBinding.btnBlackList.getText().equals(StringUtil.getStirng(R.string.add_black_list))){
                trustOrList("0", "805110");
            }else {
                trustOrList("0", "805111");
            }
        });
    }

    @Override
    protected boolean canLoadTopTitleView() {
        return false;
    }

    /**
     * 查询用户统计信息（包含信任 和 黑名单关系）
     */
    private void getUserData(){
        Map<String, Object> map = new HashMap<>();
        map.put("master", userId);
        map.put("visitor", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealUserData("625256", StringUtils.getJsonToString(map));

        addCall(call);

        call.enqueue(new BaseResponseModelCallBack<DealUserDataModel>(this) {

            @Override
            protected void onSuccess(DealUserDataModel data, String SucMessage) {
                if (data == null)
                    return;
                setShowData(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setShowData(DealUserDataModel data) {
        // 设置昵称和头像
        mBinding.tvName.setText(nickName);
        ImgUtils.loadAvatar(UserPersonActivity.this, photo, nickName, mBinding.ivAvatar, mBinding.tvAvatar);

        mBinding.tvTimes.setText(Html.fromHtml(getString(R.string.user_person_deal)+"<font color='#f15353'>" + data.getBetweenTradeTimes() + "</font>"+getString(R.string.user_person_times)));

        mBinding.tvDeal.setText(data.getJiaoYiCount()+"");
        mBinding.tvTrust.setText(data.getBeiXinRenCount()+"");
        if(data.getBeiPingJiaCount() == 0){
            mBinding.tvGood.setText("0%");
        }else {
            double hpRate = data.getBeiHaoPingCount() / data.getBeiPingJiaCount();
            mBinding.tvGood.setText(AccountUtil.formatInt(hpRate * 100)+"%");
        }

        double dh = Double.parseDouble(AccountUtil.weiToEth(new BigDecimal(data.getTotalTradeCount())));
        if(dh == 0){
            mBinding.tvHistory.setText("0 ETH");
        } else if (dh < 0.5){
            mBinding.tvHistory.setText("0-0.5 ETH");
        }else if(0.5 <= dh && dh <= 1){
            mBinding.tvHistory.setText("0.5-1 ETH");
        }else {
            mBinding.tvHistory.setText(AccountUtil.weiToEth(new BigDecimal(data.getTotalTradeCount())).split("\\.")[0]+"+ ETH");
        }

        if(data.getIsTrust().equals("0")){ // 未信任
            mBinding.btnTrust.setText(StringUtil.getStirng(R.string.get_trust));
        }else { // 已信任
            mBinding.btnTrust.setText(StringUtil.getStirng(R.string.lost_trust));
        }

        if(data.getIsAddBlackList().equals("0")){ // 未被加入黑名单
            mBinding.btnBlackList.setText(StringUtil.getStirng(R.string.add_black_list));
        }else { // 已被加入黑名单
            mBinding.btnBlackList.setText(StringUtil.getStirng(R.string.remove_black_list));
        }

    }

    private void trustOrList(String type, String code) {

        Map<String, Object> map = new HashMap<>();
        map.put("toUser", userId);
        map.put("type", type); //黑名单相关的type = 0, 信任type = 1
        map.put("userId", SPUtilHelper.getUserId());
        map.put("token", SPUtilHelper.getUserToken());

        Call call = RetrofitUtils.getBaseAPiService().successRequest(code, StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IsSuccessModes>(this) {

            @Override
            protected void onSuccess(IsSuccessModes data, String SucMessage) {
                if (data == null)
                    return;

                if (data.isSuccess())
                    getUserData();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }
}
