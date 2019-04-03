package com.keke.sanshui.portal.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.pay.easyjh.EasyJhPayService;
import com.keke.sanshui.pay.easyjh.callback.EasyJhCallbackVo;
import com.keke.sanshui.pay.huayue.HuayuePayService;
import com.keke.sanshui.pay.huayue.callback.EastYCallbackVo;
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
import java.net.URLDecoder;
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

    @Autowired
    private HuayuePayService huayuePayService;

    @RequestMapping(value = "/huayue/callback")
    public void huayueCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseStr = new String(getRequestPostBytes(request));
        log.info("responseStr = {}",responseStr);
        EastYCallbackVo callbackVo = JSONObject.parseObject(responseStr,EastYCallbackVo.class);
        boolean matchSign = huayuePayService.checkCallbackSign(callbackVo);
        if (matchSign) {
            //支付成功,发送给游戏服务
            if (StringUtils.equalsIgnoreCase(callbackVo.getCode(), "400028")) {
                try {
                    String orderId = callbackVo.getOrderId();
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
                    updateOrder.setPayState(Integer.valueOf(callbackVo.getCode()));
                    updateOrder.setPayTime(String.valueOf(callbackVo.getDateTime()));
                    updateOrder.setLastUpdateTime(System.currentTimeMillis());
                    updateOrder.setOrderNo(callbackVo.getOutTradeNo());
                    int updateStatus = orderService.updateOrder(updateOrder);
                    if (updateStatus == 0) {
                        log.warn("update data effect 0,{}", JSON.toJSONString(callbackVo));
                    }


                    String attach =    order.getAttach();
                    JSONObject jsonObject = JSON.parseObject(attach);

                    String card = jsonObject.getString("card");

                    String more = jsonObject.getString("more");
                    //发送给gameServer
                    Pair<Boolean, Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                            String.valueOf(Integer.valueOf(order.getPrice())/100), card,more);
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
                    response.getWriter().write("success");
                } catch (Exception e) {
                    log.error(" update error ", e);
                }
            }
        } else {
            log.error("秘钥匹配不上{}", callbackVo);
        }
    }

    @RequestMapping(value = "/easyJh/callback")
    public void easyJhCallback(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String responseStr = new String(getRequestPostBytes(request));
        Map<String, String> params = Maps.newHashMap();
        String[] arr = responseStr.split("&");
        for(int i = 0; i < arr.length; i++){
            String[] arr1 = arr[i].split("=");
            if(arr1.length == 2){
                params.put(arr1[0], URLDecoder.decode(arr1[1]));
            }
        }
        log.info("params = {}",params);
        EasyJhCallbackVo responseVo = new EasyJhCallbackVo();

        responseVo.setAttach(params.get("attach"));
        responseVo.setBody(params.get("body"));
        responseVo.setTrade_status(params.get("trade_status"));
        responseVo.setMerchant_id(params.get("merchant_id"));
        responseVo.setOut_trade_no(params.get("out_trade_no"));
        responseVo.setSubject(params.get("subject"));
        responseVo.setTotal_fee(params.get("total_fee"));
        responseVo.setTrade_result(params.get("trade_result"));
        responseVo.setTrade_time(params.get("trade_time"));
        responseVo.setTrade_no(params.get("trade_no"));
        responseVo.setSign(params.get("sign"));
        responseVo.setMerchant_id(params.get("merchant_id"));
        log.info("get responseVo = {}",responseVo);
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

                    String attach = responseVo.getAttach();
                    JSONObject jsonObject = JSON.parseObject(attach);

                    String card = jsonObject.getString("card");

                    String more = jsonObject.getString("more");
                    //发送给gameServer
                    Pair<Boolean, Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                            String.valueOf(Integer.valueOf(order.getPrice())/100), card,more);
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
                    response.getWriter().write("success");
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
