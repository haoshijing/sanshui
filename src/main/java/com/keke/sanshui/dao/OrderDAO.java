package com.keke.sanshui.dao;

import com.keke.sanshui.po.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDAO {

     int insert(@Param("order") Order order);

     int updateByOrderId( @Param("order") Order updateOrder);

     Order getByOrderId(String orderNo);
}
