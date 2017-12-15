package com.cdkj.baselibrary.nets;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by Administrator on 2016-04-20.*/

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }
/*
    * 转换方法*/


    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        if (tempStr.endsWith("\r\n")) {
            tempStr = tempStr.replace("\r\n", "");
        }
        bufferedSource.close();
        return JSON.parseObject(tempStr, type);

    }
}
