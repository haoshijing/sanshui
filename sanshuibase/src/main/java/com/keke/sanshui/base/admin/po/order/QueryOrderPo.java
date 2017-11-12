package com.keke.sanshui.base.admin.po.order;

import lombok.Data;

import java.util.List;

@Data
public class QueryOrderPo  extends  Order{
    private List<Integer> clientGuids;
    private Integer offset = 0;
    private Integer limit = 20;
    private Long startTimestamp;
    private Long endTimestamp;
}
