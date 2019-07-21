/*
 * @(#) EasyJhRequestVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh.order;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;

import java.util.UUID;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class EasyJhRequestVo extends BaseWithMapVo {
    private String version = "1.0";
    private String charset = "UTF-8";
    private String merchant_id;
    private String out_trade_no;
    private String trade_type;
    private String user_ip;
    private String subject;
    private String body;
    private String user_id;
    private String total_fee;
    private String notify_url;
    private String return_url;
    private String nonce_str = UUID.randomUUID().toString().replace("-", "");
    private String biz_content;
    private String attach;
    private String sign;
}
