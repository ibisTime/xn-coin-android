package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.DealDetailModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.appmanager.MyConfig.CURRENCY;
import static com.cdkj.bcoin.util.DealUtil.setPushTradeType;

/**
 * Created by lei on 2018/3/13.
 */

public class PushAdapter extends BaseQuickAdapter<DealDetailModel,BaseViewHolder> {

    public PushAdapter(@Nullable List<DealDetailModel> data) {
        super(R.layout.item_push, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DealDetailModel item) {
        helper.setText(R.id.tv_price, AccountUtil.formatDouble(item.getTruePrice())+CURRENCY);
        helper.setText(R.id.tv_left,  AccountUtil.amountFormatUnit(new BigDecimal(item.getLeftCountString()), item.getTradeCoin(), 8));
        helper.setText(R.id.tv_amount, AccountUtil.formatDouble(item.getTruePrice() * Double.parseDouble(AccountUtil.amountFormatUnit(new BigDecimal(item.getLeftCountString()), item.getTradeCoin(), 8)))+CURRENCY);

        Button btnConfirm = helper.getView(R.id.btn_confirm);
        setPushTradeType(mContext, btnConfirm, item);

        if (item.getUser() == null)
            return;
        TextView tvAvatar = helper.getView(R.id.tv_avatar);
        ImageView ivAvatar = helper.getView(R.id.iv_avatar);
        ImgUtils.loadAvatar(mContext, item.getUser().getPhoto(), item.getUser().getNickname(), ivAvatar, tvAvatar);

        if (item.getUserStatistics() == null)
            return;
        helper.setText(R.id.tv_deal, StringUtil.getString(R.string.deal) + item.getUserStatistics().getJiaoYiCount());
        helper.setText(R.id.tv_trust, StringUtil.getString(R.string.trust) + item.getUserStatistics().getBeiXinRenCount());
        if (item.getUserStatistics().getBeiPingJiaCount() == 0) {
            helper.setText(R.id.tv_good, StringUtil.getString(R.string.good) +"0%");
        } else {
            double hpRate = item.getUserStatistics().getBeiHaoPingCount() / item.getUserStatistics().getBeiPingJiaCount();
            helper.setText(R.id.tv_good, StringUtil.getString(R.string.good) + AccountUtil.formatInt(hpRate * 100)+"%");
        }

    }
}
