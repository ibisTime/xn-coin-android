package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.ExchangeModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lei on 2017/10/31.
 */

public class ExchangeAdapter extends BaseQuickAdapter<ExchangeModel.ListBean, BaseViewHolder> {

    public ExchangeAdapter(@Nullable List<ExchangeModel.ListBean> data) {
        super(R.layout.item_exchange, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExchangeModel.ListBean item) {

        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getUpdateDatetime(),DateUtil.DATE_YMD));
        helper.setText(R.id.tv_today,item.getRate()+"");
//        helper.setText(R.id.tv_name_f,item.getName().subSequence(0, 1).toString().toUpperCase());
    }
}
