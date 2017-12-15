package com.cdkj.baselibrary.interfaces;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;

/**
 * 刷新方法回调
 * Created by cdkj on 2017/10/17.
 */

public abstract class BaseRefreshCallBack<T> implements RefreshInterface {

    private EmptyViewBinding emptyViewBinding;

    @Override
    public View getEmptyView(Activity context) {
        emptyViewBinding = DataBindingUtil.inflate(context.getLayoutInflater(), R.layout.empty_view, null, false);
        return emptyViewBinding.getRoot();
    }

    @Override
    public void setErrorTxt(String errorMsg) {
        emptyViewBinding.tv.setText(errorMsg);
    }

    @Override
    public void setErrorImg(int errorImg) {
        emptyViewBinding.img.setBackgroundResource(errorImg);
    }

    @Override
    public void onRefresh(int pageindex, int limit) {

    }

    @Override
    public void onLoadMore(int pageindex, int limit) {

    }


}
