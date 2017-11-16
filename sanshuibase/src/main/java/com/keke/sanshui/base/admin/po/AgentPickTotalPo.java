package com.keke.sanshui.base.admin.po;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:33
 **/
@Data
public class AgentPickTotalPo {
    private Integer id;
    private Integer week;
    private Integer agentId;
    private Long totalMoney; /*单位元*/
    private Long totalUnderMoney; /*单位元*/
    private Long lastUpdateTime;
}
