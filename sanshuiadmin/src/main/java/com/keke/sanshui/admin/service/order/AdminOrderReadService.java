package com.keke.sanshui.admin.service.order;

import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.order.OrderQueryVo;
import com.keke.sanshui.admin.response.order.OrderItemVo;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import com.keke.sanshui.base.admin.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminOrderReadService {

    @Autowired
    private OrderService orderService;
    public List<OrderItemVo> queryList(OrderQueryVo orderQueryVo) {

        QueryOrderPo queryOrderPo = parseFromQueryVo(orderQueryVo);
        List<Order> orders = orderService.selectList(queryOrderPo);

        List<OrderItemVo> orderItemVos = orders.stream().map(order -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            orderItemVo.setAgentId(0);
            orderItemVo.setClientGuid(order.getClientGuid());
            orderItemVo.setId(order.getId());
            orderItemVo.setInsertTime(order.getInsertTime());
            orderItemVo.setLastUpdateTime(order.getLastUpdateTime());
            orderItemVo.setSelfOrderNo(order.getSelfOrderNo());
            orderItemVo.setOrderStatus(order.getOrderStatus());
            orderItemVo.setMoney(order.getMoney());
            orderItemVo.setSendStatus(order.getSendStatus());
            orderItemVo.setTitle(order.getTitle());
            orderItemVo.setPayType(order.getPayType());
            orderItemVo.setPrice(order.getPrice());
            return orderItemVo;
        }).collect(Collectors.toList());
        return orderItemVos;
    }

    public Long queryCount(OrderQueryVo orderQueryVo){
        return orderService.selectCount(parseFromQueryVo(orderQueryVo));
    }

    private QueryOrderPo parseFromQueryVo(OrderQueryVo orderQueryVo){
        QueryOrderPo queryOrderPo = new QueryOrderPo();
        queryOrderPo.setOrderStatus(orderQueryVo.getOrderStatus());
        queryOrderPo.setSelfOrderNo(orderQueryVo.getOrderId());
        queryOrderPo.setStartTimestamp(orderQueryVo.getStartTimestamp());
        queryOrderPo.setEndTimestamp(orderQueryVo.getEndTimestamp());
        queryOrderPo.setLimit(orderQueryVo.getLimit());
        Integer page = 0;
        if (orderQueryVo.getPage() > 0) {
            page = orderQueryVo.getPage() - 1;
        }
        if (orderQueryVo.getGuid() != null) {
            try {
                queryOrderPo.setClientGuids(Lists.newArrayList(Integer.valueOf(orderQueryVo.getGuid())));
            } catch (Exception e) {

            }
        }
        queryOrderPo.setOffset(page * queryOrderPo.getLimit());
        return queryOrderPo;
    }
}
