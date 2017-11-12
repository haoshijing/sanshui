package com.keke.sanshui.base.admin.po.agent;

import lombok.Data;

@Data
public class AgentQueryPo extends AgentPo {
    private int offset = 0;
    private int limit = 20;
}
