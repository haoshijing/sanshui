package com.keke.sanshui.pay.zpay;

import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.util.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class ZPayService {
    @Value("${appId}")
    private String appId;
    @Value("${partnerId}")
    private String partnerId;
    @Value("${callbackHost}")
    private String callbackHost;
    @Value("${zpayKey}")
    private String signKey;
    @Autowired
    private HttpClient httpClient;
    private final static String ZPAY_BASE_URL = "http://pay.csl2016.cn:8000";

    public ZPayRequestVo createRequestVo(PayLink payLink, String payType,String orderId){
        ZPayRequestVo requestVo = new ZPayRequestVo();
        requestVo.setApp_id(Integer.valueOf(appId));
        requestVo.setPartner_id(partnerId);
        requestVo.setMoney(payLink.getPickRmb());
        requestVo.setImei("");
        requestVo.setQn("zyap4107_57089_100");
        requestVo.setOut_trade_no(orderId);
        requestVo.setWap_type(Integer.valueOf(payType));
        try {
            requestVo.setReturn_url(URLEncoder.encode(callbackHost+"/pay/user/"+orderId,"UTF-8"));
            requestVo.setSubject(URLEncoder.encode(payLink.getPickCouponVal()+"豆","UTF-8"));
        }catch (Exception e){

        }
        Pair<String,String> pair =  SignUtil.createZPayRequestSign(requestVo,signKey);
        requestVo.setSign(pair.getLeft());
        requestVo.setParamUrl(pair.getRight());
        return  requestVo;
    }

    public boolean checkSign(ZPayResponseVo responseVo) {
        String sign = SignUtil.createZPayResponseSign(responseVo,signKey);
        return StringUtils.equals(responseVo.getSign(),sign);
    }

    public String queryOrder(String orderId) {
        ZPayQueryRequestVO zPayQueryRequestVO = createQueryRequestVo(orderId);
        String queryUrl = new StringBuilder(ZPAY_BASE_URL).append("/queryOrder.e").append("?partner_id=")
                .append(zPayQueryRequestVO.getPartner_id())
                .append("&app_id=").append(zPayQueryRequestVO.getApp_id())
                .append("&out_trade_no=").append(zPayQueryRequestVO.getOut_trade_no())
                .append("&sign=").append(zPayQueryRequestVO.getSign()).toString();
        try {
            ContentResponse contentResponse = httpClient.newRequest(queryUrl).timeout(5000, TimeUnit.MILLISECONDS).send();
            return contentResponse.getContentAsString();
        }catch(Exception e){
            log.error("query Order error ",e);
        }
        return "";
    }

    private ZPayQueryRequestVO createQueryRequestVo(String orderId) {
        ZPayQueryRequestVO requestVO = new ZPayQueryRequestVO();
        requestVO.setApp_id(Integer.valueOf(appId));
        requestVO.setPartner_id(partnerId);
        requestVO.setOut_trade_no(orderId);
        String sign = SignUtil.createZPayQuerySign(requestVO,signKey);
        requestVO.setSign(sign);
        return requestVO;
    }
}
