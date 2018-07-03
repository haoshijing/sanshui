package com.keke.pay.pre;

import lombok.Data;

/**
 * 支付请求
 * @author haoshijing
 * @version 2018年07月03日 09:07
 **/
@Data
public class BasePayRequestRes {
    private String sign;
    private String name;
    private String money;
    private String clientIP;
}
