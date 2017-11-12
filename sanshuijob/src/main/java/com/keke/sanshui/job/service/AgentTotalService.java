package com.keke.sanshui.job.service;

import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.util.WeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:54
 **/
@Service
public class AgentTotalService {

    @Autowired
    AgentService agentService;

    @Autowired
    private PlayerRelationDAO playerRelationDAO;

    @Autowired
    private PlayerPickTotalDAO playerPickTotalDAO;

    @Autowired
    private AgentPickTotalDAO agentPickTotalDAO;

    void work() {
        int week = WeekUtil.getCurrentWeek();
        List<AgentPo> agentPoList = agentService.selectAll();
        agentPoList.stream().forEach(agentPo -> {
            AgentPickTotalPo agentPickTotalPo = new AgentPickTotalPo();
            agentPickTotalPo.setAgentId(agentPo.getId());
            Set<Integer> underAgentIds = agentService.getAllBranchAgent(agentPo.getId(), false);
            underAgentIds.stream().forEach(agentId -> {
                AgentPo underAgentPo = agentService.selectById(agentId);
                if (underAgentPo != null) {
                    List<PlayerRelationPo> underPlayerPos = playerRelationDAO.selectUnderByPlayerId(underAgentPo.getPlayerId());
                    List<Integer> playerIds = underPlayerPos.stream().map((playerRelationPo -> {
                        return playerRelationPo.getPlayerId();
                    })).collect(Collectors.toList());
                    if (playerIds.size() > 0) {
                        Long pickSum = playerPickTotalDAO.sumPickUp(playerIds, week);
                        if (pickSum != null && pickSum > 0) {
                             AgentPickTotalPo queryAgentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(), week);
                            if (queryAgentPickTotalPo != null) {
                                AgentPickTotalPo updatePickTotalPo = new AgentPickTotalPo();
                                updatePickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                                updatePickTotalPo.setTotalMoney(pickSum);
                                updatePickTotalPo.setId(queryAgentPickTotalPo.getId());
                                int ret = agentPickTotalDAO.updateTotalPo(updatePickTotalPo);
                            } else {
                                AgentPickTotalPo newAgentPickTotalPo = new AgentPickTotalPo();
                                newAgentPickTotalPo.setTotalMoney(pickSum);
                                newAgentPickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                                newAgentPickTotalPo.setAgentId(agentPo.getId());
                                newAgentPickTotalPo.setWeek(week);
                                agentPickTotalDAO.insertTotalPo(newAgentPickTotalPo);
                            }
                        }
                    }
                }
            });

        });

    }


}
