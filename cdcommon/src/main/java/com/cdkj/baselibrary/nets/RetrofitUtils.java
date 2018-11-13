package com.cdkj.baselibrary.nets;


import com.cdkj.baselibrary.api.BaseApiServer;
import com.cdkj.baselibrary.appmanager.EventTags;
import com.cdkj.baselibrary.appmanager.MyConfig;
import com.cdkj.baselibrary.appmanager.SPUtilHelper;

import org.greenrobot.eventbus.Subscribe;

import retrofit2.Retrofit;

/**
 * 服务器api
 * Created by Administrator on 2016/9/1.
 */
public class RetrofitUtils {

    public static String baseURL = null;

    private static Retrofit retrofitInstance = null;

    private RetrofitUtils() {
    }

    /**
     * 获取Retrofit实例
     *
     * @return Retrofit
     */
    private static Retrofit getInstance() {
//        if (retrofitInstance == null) {
            retrofitInstance = new Retrofit.Builder()
                    .baseUrl(getBaseURL())
                    .client(OkHttpUtils.getInstance())
                    .addConverterFactory(FastJsonConVerter.create())
                    .build();
//        }
        return retrofitInstance;
    }

    /**
     * 切换环境之后重新初始化Retrofit
     * @param tag
     */
    @Subscribe
    public void clearRetrofit(String tag){
        if (tag.equals(EventTags.BUILD_TYPE)){
            retrofitInstance = null;
        }
    }

    /**
     * 创建Retrofit请求Api
     *
     * @param clazz Retrofit Api接口
     * @return api实例
     */
    public static <T> T createApi(Class<T> clazz) {
        return getInstance().create(clazz);
    }

    public static BaseApiServer getBaseAPiService() {
        return createApi(BaseApiServer.class);
    }

    /**
     *
     * @return
     */
    public static String getBaseURL() {

        if (MyConfig.IS_DEBUG){

            switch (SPUtilHelper.getAPPBuildType()){

                case SPUtilHelper.BUILD_TYPE_TEST: // 测试
                    return MyConfig.BASE_URL_TEST;

                default: // 研发
                    return MyConfig.BASE_URL_DEV;

            }
        }else{
            // 线上
            return MyConfig.BASE_URL_ONLINE;
        }

    }
}
