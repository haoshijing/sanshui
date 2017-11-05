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
import com.keke.sanshui.base.util.WeekUtil;
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
        Integer week = WeekUtil.getCurrentWeek();
        List<AgentVo> agentVos = agentPos.stream().map(eachAgentPo -> {
            AgentVo agentVo = new AgentVo();
            agentVo.setGameId(eachAgentPo.getPlayerId());
            agentVo.setName(eachAgentPo.getAgentName());
            agentVo.setNickName(eachAgentPo.getAgentNickName());
            agentVo.setWeChartNo(eachAgentPo.getAgentWeChartNo());
            agentVo.setAgentId(eachAgentPo.getId());
            return agentVo;

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
                    PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentVo.getGameId(), week);
                    if(playerPickTotalPo != null) {
                        agentVo.setAgentTotalPickUp(playerPickTotalPo.getTotalMoney() / 100);
                    }else{
                        agentVo.setAgentTotalPickUp(0l);
                    }
                    AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentVo.getGameId(), week);
                    if(agentPickTotalPo != null) {
                        agentVo.setAgentTotalPickUp(agentPickTotalPo.getTotalMoney()/100);
                    }else{
                        agentVo.setAgentTotalPickUp(0L);
                    }
                    return agentVo;
                }
        ).collect(Collectors.toList());
        return agentVos;
    }

}
