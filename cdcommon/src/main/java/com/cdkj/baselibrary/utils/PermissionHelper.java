package com.cdkj.baselibrary.utils;

/**
 * Created by 李先俊 on 2017/8/14.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import java.util.Arrays;
import java.util.List;

/**
 * @usage android >=M 的权限申请统一处理
 *
 */
public class PermissionHelper {

    private static final int REQUEST_PERMISSION_CODE = 666;

    private Object mContext;

    private PermissionListener mListener;

    private List<String> mPermissionList;

    public PermissionHelper(@NonNull Object object){
        checkCallingObjectSuitability(object);
        this.mContext = object;

    }


    /**
     * 权限授权申请
     * @param permissions
     *              要申请的权限
     *
     * @param listener
     *              申请成功之后的callback
     */
    public void requestPermissions(@Nullable PermissionListener listener,
                                   @NonNull final String... permissions){

        if(listener != null){
            mListener = listener;
        }

        mPermissionList = Arrays.asList(permissions);

        //没全部权限
        if (!hasPermissions(mContext, permissions)) {
            LogUtil.E("需要申请权限");
            executePermissionsRequest(mContext, permissions,
                    REQUEST_PERMISSION_CODE);
        }else if(mListener != null) { //有全部权限
            LogUtil.E("不需要申请权限");
            mListener.doAfterGrand(permissions);
        }
    }

    /**
     * 权限授权申请
     * @param hintMessage
     *              要申请的权限的提示
     *
     * @param permissions
     *              要申请的权限
     *
     * @param listener
     *              申请成功之后的callback
     */
    public void requestPermissions(@NonNull CharSequence hintMessage,
                                   @Nullable PermissionListener listener,
                                   @NonNull final String... permissions){

        if(listener != null){
            mListener = listener;
        }

        mPermissionList = Arrays.asList(permissions);

        //没全部权限
        if (!hasPermissions(mContext, permissions)) {
            LogUtil.E("需要申请权限");
            //需要向用户解释为什么申请这个权限
            boolean shouldShowRationale = true;
            for (String perm : permissions) {
                shouldShowRationale =
                        shouldShowRationale || shouldShowRequestPermissionRationale(mContext, perm);
            }

            if (shouldShowRationale) {
                showMessageOKCancel(hintMessage, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        executePermissionsRequest(mContext, permissions,
                                REQUEST_PERMISSION_CODE);

                    }
                });
            }else {
                executePermissionsRequest(mContext, permissions,
                        REQUEST_PERMISSION_CODE);
            }
        }else if(mListener != null) { //有全部权限
            LogUtil.E("不需要申请权限");
            mListener.doAfterGrand(permissions);
        }
    }

    /**
     * 处理onRequestPermissionsResult
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void handleRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        LogUtil.E("权限回调");
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                boolean allGranted = true;
                for (int grant: grantResults) {
                    if(grant != PackageManager.PERMISSION_GRANTED){
                        allGranted = false;
                        break;
                    }
                }

                if (allGranted && mListener != null) {
                    LogUtil.E("有权限");
                    mListener.doAfterGrand((String[])mPermissionList.toArray());

                }else if(!allGranted && mListener != null){
                    LogUtil.E("没有权限");
                    mListener.doAfterDenied((String[])mPermissionList.toArray());
                }
                break;
        }
    }

    /**
     * 判断是否具有某权限
     * @param object
     * @param perms
     * @return
     */
    public static boolean hasPermissions(@NonNull Object object, @NonNull String... perms) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String perm : perms) {
            boolean hasPerm = (ContextCompat.checkSelfPermission(getActivity(object), perm) ==
                    PackageManager.PERMISSION_GRANTED);
            if (!hasPerm) {
                return false;
            }
        }

        return true;
    }




    /**
     * 兼容fragment
     * @param object
     * @param perm
     * @return
     */
    @TargetApi(23)
    private static boolean shouldShowRequestPermissionRationale(@NonNull Object object, @NonNull String perm) {
        if (object instanceof Activity) {
            return ActivityCompat.shouldShowRequestPermissionRationale((Activity) object, perm);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).shouldShowRequestPermissionRationale(perm);
        } else {
            return false;
        }
    }

    /**
     * 执行申请,兼容fragment
     * @param object
     * @param perms
     * @param requestCode
     */
    @TargetApi(23)
    private void executePermissionsRequest(@NonNull Object object, @NonNull String[] perms, int requestCode) {
        if (object instanceof Activity) {
            ActivityCompat.requestPermissions((Activity) object, perms, requestCode);
        } else if (object instanceof Fragment) {
            ((Fragment) object).requestPermissions(perms, requestCode);
        } else if (object instanceof android.app.Fragment) {
            ((android.app.Fragment) object).requestPermissions(perms, requestCode);
        }
    }

    /**
     * 检查传递Context是否合法
     * @param object
     */
    private void checkCallingObjectSuitability(@Nullable Object object) {
        if (object == null) {
            throw new NullPointerException("Activity or Fragment should not be null");
        }

        boolean isActivity = object instanceof Activity;
        boolean isSupportFragment = object instanceof Fragment;
        boolean isAppFragment = object instanceof android.app.Fragment;
        if (!(isSupportFragment || isActivity || (isAppFragment && isNeedRequest()))) {
            if (isAppFragment) {
                throw new IllegalArgumentException(
                        "Target SDK needs to be greater than 23 if caller is android.app.Fragment");
            } else {
                throw new IllegalArgumentException("Caller must be an Activity or a Fragment.");
            }
        }
    }


    @TargetApi(11)
    public static Activity getActivity(@NonNull Object object) {
        if (object instanceof Activity) {
            return ((Activity) object);
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof android.app.Fragment) {
            return ((android.app.Fragment) object).getActivity();
        } else {
            return null;
        }
    }

    public static boolean isNeedRequest(){
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public void showMessageOKCancel(CharSequence message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity(mContext))
                .setMessage(message)
                .setPositiveButton("确定", okListener)
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    public interface PermissionListener {

        void doAfterGrand(String... permission);//全部权限获取成功

        void doAfterDenied(String... permission);//失败
    }
}
