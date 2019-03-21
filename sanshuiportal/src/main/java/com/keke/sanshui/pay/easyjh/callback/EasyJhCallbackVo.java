/*
 * @(#) EasyJhCallbackVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh.callback;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class EasyJhCallbackVo {
    private String merchant_id;
    private String out_trade_no;
    private String trade_no;
    private String subject;
    private String body;
    private String attach;
    private String total_fee;
    private String trade_time;
    private String trade_status;
    private String trade_result;
    private String sign;
}
