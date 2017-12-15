package com.cdkj.baselibrary.activitys;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.cdkj.baselibrary.R;
import com.cdkj.baselibrary.base.AbsBaseActivity;
import com.cdkj.baselibrary.databinding.ActivityRichTextBinding;
import com.zzhoujay.richtext.RichText;

/**
 * Created by lei on 2017/11/22.
 */

public class RichTextActivity extends AbsBaseActivity {

    private ActivityRichTextBinding mBinding;

    /**
     * 加载activity
     *
     * @param activity 上下文
     */
    public static void open(Context activity, String title, String richText) {
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, RichTextActivity.class);
        intent.putExtra("richText", richText);
        intent.putExtra("title", title);
        activity.startActivity(intent);

    }

    @Override
    public View addMainView() {
        mBinding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_rich_text, null, false);
        return mBinding.getRoot();
    }

    @Override
    public void afterCreate(Bundle savedInstanceState) {
        setSubLeftImgState(true);
        setTopLineState(true);
        initData();
    }

    private void initData() {
        if (getIntent() == null) {
            return;
        }

        try{
            setTopTitle(getIntent().getStringExtra("title"));
            RichText.from(getIntent().getStringExtra("richText")).into(mBinding.tvRichText);
        }catch (Exception e){
            e.printStackTrace();
        }



    }
}
