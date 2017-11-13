package com.keke.sanshui.base.admin.po.log;

import lombok.Data;

/**
 * 操作日志表
 */
@Data
public class OperLogPo {
    private Integer id;
    private Integer operType;
    private Integer operTarget;
    private String mark;
    private Long insertTime;
}
