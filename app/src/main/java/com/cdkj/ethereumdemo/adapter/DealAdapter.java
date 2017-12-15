package com.cdkj.ethereumdemo.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.DealDetailModel;
import com.cdkj.ethereumdemo.util.AccountUtil;
import com.cdkj.ethereumdemo.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.cdkj.ethereumdemo.util.DealUtil.setBgRes;
import static com.cdkj.ethereumdemo.util.DealUtil.setTradeType;

/**
 * Created by lei on 2017/10/31.
 */

public class DealAdapter extends BaseQuickAdapter<DealDetailModel, BaseViewHolder> {

    public DealAdapter(@Nullable List<DealDetailModel> data) {
        super(R.layout.item_deal, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DealDetailModel item) {

        helper.setText(R.id.tv_price, AccountUtil.formatDouble(item.getTruePrice())+"CNY");
        helper.setText(R.id.tv_max, StringUtil.getStirng(R.string.limit) + item.getMinTrade()+"-"+item.getMaxTrade()+"CNY");

        helper.setText(R.id.btn_confirm, setTradeType(item));

        helper.setBackgroundRes(R.id.iv_payType, setBgRes(item));

        if (item.getUser() == null)
            return;
        helper.setText(R.id.tv_name, item.getUser().getNickname());
        TextView tvAvatar = helper.getView(R.id.tv_avatar);
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImgUtils.loadAvatar(mContext, item.getUser().getPhoto(), item.getUser().getNickname(), ivAvatar, tvAvatar);

        if (item.getUserStatistics() == null)
            return;

        helper.setText(R.id.tv_deal, StringUtil.getStirng(R.string.deal) + item.getUserStatistics().getJiaoYiCount());
        helper.setText(R.id.tv_trust, StringUtil.getStirng(R.string.trust) + item.getUserStatistics().getBeiXinRenCount());
        if(item.getUserStatistics().getBeiPingJiaCount() == 0){
            helper.setText(R.id.tv_good, StringUtil.getStirng(R.string.good) +"0%");
        }else {
            double hpRate = item.getUserStatistics().getBeiHaoPingCount() / item.getUserStatistics().getBeiPingJiaCount();
            helper.setText(R.id.tv_good, StringUtil.getStirng(R.string.good) + AccountUtil.formatInt(hpRate * 100)+"%");
        }
    }

}
