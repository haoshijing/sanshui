package com.keke.pay;

import com.keke.pay.pre.BasePayRequestRes;

/**
 * @author haoshijing
 * @version 2018年07月03日 09:04
 **/
public interface PayService{

    /**
     *
     * @param payContext
     * @return
     */
    BasePayRequestRes createPayRequest(PayContext payContext);

}
