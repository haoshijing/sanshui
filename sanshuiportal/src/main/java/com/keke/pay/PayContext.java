package com.keke.pay;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2018年07月03日 09:06
 **/
@Data
public class PayContext {
    /**
     * 支付链接id
     */
    private Integer  payLinkId;
    /**
     * 客户guid
     */
    private Integer guid;
    /**
     * 支付方式
     */
    private String payType;
}
