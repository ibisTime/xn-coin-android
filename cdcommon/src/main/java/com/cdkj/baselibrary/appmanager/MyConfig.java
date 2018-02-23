package com.cdkj.baselibrary.appmanager;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public class MyConfig {

    // 法币币种
    public final static String CURRENCY = "CNY";
    // 支持币种
    public final static String[] COIN_TYPE = {"ETH","SC"};

    public final static String COMPANYCODE = "CD-COIN000017";
    public final static String SYSTEMCODE = "CD-COIN000017";

    public final static String USERTYPE="C";//用户类型

    public static String IMGURL = "http://ozfszueqz.bkt.clouddn.com/";

    public static boolean IS_DEBUG = false;

    // 拍照文件保存路径
    public static final String CACHDIR = "beikeying";
    // 环境访问地址
    public static final String BASE_URL_DEV="http://121.43.101.148:4001/forward-service/"; // 研发
    public static final String BASE_URL_TEST="http://47.96.161.183:4001/forward-service/"; // 测试
    public static final String BASE_URL_ONLINE="https://www.bcoin.im/"; // 线上
}
