package com.cdkj.baselibrary.nets;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/*
*
 * Created by Administrator on 2016-04-20.

*/

public class FastJsonConVerter<T> extends Converter.Factory{

    public static FastJsonConVerter create() {
        return new FastJsonConVerter();
    }

/**
     * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
  */

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type);
    }

/*
*
     * 需要重写父类中responseBodyConverter，该方法用来转换发送给服务器的数据

*/

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>();
    }
}
