package com.keke.sanshui.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.dao.OrderDAO;
import com.keke.sanshui.enums.SendStatus;
import com.keke.sanshui.po.Order;
import com.keke.sanshui.po.PayLink;
import com.keke.sanshui.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderService {

    @Autowired
    OrderDAO orderDAO;

    public int insert(PayVo payVo) {
        Order order = new Order();
        order.setClientGuid(Integer.valueOf(payVo.getP_attach()));
        order.setMoney(payVo.getP_money());
        order.setTitle(payVo.getP_title());
        order.setPrice(payVo.getP_price());
        order.setPayState(Integer.valueOf(payVo.getP_state()));
        order.setPayTime(payVo.getP_time());
        order.setOrderNo(payVo.getP_no());
        order.setPayType(payVo.getP_type());
        order.setSendStatus(SendStatus.Not_Send.getCode());
        order.setInsertTime(System.currentTimeMillis());
        int insertRet = saveOrder(order);
        return insertRet;
    }

    public int saveOrder(Order order) {
        int insertRet = 0;
        try {
            insertRet = orderDAO.insert(order);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertRet;
    }

    public Order queryOrderByNo(String orderNo) {
        return orderDAO.getByOrderId(orderNo);
    }

    public int updateOrder(Order updateOrder) {
       return orderDAO.updateByOrderId(updateOrder);
    }

    public int insertOrder(PayLink payLink, Map<String,String> attach, String selfOrderId) {
        Order order = new Order();
        order.setClientGuid(Integer.valueOf(attach.get("guid")));
        order.setSelfOrderNo(selfOrderId);
        order.setMoney(payLink.getPickCouponVal().toString());
        order.setTitle(payLink.getPickCouponVal()+"豆");
        order.setPrice(String.valueOf(payLink.getPickRmb()*100));
        order.setOrderStatus(1);
        order.setSendStatus(SendStatus.Not_Send.getCode());
        order.setInsertTime(System.currentTimeMillis());
        order.setLastUpdateTime(System.currentTimeMillis());
        int insertRet = saveOrder(order);
        return insertRet;
    }
}
