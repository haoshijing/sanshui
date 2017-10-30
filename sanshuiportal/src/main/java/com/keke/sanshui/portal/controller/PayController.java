package com.keke.sanshui.portal.controller;

import com.google.common.collect.Maps;
import com.keke.sanshui.po.PayLink;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.admin.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class PayController {
    @Autowired
    private PayService payService;

    @Value("${callbackHost}")
    private String callbackHost;

    @Autowired
    private OrderService orderService;


    @RequestMapping("/goPay")
    String goPay(HttpServletRequest request ,String guid, Model modelAttribute){
        log.info("guid = {}",guid);
        Integer defaultPick = 0;
        String token = new StringBuilder(guid).append("-").append(System.currentTimeMillis()).toString();
        request.getSession().setAttribute("payToken", token);
        List<PayLink> payLinks = payService.queryAllLink();
        if(payLinks.size() > 0){
            defaultPick = payLinks.get(0).getId();
        }
        modelAttribute.addAttribute("payToken", token);
        modelAttribute.addAttribute("payLinks",payLinks);
        modelAttribute.addAttribute("guid",guid);
        modelAttribute.addAttribute("defaultPick",defaultPick);
        return "recharge";
    }

    @RequestMapping("/pay/user/sucess")
    String success(){
        return  "success";
    }

    @RequestMapping("/goPayPage")
    String doGoPayPage(Integer pickId, Integer guid, String token, HttpServletRequest request,  Model modelAttribute){
        log.info("pickId={},guid = {}",pickId,guid);
        String sessionToken = (String)request.getSession().getAttribute("payToken");
        StringBuilder buildUrl = new StringBuilder();
        PayLink payLink  = payService.getCid(pickId);
        Map<String,String> attach = Maps.newHashMap();
        attach.put("guid",guid.toString());
        String selfOrderId = guid+""+System.currentTimeMillis();
        buildUrl.append(payLink.getCIdNo()).append(".js?type=div");
        buildUrl.append("&attach=").append(selfOrderId);
        try {
            //buildUrl.append("&redirect=http://game.youthgamer.com:8080/sanshui/pay/user/sucess");
            buildUrl.append("&callback=").append(URLEncoder.encode(callbackHost + "/pay/callback", "utf-8"));
        }catch (Exception e){

        }
        orderService.insertOrder(payLink, attach,selfOrderId);
        modelAttribute.addAttribute("url",buildUrl.toString());

        return "payPage";
    }

}
