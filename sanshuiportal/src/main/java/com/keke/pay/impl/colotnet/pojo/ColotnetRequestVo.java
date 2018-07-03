package com.keke.pay.impl.colotnet.pojo;

import com.google.common.collect.Maps;
import com.keke.pay.pre.BasePayRequestRes;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;

@Data
public class ColotnetRequestVo extends BasePayRequestRes {
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
    private String remark;
    private String extendField;
    private String signature;

    public Map<String,String> toMap(){
        Map<String,String> sortedMap = Maps.newTreeMap();
        Field[] fields =  ColotnetRequestVo.class.getDeclaredFields();
        for(Field field:fields){
            if (!field.getName().equals("signature")) {
                field.setAccessible(true);
                String data = String.valueOf(ReflectionUtils.getField(field,this));
                if(StringUtils.isEmpty(data)|| StringUtils.equals("null",data)) {
                    continue;
                }
                sortedMap.put(field.getName(),data);
            }
        }
        return sortedMap;
    }

    public static void main(String[] args) {
        ColotnetRequestVo colotnetRequestVo = new ColotnetRequestVo();
        colotnetRequestVo.setOrderDate("20171203");
        System.out.println(colotnetRequestVo.toMap());
    }
}
