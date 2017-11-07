package com.keke.sanshui.pay.zpay;

import lombok.Data;

@Data
public class ZPayRequestVo {
    private String partner_id;
    private Integer app_id;
    private Integer wap_type;
    private Integer money;
    private String out_trade_no;
    private String subject;
    private String imei;
    private String qn;
    private String return_url;
    private String sign;

    private String paramUrl = "";
}
