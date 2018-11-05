package com.cdkj.bcoin.loader;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        Glide.with(context)
                .load(Integer.parseInt((String) path))
                .into(imageView);

//        if (path.toString().indexOf("http") != -1) {
//            Glide.with(context)
//                    .load(path.toString())
//                    .into(imageView);
//        } else {
//
//            Glide.with(context)
//                    .load(MyConfig.IMGURL + path.toString())
//                    .into(imageView);
//        }

    }
}
