package com.keke.sanshui.base.admin.po.log;

import lombok.Data;

@Data
public class QueryOperLogPo extends OperLogPo {
    private Integer offset;
    private Integer limit;
}
