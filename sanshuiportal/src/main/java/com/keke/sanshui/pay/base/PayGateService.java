package com.keke.sanshui.pay.base;

/**
 * 支付网关接口抽象
 * @author haoshijing
 * @version 2017年11月02日 13:24
 **/
public interface PayGateService {

    /**
     * 生成请求支付请求对象
     * @param payGateRequestContext
     * @return
     */
    PayGateRequestBase createPayRequest(PayGateRequestContext payGateRequestContext);

    /**
     * 响应网关回调
     * @param responseBody
     * @param payType
     */
    void handlerPayCallback(byte[] responseBody,int payType);
}
