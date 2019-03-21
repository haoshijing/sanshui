/*
 * @(#) EasyJhPayService.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh;

import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.pay.easyjh.order.EasyJhRequestVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhResponseVo;
import com.keke.sanshui.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.Base64;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Component
@Slf4j
public class EasyJhPayService {

    @Value("${easyJhAppId}")
    private String appId;
    @Value("${callbackHost}")
    private String callbackHost;

    @Value("${easyJhAppEncryptKey}")
    private String signKey;

    public EasyJhRequestVo createRequestVo(PayLink payLink, String payType, String orderId, String guid, String ip) {
        EasyJhRequestVo requestVo = new EasyJhRequestVo();
        requestVo.setMerchant_id(appId);
        requestVo.setOut_trade_no(orderId);
        if (StringUtils.equalsIgnoreCase(payType, "1")) {
            requestVo.setTrade_type("010008");
        } else if (StringUtils.equalsIgnoreCase(payType, "2")) {
            requestVo.setTrade_type("010007");
        }
        requestVo.setUser_ip(ip);
        requestVo.setSubject("充值" + payLink.getPickCouponVal() + "钻石");
        requestVo.setBody("充值" + payLink.getPickCouponVal() + "钻石");
        requestVo.setUser_id(guid);
        requestVo.setTotal_fee(String.valueOf(payLink.getPickRmb() * 1.0 / 100));
        requestVo.setOut_trade_no(orderId);

        try {
            requestVo.setReturn_url(URLEncoder.encode(callbackHost + "/easyjhpay/" + orderId, "UTF-8"));
            requestVo.setReturn_url(URLEncoder.encode(callbackHost + "/gateway/easyjhpay/notify", "UTF-8"));

            JSONObject bizContent = new JSONObject();
            bizContent.put("mch_app_id", "game");
            bizContent.put("device_info", "iOS_SDK");
            bizContent.put("ua", "Mozilla/5.0 (Linux; U; Android 6.0.1");
            bizContent.put("mch_app_name", "game");

            String biz_content = Base64.getEncoder().encodeToString(URLEncoder.encode(bizContent.toJSONString(), "utf-8").getBytes());
            requestVo.setBiz_content(biz_content);
        } catch (Exception e) {

        }
        String sign = SignUtil.createEasyJhSign(requestVo, signKey);
        requestVo.setSign(sign);
        return requestVo;
    }

    public boolean checkSign(EasyJhResponseVo responseVo) {
        String sign = SignUtil.createEasyJhResponseSign(responseVo, signKey);
        return StringUtils.equals(responseVo.getSign(), sign);
    }

}
