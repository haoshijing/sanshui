package com.keke.sanshui.admin.service;

import com.keke.sanshui.admin.response.index.PickDataResponse;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class IndexService {

    @Autowired
    private OrderService orderService;

    public PickDataResponse getCurrentDayPick() {
        PickDataResponse pickDataResponse = new PickDataResponse();

        QueryOrderPo queryOrderPo = new QueryOrderPo();
        queryOrderPo.setOrderStatus(2);
        queryOrderPo.setStartTimestamp(TimeUtil.getDayStartTimestamp(0));
        queryOrderPo.setEndTimestamp(TimeUtil.getDayEndTimestamp(0));
        queryOrderPo.setLimit(1000);
        queryOrderPo.setOffset(0);
        List<Order> orderList = orderService.selectList(queryOrderPo);
        int sum = orderList.stream().mapToInt(order->{
            return Integer.valueOf(order.getPrice());
        }).sum();
        pickDataResponse.setDaySuccessTotal(Long.valueOf(sum/100));
        QueryOrderPo newQueryPo = new QueryOrderPo();
        newQueryPo.setStartTimestamp(TimeUtil.getDayStartTimestamp(0));
        newQueryPo.setEndTimestamp(TimeUtil.getDayEndTimestamp(0));
        newQueryPo.setLimit(1000);
        newQueryPo.setOffset(0);
        orderList = orderService.selectList(newQueryPo);
        int sumTotal = orderList.stream().mapToInt(order->{
            return Integer.valueOf(order.getPrice());
        }).sum();
        pickDataResponse.setDayPickTotal(Long.valueOf(sumTotal/100));
        return pickDataResponse;
    }

}
