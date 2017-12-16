package com.keke.sanshui.job.service;

import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:54
 **/
@Service
@Slf4j
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        log.info("开始代理玩家的总充值统计:{}",format.format(new Date()));
        int week = WeekUtil.getCurrentWeek();
        try {
            agentPickTotalDAO.deleteAgentTotal(week);
            staticNormalAgent(week);
            staticAreaAgent(week);
        }catch (Exception e){
            log.error("{}",e);
        }
        log.info("结束代理玩家的总充值统计:{}",format.format(new Date()));
    }
    public void staticAreaAgent(int week) {
        List<AgentPo> agentPoList = agentService.selectAll();
        agentPoList.stream().filter(agentPo -> {
            return agentPo.getLevel() == 2;
        }).forEach(agentPo -> {
            AgentQueryPo agentQueryPo = new AgentQueryPo();
            agentQueryPo.setParentId(agentPo.getId());
            agentQueryPo.setLimit(10000);
            List<AgentPo> agentPos = agentService.selectList(agentQueryPo);

            List<Integer> agentIds = agentPos.stream().map(agentPo1 -> {
                return agentPo1.getId();
            }).collect(Collectors.toList());
            if(agentPos.size() > 0) {
                Long agentUnderTotal = agentPickTotalDAO.sumPickUp(agentIds, week);
                if(agentUnderTotal == null){
                    agentUnderTotal = 0L;
                }
                AgentPickTotalPo agentPickTotalPo = new AgentPickTotalPo();
                agentPickTotalPo.setTotalUnderMoney(agentUnderTotal);

                AgentPickTotalPo queryAgentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(), week);
                if(queryAgentPickTotalPo == null){
                    AgentPickTotalPo newAgentPickTotalPo = new AgentPickTotalPo();
                    newAgentPickTotalPo.setTotalMoney(0L);
                    newAgentPickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                    newAgentPickTotalPo.setAgentId(agentPo.getId());
                    newAgentPickTotalPo.setTotalUnderMoney(agentUnderTotal);
                    newAgentPickTotalPo.setWeek(week);
                    agentPickTotalDAO.insertTotalPo(newAgentPickTotalPo);
                }else {
                    agentPickTotalPo.setId(queryAgentPickTotalPo.getId());
                    agentPickTotalDAO.updateTotalPo(agentPickTotalPo);
                }
                log.info("agentId = {} ,agentUnderTotal = {} ",agentPo.getId(),agentUnderTotal);
            }
        });
    }

    public void staticNormalAgent(int week) {
        List<AgentPo> agentPoList = agentService.selectAll();
        agentPoList.stream().forEach(agentPo -> {
            AgentPickTotalPo agentPickTotalPo = new AgentPickTotalPo();
            agentPickTotalPo.setAgentId(agentPo.getId());
            List<PlayerRelationPo> underPlayerPos = playerRelationDAO.selectUnderByPlayerId(agentPo.getPlayerId());
            List<Integer> playerIds = underPlayerPos.stream().map((playerRelationPo -> {
                return playerRelationPo.getPlayerId();
            })).collect(Collectors.toList());
            if (playerIds.size() > 0) {
                Long pickSum = playerPickTotalDAO.sumPickUp(playerIds, week);
                if (pickSum != null && pickSum > 0) {
                    handlerData(agentPo.getId(),week,pickSum);
                }
            }
        });
    }

    private void handlerData(Integer agentId,Integer week,Long pickSum){
        AgentPickTotalPo queryAgentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentId, week);
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
            newAgentPickTotalPo.setAgentId(agentId);
            newAgentPickTotalPo.setTotalUnderMoney(0L);
            newAgentPickTotalPo.setWeek(week);
            agentPickTotalDAO.insertTotalPo(newAgentPickTotalPo);
        }
    }
}
