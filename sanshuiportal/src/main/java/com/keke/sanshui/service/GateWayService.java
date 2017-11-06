package com.keke.sanshui.service;

import com.keke.sanshui.base.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class GateWayService {

    @Value("${gameServerKey}")
    private String gameServerKey;

    @Value("${gameServerHost}")
    private String gameServerHost;

    @Autowired
    private HttpClient httpClient;

    public boolean sendToGameServer(String orderId, Integer gUid, String payMoney, String payCoupon) {
        String sign = SignUtil.createSign(orderId, gUid, payMoney, gameServerKey);
        String sendUrl = String.format("%s/?method=PlayerRecharge&OrderId=%s" +
                "&Guid=%s&RechargeMoney=%s&RechargeGold=%s&Sign=%s", gameServerHost,orderId, gUid, payMoney, payCoupon, sign);
        try {
            ContentResponse contentResponse = httpClient.newRequest(sendUrl).timeout(3000, TimeUnit.MILLISECONDS).send();
            log.info("contentResponse = {}", contentResponse.getContentAsString());
            return true;
           // return contentResponse.getStatus() == 200;
        } catch (Exception e) {
            log.error("send error",e);
        }
        return false;
    }
}
