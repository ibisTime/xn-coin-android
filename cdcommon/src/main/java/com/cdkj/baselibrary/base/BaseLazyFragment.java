package com.cdkj.baselibrary.base;

/**
 * Fragment 懒加载 防止fragment初始化时加载大量数据
 */
public abstract class BaseLazyFragment extends BaseFragment {

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            lazyLoad();
        } else {
            onInvisible();
        }
    }

    /**
     * fragment显示时调用
     */
    protected abstract void lazyLoad();

    /**
     * fragment隐藏时调用
     */
    protected abstract void onInvisible();

}