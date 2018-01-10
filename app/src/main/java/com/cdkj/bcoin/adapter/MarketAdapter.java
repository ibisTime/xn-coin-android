package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.MarketModel;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.bcoin.util.AccountUtil.formatDoubleDiv;
import static java.lang.Double.parseDouble;

/**
 * Created by lei on 2017/10/27.
 */

public class MarketAdapter extends BaseQuickAdapter<MarketModel,BaseViewHolder> {

    public MarketAdapter(@Nullable List<MarketModel> data) {
        super(R.layout.item_market, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MarketModel item) {
        helper.setText(R.id.tv_name,item.getSymbol());
        // CNY
        helper.setText(R.id.tv_cny,"¥"+item.getPrice_cny()+"");
        // USD
        helper.setText(R.id.tv_usd,"$"+item.getPrice_usd()+"");

        try {
            // 成交量
            if (parseDouble(item.getOne_day_volume_usd()) > 10000){
                helper.setText(R.id.tv_coin,formatDoubleDiv(item.getOne_day_volume_usd(), 10000)+ StringUtil.getStirng(R.string.market_wan));
            }else {
                helper.setText(R.id.tv_coin,item.getOne_day_volume_usd()+"");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 取24小时交易信息
        helper.setText(R.id.tv_dValue,item.getPercent_change_24h()+"%");

        if (item.getPercent_change_24h() == null)
            return;

        if (parseDouble(item.getPercent_change_24h()) >= 0){
            helper.setTextColor(R.id.tv_usd, ContextCompat.getColor(mContext, R.color.colorAccent));
            helper.setTextColor(R.id.tv_cny, ContextCompat.getColor(mContext, R.color.colorAccent));
            helper.setBackgroundRes(R.id.tv_dValue, R.drawable.corner_market_d_value_red);

            helper.setText(R.id.tv_dValue,"+"+item.getPercent_change_24h()+"%");
        }else {
            helper.setTextColor(R.id.tv_usd, ContextCompat.getColor(mContext, R.color.green));
            helper.setTextColor(R.id.tv_cny, ContextCompat.getColor(mContext, R.color.green));
            helper.setBackgroundRes(R.id.tv_dValue, R.drawable.corner_market_d_value_green);
        }
    }
}
