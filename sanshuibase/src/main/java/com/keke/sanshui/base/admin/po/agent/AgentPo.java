package com.keke.sanshui.base.admin.po.agent;

import lombok.Data;

/**
 *
 */
@Data
public class AgentPo {
    private Integer id;
    private String agentName;
    private Integer playerId;
    private String agentWeChartNo;
    private String agentNickName;
    private Integer level;
    private Integer parentId;
    private Long insertTime;
    private Long lastUpdateTime;
    private Integer status;
    private String memo;
    private String password;
    private String avatar;
    private Integer isNeedAreaCal;
}
