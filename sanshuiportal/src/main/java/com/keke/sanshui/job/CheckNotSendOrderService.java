package com.keke.sanshui.job;

import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.service.GateWayService;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class CheckNotSendOrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GateWayService gateWayService;

    @EventListener
    public void startWork(ContextStartedEvent event){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(
                new DefaultThreadFactory("ScanNotSendOrderThread"));
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                orderService.queryNotSendList().forEach(order -> {
                    log.info("开始补偿order,orderId = {}",order.getSelfOrderNo());
                    Pair<Boolean,Boolean> pair = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                            order.getMoney(), "0");
                    if(pair.getLeft()){
                        Order updateOrder = new Order();
                        updateOrder.setSelfOrderNo(order.getSelfOrderNo());
                        if(pair.getRight()) {
                            updateOrder.setOrderStatus(2);
                        }
                        updateOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                        updateOrder.setSendTime(System.currentTimeMillis());
                        orderService.updateOrder(updateOrder);
                    }
                });

            }
        },10,60, TimeUnit.SECONDS);
    }
}
