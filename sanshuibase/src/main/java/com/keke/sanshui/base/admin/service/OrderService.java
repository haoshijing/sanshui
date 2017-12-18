package com.keke.sanshui.base.admin.service;


import com.keke.sanshui.base.admin.dao.OrderDAO;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import com.keke.sanshui.base.enums.SendStatus;

import com.keke.sanshui.base.vo.PayVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
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
        order.setPrice(String.valueOf(payLink.getPickRmb()));
        order.setOrderStatus(1);
        order.setSendStatus(SendStatus.Not_Send.getCode());
        order.setInsertTime(System.currentTimeMillis());
        order.setLastUpdateTime(System.currentTimeMillis());
        int insertRet = saveOrder(order);
        return insertRet;
    }

    public List<Order> queryNotSendList(){
      List<Order> orderList =   orderDAO.queryNotSendList();
        return  orderList;
    }

    public Long queryPickupSum(Integer guid,Long startTimeStamp,Long endTimestamp){
        return orderDAO.queryPickupSum(guid,startTimeStamp,endTimestamp);
    }

    public List<Order> selectList(QueryOrderPo queryOrderPo) {
        return orderDAO.selectList(queryOrderPo);
    }
    public Long selectCount(QueryOrderPo queryOrderPo){
        return orderDAO.queryCount(queryOrderPo);
    }
}
