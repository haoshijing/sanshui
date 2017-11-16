package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderDAO {

     int insert(@Param("order") Order order);

     int updateByOrderId(@Param("order") Order updateOrder);

     Order getByOrderId(String orderNo);

     List<Order> queryNotSendList();

    Long selectCount(@Param("param")QueryOrderPo orderPo);

    Long queryPickupSum(@Param("guid") Integer guid, @Param("startTimestamp") Long startTimeStamp,@Param("endTimestamp") Long endTimestamp);

    List<Order> selectList(@Param("param")QueryOrderPo queryOrderPo);

    Long queryCount(@Param("param")QueryOrderPo queryOrderPo);
}
