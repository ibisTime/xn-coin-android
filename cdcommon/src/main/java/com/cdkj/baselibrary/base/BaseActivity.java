package com.cdkj.baselibrary.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.alibaba.android.arouter.launcher.ARouter;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.dialog.LoadingDialog;
import com.cdkj.baselibrary.model.EventBusModel;
import com.cdkj.baselibrary.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;

import static com.cdkj.baselibrary.appmanager.EventTags.EVENT_REFRESH_LANGUAGE;


/**
 * Actvity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    // 是否需要根据点击坐标来显示键盘
    public boolean isNeedHideKeyBord = true;

    protected LoadingDialog loadingDialog;
    private List<Call> mCallList;
    protected CompositeDisposable mSubscription;

    public static final String TRADITIONAL = "traditional";
    public static final String SIMPLIFIED = "simplified";
    public static final String ENGLISH = "english";

    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mSubscription = new CompositeDisposable();
        loadingDialog = new LoadingDialog(this);
        EventBus.getDefault().register(this);
        mCallList = new ArrayList<>();

        setAppLanguage();

    }


    protected void addCall(Call call) {
        mCallList.add(call);
    }

    protected void clearCall() {

        for (Call call : mCallList) {
            if (call == null) {
                continue;
            }
            call.cancel();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        clearCall();

        if (mSubscription != null){
            mSubscription.dispose();
            mSubscription.clear();
        }

        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        EventBus.getDefault().unregister(this);
    }


    /**
     * 隐藏Dialog
     */
    public void disMissLoading() {

        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.closeDialog();
        }
    }

    /**
     * 显示dialog
     */
    public void showLoadingDialog() {
        if(loadingDialog==null){
            loadingDialog=new LoadingDialog(this);
        }
        if (loadingDialog != null && !loadingDialog.isShowing()) {
            loadingDialog.showDialog();
        }
    }


    public void showToast(String str) {
        ToastUtil.show(this, str);
    }

    protected void showDoubleWarnListen(String str, CommonDialog.OnPositiveListener onPositiveListener) {

        if (isFinishing()) {
            return;
        }

        CommonDialog commonDialog = new CommonDialog(this).builder()
                .setTitle(getString(R.string.activity_base_tip)).setContentMsg(str)
                .setPositiveBtn(getString(R.string.activity_base_confirm), onPositiveListener)
                .setNegativeBtn(getString(R.string.activity_base_cancel), null, false);

        commonDialog.show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev) && isNeedHideKeyBord) {
                hideKeyboard(v.getWindowToken());
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */

    private boolean isShouldHideKeyboard(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    /**
     * 隐藏键盘
     */
    public void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public void hideKeyboard(Activity activity) {
        if (activity == null || activity.getWindow() == null) {
            return;
        }
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            hideKeyboard(view);
        }
    }

    @Override
    public void finish() {
        hideKeyboard(this);
        super.finish();
    }

    @Subscribe
    public void finishAll(String i) {
        if (TextUtils.equals(EventTags.AllFINISH, i)) {
            if (canEvenFinish()) {
                this.finish();
            }
        }
    }

    /**
     * 能否通过 EventBUS事件结束
     * @return
     */
    protected boolean canEvenFinish() {
        return true;
    }

    /**
     * 设置状态栏颜色（5.0以上系统）
     *
     * @param stateTitleColor
     */
    public void setStateTitleColor(int stateTitleColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0以上系统实现状态栏颜色改变
            try {
                Window window = getWindow();
                //取消设置透明状态栏,使 ContentView 内容不再沉浸到状态栏下
                if (window != null) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    //设置状态栏颜色
                    window.setStatusBarColor(stateTitleColor);
                }
            } catch (Exception e) {

            }
        }
    }

    /**
     * 获取StringRes
     * @return StringRes
     */
    public String getStrRes(int resources){

        return getString(resources);
    }

    /**
     * * 获取版本号
     * @return 当前应用的版本号
     */
    public int getVersionCode() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            int versionCode = info.versionCode;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * * 获取版本名
     * @return 当前应用的版本名
     */
    public String getVersionName() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String versionCode = info.versionName;
            return versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return "未能检测出当前版本";
        }
    }

    /**
     * 设置APP使用的语言
     */
    public void setAppLanguage() {
        Locale myLocale;

        switch (SPUtilHelper.getLanguage()){
            case ENGLISH:
                myLocale = Locale.ENGLISH;
                break;

            case TRADITIONAL:
                myLocale = Locale.TRADITIONAL_CHINESE;
                break;

            default:
                myLocale = Locale.SIMPLIFIED_CHINESE;
                break;
        }

        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

    }

    @Subscribe
    public void onEvent(EventBusModel model) {
        if (model.getTag().equals(EVENT_REFRESH_LANGUAGE)){

            try {
                setAppLanguage();

                //刷新界面
                ARouter.getInstance().build("/main/page").navigation();
                EventBus.getDefault().post(EventTags.AllFINISH);

            }catch (Exception e){
                e.printStackTrace();
            }


        }
    }
}
