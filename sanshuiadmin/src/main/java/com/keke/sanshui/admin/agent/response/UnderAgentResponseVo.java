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


    /**
     * 不属于自己的下属总充值
     */
    private Long  notBelongToSelfPickTotal;

    private List<UnderProxyVo> underProxyVos;
}
