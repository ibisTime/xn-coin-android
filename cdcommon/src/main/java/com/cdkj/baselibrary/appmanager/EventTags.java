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
    public final static String MAINCHANGESHOWINDEX = "4";
    //结束MAINACTIVITY
    public final static String MAINFINISH= "5";
    //登录成功刷新
    public final static String LOGINREFRESH= "6";
    //登录成功刷新
    public final static String BUILD_TYPE= "7";
    // 刷新语言
    public final static String EVENT_REFRESH_LANGUAGE= "8";

    //改变交易显示界面
    public final static String DEAL_PAGE_CHANGE = "deal_page_change";

    public final static String CHANGE_CODE_BTN= "change_code_btn";

    // 提币地址选择
    public final static String ADDRESS_SELECT= "address_select";

    // 最新行情价格
    public final static String COIN_PRICE_CHANGE= "coin_price_change";

    // 消息提醒
    public final static String IM_MSG_UPDATE= "im_msg_uodate";
    public final static String IM_MSG_UPDATE_ORDER= "im_msg_uodate_order";
    // 消息提醒
    public final static String IM_MSG_TIP_NEW= "im_msg_tip_new";
    // 消息提醒
    public final static String IM_MSG_TIP_DONE= "im_msg_tip_done";
}
