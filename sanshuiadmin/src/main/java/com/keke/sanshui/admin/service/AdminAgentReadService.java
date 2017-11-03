package com.keke.sanshui.admin.service;


import com.keke.sanshui.admin.request.AgentRequestVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
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

    @Autowired
    AgentPickTotalDAO agentPickTotalDAO;

    @Autowired
    PlayerPickTotalDAO playerPickTotalDAO;

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
                    PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentVo.getGameId(), 0);
                    agentVo.setAgentTotalPickUp(playerPickTotalPo.getTotalMoney());
                    AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentVo.getGameId(), 0);
                    agentVo.setAgentTotalPickUp(agentPickTotalPo.getTotalMoney());
                    return agentVo;
                }
        ).collect(Collectors.toList());
        return agentVos;
    }

    public Boolean createOrUpdateAgent(AgentRequestVo agentRequestVo, Integer adminId) {
        Boolean ret = false;
        if (agentRequestVo.getId() == null) {
            AgentPo agentPo = new AgentPo();
            agentPo.setParentId(agentRequestVo.getParentAgentId());
            agentPo.setStatus(agentRequestVo.getStatus());
            agentPo.setPlayerId(agentRequestVo.getPlayerId());
            agentPo.setLevel(agentRequestVo.getLevel());
            agentPo.setInsertTime(System.currentTimeMillis());
            agentPo.setAgentWeChartNo(agentRequestVo.getAgentWechartNo());
            agentPo.setLastUpdateTime(System.currentTimeMillis());
            agentService.insertAgent(agentPo, adminId);
        }
        return ret;
    }
}
