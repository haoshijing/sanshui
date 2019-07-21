package com.keke.sanshui.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.service.GateWayService;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class OrderPushJob {

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory("OrderPushJobThread"));

    @Autowired
    private OrderService orderService;

    @Autowired
    private GateWayService gateWayService;

    @PostConstruct
    public void init() {
        executorService.scheduleWithFixedDelay(() -> {
            List<Order> orderList = orderService.queryNotSendList();
            orderList.forEach(order -> {
                String orderId = order.getSelfOrderNo();
                String attach = order.getAttach();
                JSONObject jsonObject = JSON.parseObject(attach);
                String card = jsonObject.getString("card");

                String more = jsonObject.getString("more");
                try {
                    Pair<Boolean, Boolean> pair = gateWayService.sendToGameServer(orderId, order.getClientGuid(),
                            String.valueOf(Integer.valueOf(order.getPrice())/100), card,more);
                    if (pair.getLeft()) {
                        Order newUpdateOrder = new Order();
                        newUpdateOrder.setSelfOrderNo(orderId);
                        newUpdateOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                        newUpdateOrder.setSendTime(System.currentTimeMillis());
                        orderService.updateOrder(newUpdateOrder);
                    }


                } catch (Exception e) {
                    log.error("order = {}", order, e);
                }

            });

        }, 1, 30, TimeUnit.SECONDS);
    }
}
