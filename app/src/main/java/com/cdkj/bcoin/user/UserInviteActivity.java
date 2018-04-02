package com.cdkj.bcoin.user;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.cdkj.baselibrary.activitys.WebViewActivity;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.nets.BaseResponseListCallBack;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.WxUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.api.MyApi;
import com.cdkj.bcoin.databinding.ActivityInviteBinding;
import com.cdkj.bcoin.loader.BannerImageLoader;
import com.cdkj.bcoin.model.BannerModel;
import com.cdkj.bcoin.model.InviteModel;
import com.cdkj.bcoin.model.SystemParameterModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by lei on 2017/11/1.
 */

public class UserInviteActivity extends AbsBaseActivity {

    private ActivityInviteBinding mBinding;

    private String regUrl;

    private List<String> banner = new ArrayList<>();
    private List<BannerModel> bannerData = new ArrayList<>();

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, UserInviteActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_invite, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setTopTitle(getString(R.string.user_invite_title));
        setTopLineState(true);
        setSubLeftImgState(true);
        setSubRightTitleAndClick(getString(R.string.user_invite_title_right), v -> {
            UserInviteHistoryActivity.open(this);
        });

        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();

        getRegUrl();
        getBanner();
        getActivityRule();
        getInviteInfo();
    }

    private void initListener() {
        mBinding.btnUrl.setOnClickListener(view -> {
            popupInvite(view);
        });

        mBinding.btnPic.setOnClickListener(view -> {
            WebViewActivity.openURL(this, getStrRes(R.string.user_invite_title_web),regUrl+"/user/qrcode.html?inviteCode="+SPUtilHelper.getSecretUserId());
        });

        mBinding.llInvite.setOnClickListener(view -> {
            UserInviteHistoryActivity.open(this);
        });

        mBinding.llInviteMore.setOnClickListener(view -> {
            UserInviteProfitActivity.open(this);
        });
    }


    private void popupInvite(View view) {
        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_web_sharet, null);

        LinearLayout llClose = (LinearLayout) mView.findViewById(R.id.ll_close);
        LinearLayout llWx = (LinearLayout) mView.findViewById(R.id.ll_wx);
        LinearLayout llPyq = (LinearLayout) mView.findViewById(R.id.ll_pyq);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.PopupAnimation);

        popupWindow.setTouchInterceptor((v, event) -> {

            // 这里如果返回true的话，touch事件将被拦截
            // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            return false;
        });

        llClose.setOnClickListener(v -> {
            popupWindow.dismiss();
        });

        llWx.setOnClickListener(v -> {
            WxUtil.shareText(this, false, regUrl+"/user/register.html?inviteCode="+SPUtilHelper.getSecretUserId(), "邀请好友", "即将开启新币种push交易", R.mipmap.app_icon);
            popupWindow.dismiss();
        });

        llPyq.setOnClickListener(v -> {
            WxUtil.shareText(this, true, regUrl+"/user/register.html?inviteCode="+SPUtilHelper.getSecretUserId(), "邀请好友", "即将开启新币种push交易", R.mipmap.app_icon);
            popupWindow.dismiss();
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    public void getInviteInfo(){
        Map<String, Object> map = new HashMap<>();
        map.put("userId", SPUtilHelper.getUserId());

        Call call = RetrofitUtils.createApi(MyApi.class).getInvite("805123", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<InviteModel>(this) {

            @Override
            protected void onSuccess(InviteModel data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.tvCount.setText(data.getInviteCount()+"");

                mBinding.tvProfitBtc.setVisibility(View.VISIBLE);
                mBinding.tvProfitBtc.setText(AccountUtil.amountFormatUnit(new BigDecimal(data.getInviteProfitBtc()), "BTC", 8)+"BTC");

            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取分享链接
     * @return
     */
    private void getRegUrl() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "reg_url");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                regUrl = data.getCvalue();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    /**
     * 获取banner
     */
    private void getBanner() {
        Map<String, String> map = new HashMap<>();
        map.put("location", "activity"); // 轮播位置
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getBanner("805806", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseListCallBack<BannerModel>(this) {


            @Override
            protected void onSuccess(List<BannerModel> data, String SucMessage) {
                if (data != null){
                    bannerData = data;
                    banner.clear();
                    for (BannerModel model : data) {
                        banner.add(model.getPic());
                    }
                }

                initBanner();
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

    private void initBanner() {
        if (banner == null) return;

        //设置banner样式
        mBinding.banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBinding.banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        mBinding.banner.setImages(banner);
        //设置banner动画效果
        mBinding.banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        banner.setBannerTitles(Arrays.asList(titles));
        //设置自动轮播，默认为true
        mBinding.banner.isAutoPlay(true);
        //设置轮播时间
        mBinding.banner.setDelayTime(3500);
        //设置指示器位置（当banner模式中有指示器时）
        mBinding.banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置banner点击事件
        mBinding.banner.setOnBannerClickListener(position -> {

            if (bannerData.get(position-1).getUrl()!=null){
                if (bannerData.get(position-1).getUrl().indexOf("http") != -1){
                    WebViewActivity.openURL(this, bannerData.get(position-1).getName(),bannerData.get(position-1).getUrl());
                }

            }

        });
        //banner设置方法全部调用完毕时最后调用
        mBinding.banner.start();

        // 设置在操作Banner时listView事件不触发
//        mBinding.banner.setOnPageChangeListener(new MyPageChangeListener());
    }

    /**
     * 获取活动规则
     * @return
     */
    private void getActivityRule() {
        Map<String, String> map = new HashMap<>();
        map.put("ckey", "activity_rule");
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.createApi(MyApi.class).getSystemParameter("660917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<SystemParameterModel>(this) {

            @Override
            protected void onSuccess(SystemParameterModel data, String SucMessage) {
                if (data == null)
                    return;

                mBinding.tvRule.setText(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });

    }

}
