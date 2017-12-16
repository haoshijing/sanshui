package com.keke.sanshui.pay.fuqianla;

import lombok.Data;

@Data
public class FuqianlaRequestVo {
    private String appId;
    private String orderId;
    private Integer amount;
    private String channel;
    private String subject;
    private String notifyUrl;
    private String guid;
}
