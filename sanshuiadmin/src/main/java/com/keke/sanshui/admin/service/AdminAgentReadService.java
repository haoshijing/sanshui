package com.keke.sanshui.admin.service;


import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.admin.service.PlayerCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
public class AdminAgentReadService {

    @Autowired
    AgentService agentService;

    @Autowired
    PlayerCouponService playerCouponService;


    public List<AgentVo> selectAgentVoList(AgentPo agentPo) {
        List<AgentPo> agentPos = agentService.selectList(agentPo);
        List<AgentVo> agentVos = agentPos.stream().map(new Function<AgentPo, AgentVo>() {
            @Override
            public AgentVo apply(AgentPo agentPo) {
                AgentVo agentVo = new AgentVo();
                agentVo.setGameId(agentPo.getPlayerId());
                agentVo.setName(agentPo.getAgentName());
                agentVo.setNickName(agentPo.getAgentNickName());
                agentVo.setWeChartNo(agentPo.getAgentWeChartNo());
                agentVo.setAgentId(agentPo.getId());
                return agentVo;
            }
        }).map(agentVo -> {
            PlayerCouponPo playerCouponPo = playerCouponService.selectByPlayerId(agentVo.getGameId());
            if (playerCouponPo != null) {
                agentVo.setGoldCount(playerCouponPo.getGoldCount());
                agentVo.setSliverCount(playerCouponPo.getSilverCount());
            } else {
                agentVo.setGoldCount(0);
                agentVo.setSliverCount(0);
            }
            return agentVo;
        }).map(agentVo -> {
                    return agentVo;
                }
        ).collect(Collectors.toList());
        return agentVos;
    }
}
