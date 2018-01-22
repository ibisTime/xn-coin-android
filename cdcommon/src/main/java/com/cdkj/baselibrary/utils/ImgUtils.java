package com.cdkj.baselibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.utils.glidetransforms.GlideCircleTransform;
import com.cdkj.baselibrary.utils.glidetransforms.GlideRoundTransform;

/**
 * 图片加载工具类
 * Created by Administrator on 2016-09-14.
 */
public class ImgUtils {

    public static void loadAvatar(Activity context, String imgid, ImageView img){
        if (!AppUtils.isActivityExist(context)){
            return;
        }
        if(context==null || img==null) {
            return;
        }
        LogUtil.E("图片"+imgid);

        try {
            if (imgid.indexOf("http://") != -1){
                Glide.with(context).load(imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new GlideCircleTransform(context)).into(img);
            }else {
                Glide.with(context).load(MyConfig.IMGURL+imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new GlideCircleTransform(context)).into(img);
            }

        }catch (Exception e){
            LogUtil.E("图片加载错误");
        }
    }

    public static void loadAvatar(Context context, String imgid, ImageView img){
        if(context==null || img==null) {
            return;
        }
        LogUtil.E("图片"+imgid);

        try {
            if (imgid.indexOf("http://") != -1){
                Glide.with(context).load(imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new GlideCircleTransform(context)).into(img);
            }else {
                Glide.with(context).load(MyConfig.IMGURL+imgid).placeholder(R.drawable.photo_default).error(R.drawable.photo_default).transform(new GlideCircleTransform(context)).into(img);
            }
        }catch (Exception e){
            LogUtil.E("图片加载错误");
        }
    }

    public static void loadRoundImage(Context context, String imgid, ImageView img){
        if(context==null || img==null) {
            return;
        }
        LogUtil.E("图片"+imgid);

        try {
            Glide.with(context).load(imgid).transform(new GlideRoundTransform(context,15)).into(img);
        }catch (Exception e){
            LogUtil.E("图片加载错误");
        }
    }

    public static void loadActImg(Activity context,String imgid,ImageView img){

        if (!AppUtils.isActivityExist(context)){

            LogUtil.E("图片加载界面销毁");
            return;
        }

        if(context==null || img==null)
        {
            return;
        }

        LogUtil.E("图片"+imgid);

        try {
            Glide.with(context).load(imgid).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(img);
        }catch (Exception e){
            LogUtil.E("图片加载错误");
        }

    }

    public static void  loadActImgId(Activity context,int imgid,ImageView img){

        if (!AppUtils.isActivityExist(context)){

            LogUtil.E("图片加载界面销毁");
            return;
        }

        if(context==null || img==null)
        {
            return;
        }

        LogUtil.E("图片"+imgid);

        try {
            Glide.with(context).load(imgid).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(img);
        }catch (Exception e){
            LogUtil.E("图片加载错误");
        }

    }


    public static void loadFraImgId(Fragment context, int imgid, ImageView img){

        if(context==null || img==null)
        {
            return;
        }

        LogUtil.E("图片"+imgid);

        try {
            Glide.with(context).load(imgid).placeholder(R.drawable.default_pic).error(R.drawable.default_pic).into(img);
        }catch (Exception e){
            LogUtil.E("图片加载错误");
        }

    }

    public static void loadImage(Context context, String path, ImageView iv){

        if(context==null || path==null)
            return;


        if (path.toString().indexOf("http") != -1) {
            Glide.with(context)
                    .load(path.toString())
                    .into(iv);
        } else {

            Log.e("loadImage",MyConfig.IMGURL + path.toString());

            Glide.with(context)
                    .load(MyConfig.IMGURL + path.toString())
                    .into(iv);
        }

    }

    /**
     * 设置头像，头像为空时用昵称首字符作为头像，英文则须大写
     * @param context 上下文
     * @param imgUrl 图片URL
     * @param nickName 用于设置文字头像的用户昵称
     * @param imgAvatar 图片头像
     * @param txtAvatar 文字头像(取用户昵称的第一个字符)
     */
    public static void loadAvatar(Context context, String imgUrl, String nickName, ImageView imgAvatar, TextView txtAvatar){

        if (context == null || imgUrl == null)
            return;

        if (imgUrl.equals("")){ // 没有头像
            // 隐藏图片头像
            imgAvatar.setVisibility(View.GONE);
            // 设置文字头像
            txtAvatar.setVisibility(View.VISIBLE);

            if (nickName.length() > 1){
                txtAvatar.setText(nickName.substring(0,1).toUpperCase());
            }

        }else {
            // 隐藏文字头像
            txtAvatar.setVisibility(View.GONE);
            // 设置图片头像
            imgAvatar.setVisibility(View.VISIBLE);

            try {
                Glide.with(context)
                        .load(MyConfig.IMGURL+imgUrl)
                        .placeholder(R.drawable.photo_default)
                        .error(R.drawable.photo_default)
                        .transform(new GlideCircleTransform(context)).into(imgAvatar);

//                if (imgUrl.indexOf("http://") != -1){
//                    Glide.with(context)
//                            .load(imgUrl)
//                            .placeholder(R.drawable.photo_default)
//                            .error(R.drawable.photo_default)
//                            .transform(new GlideCircleTransform(context)).into(imgAvatar);
//                }else {
//
//                }

            }catch (Exception e){
                LogUtil.E("图片加载错误");
            }
        }

    }

}
