package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Date;
import java.util.List;

import static com.cdkj.bcoin.util.DealUtil.setDealPayType;
import static com.cdkj.bcoin.util.DealUtil.setTradeType;

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
        helper.setText(R.id.tv_deal, StringUtil.getStirng(R.string.deal) + item.getUserStatistics().getJiaoYiCount());
        helper.setText(R.id.tv_trust, StringUtil.getStirng(R.string.trust) + item.getUserStatistics().getBeiXinRenCount());
        if (item.getUserStatistics().getBeiPingJiaCount() == 0) {
            helper.setText(R.id.tv_good, StringUtil.getStirng(R.string.good) +"0%");
        } else {
            double hpRate = item.getUserStatistics().getBeiHaoPingCount() / item.getUserStatistics().getBeiPingJiaCount();
            helper.setText(R.id.tv_good, StringUtil.getStirng(R.string.good) + AccountUtil.formatInt(hpRate * 100)+"%");
        }

        if (item.getUser().getLastLogin() == null)
            return;
        // 绿灯10以内；黄色10-30；灰色30以后
        int loginTimeDValue = DateUtil.getDateDValue(new Date(),new Date(item.getUser().getLastLogin()));
        Log.e("loginTimeDValue",loginTimeDValue+"");
        if (loginTimeDValue <= 10){
            helper.setBackgroundRes(R.id.tv_status, R.drawable.corner_deal_online_status_green);
        }else if (loginTimeDValue > 10 && loginTimeDValue <=30 ){
            helper.setBackgroundRes(R.id.tv_status, R.drawable.corner_deal_online_status_yellow);
        }else {
            helper.setBackgroundRes(R.id.tv_status, R.drawable.corner_deal_online_status_gray);
        }
    }

}
