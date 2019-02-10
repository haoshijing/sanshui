package com.keke.sanshui.admin.response.agent;

import lombok.Data;

@Data
public class AgentExportVo {
    /**
     * guid
     */
    private Integer guid;
    /**
     * 下属直冲
     */
    private Long underMonery;
    /**
     * 下级代理总充
     */
    private Long underAgentMoney;

    private String name;
    /**
     * 所在周
     */
    private String week;
    /**
     *
     */
    private Long selfPickTotal;
}
