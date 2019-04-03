package com.keke.sanshui.portal.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.admin.service.PayService;
import com.keke.sanshui.pay.alipay.AlipayConfig;
import com.keke.sanshui.pay.easyjh.EasyJhPayService;
import com.keke.sanshui.pay.easyjh.order.EasyJhRequestVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhResponseVo;
import com.keke.sanshui.pay.easyjh.query.OrderQueryRequestVo;
import com.keke.sanshui.pay.easyjh.query.OrderQueryResponseVo;
import com.keke.sanshui.pay.huayue.HuayuePayService;
import com.keke.sanshui.pay.huayue.order.EastYOrderRequestVo;
import com.keke.sanshui.pay.huayue.order.EastYOrderResponseVo;
import com.keke.sanshui.pay.huayue.query.EastYQueryVo;
import com.keke.sanshui.pay.wechart.MyWxConfig;
import com.keke.sanshui.util.IpUtils;
import com.keke.sanshui.util.SignUtil;
import com.keke.sanshui.util.easyjh.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.FormContentProvider;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.eclipse.jetty.util.Fields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
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
    private EasyJhPayService easyJhPayService;

    @Autowired
    private HuayuePayService huayuePayService;

    @Value("${callbackHost}")
    private String callbackHost;

    @Value("${easyJhAppId}")
    private String appId;

    @Value("${easyJhAppEncryptKey}")
    private String signKey;
    @Autowired
    AlipayConfig alipayConfig;

    @Autowired
    private HttpClient httpClient;

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

    @GetMapping("/goEastYPay")
    public String goEastYPay(Integer pickId, String payType, Integer guid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        PayLink payLink = payService.getCid(pickId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        attach.put("more", payLink.getMoreCouponVal().toString());
        attach.put("card", payLink.getPickCouponVal().toString());
        String selfOrderId = guid + "" + System.currentTimeMillis();
        try {
            EastYOrderRequestVo requestVo = huayuePayService.createRequestVo(payLink, payType, selfOrderId, String.valueOf(guid), IpUtils.getIpAddr(request));
            Fields fields = new Fields();

            for(Map.Entry<String,String> entry :requestVo.toMap().entrySet()){
                fields.add(entry.getKey(),entry.getValue());
            }

            String data = httpClient.POST("http://pay.sytpay.cn/index.php/Api/Index/createOrder")
                    .content(new FormContentProvider(fields)).send().getContentAsString();
            log.info("data = {}",data);
            EastYOrderResponseVo responseVo = JSONObject.parseObject(data,EastYOrderResponseVo.class);
            if(huayuePayService.checkSign(responseVo) && responseVo.getCode() == 0){
                orderService.insertOrder(payLink, attach, JSON.toJSONString(attach), selfOrderId);
                String url = responseVo.getUrl();
                httpServletResponse.sendRedirect(url);
            }
        } catch (Exception e) {
            log.error("submit error pId = {}, guid = {},payType = {}", pickId, guid, payType, e);
        }
        return "payPage";
    }

    @GetMapping("/huayue/{orderId}")
    public String eastYQuery(@PathVariable String orderId, ModelMap modelMap) {
        EastYQueryVo queryRequestVo = huayuePayService.createQueryOrderRequest(orderId);
        String showMessage = "支付结果未知";
        try {
            String data = httpClient.GET(String.format("api.hypay.xyz/index.php/Api/Check/index?partner=%s" + "&orderId=%s&sign=%s",
                    queryRequestVo.getPartner(),
                    queryRequestVo.getOrderId(),
                    queryRequestVo.getSign())).getContentAsString();

            JSONObject jsonObject = JSON.parseObject(data);
            if(jsonObject != null){
                if(jsonObject.containsKey("status")){
                    showMessage = jsonObject.getString("status");
                }
            }
        }catch (Exception e){

        }
        modelMap.addAttribute("message", showMessage);
        return "succes";
    }


    @PostMapping("/goEasyJhPay")
    public String doEasyJhPay(Integer pickId, String payType, Integer guid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        PayLink payLink = payService.getCid(pickId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        String selfOrderId = guid + "" + System.currentTimeMillis();
        try {
            EasyJhRequestVo requestVo = easyJhPayService.createRequestVo(payLink, payType, selfOrderId, String.valueOf(guid), IpUtils.getIpAddr(request));
            String xml_data = XmlUtils.toXml(requestVo.toMap());

            String data = httpClient.POST("http://open.eyouc.net/gateway/soa")
                    .header("Content-Type", "text/xml;charset=UTF-8")
                    .content(new StringContentProvider(xml_data)).send().getContentAsString();
            log.info("data = {}",data);
            Map<String, String> map = XmlUtils.parse(data);
            EasyJhResponseVo responseVo = EasyJhResponseVo.buildFromMap(map);
            boolean checkSignOk = easyJhPayService.checkSign(responseVo);
            if (checkSignOk && StringUtils.equalsIgnoreCase("0", responseVo.getStatus())) {
                orderService.insertOrder(payLink, attach,requestVo.getAttach(), selfOrderId);
                httpServletResponse.sendRedirect(responseVo.getPay_info());
                return null;
            }
        } catch (Exception e) {
            log.error("submit error pId = {}, guid = {},payType = {}", pickId, guid, payType, e);
        }
        return "payPage";
    }

    @GetMapping("/easyjhpay/{orderId}")
    public String easyjhpayQuery(@PathVariable String orderId, ModelMap modelMap) {
        OrderQueryRequestVo requestVo = new OrderQueryRequestVo();
        requestVo.setOut_trade_no(orderId);
        requestVo.setMerchant_id(appId);
        requestVo.setSign(SignUtil.createSign(requestVo.toMap(), signKey));
        String xml_data = XmlUtils.toXml(requestVo.toMap());
        String showMessage = "支付结果未知";
        try {
            String data = httpClient.POST("http://open.eyouc.net /gateway/paystatus")
                    .header("Content-Type", "text/xml;charset=UTF-8")
                    .content(new StringContentProvider(xml_data)).send().getContentAsString();
            Map<String, String> map = XmlUtils.parse(data);
            OrderQueryResponseVo responseVo = OrderQueryResponseVo.buildFromMap(map);
            if (responseVo != null) {
                if (StringUtils.equalsIgnoreCase("0", responseVo.getStatus())) {
                    if (StringUtils.equalsIgnoreCase(responseVo.getResult_code(), "1")) {
                        showMessage = "支付成功";
                    } else {
                        showMessage = responseVo.getMessage();
                    }
                }
            }
        } catch (Exception e) {

        }
        modelMap.addAttribute("message", showMessage);
        return "success";
    }
}
