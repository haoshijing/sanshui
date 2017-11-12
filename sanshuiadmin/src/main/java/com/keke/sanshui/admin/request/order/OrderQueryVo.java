package com.keke.sanshui.admin.request.order;

import lombok.Data;

@Data
public class OrderQueryVo {
    private Integer orderStatus;
    private String orderId;
    private String guid;
    private Long startTimestamp;
    private Long endTimestamp;
    private Integer page = 1;
    private Integer limit = 20;
}
