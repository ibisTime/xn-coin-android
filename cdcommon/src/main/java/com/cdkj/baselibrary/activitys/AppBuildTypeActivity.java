package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityAppBuildTypeBinding;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lei on 2017/12/1.
 */

public class AppBuildTypeActivity extends AbsBaseActivity {

    private ActivityAppBuildTypeBinding mBinding;

    public static void open(Context context){
        if (context == null) {
            return;
        }
        context.startActivity(new Intent(context, AppBuildTypeActivity.class));
    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_app_build_type, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        if (!MyConfig.IS_DEBUG){
            finish();
        }
        setTopTitle("环境切换");
        setTopLineState(true);
        setSubLeftImgState(true);

        init();
    }

    private void init() {

        mBinding.llBuildDebug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuildType(v,SPUtilHelper.BUILD_TYPE_DEBUG);
            }
        });

        mBinding.llBuildTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBuildType(v,SPUtilHelper.BUILD_TYPE_TEST);
            }
        });

        Glide.with(this).load(R.mipmap.pu).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mBinding.ivDebug);
        Glide.with(this).load(R.mipmap.pu).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mBinding.ivTest);
    }

    public void setBuildType(View view, String type){

        if (SPUtilHelper.getAPPBuildType().equals(type)){

            popupWqbb(view, type);

        }else {
            popupMa(view, type);
        }

    }

    private void popupWqbb(View view, final String type) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_wqbb, null);

        final TextView tvTip = (TextView) mView.findViewById(R.id.tv_tip);
        TextView tvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        final TextView tvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        switch (type){
            case SPUtilHelper.BUILD_TYPE_DEBUG:
                tvTip.setText("已经是研发环境了，您还要我怎样?");
                tvConfirm.setText("对不起，那切换到测试吧");
                break;

            case SPUtilHelper.BUILD_TYPE_TEST:
                tvTip.setText("已经是测试环境了，您还要我怎样?");
                tvConfirm.setText("对不起，那切换到研发吧");
                break;
        }


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                switch (type){
                    case SPUtilHelper.BUILD_TYPE_DEBUG:
                        SPUtilHelper.setAPPBuildType(SPUtilHelper.BUILD_TYPE_TEST);
                        break;

                    case SPUtilHelper.BUILD_TYPE_TEST:
                        SPUtilHelper.setAPPBuildType(SPUtilHelper.BUILD_TYPE_DEBUG);
                        break;
                }

                close();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void popupMa(View view, final String type) {

        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_ma, null);

        final TextView tvTip = (TextView) mView.findViewById(R.id.tv_tip);
        TextView tvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        final TextView tvConfirm = (TextView) mView.findViewById(R.id.tv_confirm);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        switch (type){
            case SPUtilHelper.BUILD_TYPE_DEBUG:
                tvTip.setText("大佬，您确定要切换到研发环境吗?");
                break;

            case SPUtilHelper.BUILD_TYPE_TEST:
                tvTip.setText("大佬，您确定要切换到测试环境吗?");
                break;
        }


        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                switch (type){
                    case SPUtilHelper.BUILD_TYPE_DEBUG:
                        SPUtilHelper.setAPPBuildType(SPUtilHelper.BUILD_TYPE_DEBUG);
                        break;

                    case SPUtilHelper.BUILD_TYPE_TEST:
                        SPUtilHelper.setAPPBuildType(SPUtilHelper.BUILD_TYPE_TEST);
                        break;
                }

                close();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }

    private void close(){
        SPUtilHelper.logOutClear();
        EventBus.getDefault().post(EventTags.AllFINISH);
        finish();

        // 初始化Retrofit
        EventBus.getDefault().post(EventTags.BUILD_TYPE);

        // 路由跳转开始页面
        ARouter.getInstance().build("/user/start")
                .navigation();
    }

}
