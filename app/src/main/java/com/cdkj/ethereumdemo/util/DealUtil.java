package com.cdkj.ethereumdemo.util;

import com.cdkj.baselibrary.appmanager.SPUtilHelper;
import com.cdkj.ethereumdemo.R;
import com.cdkj.ethereumdemo.model.DealDetailModel;

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

    public static String getPayType(String p){
        String payType = "";

        if (p.equals(StringUtil.getStirng(R.string.zhifubao))){
            payType = "0";
        } else if (p.equals(StringUtil.getStirng(R.string.weixin))){
            payType = "1";
        } else if (p.equals(StringUtil.getStirng(R.string.card))){
            payType = "2";
        }

        return payType;
    }

    public static String setTradeType(DealDetailModel item){
        String tradeType = "";

        // 是否是自己发布的
        if (item.getUser().getUserId().equals(SPUtilHelper.getUserId())){
            tradeType = StringUtil.getStirng(R.string.deal_publish_edit);
        }else {
            switch (item.getTradeType()){
                case "0":
                    tradeType = StringUtil.getStirng(R.string.sale);
                    break;

                case "1":
                    tradeType = StringUtil.getStirng(R.string.buy);
                    break;
            }
        }




        return tradeType;
    }

    public static String setStatus(DealDetailModel item){
        String tradeType;
        switch (item.getStatus()){
            case "0": //待发布
                tradeType = StringUtil.getStirng(R.string.deal_status_daifabu);
                break;

            case "3": //已下架
                tradeType = StringUtil.getStirng(R.string.deal_status_yixiajia);
                break;

            default:
                tradeType = StringUtil.getStirng(R.string.deal_status_check);
                break;
        }
        return tradeType;
    }

}
