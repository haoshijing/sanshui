package com.keke.sanshui.admin.agent.response;

import lombok.Data;

import java.util.List;

@Data
public class UnderAgentResponseVo {
    private Long weekAgentPickTotal;
    /**
     * 下属自己的充值总额
     */
    private Long underAgentSelfTotal;
    private List<UnderProxyVo> underProxyVos;
}
