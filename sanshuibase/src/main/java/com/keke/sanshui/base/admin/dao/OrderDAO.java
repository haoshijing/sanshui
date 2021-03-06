package com.keke.sanshui.base.admin.dao;


import com.keke.sanshui.base.admin.po.Order;
import org.apache.ibatis.annotations.Param;

public interface OrderDAO {

     int insert(@Param("order") Order order);

     int updateByOrderId(@Param("order") Order updateOrder);

     Order getByOrderId(String orderNo);
}
