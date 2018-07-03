package com.keke.sanshui.pay.colotnet;

import lombok.Data;

@Data
public class ColotnetNotifyResponseVo {
    private String productId;
    private String merNo;
    private String orderNo;
    private String payNo;
    private String transAmt;
    private String orderDate;
    private String remark;
    private String extendField;
    private String respCode;
    private String respDesc;
    private String signature;
}
