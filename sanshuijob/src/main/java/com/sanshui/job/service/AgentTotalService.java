package com.sanshui.job.service;

import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.util.WeekUtil;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
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
            final AtomicLong sum = new AtomicLong(0);
            agentPickTotalPo.setTotalMoney(sum.get());
            agentPickTotalPo.setWeek(0);
            agentPickTotalPo.setLastUpdateTime(System.currentTimeMillis());
            Set<Integer> underAgentIds = agentService.getAllBranchAgent(agentPo.getId(), true);
            underAgentIds.stream().forEach(agentId -> {
                AgentPo underAgentPo = agentService.selectById(agentId);
                if (underAgentPo != null) {
                    List<PlayerRelationPo> underPlayerPos = playerRelationDAO.selectUnderByPlayerId(underAgentPo.getPlayerId());
                    List<Integer> playerIds = underPlayerPos.stream().map((playerRelationPo -> {
                        return playerRelationPo.getId();
                    })).collect(Collectors.toList());
                    sum.set(playerPickTotalDAO.sumPickUp(playerIds,week));

                }
            });
            AgentPickTotalPo queryAgentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(),week);
            if(queryAgentPickTotalPo != null){
                AgentPickTotalPo updatePickTotalPo = new AgentPickTotalPo();
                updatePickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                updatePickTotalPo.setTotalMoney(sum.get());
                updatePickTotalPo.setId(queryAgentPickTotalPo.getId());
                int ret = agentPickTotalDAO.updateTotalPo(updatePickTotalPo);

            }else{
                AgentPickTotalPo newAgentPickTotalPo = new AgentPickTotalPo();
                newAgentPickTotalPo.setTotalMoney(sum.get());
                newAgentPickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                newAgentPickTotalPo.setAgentId(agentPo.getId());
                newAgentPickTotalPo.setWeek(week);
                agentPickTotalDAO.insertTotalPo(newAgentPickTotalPo);
            }
        });

    }



}
