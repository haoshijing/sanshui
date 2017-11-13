package com.keke.sanshui.admin.response.log;

import lombok.Data;

@Data
public class LogVo {
    private Integer logId;
    private Integer type;
    private String mark;
    private Long lastUpdateTime;
}
