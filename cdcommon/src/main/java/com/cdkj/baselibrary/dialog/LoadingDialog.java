package com.cdkj.baselibrary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.cdkj.baselibrary.R;


public final class LoadingDialog extends Dialog {

    //    private ImageView mInnerImg;
    private ImageView mOuterImg;
    private Animation mAnimation;
    private int mWidth;
    private int mHeight;

    public static LoadingDialog loadingDialog;

//    private android.widget.MediaController mediaController;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogLight);
        this.mWidth = ViewGroup.LayoutParams.WRAP_CONTENT;
        this.mHeight = ViewGroup.LayoutParams.WRAP_CONTENT;
    }


    public LoadingDialog(Context context, int width, int height) {
        super(context);
        this.mWidth = width;
        this.mHeight = height;
    }

    public LoadingDialog(Context context, int width, int height, int theme) {
        super(context, theme);
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        getWindow().setLayout(mWidth, mHeight);  //设置宽高
        getWindow().setGravity(Gravity.CENTER);  //设置居中
        initData();
        initView();
    }

    private void initData() {
        mAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_rotate);
        setCancelable(false);  // 设置当返回键按下是否关闭对话框
        setCanceledOnTouchOutside(false);  // 设置当点击对话框以外区域是否关闭对话框
    }

    private void initView() {
        mOuterImg = (ImageView) findViewById(R.id.loading_outer_img);
//        mOuterImg = (GifImageView) findViewById(R.id.gif_image);
//        mOuterImg.setImageResource(R.drawable.loading);
//        mediaController = new android.widget.MediaController(mContext);
//        mediaController.setMediaPlayer((GifDrawable) mOuterImg.getDrawable());
    }

    public void showDialog() {
        try {
            if (!isShowing()) {
                show();
            }
        } catch (Exception e) {

        }
    }

    public void closeDialog() {

        try {
            if (isShowing()) {
                dismiss();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void show() {
        super.show();
//        mediaController.show();
        try {
            if (mAnimation != null) {
                mOuterImg.startAnimation(mAnimation);
            }

        } catch (Exception e) {

        }
    }

    @Override
    public void dismiss() {

        try {
            if (mAnimation != null) {
                mOuterImg.clearAnimation();
            }
        } catch (Exception e) {

        }
        super.dismiss();
//        mediaController.hide();

    }
}
