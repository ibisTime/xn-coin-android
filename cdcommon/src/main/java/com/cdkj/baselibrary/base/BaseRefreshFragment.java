package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.EmptyViewBinding;
import com.cdkj.baselibrary.databinding.FragmentRecyclerRefreshBinding;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 实现下拉刷新 上拉加载 分页逻辑
 * Created by 李先俊 on 2017/7/19.
 */

public abstract class BaseRefreshFragment<T> extends BaseLazyFragment {

    protected FragmentRecyclerRefreshBinding mBinding;

    protected int mPageIndex;//分页下标

    protected int mLimit;//分页数量

    private List<T> mDataList;

    protected BaseQuickAdapter mAdapter;

    protected EmptyViewBinding mEmptyBinding;

    private View mTopTitleView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recycler_refresh, null, false);
        if (canLoadEmptyView()) {
            mEmptyBinding = DataBindingUtil.inflate(inflater, R.layout.empty_view, null, false);
        }
        if (canLoadTopTitleView()) {
            if (!mBinding.toptitlelayout.isInflated()) {
                mTopTitleView = mBinding.toptitlelayout.getViewStub().inflate();
            }
        }

        init();

        return mBinding.getRoot();
    }

    protected boolean canLoadEmptyView() {
        return true;
    }

    protected boolean canLoadTopTitleView() {
        return false;
    }

    public void init() {

        mPageIndex = 1;//分页下标

        mLimit = 10;//分页数量

        mDataList = new ArrayList<T>();

        mAdapter = onCreateAdapter(mDataList);

        mBinding.rv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));

        if (mAdapter != null) {
            TextView tv = new TextView(mActivity); //先设置 不显示任何东西的 emptyView
            mAdapter.setEmptyView(tv);
            mBinding.rv.setAdapter(mAdapter);
        }

        initRefreshLayout();

        afterCreate(mPageIndex, mLimit);
    }

    /**
     * 初始化刷新加载
     */
    private void initRefreshLayout() {

        mBinding.refreshLayout.setEnableLoadmoreWhenContentNotFull(true);

        mBinding.refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPageIndex = 1;
                onMRefresh(mPageIndex, mLimit);
            }

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                if (mDataList.size() > 0) {
                    mPageIndex++;
                }
                onMLoadMore(mPageIndex, mLimit);
            }
        });
    }



    //第一次加载
    protected abstract void afterCreate(int pageIndex, int limit);

    //第一次加载
    protected abstract void getListData(int pageIndex, int limit, boolean canShowDialog);

    //刷新
    protected void onMRefresh(int pageIndex, int limit) {
        getListData(pageIndex, limit, false);
    }

    //刷新
    protected void onMRefresh(int pageIndex, int limit, boolean canShowDialog) {
        mPageIndex = pageIndex;
        getListData(mPageIndex, limit, canShowDialog);
    }

    //加载
    protected void onMLoadMore(int pageIndex, int limit) {
        getListData(pageIndex, limit, false);
    }

    protected void setEnableLoadmore(boolean b){
        mBinding.refreshLayout.setEnableLoadmore(b);
    }

    protected abstract BaseQuickAdapter onCreateAdapter(List<T> mDataList);

    public abstract String getEmptyInfo();

    public abstract @DrawableRes int getEmptyImg();


    public void loadError(String str) {

        if (mPageIndex == 1 && mBinding.refreshLayout.isRefreshing()) {
            mBinding.refreshLayout.finishRefresh();
        } else if (mPageIndex > 1 && mBinding.refreshLayout.isLoading()) {
            mBinding.refreshLayout.finishLoadmore();
        }

        if (canLoadEmptyView() && mEmptyBinding != null) {
            if (TextUtils.isEmpty(str)) {
                mEmptyBinding.tv.setText("加载错误");
            } else {
                mEmptyBinding.tv.setText(str);
            }
            mEmptyBinding.img.setVisibility(View.GONE);
            if (mAdapter != null) mAdapter.setEmptyView(mEmptyBinding.getRoot());
            if (mBinding.refreshLayout.isLoading()) mBinding.refreshLayout.finishLoadmore();

        } else {
            if (getEmptyView() != null) {
                if (mAdapter != null) mAdapter.setEmptyView(getEmptyView());
            }
            if (mBinding.refreshLayout.isLoading()) mBinding.refreshLayout.finishLoadmore();
        }
    }


    /**
     * 设置加载数据
     *
     * @param datas
     */
    protected void setData(List<T> datas) {

        if (mPageIndex == 1) {
            if (mBinding.refreshLayout.isRefreshing()) mBinding.refreshLayout.finishRefresh();
            if (datas != null) {
                mDataList.clear();
                mDataList.addAll(datas);
                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }

        } else if (mPageIndex > 1) {
            if (mBinding.refreshLayout.isLoading()) mBinding.refreshLayout.finishLoadmore();
            if (datas == null || datas.size() <= 0) {
                mPageIndex--;
            } else {
                mDataList.addAll(datas);

                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }

        if (mDataList.size() == 0 && canLoadEmptyView() && mEmptyBinding != null) {
            mEmptyBinding.tv.setText(getEmptyInfo());
            if(getEmptyImg()<=0){
                mEmptyBinding.img.setVisibility(View.GONE);
            }else{
                mEmptyBinding.img.setBackgroundResource(getEmptyImg());
                mEmptyBinding.img.setVisibility(View.VISIBLE);
            }
            mEmptyBinding.img.setVisibility(View.VISIBLE);
            if (mAdapter != null) mAdapter.setEmptyView(mEmptyBinding.getRoot());
            if (mBinding.refreshLayout.isLoading()) mBinding.refreshLayout.finishLoadmore();

        } else if (mDataList.size() == 0) {
            if (getEmptyView() != null) {
                if (mAdapter != null) mAdapter.setEmptyView(getEmptyView());
            }
            if (mBinding.refreshLayout.isLoading()) mBinding.refreshLayout.finishLoadmore();
        }
    }

    public View getEmptyView() {
        return null;
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTopTitle(String title) {
        if (mTopTitleView == null && canLoadTopTitleView()) {
            if (!mBinding.toptitlelayout.isInflated()) {
                mTopTitleView = mBinding.toptitlelayout.getViewStub().inflate();
            }
        }
        if (mTopTitleView != null) {
            TextView tvTitle = (TextView) mTopTitleView.findViewById(R.id.tv_top_title_abs);
            tvTitle.setText(title);
        }
    }

    /**
     * 设置标题
     *
     * @param isShow
     */
    public void setTopTitleLine(boolean isShow) {
        if (mTopTitleView == null && canLoadTopTitleView()) {
            if (!mBinding.toptitlelayout.isInflated()) {
                mTopTitleView = mBinding.toptitlelayout.getViewStub().inflate();
            }
        }
        if (mTopTitleView != null) {
            View line = mTopTitleView.findViewById(R.id.line_bottom);
            if(isShow){
                line.setVisibility(View.VISIBLE);
            }else {
                line.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置title right 图片点击事件
     *
     * @param listener
     */
    public void setSubRightImgAndClick(Integer rid, View.OnClickListener listener) {

        if (mTopTitleView == null) {
            return;
        }
        ImageView img = (ImageView) mTopTitleView.findViewById(R.id.img_right);
        img.setVisibility(View.VISIBLE);
        ImgUtils.loadFraImgId(this, rid, img);
        mTopTitleView.findViewById(R.id.fllayout_right).setOnClickListener(listener);
    }

    public void setTopTitleViewBg(int color) {
        if (mTopTitleView != null) {
            mTopTitleView.setBackgroundColor(ContextCompat.getColor(mActivity, color));
        }
    }

    public void setTopTitleViewColor(int color) {
        if (mTopTitleView != null) {
            if (mTopTitleView != null) {
                TextView tvTitle = (TextView) mTopTitleView.findViewById(R.id.tv_top_title_abs);
                tvTitle.setTextColor(ContextCompat.getColor(mActivity,color));
            }
        }
    }

    public void setTitleBar(String coin, String btn1, String btn2){
        if (mTopTitleView == null) {
            return;
        }

        FrameLayout framTitle = (FrameLayout) mTopTitleView.findViewById(R.id.fram_title);
        framTitle.setVisibility(View.GONE);
        RelativeLayout rlTitleBar = (RelativeLayout) mTopTitleView.findViewById(R.id.rl_titleBar);
        rlTitleBar.setVisibility(View.VISIBLE);

        TextView tvCoin = (TextView) mTopTitleView.findViewById(R.id.tv_coin);
        tvCoin.setText(coin);

        TextView tvBtn1 = (TextView) mTopTitleView.findViewById(R.id.tv_btn1);
        tvBtn1.setText(btn1);

        TextView tvBtn2 = (TextView) mTopTitleView.findViewById(R.id.tv_btn2);
        tvBtn2.setText(btn2);
    }

    public void setTitleBarCoin(String coin){
        TextView tvCoin = (TextView) mTopTitleView.findViewById(R.id.tv_coin);

        tvCoin.setText(coin);
    }

    public void setTitleBarCoinClick(View.OnClickListener listener){
        LinearLayout llCoin = (LinearLayout) mTopTitleView.findViewById(R.id.ll_coin);
        llCoin.setOnClickListener(listener);
    }

//    public void setTitleBarBtn(String btn1, String btn2){
//        TextView tvBtn1 = (TextView) mTopTitleView.findViewById(R.id.tv_btn1);
//        TextView tvBtn2 = (TextView) mTopTitleView.findViewById(R.id.tv_btn2);
//
//        tvBtn1.setText(btn1);
//        tvBtn2.setText(btn2);
//    }

    public void setTitleBarBtn1Click(View.OnClickListener listener){
        RelativeLayout rlBtn1 = (RelativeLayout) mTopTitleView.findViewById(R.id.rl_btn1);
        rlBtn1.setOnClickListener(listener);
    }

    public void setTitleBarBtn2Click(View.OnClickListener listener){
        RelativeLayout rlBtn2 = (RelativeLayout) mTopTitleView.findViewById(R.id.rl_btn2);
        rlBtn2.setOnClickListener(listener);
    }

    public void setTitleBarBtnViewChange(int location){
        View vBtn1 = mTopTitleView.findViewById(R.id.v_btn1);
        TextView tvBtn1 = (TextView) mTopTitleView.findViewById(R.id.tv_btn1);

        View vBtn2 = mTopTitleView.findViewById(R.id.v_btn2);
        TextView tvBtn2 = (TextView) mTopTitleView.findViewById(R.id.tv_btn2);

        // 初始化
        tvBtn1.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
        vBtn1.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));
        tvBtn2.setTextColor(ContextCompat.getColor(mActivity, R.color.black));
        vBtn2.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.white));

        if (location == 1){
            tvBtn1.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
            vBtn1.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        }else {
            tvBtn2.setTextColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
            vBtn2.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
        }

    }

    public void setTitleBarRightClick(View.OnClickListener listener){

        if (mTopTitleView == null) {
            return;
        }
        LinearLayout llSearch = (LinearLayout) mTopTitleView.findViewById(R.id.ll_search);

        llSearch.setVisibility(View.VISIBLE);
        llSearch.setOnClickListener(listener);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected void onInvisible() {
    }


}
