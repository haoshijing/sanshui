package com.keke.sanshui.pay.huayue.order;

import com.google.common.collect.Maps;
import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

@Data
public class EastYOrderRequestVo extends BaseWithMapVo {
    private String orderAmount;
    private String orderId;
    private String merchant;
    private String payMethod;
    private String payType;
    private String signType;
    private String version;
    private String outcome;
    private String productName;
    private String productDesc;
    private String createTime = String.valueOf(System.currentTimeMillis()/1000);
    private String notifyUrl;
    private String returnUrl;

    public Map<String, String> toSignMap() {
        Map<String, String> sortedMap = toMap();
        sortedMap.remove("notifyUrl");
        sortedMap.remove("returnUrl");
        sortedMap.remove("createTime");
        sortedMap.remove("productName");
        sortedMap.remove("productDesc");
        return sortedMap;
    }

    public String toStr(){
        StringBuilder stringBuilder = new StringBuilder(2048);
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String data = (String) ReflectionUtils.getField(field, this);
            if (StringUtils.isEmpty(data)) {
                data = "";
            }
            stringBuilder.append(field.getName()).append("=").append(data).append("&");

        }
        return stringBuilder.toString();
    }
}
