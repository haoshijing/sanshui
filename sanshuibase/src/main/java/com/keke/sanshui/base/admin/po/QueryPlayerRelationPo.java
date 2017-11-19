package com.keke.sanshui.base.admin.po;

import lombok.Data;

@Data
public class QueryPlayerRelationPo extends PlayerRelationPo {
    private Integer offset;
    private Integer limit;
}
