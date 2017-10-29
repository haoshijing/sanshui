package com.keke.sanshui.event;

import com.keke.sanshui.service.OrderService;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class OrderHandlerListener implements ApplicationListener<OrderEvent> {

    @Autowired
    private OrderService orderService;

    @Override
    @Async
    public void onApplicationEvent(OrderEvent event) {
        log.info("orderId = {},type = {}",event.getOrderId(),event.getType());
    }
}
