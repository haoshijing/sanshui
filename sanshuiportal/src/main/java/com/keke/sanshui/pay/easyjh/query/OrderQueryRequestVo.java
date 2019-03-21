/*
 * @(#) OrderQueryVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh.query;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class OrderQueryRequestVo extends BaseWithMapVo {
    private String merchant_id;
    private String out_trade_no;
    private String version = "1.0";
    private String charset = "UTF-8";
    private String attach;
    private String sign;

    public static void main(String[] args) {
        OrderQueryRequestVo orderQueryRequestVo = new OrderQueryRequestVo();
        orderQueryRequestVo.setAttach("222");
        System.out.println("args = [" + orderQueryRequestVo.toMap() + "]");
    }


}
