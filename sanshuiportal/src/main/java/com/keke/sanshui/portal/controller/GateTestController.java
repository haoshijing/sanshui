package com.keke.sanshui.portal.controller;

import com.alibaba.fastjson.JSON;
import com.keke.sanshui.util.SignUtil;
import com.keke.sanshui.base.vo.PayVo;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.client.api.Result;
import org.eclipse.jetty.client.util.BytesContentProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
public class GateTestController implements ApplicationContextAware{

    @Autowired
    private HttpClient httpClient;

    private ApplicationContext context;

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
        httpClient.newRequest("http://www.baidu.com").send(new Response.CompleteListener() {
            @Override
            public void onComplete(Result result) {

            }
        });
        request.content(new BytesContentProvider(JSON.toJSONBytes(payVo)),"application/x-www-form-urlencoded");
        try {
            ContentResponse contentResponse = request.send();
            System.out.println(contentResponse.getContentAsString());
        }catch (Exception e){

        }
    }


    @RequestMapping("/testEvent")
    @ResponseBody
    public String testRequest(String orderId,Integer type){
        return "success";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
