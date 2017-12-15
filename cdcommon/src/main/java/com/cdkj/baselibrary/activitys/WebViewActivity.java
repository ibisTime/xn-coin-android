package com.cdkj.baselibrary.activitys;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityWebviewBinding;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 介绍类webview
 */
public class WebViewActivity extends AbsBaseActivity {

    private ActivityWebviewBinding mBinding;


    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void openkey(Activity activity, String title, String code) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("code", code);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }

    /**
     * 加载activity,加载富文本
     *
     * @param activity 上下文
     */
    public static void openContent(Context activity, String title, String content) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("content", content);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void openURL(Activity activity, String title, String url) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }


    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_webview, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);
        setTopLineState(true);
        initLayout();
        initData();

    }

    private void initLayout() {
        //输入法
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        mBinding.webView.getSettings().setJavaScriptEnabled(true);//js
        mBinding.webView.getSettings().setDefaultTextEncodingName("UTF-8");
//        mBinding.webView.getSettings().setSupportZoom(true);   //// 支持缩放
//        mBinding.webView.getSettings().setBuiltInZoomControls(true);//// 支持缩放
//        mBinding.webView.getSettings().setDomStorageEnabled(true);//开启DOM
//        mBinding.webView.getSettings().setLoadWithOverviewMode(false);//// 缩放至屏幕的大小
//        mBinding.webView.getSettings().setUseWideViewPort(true);//将图片调整到适合webview的大小
//        mBinding.webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        mBinding.webView.setWebChromeClient(new MyWebViewClient1());
//        webView.setWebViewClient(new MyWebViewClient());
        mBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }

        setTopTitle(getIntent().getStringExtra("title"));

        if (TextUtils.isEmpty(getIntent().getStringExtra("url"))) {

            if (TextUtils.isEmpty(getIntent().getStringExtra("content"))){
                getKeyUrl(getIntent().getStringExtra("code"));
            }else {
                showContent(getIntent().getStringExtra("content"));
            }
        } else {
            mBinding.webView.loadUrl(getIntent().getStringExtra("url"));
        }
    }


    public void getKeyUrl(String key) {

        if (TextUtils.isEmpty(key)) {
            return;
        }

        Map<String, String> map = new HashMap<>();
        map.put("key", key);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("625917", StringUtils.getJsonToString(map));

        addCall(call);

        showLoadingDialog();

        call.enqueue(new BaseResponseModelCallBack<IntroductionInfoModel>(this) {
            @Override
            protected void onSuccess(IntroductionInfoModel data, String SucMessage) {
                if (TextUtils.isEmpty(data.getCvalue())) {
                    return;
                }
                showContent(data.getCvalue());
            }

            @Override
            protected void onFinish() {
                disMissLoading();
            }
        });


    }

    private void showContent(String content){
        mBinding.webView.loadData(content , "text/html;charset=UTF-8", "UTF-8");
    }

    private class MyWebViewClient1 extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            mBinding.pb.setProgress(newProgress);

            if (newProgress > 90) {
                mBinding.pb.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (mBinding.webView.canGoBack()) {
            mBinding.webView.goBack();
        } else {
            finish();
        }
    }

}
