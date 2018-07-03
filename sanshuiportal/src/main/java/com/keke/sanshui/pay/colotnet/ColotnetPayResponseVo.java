package com.keke.sanshui.pay.colotnet;

import lombok.Data;

import java.util.UUID;

@Data
public class ColotnetPayResponseVo {
    private String version = "v1.0";
    private String requestNo = UUID.randomUUID().toString().replace("-","");
    private String transId;
    private String merNo;
    private String productId;
    private String orderDate;
    private String orderNo;
    private String payNo;
    private String transAmt;
    private String notifyUrl;
    private String returnUrl;
    private String commodityName;
    private String mwebUrl;
    private String remark;
    private String extendField;
    private String respCode;
    private String respDesc;
    private String signature;
}
