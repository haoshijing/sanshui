package com.keke.sanshui.pay.paypull;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月02日 12:45
 **/
@Data
public class PayPullCallbackVo {
    private Integer amount;
    private Long receive_time;
    private Long complete_time;
    private String merch_id;
    private String charge_id;
    private String order_no;
    private String ret_code;
    private String ret_info;
    private String optional;
    private String version;
    private String sign_type;
    private String sign_info;
}
