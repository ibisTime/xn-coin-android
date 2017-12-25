package com.cdkj.bcoin.util;

import com.cdkj.bcoin.R;
import com.cdkj.bcoin.model.OrderDetailModel;

/**
 * Created by lei on 2017/11/17.
 */

public class OrderUtil {

    public static String getOrderStatus(OrderDetailModel item){

        switch (item.getStatus()){

            case "-1":
                return StringUtil.getStirng(R.string.order_status_daixiadan);

            case "0":
                return StringUtil.getStirng(R.string.order_status_daizhifu);

            case "1":
                return StringUtil.getStirng(R.string.order_status_daishifang);

            case "2":
                return StringUtil.getStirng(R.string.order_status_daipingjia);

            case "3":
                return StringUtil.getStirng(R.string.order_status_yiwancheng);

            case "4":
                return StringUtil.getStirng(R.string.order_status_yiquxiao);

            case "5":
                return StringUtil.getStirng(R.string.order_status_zhongcaizhong);

            default:
                return "";
        }

    }

}
