package com.keke.sanshui.portal.controller;

import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.admin.service.PayService;
import com.keke.sanshui.pay.alipay.AlipayConfig;
import com.keke.sanshui.pay.easyjh.EasyJhPayService;
import com.keke.sanshui.pay.easyjh.order.EasyJhRequestVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhResponseVo;
import com.keke.sanshui.pay.wechart.MyWxConfig;
import com.keke.sanshui.pay.zpay.ZPayService;
import com.keke.sanshui.util.IpUtils;
import com.keke.sanshui.util.easyjh.XmlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.util.StringContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    private ZPayService zPayService;

    @Autowired
    private EasyJhPayService easyJhPayService;

    @Value("${callbackHost}")
    private String callbackHost;

    @Autowired
    AlipayConfig alipayConfig;

    @Autowired
    MyWxConfig wxPayConfig;

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


    @GetMapping("/goEasyJhPay")
    public String doEasyJhPay(Integer pickId, String payType, Integer guid, HttpServletRequest request, HttpServletResponse httpServletResponse) {
        PayLink payLink = payService.getCid(pickId);
        Map<String, String> attach = Maps.newHashMap();
        attach.put("guid", guid.toString());
        String selfOrderId = guid + "" + System.currentTimeMillis();
        try {
            EasyJhRequestVo requestVo = easyJhPayService.createRequestVo(payLink, payType, selfOrderId, String.valueOf(guid), IpUtils.getIpAddr(request));
            String xml_data = XmlUtils.toXml(requestVo.toMap());

            String data = httpClient.POST("http://open.eyouc.net/gateway/soa").header("Content-Type", "text/xml;charset=UTF-8")
                    .content(new StringContentProvider(xml_data)).send().getContentAsString();
            Map<String, String> map = XmlUtils.parse(data);

            EasyJhResponseVo responseVo = EasyJhResponseVo.buildFromMap(map);
            boolean checkSignOk = easyJhPayService.checkSign(responseVo);
            if (checkSignOk && StringUtils.equalsIgnoreCase("0", responseVo.getStatus())) {
                orderService.insertOrder(payLink, attach, selfOrderId);
                httpServletResponse.sendRedirect(responseVo.getPay_info());
                return null;
            }
        } catch (Exception e) {
            log.error("submit error pId = {}, guid = {},payType = {}", pickId, guid, payType);
        }
        return "payPage";
    }

}
