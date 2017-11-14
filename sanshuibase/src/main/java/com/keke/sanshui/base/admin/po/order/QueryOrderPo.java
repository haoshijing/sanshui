package com.keke.sanshui.base.admin.po.order;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月11日 14:53
 **/
@Data
public class QueryOrderPo extends Order {
    private Integer limit = 20;
    private List<Integer> clientGuids = Lists.newArrayList();
    private Long startTimestamp;
    private Long endTimestamp;
    private Integer offset;
}
