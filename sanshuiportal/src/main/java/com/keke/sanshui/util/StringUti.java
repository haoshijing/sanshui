package com.keke.sanshui.util;

import com.alibaba.fastjson.JSONObject;

public class StringUti {

    static final String quote = "\"";
    static final String dot = ",";
    static final String mao = ":";
    static String[] l1Key = new String[]{"code","message","data"};
    static String[] l2Key = new String[]{"orderId", "tradeNo","outTradeNo", "orderAmount","payAmount", "dateTime"};

    public static String parseJson(JSONObject paramJson) {
        //code message data{orderId tradeNo outTradeNo orderAmount dateTime}
        String res = parseLevel(paramJson, l1Key);
        res = res.substring(0, res.length()-1)+"}}";
        System.out.println(res);
        return res;
    }

    private static String parseLevel(JSONObject jsonObject, String[] keys){
        StringBuilder stringBuilder = new StringBuilder("{");
        for(String key1 : keys){
            Object object = jsonObject.get(key1);
            if(object instanceof String){
                stringBuilder.append(quote).append(key1).append(quote).append(mao).append(quote).append(object).append(quote).append(dot);
            }else if(object instanceof JSONObject){
                String level2 = parseLevel((JSONObject) object, l2Key);
                stringBuilder.append(quote).append(key1).append(quote).append(mao);
                stringBuilder.append(level2);
            }else{
                stringBuilder.append(quote).append(key1).append(quote).append(mao).append("").append(object).append("").append(dot);
            }
        }
        return stringBuilder.toString();
    }
}
