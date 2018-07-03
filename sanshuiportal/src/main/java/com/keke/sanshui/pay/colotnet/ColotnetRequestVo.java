package com.keke.sanshui.pay.colotnet;

import lombok.Data;

import java.util.UUID;

@Data
public class ColotnetRequestVo {
    private String version = "v1.0";
    private String requestNo = UUID.randomUUID().toString().replace("-","");
    private String transId;
    private String merNo;
    private String productId;
    private String orderDate;
    private String orderNo;
    private String notifyUrl;
    private String transAmt;
    private String phoneNo;
    private String customerName;
    private String commodityName;
    private String returnUrl;
    private String clientIP;
    private String remark;
    private String extendField;
    private String signature;
}
