package com.keke.sanshui.portal.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.github.wxpay.sdk.WXPay;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.admin.service.PayService;
import com.keke.sanshui.pay.alipay.AliPayService;
import com.keke.sanshui.pay.alipay.AlipayConfig;
import com.keke.sanshui.pay.fuqianla.FuqianlaPayService;
import com.keke.sanshui.pay.fuqianla.FuqianlaRequestVo;
import com.keke.sanshui.pay.paypull.PaypullRequestVo;
import com.keke.sanshui.pay.wechart.MyWxConfig;
import com.keke.sanshui.pay.wechart.WechartPayService;
import com.keke.sanshui.pay.zpay.ZPayRequestVo;
import com.keke.sanshui.pay.zpay.ZPayService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class PayController {
    @Autowired
    private PayService payService;

    @Value("${payPullAppId}")
    private String payPullAppId;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ZPayService zPayService;

    private final static String ZPAY_BASE_URL = "http://pay.csl2016.cn:8000";

    @Autowired
    private FuqianlaPayService fuqianlaPayService;

    @Value("${callbackHost}")
    private String callbackHost;
    @Autowired
    private WechartPayService wechartPayService;
    @Resource
    AliPayService aliPayService;

    @Autowired
    AlipayConfig alipayConfig;

    @Autowired
    MyWxConfig wxPayConfig;

    @RequestMapping("/goPay")
    String goPay(HttpServletRequest request, String guid, Model modelAttribute, HttpServletResponse httpResponse) {
        log.info("guid = {}", guid);
        Integer defaultPick = 0;
        String token = new StringBuilder(guid).append("-").append(System.currentTimeMillis()).toString();
        request.getSession().setAttribute("payToken", token);
        List<PayLink> payLinks = payService.queryAllLink();
        if (payLinks.size() > 0) {
            defaultPick = payLinks.get(0).getId();
        }
        modelAttribute.addAttribute("payToken", token);
        modelAttribute.addAttribute("payLinks", payLinks);
        modelAttribute.addAttribute("guid", guid);
        modelAttribute.addAttribute("defaultPick", defaultPick);
        modelAttribute.addAttribute("defaultPayType", 1);
        return "recharge";
    }

    @RequestMapping("/pay/user/{orderId}")
    String success(@PathVariable String orderId, Model model) {
        String response = zPayService.queryOrder(orderId);
        log.info("response = {}", response);
        try {
            Map<String, String> data = JSON.parseObject(response, Map.class);
            model.addAttribute("message", data.get("message"));
        } catch (Exception e) {
            model.addAttribute("message", "支付未完成");
        }
        return "success";
    }
    @RequestMapping("/alipay/user/{orderId}")
    String alipaySuccess(HttpServletRequest request ,@PathVariable String orderId, Model model) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", alipayConfig.getAppId(),
                alipayConfig.getRsaPrivateKey(),
                "json", AlipayConfig.CHARSET,
                alipayConfig.getRsaPublicKey(),
                "RSA2"); //获得初始化的AlipayClient
        AlipayTradeQueryRequest alipayTradeQueryRequest = new AlipayTradeQueryRequest();//创建API对应的request类
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no",orderId);
        alipayTradeQueryRequest.setBizContent(jsonObject.toJSONString());//设置业务参数
        try {
            AlipayTradeQueryResponse response = alipayClient.execute(alipayTradeQueryRequest);
            log.info("response = {}",JSON.toJSONString(response));
            if(response != null){
                String tradeStatus = response.getTradeStatus();
                model.addAttribute("message","");
                if(StringUtils.equals(tradeStatus,"TRADE_SUCCESS")){
                    model.addAttribute("message", "支付成功");
                }else if(StringUtils.equals(tradeStatus,"TRADE_CLOSED")){
                    model.addAttribute("message", "交易已关闭");
                }else if(StringUtils.equals(tradeStatus,"TRADE_FINISHED")){
                    model.addAttribute("message", "交易结束");
                }else if(StringUtils.equals(tradeStatus,"WAIT_BUYER_PAY")){
                    model.addAttribute("message", "未付款");
                }
            }
        }catch (Exception e){
            model.addAttribute("message","");
            log.error("查询失败,",e);
        }
        return "success";
    }

    @RequestMapping("/wxpay/user/{orderId}")
    String wxSuccess(@PathVariable String orderId, Model model) {
        WXPay wxpay = new WXPay(wxPayConfig);

        Map<String, String> data = new HashMap<String, String>();
        data.put("out_trade_no", orderId);
        try {
            Map<String, String> resp = wxpay.orderQuery(data);
            log.info("resp = {}",resp);
            String resultCode = resp.get("result_code");
            model.addAttribute("message", "");
            if(StringUtils.equals(resultCode,"SUCCESS")){
                String trade_state = resp.get("trade_state");
                if(StringUtils.equals(trade_state,"SUCCESS")){
                    model.addAttribute("message", "支付成功");
                }else if(StringUtils.equals(trade_state,"NOTPAY")){
                    model.addAttribute("message", "未支付");
                }else if(StringUtils.equals(trade_state,"CLOSED")){
                    model.addAttribute("message", "支付已关闭");
                }else if(StringUtils.equals(trade_state,"USERPAYING")){
                    model.addAttribute("message", "用户支付中");
                }
            }
        } catch (Exception e) {
            model.addAttribute("message", "支付未完成");
        }
        return "success";
    }


    @RequestMapping("/goPayPage")
    String doGoPayPage(Integer pickId, Integer guid, String token, HttpServletRequest request, Model modelAttribute) {
        StringBuilder buildUrl = new StringBuilder();
        PayLink payLink = payService.getCid(pickId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        String selfOrderId = guid + "" + System.currentTimeMillis();
        buildUrl.append(payLink.getCIdNo()).append(".js?type=div");
        buildUrl.append("&attach=").append(selfOrderId);
        try {
            //buildUrl.append("&redirect=http://game.youthgamer.com:8080/sanshui/pay/user/sucess");
            buildUrl.append("&callback=").append(URLEncoder.encode(callbackHost + "/pay/callback", "utf-8"));
        } catch (Exception e) {

        }
        orderService.insertOrder(payLink, attach, selfOrderId);
        modelAttribute.addAttribute("url", buildUrl.toString());

        return "payPage";
    }

    @RequestMapping("/doFuQianLa")
    public String doFuQianLa(Integer pickId, Integer guid, String payType, Model modelAttribute) {
        log.info("doFuQianLa pickId={},guid = {}", pickId, guid);
        String selfOrderId = guid + "" + System.currentTimeMillis();
        PayLink payLink = payService.getCid(pickId);
        FuqianlaRequestVo payRequest = fuqianlaPayService.createRequestVo(payLink, payType, selfOrderId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        payRequest.setGuid(guid.toString());
        orderService.insertOrder(payLink, attach, selfOrderId);
        modelAttribute.addAttribute("payRequest", payRequest);
        return "fuqian";
    }
    @RequestMapping("/doNewPay")
    public String doNewPay(HttpServletRequest request,Integer pickId, Integer guid, String payType,HttpServletResponse response) {
        if(StringUtils.equals(payType,"1")){
            return doWxPay(request,pickId,guid,response);
        }else{
            return doAlipay(request,pickId,guid,response);
        }
    }

    @RequestMapping("/doWxPay")
    public String doWxPay(HttpServletRequest request, Integer pickId, Integer guid, HttpServletResponse response) {
        log.info("doWxPay pickId={},guid = {}", pickId, guid);
        String selfOrderId = guid + "" + System.currentTimeMillis();
        PayLink payLink = payService.getCid(pickId);
        WXPay wxPay = new WXPay(wxPayConfig);
        Map<String, String> datas = wechartPayService.createPreOrderVo(request, payLink, selfOrderId);
        try {
            Map<String, String> responseData = wxPay.unifiedOrder(datas);
            log.info("gui= {}, orderId = {},responseData = {},datas = {}",
                    guid,selfOrderId,responseData,datas);
            if (responseData != null) {
                String return_msg = responseData.get("return_msg");
                if (StringUtils.equals(return_msg, "OK")) {
                    String url = responseData.get("mweb_url");
                    String prepare_id = responseData.get("prepare_id");
                    Map<String, String> attach = Maps.newHashMap();
                    attach.put("guid", guid.toString());
                    attach.put("prepare_id", prepare_id);
                    orderService.insertOrder(payLink, attach, selfOrderId);
                    url+="&redirect_url="+wechartPayService.getReturnUrl(selfOrderId);
                    if (StringUtils.isNotEmpty(url)) {
                        response.sendRedirect(url);
                    }
                    return null;
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return "forward:/wxpay/user/" + selfOrderId;
    }
    @RequestMapping("/doAlipay")
    public String doAlipay(HttpServletRequest request, Integer pickId, Integer guid, HttpServletResponse response) {
        log.info("doAlipay pickId={},guid = {}", pickId, guid);
        String selfOrderId = guid + "" + System.currentTimeMillis();
        PayLink payLink = payService.getCid(pickId);
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL,
                alipayConfig.getAppId(),
                alipayConfig.getRsaPrivateKey(),
                AlipayConfig.FORMAT,
                AlipayConfig.CHARSET,
                alipayConfig.getRsaPublicKey(),
                AlipayConfig.SIGNTYPE);

        AlipayTradeWapPayRequest alipayTradeWapPayRequest = aliPayService.getPayRequest(payLink,selfOrderId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        orderService.insertOrder(payLink, attach, selfOrderId);
        String form = "";
        try {
            // 调用SDK生成表单
            form = client.pageExecute(alipayTradeWapPayRequest).getBody();
            response.setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            response.getWriter().write(form);//直接将完整的表单html输出到页面
            response.getWriter().flush();
            response.getWriter().close();
        } catch (AlipayApiException e) {
            log.error("",e);
        }catch (IOException e){
            log.error("",e);
        }
        // 封装请求支付信息
        return null;
    }


    @RequestMapping("/doGo51PayPage")
    void doGo51PayPage(Integer pickId, Integer guid, String payType, HttpServletResponse response) {
        log.info("doGo51PayPage pickId={},guid = {}", pickId, guid);
        String selfOrderId = guid + "" + System.currentTimeMillis();
        PayLink payLink = payService.getCid(pickId);
        ZPayRequestVo zPayRequestVo = zPayService.createRequestVo(payLink, payType, selfOrderId);
        String url = new StringBuilder(ZPAY_BASE_URL).append("/createOrder.e").append("?")
                .append(zPayRequestVo.getParamUrl()).toString();
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        orderService.insertOrder(payLink, attach, selfOrderId);
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            log.error("send to url {} error ", url, e);
        }

    }

    @RequestMapping("/goPayPullPage")
    String doGoPayPullPage(Integer pickId, Integer guid, String token, HttpServletRequest request, Model modelAttribute) {
        log.info("doGoPayPullPage pickId={},guid = {}", pickId, guid);
        PaypullRequestVo paypullRequestVo = new PaypullRequestVo();
        String selfOrderId = guid + "" + System.currentTimeMillis();
        PayLink payLink = payService.getCid(pickId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        paypullRequestVo.setAmount(payLink.getPickRmb().toString());
        paypullRequestVo.setSubject(new StringBuilder("充值").append(payLink.getPickCouponVal()).append("豆").toString());
        paypullRequestVo.setOrderNo(selfOrderId);
        paypullRequestVo.setExtra(JSON.toJSONString(attach));
        paypullRequestVo.setAppId(payPullAppId);
        try {
            paypullRequestVo.setNotifyUrl(URLEncoder.encode(callbackHost + "/paypuall/callback", "utf-8"));
        } catch (Exception e) {

        }
        orderService.insertOrder(payLink, attach, selfOrderId);
        modelAttribute.addAttribute("paypullRequestVo", paypullRequestVo);
        return "paypullPage";
    }

}
