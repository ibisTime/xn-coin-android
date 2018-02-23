package com.cdkj.bcoin.util;

import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.bcoin.model.CoinModel;
import com.cdkj.bcoin.model.OrderDetailModel;
import com.cdkj.bcoin.model.OrderModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lei on 2018/2/8.
 */

public class ResponseUtil {

    /**
     *
     * @param object
     * @return
     */
    public static Object screeningDataWithConfig(Object object){
        // 允许配置的Coin
        List<String> coinTypeList = Arrays.asList(MyConfig.COIN_TYPE);

        if (object instanceof CoinModel){

            // 筛选配置允许的Coin账户
            List<CoinModel.AccountListBean> list = new ArrayList<>();

            CoinModel model = (CoinModel) object;

            for (CoinModel.AccountListBean bean : model.getAccountList()){
                if (coinTypeList.contains(bean.getCurrency())){
                    list.add(bean);
                }
            }

            return list;

        }else if (object instanceof OrderModel){

            // 筛选配置允许的Coin账户
            List<OrderDetailModel> list = new ArrayList<>();

            OrderModel model = (OrderModel) object;

            for (OrderDetailModel bean : model.getList()){
                if (coinTypeList.contains(bean.getTradeCoin())){
                    list.add(bean);
                }
            }

            return list;

        }

        return null;

    }

}
