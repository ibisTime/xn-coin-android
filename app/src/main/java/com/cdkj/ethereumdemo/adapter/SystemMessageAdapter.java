package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.SystemMessageModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/27.
 */

public class SystemMessageAdapter extends BaseQuickAdapter<SystemMessageModel.ListBean,BaseViewHolder> {

    public SystemMessageAdapter(@Nullable List<SystemMessageModel.ListBean> data) {
        super(R.layout.item_system_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SystemMessageModel.ListBean item) {
        helper.setText(R.id.txt_title, item.getSmsTitle());
        helper.setText(R.id.txt_content, item.getSmsContent());

        helper.setText(R.id.txt_time, DateUtil.formatStringData(item.getPushedDatetime(),DateUtil.DATE_YMD));


    }
}
