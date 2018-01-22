package com.cdkj.bcoin.util;

import com.cdkj.bcoin.R;

/**
 * Created by lei on 2017/11/17.
 */

public class OrderUtil {

    public static String getOrderStatus(String status){

        switch (status){

            case "-1":
                return StringUtil.getString(R.string.order_status_daixiadan);

            case "0":
                return StringUtil.getString(R.string.order_status_daizhifu);

            case "1":
                return StringUtil.getString(R.string.order_status_daishifang);

            case "2":
                return StringUtil.getString(R.string.order_status_daipingjia);

            case "3":
                return StringUtil.getString(R.string.order_status_yiwancheng);

            case "4":
                return StringUtil.getString(R.string.order_status_yiquxiao);

            case "5":
                return StringUtil.getString(R.string.order_status_zhongcaizhong);

            default:
                return "";
        }

    }

}
