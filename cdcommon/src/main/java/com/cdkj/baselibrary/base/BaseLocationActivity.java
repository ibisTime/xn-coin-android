package com.cdkj.baselibrary.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.dialog.CommonDialog;
import com.cdkj.baselibrary.utils.AppUtils;
import com.cdkj.baselibrary.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 有定位需求页面的Activity都可继承此Activity 实现定位权限申请
 * Created by Administrator on 2017-02-13.
 */

public abstract class BaseLocationActivity extends AbsBaseActivity {

    protected static final int PERMISSION__LOCATION_REQUESTCODE = 13;//定位权限检测请求码
    protected static final int APPLICATION_DETAIL_REQUESTCODE = 14;//应用详情界面请求码
    //定位功能
    protected AMapLocationClient locationClient = null;


    //需要进行定位功能的权限数组
    protected String[] needLocationPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    /**
     * 定位成功
     *
     * @return
     */
    protected abstract void locationSuccessful(AMapLocation aMapLocation);

    /**
     * 定位失败
     *
     * @param aMapLocation
     */
    protected abstract void locationFailure(AMapLocation aMapLocation);


    /**
     * 用于权限获取失败时点击取消按钮
     */
    protected abstract void onNegativeButton();


    /**
     * 初始化定位
     */
    protected void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if (null != aMapLocation && aMapLocation.getErrorCode() == 0) {  //定位成功
                    LogUtil.E("定位成功");
                    locationSuccessful(aMapLocation);
                } else {                                        //定位失败
                    LogUtil.E("定位失败" + aMapLocation.getErrorCode() + aMapLocation.getErrorInfo());
                    locationFailure(aMapLocation);
                }
                //停止定位
                stopLocation();
            }
        });


    }


    /**
     * 启动定位
     */
    protected void startLocation() {
        // 启动定位
        //如果Android6.0进行安卓权限检测
        if (AppUtils.getAndroidVersion(Build.VERSION_CODES.M)) {
            checkPermissions(needLocationPermissions);
        } else {
            // 启动定位
            if (locationClient != null) {
                locationClient.setLocationOption(getDefaultOption());
                locationClient.startLocation();
                LogUtil.E("开始定位 ");
            }

        }
    }

    /**
     * 销毁定位
     */
    private void destroyLocation() {
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    /**
     * 默认定位参数
     *
     * @return
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        mOption.setLocationCacheEnable(false);  // 设置是否开启缓存
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }

    /**
     * 需要权限检查
     *
     * @param permissions
     */
    private void checkPermissions(String... permissions) {
        List<String> needRequestPermissonList = findDeniedPermissions(permissions);
        if (null != needRequestPermissonList
                && needRequestPermissonList.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    needRequestPermissonList.toArray(
                            new String[needRequestPermissonList.size()]),
                    PERMISSION__LOCATION_REQUESTCODE);
        } else {//安卓6.0已授权的，开始定位
            if (locationClient != null) {
                locationClient.startLocation();
            }
        }
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<String>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this,
                    perm) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(
                    this, perm)) {
                needRequestPermissonList.add(perm);
            }
        }
        return needRequestPermissonList;
    }

    /**
     * 是否允许提示弹框
     *
     * @return
     */
    protected boolean canShowTipsDialog() {
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION__LOCATION_REQUESTCODE) {//定位权限请求返回
            if (!verifyPermissions(grantResults)) {
                if (canShowTipsDialog()) {
                    showMissingPermissionDialog();
                }
            } else {
                startLocation();
            }
        }
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {

        new CommonDialog(this).builder()
                .setTitle("系统提示").setContentMsg("未取得您的位置信息使用权限，定位功能无法使用。请前往应用权限设置打开权限。")

                .setPositiveBtn("去打开", new CommonDialog.OnPositiveListener() {
                    @Override
                    public void onPositive(View view) {
                        // 根据包名跳转到系统自带的应用程序信息界面
                        startAppSettings();
                        finish();
                    }
                })
                .setNegativeBtn("取消", new CommonDialog.OnNegativeListener() {
                    @Override
                    public void onNegative(View view) {
                        onNegativeButton();
                    }
                }).show();
    }


    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    /**
     * 启动应用详情的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, APPLICATION_DETAIL_REQUESTCODE);

    }


    /**
     * 停止定位
     */
    protected void stopLocation() {
        // 停止定位
        if (locationClient != null) {
            locationClient.stopLocation();
            LogUtil.E("停止定位");
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        initLocation();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁定位
        destroyLocation();
    }
}
