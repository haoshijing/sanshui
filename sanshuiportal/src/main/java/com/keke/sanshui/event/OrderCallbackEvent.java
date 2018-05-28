package com.keke.sanshui.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author haoshijing
 * @version 2018年05月28日 11:00
 **/
public class OrderCallbackEvent extends ApplicationEvent {
    private String orderId;
    private String payType;
    private String sign;
    private String serverOrderId;
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public OrderCallbackEvent(Object source) {
        super(source);
    }
}
