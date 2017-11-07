package com.keke.sanshui.pay.zpay;

import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;

@Repository
public class ZPayService {
    @Value("appId")
    private String appId;
    @Value("partnerId")
    private String partnerId;
    @Value("callbackHost")
    private String callbackHost;
    @Value("zpayKey")
    private String signKey;

    public ZPayRequestVo createRequestVo(PayLink payLink, String orderId){
        ZPayRequestVo requestVo = new ZPayRequestVo();
        requestVo.setApp_id(Integer.valueOf(appId));
        requestVo.setPartner_id(partnerId);
        requestVo.setMoney(payLink.getPickCouponVal());
        requestVo.setImei("");
        requestVo.setQn("zyap4107_57089_100");
        requestVo.setOut_trade_no(orderId);
        requestVo.setWap_type(1);
        try {
            requestVo.setReturn_url(URLEncoder.encode(callbackHost+"/pay/user/sucess","UTF-8"));
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
}
