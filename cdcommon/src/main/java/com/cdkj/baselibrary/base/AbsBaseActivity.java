package com.cdkj.baselibrary.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.databinding.ActivityAbsBaseBinding;


/**
 * 带空页面，错误页面显示的BaseActivity
 */
public abstract class AbsBaseActivity extends BaseActivity {
    private View mErrorView;
    private View mMainView;
    private View mTopTitleView;

    /**
     * 布局文件xml的resId,无需添加标题栏、加载、错误及空页面
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityAbsBaseBinding mBaseBinding= DataBindingUtil.setContentView(this, R.layout.activity_abs_base);

        if(canLoadTopTitleView()){

            if (!mBaseBinding.toptitlelayout.isInflated()) {
                mTopTitleView= mBaseBinding.toptitlelayout.getViewStub().inflate();
            }

            mTopTitleView.findViewById(R.id.fram_img_back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(canFinish()){
                        finish();
                    }
                }
            });
        }

        if(canLoadErrorView()){
            if (!mBaseBinding.emptylayout.isInflated()) {
                mErrorView= mBaseBinding.emptylayout.getViewStub().inflate();
            }
        }

        mMainView = addMainView();

        if(mMainView!=null){
            mBaseBinding.contentView.addView(mMainView, 1);
        }

        afterCreate(savedInstanceState);

    }

    /**
     * 能否结束当前页面
     * @return
     */
    protected  boolean canFinish(){
        return true;
    }

    /**
     * 能否加载标题
     * @return
     */
    protected  boolean canLoadTopTitleView(){
        return true;
    }
    /**
     * 能否加载错误提示View
     * @return
     */
    protected  boolean canLoadErrorView(){
        return false;
    }


    /**
     * 添加要显示的View
     *
     */
    public abstract View addMainView();

    /**
     * activity的初始化工作
     */
    public abstract void afterCreate(Bundle savedInstanceState);

    /**
     * 设置错误显示图标
     * @param resId
     */
    protected void setErrorIcon(int resId) {
        if(mErrorView==null)return;
        ImageView mIcon = (ImageView) mErrorView.findViewById(R.id.iv_icon_error);
        mIcon.setImageResource(resId);
    }
    /**
     * 设置错误显示文本
     * @param  errer
     */
    protected void setErroryText(String errer) {
        if(mErrorView==null) return;
        TextView mText = (TextView) mErrorView.findViewById( R.id.tv_error);
        mText.setText(errer);
    }

    /**
     * 隐藏所有界面
     */
    private void hideAll() {
        hideAllNoTitle();
        if(mTopTitleView!=null) mTopTitleView.setVisibility(View.GONE);
    }
    /**
     * 隐藏除标题栏的所有界面,辅助显示各个页面的
     */
    protected void hideAllNoTitle() {
        if (mErrorView != null) {
            mErrorView.setVisibility(View.GONE);
        }
        if (mMainView != null) {
            mMainView.setVisibility(View.GONE);
        }
    }


    public void showErrorView() {
        hideAllNoTitle();
        if(mErrorView!=null) mErrorView.setVisibility(View.VISIBLE);
    }

    public void showContentView() {
        hideAllNoTitle();
        if(mMainView!=null) mMainView.setVisibility(View.VISIBLE);
    }

    public void showErrorView(int imgResId, String error) {
        hideAllNoTitle();
        if(mErrorView!=null)  mErrorView.setVisibility(View.VISIBLE);
        setErrorIcon(imgResId);
        setErroryText(error);
    }

    public void showErrorView(String error) {
        hideAllNoTitle();
        if(mErrorView!=null)  mErrorView.setVisibility(View.VISIBLE);
        setErroryText(error);
    }



    /**
     * 设置标题
     * @param title
     */
    public void setTopTitle(String title) {
        if(mTopTitleView == null){
            return;
        }
        TextView tvTitle = (TextView) mTopTitleView.findViewById(R.id.tv_top_title_abs);
        tvTitle.setText(title);
    }

    /**
     * 设置标题点击事件
     * @param listener
     */
    public void setTopTitleClickListener(View.OnClickListener listener) {
        if(mTopTitleView == null){
            return;
        }
        TextView tvTitle = (TextView) mTopTitleView.findViewById(R.id.tv_top_title_abs);
        tvTitle.setOnClickListener(listener);
    }

    /**
     * 设置标题图片是否显示
     * @param b
     */
    public void setTopImgEnable(boolean b) {
        if(mTopTitleView == null){
            return;
        }
        ImageView ivImg = (ImageView) mTopTitleView.findViewById(R.id.iv_title_img);
        ivImg.setVisibility(b ? View.VISIBLE : View.GONE);
    }

    /**
     * 设置标题
     * @param b
     */
    public void setTopLineState(boolean b) {
        if(mTopTitleView == null){
            return;
        }
        View line = mTopTitleView.findViewById(R.id.line_bottom);
        if(b){
            line.setVisibility(View.VISIBLE);
        }else {
            line.setVisibility(View.GONE);
        }
    }

    /**
     * 设置title left 文本点击事件
     * @param subTitle
     */
    public void setSubLeftTitleAndClick(String subTitle) {
        if(mTopTitleView == null){
            return;
        }
        FrameLayout frameLayout = (FrameLayout) mTopTitleView.findViewById(R.id.fram_img_back);
        TextView tvSubTitle = (TextView) mTopTitleView.findViewById(R.id.tv_back);
        tvSubTitle.setText(subTitle);
        tvSubTitle.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
    }


    /**
     * title  left img 显示状态
     * @param isShow
     */
    public void setSubLeftImgState(boolean isShow) {

        if(mTopTitleView == null){
            return;
        }

        ImageView tvSubTitle = (ImageView) mTopTitleView.findViewById(R.id.img_back);
        FrameLayout frameLayout = (FrameLayout) mTopTitleView.findViewById(R.id.fram_img_back);
        if(isShow){
            tvSubTitle.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
        }else{
            tvSubTitle.setVisibility(View.GONE);
            frameLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 设置title right 图片点击事件
     * @param listener
     */
    public void setSubRightImgAndClick(int rid, View.OnClickListener listener) {

        if(mTopTitleView== null){
            return;
        }

        ImageView img= (ImageView) mTopTitleView.findViewById(R.id.img_right);
        img.setVisibility(View.VISIBLE);
        img.setImageResource(rid);
        findViewById(R.id.fllayout_right).setOnClickListener(listener);
    }

    /**
     * 设置title right 文本点击事件
     * @param subTitle
     * @param listener
     */
    public void setSubRightTitleAndClick(String subTitle, View.OnClickListener listener) {

        if(mTopTitleView == null)return;

        if (subTitle == null)
            return;

        FrameLayout frameLayout = (FrameLayout) mTopTitleView.findViewById(R.id.fllayout_right);
        TextView tvSubTitle = (TextView) mTopTitleView.findViewById(R.id.tv_top_right);
        tvSubTitle.setText(subTitle);
        tvSubTitle.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        tvSubTitle.setOnClickListener(listener);
    }
    /**
     * 设置title right 消失
     */
    public void setSubRightTitHide() {

        if(mTopTitleView == null)return;

        FrameLayout frameLayout = (FrameLayout) mTopTitleView.findViewById(R.id.fllayout_right);
        TextView tvSubTitle = (TextView) mTopTitleView.findViewById(R.id.tv_top_right);
        tvSubTitle.setVisibility(View.GONE);
        frameLayout.setVisibility(View.GONE);
    }



}
