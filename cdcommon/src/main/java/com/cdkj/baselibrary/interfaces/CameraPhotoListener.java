package com.cdkj.baselibrary.interfaces;

/**
 * 启用相机相册监听
 * Created by cdkj on 2017/11/7.
 */

public interface CameraPhotoListener {

    void onPhotoSuccessful(int requestCode, String path);

    void onPhotoFailure(int requestCode, String msg);

    void noPermissions(int requestCode);//没有权限

}
