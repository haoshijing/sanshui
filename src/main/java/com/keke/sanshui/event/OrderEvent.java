package com.keke.sanshui.event;

import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

@Data
public class OrderEvent extends ApplicationContextEvent {
    /**
     * 订单号
     */
    private String orderId;
    /**
     * 处理类型
     */
    private Integer type;
    /**
     * Create a new ContextStartedEvent.
     *
     * @param source the {@code ApplicationContext} that the event is raised for
     *               (must not be {@code null})
     */
    public OrderEvent(ApplicationContext source,String orderId,Integer type) {
        super(source);
        this.orderId = orderId;
        this.type = type;
    }
}
