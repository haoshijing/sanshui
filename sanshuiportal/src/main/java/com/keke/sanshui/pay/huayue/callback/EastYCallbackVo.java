package com.keke.sanshui.pay.huayue.callback;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;

import java.util.List;

@Data
public class EastYCallbackVo extends BaseWithMapVo {
    private String paramsJson;
    private String code;
    private String message;
    private List data;
    private String orderId;
    private String orderAmount;
    private String dateTime;
    private String trade_no;
    private String outTradeNo;


}
