package com.keke.sanshui.admin.vo;

import lombok.Data;

@Data
public class AgentVo {
    private Integer agentId;
    private Integer gameId;
    private String name;
    private String weChartNo;
    private String nickName;
    private Integer goldCount;
    private Integer sliverCount;
    /**
     * 代理下级总充值
     */
    private Long agentUnderTotalPickUp;
    /**
     * 代理自身总充值
     */
    private Long agentTotalPickUp;
}
