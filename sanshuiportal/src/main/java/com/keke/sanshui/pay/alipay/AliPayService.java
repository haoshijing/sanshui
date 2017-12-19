package com.keke.sanshui.pay.alipay;

import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.keke.sanshui.base.admin.po.PayLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest; /**
 * 支付宝h5支付接口封装
 * @author haoshijing
 * @version 2017年12月19日 12:44
 **/
@Repository
public class AliPayService {

    @Autowired
    AlipayConfig alipayConfig;

    public AlipayTradeWapPayRequest getPayRequest(PayLink payLink, String selfOrderId) {
        AlipayTradeWapPayRequest alipayTradeWapPayRequest = new AlipayTradeWapPayRequest();
        AlipayTradeWapPayModel model=new AlipayTradeWapPayModel();
        model.setOutTradeNo(selfOrderId);
        model.setSubject(new StringBuilder(payLink.getPickCouponVal()).append("豆").toString());
        model.setTotalAmount(String.valueOf(payLink.getPickRmb()/100));
        model.setBody("充值"+model.getSubject());
        model.setTimeoutExpress("5m");
        model.setProductCode("QUICK_WAP_WAY");
        alipayTradeWapPayRequest.setBizModel(model);
        String notifyUrl = alipayConfig.getNotifyUrl();
        String returnUrl = alipayConfig.getReturnUrl(selfOrderId);
        alipayTradeWapPayRequest.setReturnUrl(returnUrl);
        alipayTradeWapPayRequest.setNotifyUrl(notifyUrl);
        return alipayTradeWapPayRequest;
    }
}
