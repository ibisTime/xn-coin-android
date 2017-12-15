package com.cdkj.baselibrary.nets;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2016-04-20.
 */


public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) throws IOException {
        String s = JSON.toJSONString(value);
//        JSON.toJSONBytes(value)
        return RequestBody.create(MEDIA_TYPE,s.getBytes("UTF-8"));
    }
}
