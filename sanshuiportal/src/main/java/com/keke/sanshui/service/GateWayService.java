package com.keke.sanshui.service;

import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.base.cache.SystemConfigService;
import com.keke.sanshui.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class GateWayService {

    @Autowired
    private HttpClient httpClient;

    @Autowired
    private SystemConfigService systemConfigService;

    public Pair<Boolean,Boolean> sendToGameServer(String orderId, Integer gUid, String payMoney, String payCoupon,String moreCoupon) {
        String gameServerKey = systemConfigService.getConfigValue("gameServerKey","sysssfasdfj;hldfas;yyssssdafsafjahl");
        String gameServerHost = systemConfigService.getConfigValue("gameServerHost","http://sss.cn-newworld.com:3344");
        String sign = SignUtil.createSign(orderId, gUid, payMoney,payCoupon,moreCoupon, gameServerKey);
        String sendUrl = String.format("%s/?Method=PlayerRecharge&OrderId=%s" +
                "&Guid=%s&Money=%s&Card=%s&More=%s&Sign=%s", gameServerHost,orderId, gUid, payMoney,payCoupon,moreCoupon, sign);
        try {
            log.info("sendUrl = {}",sendUrl);
            ContentResponse contentResponse = httpClient.newRequest(sendUrl).timeout(3000, TimeUnit.MILLISECONDS).send();
            JSONObject jsonObject = JSONObject.parseObject(contentResponse.getContentAsString());
            log.info("contentResponse = {}", contentResponse.getContentAsString());
            if(jsonObject != null){
                String resultCode = jsonObject.getString("resultCode");
                boolean dealOk = StringUtils.equalsIgnoreCase("Successed",resultCode) ||
                        StringUtils.equalsIgnoreCase("Begin",resultCode) ||
                        StringUtils.equalsIgnoreCase("OrderIdExist", resultCode);
                return Pair.of(true,dealOk);
            }
            return Pair.of(true,false);
           // return contentResponse.getStatus() == 200;
        } catch (Exception e) {
            log.error("send error",e);
            return Pair.of(false,false);
        }
    }
}
