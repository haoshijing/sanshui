package com.keke.sanshui.admin.request.player;

import lombok.Data;

@Data
public class PlayerQueryVo {
    private Integer week;
    private Integer guid;
    private Integer parentGuid;
    private Integer page = 1;
    private Integer limit = 20;
}
