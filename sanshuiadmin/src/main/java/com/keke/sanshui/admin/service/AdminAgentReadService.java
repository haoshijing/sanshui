package com.keke.sanshui.admin.service;


import com.keke.sanshui.admin.request.agent.AgentQueryVo;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.admin.service.PlayerCouponService;
import com.keke.sanshui.base.util.WeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
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

    public List<AgentVo> selectAgentVoList(AgentQueryVo agentQueryVo) {
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setPlayerId(agentQueryVo.getGuid());
        queryAgentPo.setLimit(agentQueryVo.getLimit());
        queryAgentPo.setLevel(agentQueryVo.getLevel());
        queryAgentPo.setAgentName(agentQueryVo.getUserName());
        queryAgentPo.setAgentNickName(agentQueryVo.getNickName());
        queryAgentPo.setAgentWeChartNo(agentQueryVo.getWechartNo());
        queryAgentPo.setStatus(1);
        Integer page = agentQueryVo.getPage();
        if(page == 0){
            page = 1;
        }
        queryAgentPo.setOffset((page- 1)*agentQueryVo.getLimit());
        List<AgentPo> agentPos = agentService.selectList(queryAgentPo);
        Integer week = WeekUtil.getCurrentWeek();
        List<AgentVo> agentVos = agentPos.stream().map(eachAgentPo -> {
            AgentVo agentVo = new AgentVo();
            agentVo.setGameId(eachAgentPo.getPlayerId());
            agentVo.setName(eachAgentPo.getAgentName());
            agentVo.setType(eachAgentPo.getLevel());
            agentVo.setNickName(eachAgentPo.getAgentNickName());
            agentVo.setWeChartNo(eachAgentPo.getAgentWeChartNo());
            agentVo.setAgentId(eachAgentPo.getId());
            agentVo.setParentAgentId(eachAgentPo.getParentId());
            agentVo.setMemo(eachAgentPo.getMemo());
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
                        agentVo.setAgentTotalPickUp(0L);
                    }
                    AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentVo.getAgentId(), week);
                    if(agentPickTotalPo != null) {
                        agentVo.setAgentUnderTotalPickUp(agentPickTotalPo.getTotalMoney()/100);
                    }else{
                        agentVo.setAgentUnderTotalPickUp(0L);
                    }
                    return agentVo;
                }
        ).collect(Collectors.toList());
        return agentVos;
    }

    public Long selectCount(AgentQueryVo agentQueryVo) {
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setPlayerId(agentQueryVo.getGuid());
        queryAgentPo.setLimit(agentQueryVo.getLimit());
        queryAgentPo.setAgentName(agentQueryVo.getUserName());
        queryAgentPo.setAgentNickName(agentQueryVo.getNickName());
        queryAgentPo.setAgentWeChartNo(agentQueryVo.getWechartNo());
        queryAgentPo.setStatus(1);
        return agentService.selectCount(queryAgentPo);
    }
}
