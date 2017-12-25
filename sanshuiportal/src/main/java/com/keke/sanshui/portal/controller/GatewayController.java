package com.keke.sanshui.portal.controller;


import com.alibaba.fastjson.JSON;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayUtil;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.pay.alipay.AlipayConfig;
import com.keke.sanshui.pay.fuqianla.FuqianResponseVo;
import com.keke.sanshui.pay.fuqianla.FuqianlaPayService;
import com.keke.sanshui.pay.zpay.ZPayResponseVo;
import com.keke.sanshui.pay.zpay.ZPayService;
import com.keke.sanshui.util.SignUtil;
import com.keke.sanshui.base.vo.PayVo;
import com.keke.sanshui.pay.paypull.PayPullCallbackVo;
import com.keke.sanshui.service.GateWayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
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
import java.util.Iterator;
import java.util.Map;

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

    @Autowired
    private WXPayConfig wxPayConfig;

    @Autowired
    FuqianlaPayService fuqianlaPayService;

    @Autowired
    AlipayConfig alipayConfig;

    @RequestMapping("/fuqianLa/callback")
    public void handleFuQianNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        FuqianResponseVo fuqianResponseVo = parseFuQianlaRepsonse(request);
        log.info("fuqianResponseVo = {}", fuqianResponseVo);
        boolean matchSign = fuqianlaPayService.checkSign(fuqianResponseVo);
        if (matchSign && StringUtils.equals(fuqianResponseVo.getRet_code(), PAY_PULL_OK)) {
            //支付成功,发送给游戏服务
            try {
                String orderId = fuqianResponseVo.getOrder_no();
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
                updateOrder.setPayState(0);
                updateOrder.setPayTime(String.valueOf(System.currentTimeMillis()));
                updateOrder.setLastUpdateTime(System.currentTimeMillis());
                updateOrder.setOrderNo(fuqianResponseVo.getMerch_id());
                int updateStatus = orderService.updateOrder(updateOrder);
                if(updateStatus == 0){
                    log.warn("update data effect 0,{}",JSON.toJSONString(fuqianResponseVo));
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
                response.getWriter().print("success");
            } catch (Exception e) {
                log.error(" update error ", e);
            }
        }else{
            log.error("秘钥匹配不上{}",fuqianResponseVo);
        }
    }



    @RequestMapping("/wechart/callback")
    public void handleWxNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        byte[] datas = getRequestPostBytes(request);
        String xmlData = new String(datas);
        log.info("xmdData = {}",xmlData);
        WXPay wxPay = new WXPay(wxPayConfig);
        try {
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xmlData);
            if(wxPay.isPayResultNotifySignatureValid(notifyMap)){
                String returnCode = notifyMap.get("return_code");
                String returnMsg = notifyMap.get("return_msg");
                if(StringUtils.equals(returnCode,"SUCCESS")){
                    String orderId = notifyMap.get("out_trade_no");
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
                    if(order.getOrderStatus() == 2){
                        log.info("order deal ready {}",order.getSelfOrderNo());
                        response.getWriter().print("<xml>\n" +
                                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                                "</xml>");
                    }
                    Order updateOrder = new Order();
                    //已支付
                    updateOrder.setSelfOrderNo(orderId);
                    updateOrder.setOrderStatus(3);
                    updateOrder.setPayState(0);
                    updateOrder.setPayType("wxpay");
                    updateOrder.setPayTime(String.valueOf(System.currentTimeMillis()));
                    updateOrder.setLastUpdateTime(System.currentTimeMillis());
                    updateOrder.setOrderNo(notifyMap.get("transaction_id"));
                    int updateStatus = orderService.updateOrder(updateOrder);
                    if(updateStatus == 0){
                        log.warn("update data effect 0,{}",JSON.toJSONString(notifyMap));
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
                    response.getWriter().print("<xml>\n" +
                            "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                            "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                            "</xml>");
                }
            }else{
                log.info("sign error");
            }
        }catch (Exception e){
            log.error("handleWxNotify error",e);
        }

    }

    @RequestMapping("/alipay/callback")
    public void handleAlipayNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String,String> params = Maps.newHashMap();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        log.info("params = {}",params);
        String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");
        String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");
        String trade_status = new String(request.getParameter("trade_status").getBytes("ISO-8859-1"),"UTF-8");
        try {
            boolean verify_result = AlipaySignature.rsaCheckV1(params, alipayConfig.getRsaPublicKey(), AlipayConfig.CHARSET, "RSA2");
            if(verify_result){
                if(trade_status.equals("TRADE_FINISHED")){
                    log.warn("{} finished",out_trade_no);
                }else if (trade_status.equals("TRADE_SUCCESS")){
                    Order order = orderService.queryOrderByNo(out_trade_no);
                    if (order == null) {
                        log.error("错误的订单,orderId = {}", out_trade_no);
                        //修改订单
                        response.getWriter().println("success");
                        return;
                    }
                    if(order.getOrderStatus() == 2){
                        log.info("order deal ready {}",order.getSelfOrderNo());
                        response.flushBuffer();
                        response.getWriter().println("success");
                    }
                    Order updateOrder = new Order();
                    //已支付
                    updateOrder.setSelfOrderNo(out_trade_no);
                    updateOrder.setOrderStatus(3);
                    updateOrder.setPayState(0);
                    updateOrder.setPayType("aplipay");
                    updateOrder.setPayTime(params.get("gmt_payment"));
                    updateOrder.setLastUpdateTime(System.currentTimeMillis());
                    updateOrder.setOrderNo(trade_no);
                    int updateStatus = orderService.updateOrder(updateOrder);
                    if(updateStatus == 0){
                        log.warn("update data effect 0,{}",JSON.toJSONString(params));
                    }
                    //发送给gameServer
                    Pair<Boolean,Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                            order.getMoney(), "0");
                    log.info("orderId={},pair = {}",out_trade_no,JSON.toJSONString(pair));
                    if(pair.getLeft()){
                        Order updateSendOrder = new Order();
                        updateSendOrder.setSelfOrderNo(out_trade_no);
                        if(pair.getRight()) {
                            updateSendOrder.setOrderStatus(2);
                        }
                        updateSendOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                        updateSendOrder.setSendTime(System.currentTimeMillis());
                        log.info("orderStatus = {}",updateOrder.getOrderStatus());
                        orderService.updateOrder(updateSendOrder);
                    }
                }
                response.flushBuffer();
                response.getWriter().println("success");
            }else{//验证失败
                log.error("签名验证失败,{}",params);
                response.getWriter().println("fail");
            }
        }catch (Exception e){
            log.error("",e);
        }

    }

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
                            if(pair.getRight()) {
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

    private FuqianResponseVo parseFuQianlaRepsonse(HttpServletRequest request) {
        FuqianResponseVo fuqianResponseVo = new FuqianResponseVo();
        Integer  amount =  Integer.valueOf(request.getParameter("amount"));
        String  receive_time =  request.getParameter("receive_time");
        String  complete_time =  request.getParameter("complete_time");
        String  merch_id =  request.getParameter("merch_id");
        String  charge_id =  request.getParameter("charge_id");
        String  order_no =  request.getParameter("order_no");
        String ret_code = request.getParameter("ret_code");
        String ret_info = request.getParameter("ret_info");
        String optional = request.getParameter("optional");
        String version = request.getParameter("version");
        String sign_type = request.getParameter("sign_type");
        String sign_info = request.getParameter("sign_info");

        fuqianResponseVo.setAmount(amount);
        fuqianResponseVo.setReceive_time(receive_time);
        fuqianResponseVo.setComplete_time(complete_time);
        fuqianResponseVo.setMerch_id(merch_id);
        fuqianResponseVo.setCharge_id(charge_id);
        fuqianResponseVo.setOrder_no(order_no);
        fuqianResponseVo.setRet_code(ret_code);
        fuqianResponseVo.setRet_info(ret_info);
        fuqianResponseVo.setOptional(optional);
        fuqianResponseVo.setVersion(version);
        fuqianResponseVo.setSign_type(sign_type);
        fuqianResponseVo.setSign_info(sign_info);
        return fuqianResponseVo;
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
