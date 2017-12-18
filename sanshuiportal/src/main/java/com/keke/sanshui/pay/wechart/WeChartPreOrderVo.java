package com.keke.sanshui.pay.wechart;

import lombok.Data;

import java.util.UUID;

/**
 * 微信支付下单请求vo
 * @author haoshijing
 * @version 2017年11月01日 18:06
 **/
@Data
public class WeChartPreOrderVo {
    private String appId;
    private String mch_id;
    private String device_info = "WEB";
    private String nonce_str = UUID.randomUUID().toString().replace("-","");
    private String sign;
    private String sign_type;
    private String body;
    private String detail;
    private String attach;
    private String out_trade_no;
    private String fee_type;
    private Integer total_fee;
    private String spbill_create_ip;
    private String time_start;
    private String time_expire;
    private String goods_tag;
    private String notify_url;
    private String return_url;
    private String trade_type;
    private String product_id;
    private String limit_pay;
    private String openid;
    private String scene_info;

}
