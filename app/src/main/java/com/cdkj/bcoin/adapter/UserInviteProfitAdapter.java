package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.UserInviteProfitModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by lei on 2018/3/14.
 */

public class UserInviteProfitAdapter extends BaseQuickAdapter<UserInviteProfitModel,BaseViewHolder> {

    public UserInviteProfitAdapter(@Nullable List<UserInviteProfitModel> data) {
        super(R.layout.item_user_invite_profit, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserInviteProfitModel item) {
        ImageView ivCoin = helper.getView(R.id.iv_coin);
        ImgUtils.loadImage(mContext, item.getCoin().getIcon(), ivCoin);

        helper.setText(R.id.tv_coin, item.getCoin().getEname());
        helper.setText(R.id.tv_profit, AccountUtil.amountFormatUnit(new BigDecimal(item.getInviteProfit()), item.getCoin().getSymbol(), 8)+item.getCoin().getSymbol());

    }
}
