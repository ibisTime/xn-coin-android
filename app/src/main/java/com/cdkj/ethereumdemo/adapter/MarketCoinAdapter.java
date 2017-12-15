package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.MarketCoinModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2017/10/27.
 */

public class MarketCoinAdapter extends BaseQuickAdapter<MarketCoinModel,BaseViewHolder> {

    public MarketCoinAdapter(@Nullable List<MarketCoinModel> data) {
        super(R.layout.item_market_coin, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MarketCoinModel item) {
        // 来源站点
//        helper.setText(R.id.tv_name,item.getOrigin());
        helper.setText(R.id.tv_name,null);
        // CNY
        helper.setText(R.id.tv_cny,item.getMid()+"");
        // 成交量
        helper.setText(R.id.tv_coin,item.getVolume()+"");

        // USD
        BigDecimal cny = new BigDecimal(item.getMid());
        Log.e("USD", SPUtilHelper.getRate(SPUtilHelper.USD));
        BigDecimal rate = new BigDecimal(SPUtilHelper.getRate(SPUtilHelper.USD));

        helper.setText(R.id.tv_usd,cny.divide(rate, 8, BigDecimal.ROUND_HALF_DOWN)+"");
    }
}
