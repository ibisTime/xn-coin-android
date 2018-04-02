package com.cdkj.bcoin.util;

import com.cdkj.baselibrary.model.BaseCoinModel;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lei on 2018/2/8.
 */

public class CoinUtil {
    static List<BaseCoinModel> baseCoinList = new ArrayList<>();


    private static void initBaseCoinList(){
        baseCoinList.clear();
        baseCoinList.addAll(DataSupport.findAll(BaseCoinModel.class));
    }

    public static String getCoinNameWithCurrency(String currency){
        initBaseCoinList();

        for (BaseCoinModel model : baseCoinList){

            if (model.getSymbol().equals(currency)){
                return model.getCname();
            }

        }

        return "";

    }

    /**
     *
     * @param currency 币种
     * @param position 要哪张图片 0:icon官方图标,1:Pic1钱包水印图标,2:Pic2流水加钱图标,3:Pic3流水减钱图标
     * @return
     */
    public static String getCoinWatermarkWithCurrency(String currency, int position){
        initBaseCoinList();

        for (BaseCoinModel model : baseCoinList){

            if (model.getSymbol().equals(currency)){
                if (position == 0){
                    return model.getIcon();
                }else if (position == 1){
                    return model.getPic1();
                }else if(position == 2){
                    return model.getPic2();
                }else {
                    return model.getPic3();
                }

            }

        }

        return "";

    }

    /**
     * 获取所有币种的简称并组装成数组
     * @return
     */
    public static String[] getAllCoinArray(){
        initBaseCoinList();

        String[] coin = new String[baseCoinList.size()];

        for (int i = 0; i < baseCoinList.size(); i++) {
            coin[i] = baseCoinList.get(i).getSymbol();
        }

        return coin;

    }

    /**
     * 获取Token币种的简称并组装成数组
     * @return
     */
    public static String[] getTokenCoinArray(){
        initBaseCoinList();

        List<BaseCoinModel> list = new ArrayList<>();

        for (BaseCoinModel model : baseCoinList){

            // type = 1: Token币
            if (model.getType().equals("1")){
                list.add(model);
            }

        }

        String[] coin = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            coin[i] = list.get(i).getSymbol();
        }

        return coin;

    }

    /**
     * 获取 非 Token币种的简称并组装成数组
     * @return
     */
    public static String[] getNotTokenCoinArray(){
        initBaseCoinList();

        List<BaseCoinModel> list = new ArrayList<>();

        for (BaseCoinModel model : baseCoinList){

            // type = 1: Token币，type = 0: 非Token币
            if (model.getType().equals("0")){
                list.add(model);
            }

        }

        String[] coin = new String[list.size()];

        for (int i = 0; i < list.size(); i++) {
            coin[i] = list.get(i).getSymbol();
        }

        return coin;

    }

    /**
     * 获取所有币种的简称并组装成集合
     * @return
     */
    public static List<BaseCoinModel> getAllCoinList(){
        initBaseCoinList();

        List<BaseCoinModel> list = new ArrayList<>();

        for (int i = 0; i < baseCoinList.size(); i++) {
            // type = 0: 非Token币
            if (!baseCoinList.get(i).getType().equals("")){
                BaseCoinModel model = new BaseCoinModel();
                try {
                    model = (BaseCoinModel) baseCoinList.get(i).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }

                list.add(model);
            }

        }

        return list;

    }

    /**
     * 获取 非 Token币种的简称并组装成集合
     * @return
     */
    public static List<BaseCoinModel> getNotTokenCoinList(){
        initBaseCoinList();

        List<BaseCoinModel> list = new ArrayList<>();

        for (int i = 0; i < baseCoinList.size(); i++) {
            // type = 0: 非Token币
            if (baseCoinList.get(i).getType().equals("0")){

                BaseCoinModel model = new BaseCoinModel();
                try {
                    model = (BaseCoinModel) baseCoinList.get(i).clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                list.add(model);

            }


        }

        return list;

    }

}
