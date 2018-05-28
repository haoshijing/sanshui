package com.keke.sanshui.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

/**
 * @author haoshijing
 * @version 2018年05月28日 11:03
 **/
@Repository
public class OrderCallbackListener {

    @EventListener
    public void handlerCallback(OrderCallbackEvent orderCallbackEvent){

    }
}
