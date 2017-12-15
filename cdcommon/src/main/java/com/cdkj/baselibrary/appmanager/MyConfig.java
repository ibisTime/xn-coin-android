package com.cdkj.baselibrary.appmanager;

/**
 * Created by 李先俊 on 2017/8/8.
 */

public class MyConfig {
    public final static String COMPANYCODE="CD-COIN000017";
    public final static String SYSTEMCODE="CD-COIN000017";

    public final static String USERTYPE="C";//用户类型

    public static String IMGURL="http://ozfszueqz.bkt.clouddn.com/";

    public static final boolean IS_DEBUG = Boolean.parseBoolean("true");

    // 拍照文件保存路径
    public static final String CACHDIR = "beikeying";


    // 环境访问地址
    public static String BASE_URL; // 研发

//    public static String BASE_URL_DEV="http://apidev.bcoin.im"; // 研发
//    public static String BASE_URL="http://47.52.77.214:4001/forward-service/"; // 测试/线上
}
