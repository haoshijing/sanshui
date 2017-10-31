package com.keke.sanshui.portal.controller;

import com.alibaba.fastjson.JSON;
import com.keke.sanshui.base.admin.po.Order;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.util.SignUtil;
import com.keke.sanshui.base.vo.PayVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.TimeUnit;


@Controller
@Slf4j
public class GatewayController {

    @Value("${pkey}")
    private String pkey;

    @Value("${gameServerKey}")
    private String gameServerKey;

    @Value("${gameServerHost}")
    private String gameServerHost;
    @Autowired
    private OrderService orderService;

    private static final String PAY_OK = "2";

    @Autowired
    private HttpClient httpClient;

    @RequestMapping(value = "/pay/callback", method = RequestMethod.POST)
    void doCallback(HttpServletRequest request) {
        PayVo payVo = parseRequest(request);
        log.info("payVo = {}", payVo);
        if (!SignUtil.match(payVo, pkey)) {
            log.warn(" 密钥匹配不上 data  = {}", JSON.toJSONString(payVo));
        }

        if (StringUtils.equals(payVo.getP_state(), PAY_OK)) {
            //支付成功,发送给游戏服务
            try {
                String orderId = payVo.getP_attach();
                Order order = orderService.queryOrderByNo(orderId);
                if (order == null) {
                    log.error("错误的订单,orderId = {}", orderId);
                }
                Order updateOrder = new Order();
                //已支付
                updateOrder.setSelfOrderNo(orderId);
                updateOrder.setOrderStatus(2);
                updateOrder.setPayState(Integer.valueOf(payVo.getP_state()));
                updateOrder.setPayType(payVo.getP_type());
                updateOrder.setPayTime(payVo.getP_time());
                updateOrder.setLastUpdateTime(System.currentTimeMillis());
                updateOrder.setOrderNo(payVo.getP_no());
                int updateStatus = orderService.updateOrder(updateOrder);
                if(updateStatus == 0){
                    log.warn("update data effect 0,{}",JSON.toJSONString(payVo));
                }

                //发送给gameServer
                boolean sendOk = sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                        order.getMoney(), "0");
                if (sendOk) {
                    Order updateSendOrder = new Order();
                    updateSendOrder.setSelfOrderNo(orderId);
                    updateSendOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                    updateSendOrder.setSendTime(System.currentTimeMillis());
                    orderService.updateOrder(updateSendOrder);
                }
            } catch (Exception e) {
                log.error(" update error ", e);
            }
        }
    }

    private PayVo parseRequest(HttpServletRequest request) {
        PayVo payVo = new PayVo();
        String bodyData = "";
        try {
            bodyData = new String(getRequestPostBytes(request), "utf-8");
            payVo = JSON.parseObject(bodyData, PayVo.class);
        } catch (Exception e) {
            log.error("bodyData = {}", bodyData, e);
        }
        return payVo;
    }

    public static byte[] getRequestPostBytes(HttpServletRequest request)
            throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0) {
            return null;
        }
        byte buffer[] = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {

            int readlen = request.getInputStream().read(buffer, i,
                    contentLength - i);
            if (readlen == -1) {
                break;
            }
            i += readlen;
        }
        return buffer;
    }

    private boolean sendToGameServer(String orderId, Integer gUid, String payMoney, String payCoupon) {
        String sign = SignUtil.createSign(orderId, gUid, payMoney, gameServerKey);
        String sendUrl = String.format("%s/?method=PlayerRecharge&OrderId=%s" +
                "&Guid=%s&RechargeMoney=%s&RechargeGold=%s&Sign=%s", gameServerHost,orderId, gUid, payMoney, payCoupon, sign);
        try {
            ContentResponse contentResponse = httpClient.newRequest(sendUrl).timeout(3000, TimeUnit.MILLISECONDS).send();
            log.info("contentResponse = {}", contentResponse.getContentAsString());
            return contentResponse.getStatus() == 200;
        } catch (Exception e) {
           log.error("send error",e);
        }
        return false;
    }
}
