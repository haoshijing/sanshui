package com.keke.sanshui.admin.response.agent;

import lombok.Data;

@Data
public class AgentExportVo {
    private Integer guid;
    private Long underMonery;
    private Long underAgentMoney;
    private String week;
}
