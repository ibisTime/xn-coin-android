package com.cdkj.bcoin.adapter;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.cdkj.baselibrary.activitys.AuthenticateActivity;
import com.cdkj.baselibrary.activitys.PayPwdModifyActivity;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.baselibrary.utils.ImgUtils;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.CoinModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.cdkj.bcoin.util.StringUtil;
import com.cdkj.bcoin.wallet.account.BillActivity;
import com.cdkj.bcoin.wallet.account.RechargeActivity;
import com.cdkj.bcoin.wallet.account.WithdrawActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.bcoin.util.CoinUtil.getCoinNameWithCurrency;
import static com.cdkj.bcoin.util.CoinUtil.getCoinWatermarkWithCurrency;
import static com.cdkj.bcoin.wallet.account.BillActivity.TYPE_ALL;
import static com.cdkj.bcoin.wallet.account.BillActivity.TYPE_FROZEN;

/**
 * Created by lei on 2017/10/25.
 */

public class CoinAdapter extends BaseQuickAdapter<CoinModel.AccountListBean, BaseViewHolder> {

    public CoinAdapter(@Nullable List<CoinModel.AccountListBean> data) {
        super(R.layout.item_coin, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CoinModel.AccountListBean item) {
        BigDecimal amount;
        BigDecimal frozenAmount;

        helper.setText(R.id.tv_name, getCoinNameWithCurrency(item.getCurrency()) + "(" + item.getCurrency() + ")");

        amount = new BigDecimal(item.getAmountString());
        frozenAmount = new BigDecimal(item.getFrozenAmountString());
        helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(amount.subtract(frozenAmount), item.getCurrency(), 8));

        helper.setText(R.id.tv_frozen, StringUtil.getString(R.string.freeze) + AccountUtil.amountFormatUnit(new BigDecimal(item.getFrozenAmountString()), item.getCurrency(), 8));
        ImageView ivCoin = helper.getView(R.id.iv_watermark);
        ImgUtils.loadImage(mContext, getCoinWatermarkWithCurrency(item.getCurrency(), 1), ivCoin);
        String coinWatermarkWithCurrency = getCoinWatermarkWithCurrency(item.getCurrency(), 1);
        Log.i(TAG, "convert: pppppp" + coinWatermarkWithCurrency);

        helper.getView(R.id.ll_recharge).setOnClickListener(v -> {
            RechargeActivity.open(mContext, item);
        });

        helper.getView(R.id.ll_withdraw).setOnClickListener(v -> {

            if (TextUtils.isEmpty(SPUtilHelper.getRealName())) {
                AuthenticateActivity.open(mContext);
            } else {
                if (SPUtilHelper.getTradePwdFlag()) {
                    WithdrawActivity.open(mContext, item);
                } else {
                    PayPwdModifyActivity.open(mContext, SPUtilHelper.getTradePwdFlag(), SPUtilHelper.getUserPhoneNum());
                }
            }

        });

        helper.getView(R.id.ll_bill).setOnClickListener(v -> {
            BillActivity.open(mContext, item.getAccountNumber(), TYPE_ALL);
        });

        helper.getView(R.id.tv_frozen).setOnClickListener(view -> {
            BillActivity.open(mContext, item.getAccountNumber(), TYPE_FROZEN);
        });
    }
}
