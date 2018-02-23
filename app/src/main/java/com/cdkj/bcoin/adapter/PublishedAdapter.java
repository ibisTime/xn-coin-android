package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.baselibrary.appmanager.MyConfig.CURRENCY;
import static com.cdkj.bcoin.util.AccountUtil.formatDouble;
import static com.cdkj.bcoin.util.DealUtil.setDealPayType;
import static com.cdkj.bcoin.util.DealUtil.setStatus;

/**
 * Created by lei on 2017/10/31.
 */

public class PublishedAdapter extends BaseQuickAdapter<DealDetailModel, BaseViewHolder> {

    public PublishedAdapter(@Nullable List<DealDetailModel> data) {
        super(R.layout.item_published, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DealDetailModel item) {
        helper.setText(R.id.tv_price, AccountUtil.formatDouble(item.getTruePrice())+CURRENCY);
        helper.setText(R.id.tv_max, StringUtil.getString(R.string.limit)+item.getMinTrade()+"-"+formatDouble(item.getMaxTrade())+CURRENCY);
        helper.setText(R.id.btn_confirm, setStatus(item));
        TextView tv = helper.getView(R.id.tv_type);
        setDealPayType(mContext, item, tv);

        if (item.getUser() == null)
            return;
        helper.setText(R.id.tv_name, item.getUser().getNickname());
        TextView tvAvatar = helper.getView(R.id.tv_avatar);
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImgUtils.loadAvatar(mContext, item.getUser().getPhoto(), item.getUser().getNickname(), ivAvatar, tvAvatar);

        if (item.getUserStatistics() == null)
            return;
        helper.setText(R.id.tv_deal, StringUtil.getString(R.string.deal)+item.getUserStatistics().getJiaoYiCount());
        helper.setText(R.id.tv_trust, StringUtil.getString(R.string.trust)+item.getUserStatistics().getBeiXinRenCount());
        if(item.getUserStatistics().getBeiPingJiaCount() == 0){
            helper.setText(R.id.tv_good,StringUtil.getString(R.string.good) + "0%");
        }else {
            double hpRate = item.getUserStatistics().getBeiHaoPingCount() / item.getUserStatistics().getBeiPingJiaCount();
            helper.setText(R.id.tv_good, StringUtil.getString(R.string.good)+ AccountUtil.formatInt(hpRate * 100)+"%");
        }
    }
}
