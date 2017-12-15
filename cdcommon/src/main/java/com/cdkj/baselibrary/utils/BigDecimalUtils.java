package com.cdkj.baselibrary.utils;

import java.math.BigDecimal;

/**
 * Created by 李先俊 on 2017/7/19.
 */

public class BigDecimalUtils {

    public static int intValue(BigDecimal b){
        if(b!=null){
            return b.intValue();
        }

        return 0;
    }
    //减法
    public static BigDecimal subtract(BigDecimal b,BigDecimal b1){
        if(b!=null && b1!=null){
            return b.subtract(b1);
        }

        return new BigDecimal(0);
    }
     //乘法
    public static BigDecimal multiply(BigDecimal b,BigDecimal b1){
        if(b!=null && b1!=null){
            return b.multiply(b1);
        }

        return new BigDecimal(0);
    }

    //加法
    public static BigDecimal add(BigDecimal b,BigDecimal b1){
        if(b!=null && b1!=null){
            return b.add(b1);
        }

        return new BigDecimal(0);
    }

}
