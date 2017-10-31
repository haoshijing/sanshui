package com.keke.sanshui.base.util;

import com.google.common.collect.Maps;
import com.keke.sanshui.base.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

@Slf4j
public class SignUtil {

    public final  static boolean match(PayVo payVo, String signKey) {
        boolean match = false;
        String md5Sign =   createPaySign(payVo,signKey);
        match = md5Sign.equals(payVo.getSign());
        return match;
    }

    public final static String createPaySign(PayVo payVo,String signKey){
        Field[] fields = PayVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                String data = (String) ReflectionUtils.getField(field, payVo);
                if (StringUtils.isEmpty(data)) {
                    data = "";
                }
                sortedMap.put(field.getName(),data);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        stringBuilder.append("&pkey=").append(signKey);
        log.info("key={}",stringBuilder.toString());
        String md5Sign = MD5Util.md5(stringBuilder.toString());
        return  md5Sign;
    }

    public final  static  String createSign(String orderId,Integer gUid,String rechargeMoney,String key){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OrderId=").append(orderId).append("&");
        stringBuilder.append("Guid=").append(gUid).append("&");
        stringBuilder.append("RechargeMoney=").append(rechargeMoney);
        stringBuilder.append("&RechargeGold=0");
        stringBuilder.append(key);
        String data =  MD5Util.md5(stringBuilder.toString()).toLowerCase();
        return data;
    }

    public static void main(String[] args) {
        PayVo payVo = new PayVo();
        payVo.setP_no("201725454545");
        payVo.setP_type("alipay");
        payVo.setSign("sssssss");
    }
}

