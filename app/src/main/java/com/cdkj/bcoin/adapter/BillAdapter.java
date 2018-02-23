package com.cdkj.bcoin.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cdkj.baselibrary.utils.DateUtil;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.BillModel;
import com.cdkj.bcoin.util.AccountUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.math.BigDecimal;
import java.util.List;

import static com.cdkj.baselibrary.utils.DateUtil.DATE_DAY;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_HM;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_M;
import static com.cdkj.baselibrary.utils.DateUtil.DATE_YM;

/**
 * Created by lei on 2017/8/22.
 */

public class BillAdapter extends BaseQuickAdapter<BillModel.ListBean,BaseViewHolder> {

    List<BillModel.ListBean> list;

    public BillAdapter(@Nullable List<BillModel.ListBean> data) {
        super (R.layout.item_bill,data);
    }

    @NonNull
    @Override
    public List<BillModel.ListBean> getData() {
        return super.getData();
    }

    @Override
    protected void convert(BaseViewHolder helper, BillModel.ListBean item) {
        if (list == null){
            list = getData();
        }

        // 当itemPosition为0时，展示日期
        if (helper.getLayoutPosition() == 0){
            helper.setVisible(R.id.tv_ym, true);
            helper.setText(R.id.tv_ym, DateUtil.formatStringData(item.getCreateDatetime(),DATE_YM));
        }else { // 当itemPosition不为0但当前item的日期与上一个item不相同时，展示日期，否则不展示
            String month_now = DateUtil.formatStringData(item.getCreateDatetime(),DATE_M);
            String month_last = DateUtil.formatStringData(list.get(helper.getLayoutPosition()-1).getCreateDatetime(),DATE_M);

            if (!month_now.equals(month_last)){
                helper.setVisible(R.id.tv_ym, true);
                helper.setText(R.id.tv_ym, DateUtil.formatStringData(item.getCreateDatetime(),DATE_YM));
            }else {
                helper.setVisible(R.id.tv_ym, false);
            }
        }

        helper.setText(R.id.tv_day, DateUtil.formatStringData(item.getCreateDatetime(),DATE_DAY));
        helper.setText(R.id.tv_time, DateUtil.formatStringData(item.getCreateDatetime(),DATE_HM));

        helper.setText(R.id.tv_remark, item.getBizNote());
        helper.setText(R.id.tv_currency, item.getCurrency());

        BigDecimal tas = new BigDecimal(item.getTransAmountString());
        int i=tas.compareTo(BigDecimal.ZERO);
        if (i==1){
            helper.setText(R.id.tv_amount, "+" + AccountUtil.amountFormatUnit(tas, item.getCurrency(), 8));
        }else {
            helper.setText(R.id.tv_amount, AccountUtil.amountFormatUnit(tas, item.getCurrency(), 8));
        }

        if (item.getKind().equals("0")){ // 非冻结流水
            switch (item.getBizType()){
                case "charge": // 充值
                    if (item.getCurrency().equals("ETH")){
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_charge);
                    } else if (item.getCurrency().equals("SC")) {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_charge);
                    } else {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_charge);
                    }
                    break;

                case "withdraw": // 取现
                    if (item.getCurrency().equals("ETH")){
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_withdraw);
                    } else if (item.getCurrency().equals("SC")) {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_withdraw);
                    } else {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_withdraw);
                    }

                    break;

                case "buy": // 买入
                    if (item.getCurrency().equals("ETH")){
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_into);
                    } else if (item.getCurrency().equals("SC")) {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_into);
                    } else {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_into);
                    }
                    break;

                case "sell": // 卖出
                    if (item.getCurrency().equals("ETH")){
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_out);
                    } else if (item.getCurrency().equals("SC")) {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_out);
                    } else {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_out);
                    }
                    break;

                case "tradefee": // 手续费
                case "withdrawfee": // 手续费
                    if (item.getCurrency().equals("ETH")){
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_fee);
                    } else if (item.getCurrency().equals("SC")) {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_fee);
                    } else {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_fee);
                    }
                    break;

                default:
                    if (item.getCurrency().equals("ETH")){
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_award);
                    } else if (item.getCurrency().equals("SC")) {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_award);
                    } else {
                        helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_award);
                    }
                    break;

            }
        }else { // 冻结流水

            if (item.getCurrency().equals("ETH")){
                if (item.getTransAmountString().contains("-")){ // 金额是负数
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_withdraw);
                } else {
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_eth_charge);
                }
            } else if (item.getCurrency().equals("SC")) {
                if (item.getTransAmountString().contains("-")){ // 金额是负数
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_withdraw);
                } else {
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_sc_charge);
                }
            } else {
                if (item.getTransAmountString().contains("-")){ // 金额是负数
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_withdraw);
                } else {
                    helper.setImageResource(R.id.iv_type, R.mipmap.bill_btc_charge);
                }
            }



        }
    }
}
