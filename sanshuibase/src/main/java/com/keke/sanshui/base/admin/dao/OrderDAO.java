package com.keke.sanshui.base.admin.dao;


import com.keke.sanshui.base.admin.po.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDAO {

     int insert(@Param("order") Order order);

     int updateByOrderId(@Param("order") Order updateOrder);

     Order getByOrderId(String orderNo);

     List<Order> queryNotSendList();

    Long queryPickupSum(Integer guid, Long startTimeStamp, Long endTimestamp);
}
