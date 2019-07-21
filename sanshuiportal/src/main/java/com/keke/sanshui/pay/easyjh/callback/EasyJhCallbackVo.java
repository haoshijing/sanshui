/*
 * @(#) EasyJhCallbackVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh.callback;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhResponseVo;
import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class EasyJhCallbackVo extends BaseWithMapVo {
    private String merchant_id;
    private String out_trade_no;
    private String trade_no;
    private String subject;
    private String body;
    private String attach;
    private String total_fee;
    private String trade_time;
    private String trade_status;
    private String trade_result;


    public static EasyJhCallbackVo buildFromMap(Map map) {
        EasyJhCallbackVo callbackVo = new EasyJhCallbackVo();
        for (Field field : EasyJhResponseVo.class.getDeclaredFields()) {
            if (map.containsKey(field.getName())) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, callbackVo, map.get(field.getName()));
            }
        }
        return callbackVo;
    }
}
