package com.keke.sanshui.admin.request;

import lombok.Data;

@Data
public class AgentRequestVo {
    private Integer id;
    private Integer playerId;
    private String password;
    private String agentName;
    private String agentWechartNo;
    private String agentNickName;
    private Integer level;
    private Integer status;
    private Integer parentAgentId;
    private String memo;
}
