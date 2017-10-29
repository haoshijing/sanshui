package com.keke.sanshui.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.keke.sanshui.util.SignUtil;
import com.keke.sanshui.vo.PayVo;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
public class GateTestController {

    @Autowired
    private HttpClient httpClient;

    @Value("${pkey}")
    private String pkey;

    @RequestMapping("/test/callback")
    public void testSendCallback(String orderId,String guid){
        Request request = httpClient.POST("http://localhost:8080/sanshui/pay/callback");
        request.timeout(3000, TimeUnit.MILLISECONDS);

        PayVo payVo = new PayVo();
        payVo.setP_type("tpay");
        payVo.setP_no("2017454545454");
        payVo.setP_money("333");
        payVo.setP_time("2017-10-28 15:19");
        payVo.setP_state("2");
        try {
            payVo.setP_attach(orderId);
            payVo.setSign(SignUtil.createPaySign(payVo,pkey));

        }catch (Exception e){

        }
        request.content(new BytesContentProvider(JSON.toJSONBytes(payVo)),"application/x-www-form-urlencoded");
        try {
            ContentResponse contentResponse = request.send();
            System.out.println(contentResponse.getContentAsString());
        }catch (Exception e){

        }

    }
}
