package com.keke.sanshui.pay.huayue.order;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;

import java.util.Map;

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
}
