package com.cdkj.baselibrary.interfaces;

import android.content.Context;

/**
 * Created by lei on 2017/12/1.
 */

public class AppBuildTypePresener {

    private AppBuildTypeInterface mListener;
    private Context mContext;

    public AppBuildTypePresener(Context context, AppBuildTypeInterface typeInterface) {
        this.mContext = context;
        this.mListener = typeInterface;
    }



}
