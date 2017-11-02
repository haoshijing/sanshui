package com.keke.sanshui.pay.paypull;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月02日 12:27
 **/
@Data
public class PaypullRequestVo {

    private String appId;
    private String orderNo;
    private String amount;
    private String subject;
    private String notifyUrl;
    private String extra;
}
