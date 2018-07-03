package com.keke.pay.notify;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2018年07月03日 13:30
 **/
@Data
public class NotifyRespBase {
    /**
     * 响应签名
     */
    private String sign;
    /**
     * 订单总金额
     */
    private String totalMoney;
    /*
    响应状态码
     */
    private String code;
    /**
     * 订单id
     */
    private String orderId;
}
