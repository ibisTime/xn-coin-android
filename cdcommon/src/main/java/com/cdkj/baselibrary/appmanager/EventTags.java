package com.cdkj.baselibrary.appmanager;

/**
 * Created by 李先俊 on 2017/7/18.
 */

public class EventTags {


    //修改手机号后刷新数据
    public final static String CHANGEPHONENUMBER_REFRESH = "1";
    //修改昵称后刷新数据
    public final static String CHANGENICK_REFRESH = "10";
    //修改资金密码
    public final static String CHANGE_PAY_PWD_REFRESH = "2";
    //结束所有界面
    public final static String AllFINISH = "3";
    //改变MainActivity显示界面
    public final static String MAIN_CHANGE_SHOW_INDEX = "4";
    //结束MAINACTIVITY
    public final static String MAINFINISH= "5";
    //登录成功刷新
    public final static String LOGINREFRESH= "6";
    //登录成功刷新
    public final static String BUILD_TYPE= "7";
    // 刷新语言
    public final static String EVENT_REFRESH_LANGUAGE= "8";

    //
    public final static String BASE_COIN_LIST= "base_coin_list";
    public final static String BASE_COIN_LIST_NOTIFY_ALL= "base_coin_list_notify_all";
    public final static String BASE_COIN_LIST_NOTIFY_SINGEL= "base_coin_list_notify_single";

    // 订单币种
    public final static String ORDER_COIN_TYPE = "order_coin_type";
    // 订单Fragment未读消息提示
    public final static String ORDER_COIN_TIP = "order_coin_tip";
    // 订单Fragment未读消息提示
    public final static String ORDER_COIN_NOW_TIP = "order_coin_now_tip";
    // 订单Fragment未读消息提示
    public final static String ORDER_COIN_DONE_TIP = "order_coin_done_tip";
    // 关闭订单页面
    public final static String ORDER_CLOSE= "order_close";

    // 改变交易显示界面
    public final static String DEAL_PAGE_CHANGE = "deal_page_change";

    public final static String CHANGE_CODE_BTN= "change_code_btn";

    // 提币地址选择
    public final static String ADDRESS_SELECT= "address_select";

    // 最新行情价格
    public final static String COIN_PRICE_CHANGE= "coin_price_change";


    // 消息加载
    public final static String IM_MSG_LOAD= "im_msg_load";
    // 消息提醒
    public final static String IM_MSG_VIBRATOR= "im_msg_vibrator";
    public final static String IM_MSG_UPDATE= "im_msg_update";
    public final static String IM_MSG_UPDATE_ORDER= "im_msg_update_order";
    // 进行中订单消息提醒
    public final static String IM_MSG_TIP_NEW= "im_msg_tip_new";
    // 已结束订单消息提醒
    public final static String IM_MSG_TIP_DONE= "im_msg_tip_done";
}
