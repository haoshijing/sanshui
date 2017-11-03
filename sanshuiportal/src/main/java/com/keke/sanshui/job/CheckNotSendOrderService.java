package com.keke.sanshui.job;

import com.keke.sanshui.base.admin.po.Order;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.enums.SendStatus;
import com.keke.sanshui.service.GateWayService;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
public class CheckNotSendOrderService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GateWayService gateWayService;

    @PostConstruct
    public void init(){
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(
                new DefaultThreadFactory("ScanNotSendOrderThread"));
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                orderService.queryNotSendList().forEach(order -> {
                    boolean sendOk = gateWayService.sendToGameServer(order.getSelfOrderNo(), order.getClientGuid(),
                            order.getMoney(), "0");
                    if (sendOk) {
                        Order updateSendOrder = new Order();
                        updateSendOrder.setSendStatus(SendStatus.Alread_Send.getCode());
                        updateSendOrder.setSendTime(System.currentTimeMillis());
                        orderService.updateOrder(updateSendOrder);
                    }
                });

            }
        },1,60, TimeUnit.SECONDS);
    }
}
