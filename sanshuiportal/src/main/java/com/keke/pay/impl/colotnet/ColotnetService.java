package com.keke.pay.impl.colotnet;

import com.keke.enums.PayTypeEnums;
import com.keke.pay.AbstractPayService;
import com.keke.pay.PayContext;
import com.keke.pay.PaySignCreator;
import com.keke.pay.impl.colotnet.pojo.ColotnetRequestVo;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

/**
 * @author haoshijing
 * @version 2018年07月03日 09:12
 **/
public class ColotnetService extends AbstractPayService {

    @Value("${colotentAppId}")
    private String merNo;


    private static final String WX_PAY = "1";
    private static final String ALI_PAY = "2";

    public ColotnetService() {
    }

    @EventListener
    public void handlerInit(ContextRefreshedEvent contextRefreshedEvent) {
        ColtnetSignImpl coltnetSignImpl = contextRefreshedEvent.getApplicationContext().getBean(ColtnetSignImpl.class);
        setPaySignCreator(coltnetSignImpl);
    }

    @Override
    protected int getPayType() {
        return PayTypeEnums.COLOTET.getType();
    }

    @Override
    protected ColotnetRequestVo doCreatePayRequest(PayContext payContext) {
        ColotnetRequestVo requestVo = new ColotnetRequestVo();
        requestVo.setTransId("75");
        requestVo.setMerNo(merNo);
        String payType = payContext.getPayType();
        if (StringUtils.equals(payType, WX_PAY)) {
            requestVo.setProductId("1205");
        } else if (StringUtils.equals(payType, ALI_PAY)) {
            requestVo.setProductId("1210");
        }
        String orderId = new StringBuilder(payContext.getGuid()).append("").append(System.currentTimeMillis()).toString();
        requestVo.setOrderNo(new DateTime(System.currentTimeMillis()).toString("yyyyMMdd"));
        requestVo.setOrderNo(orderId);
        requestVo.setTransAmt(requestVo.getMoney());
        requestVo.setNotifyUrl("");
        requestVo.setReturnUrl("");
        requestVo.setCommodityName(requestVo.getName());
        return requestVo;
    }
}
