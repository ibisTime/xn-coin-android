package com.cdkj.bcoin.deal;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import com.cdkj.baseim.fragment.ChatFragment;
import com.cdkj.baseim.model.ImUserInfo;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityDealChatBinding;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.tencent.imsdk.TIMConversationType;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.MyConfig.CURRENCY;
import static com.cdkj.bcoin.util.AccountUtil.formatDouble;

/**
 * Created by lei on 2017/11/25.
 */

public class DealChatActivity extends AbsBaseActivity {

    private ActivityDealChatBinding mBinding;

    private OrderDetailModel model;
    private ImUserInfo imUserInfo;

    public static void open(Context context, OrderDetailModel model, ImUserInfo imUserInfo){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, DealChatActivity.class).putExtra("model", model).putExtra("imUserInfo",imUserInfo));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_deal_chat, null ,false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
        initListener();
    }

    private void init() {

        // 重写键盘，不根据点击坐标隐藏键盘，避免与聊天Fragment冲突
        isNeedHideKeyBord = false;

        if (getIntent() == null)
            return;

        model = (OrderDetailModel) getIntent().getSerializableExtra("model");
        imUserInfo = getIntent().getParcelableExtra("imUserInfo");

        getDeal();
        initChatFragment();
    }

    private void initListener() {
        // 重写点击Activity布局隐藏键盘
        mBinding.llDealHide.setOnClickListener(this::hideKeyboard);
    }

    private void initChatFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        transaction.add(R.id.layout_chat, ChatFragment.getInstance(imUserInfo, TIMConversationType.Group));
        transaction.commit();
    }


    private void getDeal() {
        Map<String, String> map = new HashMap<>();
        map.put("adsCode", model.getAdsCode());
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApi.class).getDealDetail("625226", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<DealDetailModel>(this) {

            @Override
            protected void onSuccess(DealDetailModel data, String SucMessage) {
                if (data == null)
                    return;


                setView(data);
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });
    }

    private void setView(DealDetailModel data) {
        mBinding.tvPrice.setText(getStrRes(R.string.quoted)+ AccountUtil.formatDouble(data.getTruePrice())+CURRENCY);
        mBinding.tvLimit.setText(getStrRes(R.string.limit)+data.getMinTrade()+"-"+formatDouble(data.getMaxTrade())+CURRENCY);

        if (data.getTradeType().equals("1")){ // 1是卖币，UI展示买币
            setTopTitle(getStrRes(R.string.buy_order)+"("+data.getUser().getNickname()+")");
            mBinding.btnConfirm.setText(getStrRes(R.string.buy));
        }else{ //反之
            setTopTitle(getStrRes(R.string.sale_order)+"("+data.getUser().getNickname()+")");
            mBinding.btnConfirm.setText(getStrRes(R.string.sale));
        }

        Double leftAmount = Double.parseDouble(data.getLeftCountString());
        if (leftAmount == 0){
            mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn_gray);
        }else {
            mBinding.btnConfirm.setBackgroundResource(R.drawable.corner_order_btn);
        }

        mBinding.btnConfirm.setOnClickListener(view -> {
            DealActivity.open(this, data.getCode());
            finish();
        });
    }
}
