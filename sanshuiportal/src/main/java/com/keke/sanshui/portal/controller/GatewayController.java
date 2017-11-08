package com.keke.sanshui.portal.controller;


import com.alibaba.fastjson.JSON;
import com.keke.sanshui.base.admin.po.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.pay.zpay.ZPayResponseVo;
import com.keke.sanshui.pay.zpay.ZPayService;
import com.keke.sanshui.util.SignUtil;
import com.keke.sanshui.base.vo.PayVo;
import com.keke.sanshui.pay.paypull.PayPullCallbackVo;
import com.keke.sanshui.service.GateWayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@Slf4j
public class GatewayController {

    @Value("${pkey}")
    private String pkey;

    @Autowired
    private OrderService orderService;

    private static final String PAY_OK = "2";

    private static final String PAY_PULL_OK = "0000";

    @Autowired
    GateWayService gateWayService;

    @Autowired
    ZPayService zPayService;


    @RequestMapping("/paypull/callback")
    public void handleNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String retMsg = request.getParameter("msg");
        if(StringUtils.isNotEmpty(retMsg)) {
            try {
                byte[] retCodeData = getRequestPostBytes(request);
                PayPullCallbackVo payPullCallbackVo = JSON.parseObject(retCodeData, PayPullCallbackVo.class);
                if(payPullCallbackVo != null){
                    if(StringUtils.equals(payPullCallbackVo.getRet_code(),PAY_PULL_OK)) {
                        String orderId = payPullCallbackVo.getOrder_no();
                        Order order = orderService.queryOrderByNo(orderId);
                        if (order == null) {
                            log.error("错误的订单,orderId = {}", orderId);
                            //修改订单
                        }
                        Order updateOrder = new Order();
                        //已支付
                        updateOrder.setSelfOrderNo(orderId);
                        updateOrder.setOrderStatus(3);
                        updateOrder.setPayState(0);
                        updateOrder.setPayType("wechart");
                        updateOrder.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(payPullCallbackVo.getReceive_time())));
                        updateOrder.setLastUpdateTime(System.currentTimeMillis());
                        updateOrder.setOrderNo(payPullCallbackVo.getCharge_id());
                        int updateStatus = orderService.updateOrder(updateOrder);
                        if(updateStatus == 0){
                            log.warn("update data effect 0,{}",JSON.toJSONString(payPullCallbackVo));
                        }
                        //发送给gameServer
                        Pair<Boolean,Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                                order.getMoney(), "0");
                        if(pair.getLeft()){
                            Order updateSendOrder = new Order();
                            updateSendOrder.setSelfOrderNo(orderId);
                            if(!pair.getRight()) {
                                updateOrder.setOrderStatus(2);
                            }
                            updateSendOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                            updateSendOrder.setSendTime(System.currentTimeMillis());
                            orderService.updateOrder(updateSendOrder);
                        }


                        handlerResponseOk(response);
                    }
                }
            } catch (Exception e) {
                log.error("解析失败{} ", e);
            }
        }
    }


    private void handlerResponseOk(HttpServletResponse response){
        response.setContentType("text/html");
        try {
            response.getWriter().write("success");
        }catch (Exception e){

        }
    }

    @RequestMapping(value = "/zPay/callback")
    void zPayCallback(HttpServletRequest request, HttpServletResponse response) {
        ZPayResponseVo responseVo = parseZPayReponse(request);
        log.info("responseVo = {}", responseVo);
        boolean matchSign = zPayService.checkSign(responseVo);
        if (matchSign && StringUtils.equals(responseVo.getCode(), "0")) {
            //支付成功,发送给游戏服务
            try {
                String orderId = responseVo.getOut_trade_no();
                Order order = orderService.queryOrderByNo(orderId);
                if (order == null) {
                    log.error("错误的订单,orderId = {}", orderId);
                    String envName = System.getProperty("env");
                    if(StringUtils.isEmpty(envName)){
                        envName = System.getenv("env");
                    }
                    if(StringUtils.equals(envName,"test")){
                        response.getWriter().print("0");
                        return;
                    }
                }
                Order updateOrder = new Order();
                //已支付
                updateOrder.setSelfOrderNo(orderId);
                updateOrder.setOrderStatus(3);
                updateOrder.setPayState(Integer.valueOf(responseVo.getCode()));
                updateOrder.setPayType(responseVo.getPay_way());
                updateOrder.setPayTime(String.valueOf(System.currentTimeMillis()));
                updateOrder.setLastUpdateTime(System.currentTimeMillis());
                updateOrder.setOrderNo(responseVo.getInvoice_no());
                int updateStatus = orderService.updateOrder(updateOrder);
                if(updateStatus == 0){
                    log.warn("update data effect 0,{}",JSON.toJSONString(responseVo));
                }
                //发送给gameServer
                Pair<Boolean,Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                        order.getMoney(), "0");
                if(pair.getLeft()){
                    Order newUpdateOrder = new Order();
                    newUpdateOrder.setSelfOrderNo(orderId);
                    if(pair.getRight()) {
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
        }else{
            log.error("秘钥匹配不上{}",responseVo);
        }
    }

    private ZPayResponseVo parseZPayReponse(HttpServletRequest request) {
        ZPayResponseVo responseVo = new ZPayResponseVo();
        String app_id = request.getParameter("app_id");
        String code = request.getParameter("code");
        String invoice_no = request.getParameter("invoice_no");
        String money = request.getParameter("money");
        String out_trade_no = request.getParameter("out_trade_no");
        String pay_way = request.getParameter("pay_way");
        String qn = request.getParameter("qn");
        String up_invoice_no = request.getParameter("up_invoice_no");
        String sign = request.getParameter("sign");
        responseVo.setApp_id(app_id);
        responseVo.setCode(code);
        responseVo.setInvoice_no(invoice_no);
        responseVo.setMoney(money);
        responseVo.setOut_trade_no(out_trade_no);
        responseVo.setPay_way(pay_way);
        responseVo.setQn(qn);
        responseVo.setUp_invoice_no(up_invoice_no);
        responseVo.setSign(sign);
        return responseVo;
    }

    @RequestMapping(value = "/pay/callback", method = RequestMethod.POST)
    void doCallback(HttpServletRequest request) {
        PayVo payVo = parseRequest(request);
        log.info("payVo = {}", payVo);
        boolean matchSign = SignUtil.match(payVo,pkey);
        if (matchSign && StringUtils.equals(payVo.getP_state(), PAY_OK)) {
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
                updateOrder.setOrderStatus(3);
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
                Pair<Boolean,Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                        order.getMoney(), "0");
                if(pair.getLeft()){
                    Order updateSendOrder = new Order();
                    updateSendOrder.setSelfOrderNo(orderId);
                    if(pair.getRight()) {
                        updateSendOrder.setOrderStatus(2);
                    }
                    updateSendOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                    updateSendOrder.setSendTime(System.currentTimeMillis());
                    log.info("orderStatus = {}",updateOrder.getOrderStatus());
                    orderService.updateOrder(updateSendOrder);
                }
            } catch (Exception e) {
                log.error(" update error ", e);
            }
        }else{
            log.error("秘钥匹配不上{}",payVo);
        }
    }

    private PayVo parseRequest(HttpServletRequest request) {
        PayVo payVo = new PayVo();
        String bodyData = "";
        try {
            bodyData = new String(getRequestPostBytes(request), "utf-8");
            payVo = JSON.parseObject(bodyData, PayVo.class);
        } catch (Exception e) {
        }
        return payVo;
    }

    public  byte[] getRequestPostBytes(HttpServletRequest request)
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
