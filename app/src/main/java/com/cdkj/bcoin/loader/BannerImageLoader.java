package com.cdkj.bcoin.loader;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.youth.banner.loader.ImageLoader;

public class BannerImageLoader extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {

        if (path.toString().indexOf("http") != -1) {
            Glide.with(context)
                    .load(path.toString())
                    .into(imageView);
        } else {

            Log.e("BannerImageLoader",MyConfig.IMGURL + path.toString());

            Glide.with(context)
                    .load(MyConfig.IMGURL + path.toString())
                    .into(imageView);
        }

    }
}
