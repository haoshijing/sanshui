package com.keke.sanshui.pay.fuqianla;

import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.pay.zpay.ZPayResponseVo;
import com.keke.sanshui.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;

@Repository
public class FuqianlaPayService {

    @Value("${fuqianAppId}")
    private String appId;
    @Value("${callbackHost}")
    private String callbackHost;

    @Value("${fuqianlaSign}")
    private String fuqianlaSign;

    private static final String WX_PAY = "1";
    private static final String ALI_PAY = "2";

    public FuqianlaRequestVo createRequestVo(PayLink payLink, String payType, String selfOrderId) {
        FuqianlaRequestVo payRequest = new FuqianlaRequestVo();
        payRequest.setAppId(appId);
        payRequest.setAmount(payLink.getPickRmb()/100);
        if(StringUtils.equals(payType,WX_PAY)){
            payRequest.setChannel("wx_pay_pub");
        }else if(StringUtils.equals(payType,ALI_PAY)){
            payRequest.setChannel("ali_pay_wap");
        }
        try {
            payRequest.setSubject(URLEncoder.encode(payLink.getPickCouponVal()+"豆","UTF-8"));
        }catch (Exception ignore){

        }
        payRequest.setOrderId(selfOrderId);
        payRequest.setNotifyUrl(new StringBuilder(callbackHost).append("/fuqianLa/callback").toString());
        return payRequest;
    }

    public boolean checkSign(FuqianResponseVo fuqianResponseVo) {
        String sign = SignUtil.createFullqianResponseSign(fuqianResponseVo,fuqianlaSign);
        return StringUtils.equals(fuqianResponseVo.getSign_info().toUpperCase(),sign);
    }


}
