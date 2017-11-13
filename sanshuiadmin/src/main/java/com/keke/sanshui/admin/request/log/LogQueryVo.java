package com.keke.sanshui.admin.request.log;

import lombok.Data;

@Data
public class LogQueryVo {
    private Integer page = 1;
    private Integer limit = 20;
}
