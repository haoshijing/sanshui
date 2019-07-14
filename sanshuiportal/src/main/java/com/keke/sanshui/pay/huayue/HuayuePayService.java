package com.keke.sanshui.pay.huayue;

import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.cache.SystemConfigService;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.pay.easyjh.callback.EasyJhCallbackVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhRequestVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhResponseVo;
import com.keke.sanshui.pay.huayue.callback.EastYCallbackVo;
import com.keke.sanshui.pay.huayue.order.EastYOrderRequestVo;
import com.keke.sanshui.pay.huayue.order.EastYOrderResponseVo;
import com.keke.sanshui.pay.huayue.query.EastYQueryVo;
import com.keke.sanshui.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.Base64;

@Component
public class HuayuePayService {

    @Value("${huayueId}")
    private String appId;


    @Value("${huayueKey}")
    private String signKey;

    @Autowired
    private SystemConfigService systemConfigService;


    public EastYOrderRequestVo createRequestVo(PayLink payLink, String payType, String orderId, String guid, String ip) {
        EastYOrderRequestVo requestVo = new EastYOrderRequestVo();
        requestVo.setMerchant(appId);
        requestVo.setOrderId(orderId);
        if (StringUtils.equalsIgnoreCase(payType, "1")) {
            requestVo.setPayType("21");
            requestVo.setPayMethod("2");
        } else if (StringUtils.equalsIgnoreCase(payType, "2")) {
            requestVo.setPayType("11");
            requestVo.setPayMethod("1");
        }
        requestVo.setVersion("1.0");
        requestVo.setOutcome("yes");
        requestVo.setSignType("MD5" );
        requestVo.setProductName("充值" + payLink.getPickCouponVal() + "钻石");
        requestVo.setOrderAmount(String.valueOf(payLink.getPickRmb() * 1.0 / 100));

        requestVo.setProductDesc("充值" + payLink.getPickCouponVal() + "钻石");

        try {
            String callbackHost = systemConfigService.getConfigValue("callbackHost","");
            requestVo.setReturnUrl(URLEncoder.encode(callbackHost + "/huayue/" + orderId,"UTF-8"));
            requestVo.setNotifyUrl(callbackHost + "/huayue/callback");

        } catch (Exception e) {

        }
        String sign = SignUtil.createSign(requestVo.toSignMap(), "",signKey);
        requestVo.setSign(sign);
        return requestVo;
    }

    public boolean checkSign(EastYOrderResponseVo responseVo) {
        String sign = MD5Util.md5(responseVo.getMerchant()+responseVo.getOrderId());
        return StringUtils.equalsIgnoreCase(responseVo.getSign(), sign);
    }

    public EastYQueryVo createQueryOrderRequest(String orderId){
        EastYQueryVo eastYQueryVo = new EastYQueryVo();
        eastYQueryVo.setOrderId(orderId);
        eastYQueryVo.setPartner(appId);
        eastYQueryVo.setSign(MD5Util.md5(signKey+orderId));
        return eastYQueryVo;
    }

    public boolean checkCallbackSign(EastYCallbackVo callbackVo) {
        String base64 = new String(Base64.getEncoder().encode(callbackVo.getParamsJson().getBytes()));
        String sign =  MD5Util.md5(signKey+MD5Util.md5(base64));
        return StringUtils.equalsIgnoreCase(callbackVo.getSign(), sign);

    }
}
