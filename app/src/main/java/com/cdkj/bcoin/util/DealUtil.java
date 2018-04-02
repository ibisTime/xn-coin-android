package com.cdkj.bcoin.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.TextView;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.DealDetailModel;

/**
 * Created by lei on 2017/11/16.
 */

public class DealUtil {

    public static final String DAIFABU = "0";
    public static final String CAOGAO = "1";
    public static final String YIFABU = "2";

    public static int setBgRes(DealDetailModel item){
        int bgRes = 0;
        switch (item.getPayType()){
            case "0":
                bgRes = R.mipmap.deal_zfb;
                break;

            case "1":
                bgRes = R.mipmap.deal_wx;
                break;

            case "2":
                bgRes = R.mipmap.deal_yhk;
                break;
        }
        return bgRes;
    }

    public static int setDealPayType(Context context, DealDetailModel item, TextView tv){
        int bgRes = 0;
        switch (item.getPayType()){
            case "0":
                tv.setBackgroundResource(R.drawable.corner_deal_btn_blue);
                tv.setTextColor(ContextCompat.getColor(context, R.color.blue));
                tv.setText(StringUtil.getString(R.string.zhifubao));
                break;

            case "1":
                tv.setBackgroundResource(R.drawable.corner_deal_btn_green);
                tv.setTextColor(ContextCompat.getColor(context, R.color.green));
                tv.setText(StringUtil.getString(R.string.weixin));
                break;

            case "2":
                tv.setBackgroundResource(R.drawable.corner_deal_btn_red);
                tv.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
                tv.setText(StringUtil.getString(R.string.card));
                break;
        }
        return bgRes;
    }

    public static String getPayType(String p){
        String payType = "";

        if (p.equals(StringUtil.getString(R.string.zhifubao))){
            payType = "0";
        } else if (p.equals(StringUtil.getString(R.string.weixin))){
            payType = "1";
        } else if (p.equals(StringUtil.getString(R.string.card))){
            payType = "2";
        }

        return payType;
    }

    public static String setTradeType(DealDetailModel item){
        String tradeType = "";

        // 是否是自己发布的
        if (item.getUser().getUserId().equals(SPUtilHelper.getUserId())){
            tradeType = StringUtil.getString(R.string.deal_publish_edit);
        }else {
            switch (item.getTradeType()){
                case "0":
                    tradeType = StringUtil.getString(R.string.sale);
                    break;

                case "1":
                    tradeType = StringUtil.getString(R.string.buy);
                    break;
            }
        }

        return tradeType;
    }

    public static void setPushTradeType(Context context, Button button, DealDetailModel item){

        // 是否是自己发布的
        if (item.getUser().getUserId().equals(SPUtilHelper.getUserId())){
            button.setText(StringUtil.getString(R.string.deal_publish_edit));
            switch (item.getTradeType()){
                case "0":
                    button.setBackground(context.getResources().getDrawable(R.drawable.corner_push_btn_red));
                    break;

                case "1":
                    button.setBackground(context.getResources().getDrawable(R.drawable.corner_push_btn_green));
                    break;
            }
        }else {
            switch (item.getTradeType()){
                case "0":
                    button.setText(StringUtil.getString(R.string.sale));
                    button.setBackground(context.getResources().getDrawable(R.drawable.corner_push_btn_red));
                    break;

                case "1":
                    button.setText(StringUtil.getString(R.string.buy));
                    button.setBackground(context.getResources().getDrawable(R.drawable.corner_push_btn_green));
                    break;
            }
        }
    }

    public static String setStatus(DealDetailModel item){
        String tradeType;
        switch (item.getStatus()){
            case "0": // 待发布
                tradeType = StringUtil.getString(R.string.deal_status_daifabu);
                break;

            case "2": // 已下架
                tradeType = StringUtil.getString(R.string.deal_status_yixiajia);
                break;

            default: // 1 已上架
                tradeType = StringUtil.getString(R.string.deal_status_check);
                break;
        }
        return tradeType;
    }

}
