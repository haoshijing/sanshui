package com.keke.sanshui.admin.response.agent;

import lombok.Data;

@Data
public class UnderAgentVo {
    private Integer     agentId;
    private Integer     agentGuid;
    private String      username;
    private String      wechartNo;
    private String      nickname;
    private Long        proxyPickTotal;
    private Long        proxyAgentTotal;
}
