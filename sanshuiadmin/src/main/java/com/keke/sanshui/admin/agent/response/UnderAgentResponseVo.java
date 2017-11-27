package com.keke.sanshui.admin.agent.response;

import lombok.Data;

import java.util.List;

@Data
public class UnderAgentResponseVo {
    private Long weekAgentPickTotal;
    private List<UnderProxyVo> underProxyVos;
}
