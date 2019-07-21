/*
 * @(#) OrderQueryResponseVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh.query;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class OrderQueryResponseVo extends BaseWithMapVo {
    private String version = "1.0";
    private String charset = "UTF-8";
    private String sign_type;
    private String status;
    private String total_fee;
    private String subject;
    private String body;
    private String out_trade_no;
    private String trade_no;
    private String result_code;
    private String message;
    private String attach;
    private String sign;

    public static OrderQueryResponseVo buildFromMap(Map map) {
        OrderQueryResponseVo queryResponseVo = new OrderQueryResponseVo();
        for (Field field : OrderQueryResponseVo.class.getDeclaredFields()) {
            if (map.containsKey(field.getName())) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, queryResponseVo, map.get(field.getName()));
            }
        }
        return queryResponseVo;
    }
}
