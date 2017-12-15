package com.cdkj.baselibrary.interfaces;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

/**
 * Created by 李先俊 on 2017/8/8.
 */
public interface RefreshInterface<T> {

    SmartRefreshLayout getRefreshLayout();

    RecyclerView getRecyclerView();

    BaseQuickAdapter getAdapter(List<T> listData);

    View getEmptyView(Activity context);

    void setErrorTxt(String errorMsg);

    void setErrorImg(int errorImg);

    void onRefresh(int pageindex, int limit);

    void onLoadMore(int pageindex, int limit);

    void getListDataRequest(int pageindex, int limit, boolean isShowDialog);


}
