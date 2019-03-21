/*
 * @(#) EasyJhResponseVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh.order;

import lombok.Data;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class EasyJhResponseVo {
    private String version;
    private String charset;
    private String sign_type;
    private String status;
    private String pay_info;
    private String out_trade_no;
    private String trade_no;
    private String result_code;
    private String message;
    private String sign;

    public static EasyJhResponseVo buildFromMap(Map map) {
        EasyJhResponseVo easyJhResponseVo = new EasyJhResponseVo();
        for (Field field : EasyJhResponseVo.class.getDeclaredFields()) {
            if (map.containsKey(field.getName())) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, easyJhResponseVo, map.get(field.getName()));
            }
        }
        return easyJhResponseVo;
    }
}
