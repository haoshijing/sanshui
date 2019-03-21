package com.keke.sanshui.portal.controller;


import com.alibaba.fastjson.JSON;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.pay.easyjh.EasyJhPayService;
import com.keke.sanshui.pay.easyjh.callback.EasyJhCallbackVo;
import com.keke.sanshui.service.GateWayService;
import com.keke.sanshui.util.easyjh.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@Slf4j
public class GatewayController {

    @Value("${pkey}")
    private String pkey;

    @Autowired
    private OrderService orderService;

    @Autowired
    GateWayService gateWayService;

    @Autowired
    private EasyJhPayService easyJhPayService;

    @RequestMapping(value = "/easyJh/callback")
    public void easyJhCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseXml = new String(getRequestPostBytes(request));
        log.info("responseXml = {}", responseXml);
        Map<String, String> map = XmlUtils.parse(responseXml);
        EasyJhCallbackVo responseVo = EasyJhCallbackVo.buildFromMap(map);
        boolean matchSign = easyJhPayService.checkCallbackSign(responseVo);
        if (matchSign) {
            //支付成功,发送给游戏服务
            if (StringUtils.equalsIgnoreCase(responseVo.getTrade_status(), "1")) {
                try {
                    String orderId = responseVo.getOut_trade_no();
                    Order order = orderService.queryOrderByNo(orderId);
                    if (order == null) {
                        log.error("错误的订单,orderId = {}", orderId);
                        String envName = System.getProperty("env");
                        if (StringUtils.isEmpty(envName)) {
                            envName = System.getenv("env");
                        }
                        if (StringUtils.equals(envName, "test")) {
                            response.getWriter().print("0");
                            return;
                        }
                    }
                    Order updateOrder = new Order();
                    //已支付
                    updateOrder.setSelfOrderNo(orderId);
                    updateOrder.setOrderStatus(3);
                    updateOrder.setPayState(Integer.valueOf(responseVo.getTrade_status()));
                    updateOrder.setPayTime(String.valueOf(responseVo.getTrade_time()));
                    updateOrder.setLastUpdateTime(System.currentTimeMillis());
                    updateOrder.setOrderNo(responseVo.getTrade_no());
                    int updateStatus = orderService.updateOrder(updateOrder);
                    if (updateStatus == 0) {
                        log.warn("update data effect 0,{}", JSON.toJSONString(responseVo));
                    }
                    //发送给gameServer
                    Pair<Boolean, Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                            order.getMoney(), "0");
                    if (pair.getLeft()) {
                        Order newUpdateOrder = new Order();
                        newUpdateOrder.setSelfOrderNo(orderId);
                        if (pair.getRight()) {
                            newUpdateOrder.setOrderStatus(2);
                        }
                        newUpdateOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                        newUpdateOrder.setSendTime(System.currentTimeMillis());
                        orderService.updateOrder(newUpdateOrder);
                    }
                    /**
                     * 发送给服务器支付成功
                     */
                    response.getWriter().print("0");
                } catch (Exception e) {
                    log.error(" update error ", e);
                }
            }
        } else {
            log.error("秘钥匹配不上{}", responseVo);
        }
    }

    public byte[] getRequestPostBytes(HttpServletRequest request)
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

}
