package com.keke.sanshui.admin.agent.response;

import lombok.Data;

@Data
public class UnderProxyVo {
    /**
     * 代理guid
     */
    private Integer guid;
    /**
     *玩家用户名
     */
    private String otherName;
    /**
     * 代理当前周总充值
     */
    private Long pickTotal;

    /**
     *代理总充值
     */
    private Long agentTotal;

    /**
     * 周
     */
    private Integer week;
    private Integer isNotCal;
}
