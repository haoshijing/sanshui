/*
 * @(#) BaseWithMapVo.java 2019-03-21
 *
 * Copyright 2019 NetEase.com, Inc. All rights reserved.
 */

package com.keke.sanshui.pay.easyjh;

import com.google.common.collect.Maps;
import lombok.Data;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author haoshijing
 * @version 2019-03-21
 */
@Data
public class BaseWithMapVo {
    private String sign;

    public Map<String, String> toMap() {
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            String data = (String) ReflectionUtils.getField(field, this);
            if (StringUtils.isEmpty(data)) {
                data = "";
            }
            sortedMap.put(field.getName(), data);

        }
        return sortedMap;
    }
}
