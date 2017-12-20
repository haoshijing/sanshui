
package com.keke.sanshui.pay.alipay;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
@Data
public class AlipayConfig {

    @Value("${aliAppId}")
    private String appId;

    @Value("${callbackHost}")
    private String callbackHost;

    @Value("${rsaPrivateKey}")
    private String rsaPrivateKey;

    @Value("${rsaPublicKey}")
    private String rsaPublicKey;

    // 请求网关地址
    public static String URL = "https://openapi.alipay.com/gateway.do";
    // 编码
    public static String CHARSET = "UTF-8";
    // 返回格式
    public static String FORMAT = "json";

    // RSA2
    public static String SIGNTYPE = "RSA2";


    public String getNotifyUrl(){
        String url = new StringBuilder(callbackHost).append("/alipay/callback").toString();
        return url;
    }

    public String getReturnUrl(String orderId){
        String url = new StringBuilder(callbackHost).append("/alipay/user/").append(orderId).toString();
        return url;
    }
}

