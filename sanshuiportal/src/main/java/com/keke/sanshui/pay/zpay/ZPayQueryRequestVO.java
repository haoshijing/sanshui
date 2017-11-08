package com.keke.sanshui.pay.zpay;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月08日 14:12
 **/
@Data
public class ZPayQueryRequestVO {
    private String partner_id;
    private Integer app_id;
    private String out_trade_no;
    private String sign;
}
