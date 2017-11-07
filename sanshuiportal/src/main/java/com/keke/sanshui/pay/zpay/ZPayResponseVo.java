package com.keke.sanshui.pay.zpay;

import lombok.Data;

@Data
public class ZPayResponseVo {
    private String code;
    private String app_id;
    private String invoice_no;
    private String money;
    private String out_trade_no;
    private String pay_way;
    private String qn;
    private String up_invoice_no;
    private String sign;
}
