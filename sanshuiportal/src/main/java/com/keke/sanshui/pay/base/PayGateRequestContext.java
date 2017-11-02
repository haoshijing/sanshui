package com.keke.sanshui.pay.base;

import com.keke.sanshui.base.admin.po.PayLink;
import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月02日 13:29
 * 请求支付上下文
 **/
@Data
public class PayGateRequestContext {

    private PayLink payLink;
    private Integer guid;
}
