package com.keke.pay;

import com.keke.pay.pre.BasePayRequestRes;
import com.keke.sanshui.base.admin.dao.PayLinkDAO;
import com.keke.sanshui.base.admin.po.PayLink;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author haoshijing
 * @version 2018年07月03日 09:13
 **/
public abstract  class AbstractPayService implements PayService {

    private static ConcurrentHashMap<Integer,PayService> concurrentHashMap = new ConcurrentHashMap<>();

    @Setter
    private PaySignCreator paySignCreator;

    @PostConstruct
    public void init(){
        concurrentHashMap.putIfAbsent(getPayType(),this);
    }

    @Autowired
    private PayLinkDAO payLinkDAO;

    public AbstractPayService(){

    }

    public static  BasePayRequestRes handlerPay(PayContext payContext){
        return concurrentHashMap.get(payContext.getPayRequesType()).createPayRequest(payContext);
    }
    @Override
    public BasePayRequestRes createPayRequest(PayContext payContext) {
        PayLink payLink = payLinkDAO.getById(payContext.getPayLinkId());
        BasePayRequestRes pr = doCreatePayRequest(payContext);
        pr.setMoney(String.valueOf(payLink.getPickRmb()));
        pr.setName(String.valueOf(payLink.getPickCouponVal()));

        String sign = paySignCreator.genertorSign(pr);
        pr.setSign(sign);
        return pr;
    }

    protected  abstract int getPayType();

    protected abstract BasePayRequestRes doCreatePayRequest(PayContext payContext);
}
