package com.cdkj.baselibrary.utils.payutils;




import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**微信支付
 * Created by Administrator on 2016-06-07.
 */
public class WXUtils {
    public static String SpId; //appid

    /**
     *
     * 将参数按照字段名的 ASCII 码从小到大排序（字典序）后，使用 URL 键值
     * 对的格式（即key1=value1&key2=value2…）拼接成字符串
     *
     * @param paraMap
     *            转换参数
     * @param isURLEncode
     *            是否对所有Value进行URLEncod转码
     * @return
     *
     */
    public static String formatQueryParaMap(Map<String, String> paraMap, boolean isURLEncode) {
        String returnValue = null;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<>(paraMap.entrySet());
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            StringBuffer buff = new StringBuffer();
            for (int i = 0; i < infoIds.size(); i++) {
                Map.Entry<String, String> item = infoIds.get(i);
                if (null != item.getKey() && !"".equals(item.getKey())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (isURLEncode) {
                        val = URLEncoder.encode(val, "UTF-8");
						/* 空格通过URLEncoder转码后是“+”,所有要替换成“%20” */
                        val = val.replace("+", "%20");
                    }
                    buff.append(key + "=" + val + "&");
                }
            }
            returnValue = buff.toString();
            if (null != returnValue && !"".equals(returnValue)) {
                returnValue = returnValue.substring(0, returnValue.length() - 1);
//                returnValue = StringUtils.subString(returnValue,0,returnValue.length() - 1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return returnValue;
    }


//    public static String getSing(String s){
//
//        String stringSignTemp=s+"&key=SDUIFJHWSHJDBFKSBVASVFGHNVUEJGVB";
//
//        String  sign= MD5.getMD5(stringSignTemp.getBytes()).toUpperCase();
//
//
//        return sign;
//    }

}
