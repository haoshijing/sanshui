package com.keke.sanshui.admin.response.order;

import lombok.Data;

@Data
public class OrderItemVo {
    private Integer agentId;
    private String selfOrderNo;
    private Long lastUpdateTime;
    private Long insertTime;
    private Integer id;
    private Integer orderStatus;
    private String payType;
    private String money;
    private String price;
    private Integer sendStatus;
    private Integer clientGuid;
    private String title;
}
