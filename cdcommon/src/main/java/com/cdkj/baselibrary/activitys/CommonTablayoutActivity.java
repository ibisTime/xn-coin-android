package com.cdkj.baselibrary.activitys;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.adapters.TablayoutAdapter;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityTabBinding;

import java.util.List;

/**TablayoutActivity
 * Created by 李先俊 on 2017/6/15.
 */

public abstract class CommonTablayoutActivity extends AbsBaseActivity {

    protected ActivityTabBinding mbinding;

    /*Tablayout 适配器*/
    protected TablayoutAdapter tablayoutAdapter;

    @Override
    public View addMainView() {
        mbinding= DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_tab,null,false);
        return mbinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        initViewPager();
    }
    private void initViewPager() {

        tablayoutAdapter=new TablayoutAdapter(getSupportFragmentManager());

        List<Fragment> mFragments=getFragments();
        List<String> mTitles=getFragmentTitles();

        if(mFragments!=null && mTitles!=null && mFragments.size() == mTitles.size()){
            for (int i = 0; i < mFragments.size(); i++) {
                tablayoutAdapter.addFrag(mFragments.get(i),mTitles.get(i));
            }
        }
        mbinding.viewpager.setAdapter(tablayoutAdapter);
        mbinding.tablayout.setupWithViewPager(mbinding.viewpager);        //viewpager和tablayout关联
        mbinding.viewpager.setOffscreenPageLimit(tablayoutAdapter.getCount());
    }
    //获取要显示的fragment
    public abstract List<Fragment> getFragments();

    //获取要显示的title
    public abstract List<String> getFragmentTitles();

}
