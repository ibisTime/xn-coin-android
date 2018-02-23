package com.cdkj.baselibrary.activitys;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityWebviewBinding;
import com.cdkj.baselibrary.model.IntroductionInfoModel;
import com.cdkj.baselibrary.nets.BaseResponseModelCallBack;
import com.cdkj.baselibrary.nets.RetrofitUtils;
import com.cdkj.baselibrary.utils.PermissionHelper;
import com.cdkj.baselibrary.utils.StringUtils;
import com.cdkj.baselibrary.utils.WxUtil;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;


/**
 * 介绍类webview
 */
public class WebViewActivity extends AbsBaseActivity {

    private ActivityWebviewBinding mBinding;

    private PermissionHelper permissionHelper;

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
        //JS映射
        mBinding.webView.addJavascriptInterface(new WebHost(this), "js");

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
        map.put("ckey", key);
        map.put("systemCode", MyConfig.SYSTEMCODE);
        map.put("companyCode", MyConfig.COMPANYCODE);

        Call call = RetrofitUtils.getBaseAPiService().getKeySystemInfo("660917", StringUtils.getJsonToString(map));

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


    private void getViewCache(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap tBitmap = view.getDrawingCache();
        // 拷贝图片，否则在setDrawingCacheEnabled(false)以后该图片会被释放掉
        tBitmap = tBitmap.createBitmap(tBitmap);
        view.setDrawingCacheEnabled(false);
        if (tBitmap != null) {

            popupSelect(view, tBitmap);

        } else {
            showToast(getString(R.string.web_create_pic_failure));
        }
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

    /**
     * JS映射类
     */
    public class WebHost {
        public Context context;

        public WebHost(Context context){
            this.context = context;
        }

        @JavascriptInterface
        public void acllJs(){ // JS事件映射
            getViewCache(mBinding.webView);
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

    private void popupSelect(final View view, final Bitmap bitmap) {


        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_web_select, null);

        RelativeLayout rlClose = (RelativeLayout) mView.findViewById(R.id.rl_close);
        TextView tvSend = (TextView) mView.findViewById(R.id.tv_send);
        TextView tvKeep = (TextView) mView.findViewById(R.id.tv_keep);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        rlClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupInvite(view,bitmap);
                popupWindow.dismiss();
            }
        });

        tvKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keep(bitmap);
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }


    private void keep(final Bitmap bitmap){
        permissionHelper = new PermissionHelper(this);

        permissionHelper.requestPermissions(new PermissionHelper.PermissionListener() {
            @Override
            public void doAfterGrand(String... permission) {

                ImageSelectActivity.saveImageToGallery(WebViewActivity.this,bitmap);
            }

            @Override
            public void doAfterDenied(String... permission) {
                showToast(getString(R.string.storage_refused));
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    //权限处理
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionHelper.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void popupInvite(View view, final Bitmap bitmap) {


        // 一个自定义的布局，作为显示的内容
        View mView = LayoutInflater.from(this).inflate(R.layout.popup_web_sharet, null);

        LinearLayout llClose = (LinearLayout) mView.findViewById(R.id.ll_close);
        LinearLayout llWx = (LinearLayout) mView.findViewById(R.id.ll_wx);
        LinearLayout llPyq = (LinearLayout) mView.findViewById(R.id.ll_pyq);

        final PopupWindow popupWindow = new PopupWindow(mView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        popupWindow.setTouchable(true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);

        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        llClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        llWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WxUtil.shareImg(WebViewActivity.this, false, bitmap);
                popupWindow.dismiss();
            }
        });

        llPyq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WxUtil.shareImg(WebViewActivity.this, true, bitmap);
                popupWindow.dismiss();
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.corner_popup));
        // 设置好参数之后再show
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 50);

    }


}
