package com.keke.sanshui.base.admin.po;

import lombok.Data;

/**
 *
 */
@Data
public class HeadAgentPo {

    private Integer id;
    private String agentName;
    private Integer playerId;
    private String agengWeChartNo;
    private String agentNickName;
    private Long insertTime;
    private Long lastUpdateTime;
    private Integer status;
}
