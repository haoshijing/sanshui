package com.keke.sanshui.base.admin.service;

import com.google.common.collect.Sets;
import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.event.OperLogEvent;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.po.OperLogPo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public class AgentService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    AgentDAO agentDAO;

   public List<AgentPo> selectList(AgentPo agentPo){

        return agentDAO.selectList(agentPo);
    }

    public int insertAgent(AgentPo agentPo,Long adminId){
        agentDAO.insert(agentPo);
        OperLogPo operLogPo = new OperLogPo();

        applicationContext.publishEvent(new OperLogEvent(agentPo,operLogPo));
        return agentPo.getId();
    }

    /**
     * 获得它的下属playerId列表
     * @param agentId
     * @return
     */
    public Set<Integer> getAllBranchAgent(Integer agentId){
        AgentPo queryAgentPo = new AgentPo();
        queryAgentPo.setParentId(agentId);
        Set<Integer> agentList = Sets.newHashSet();

        List<AgentPo> branchAgentList = agentDAO.selectList(queryAgentPo);
        while (branchAgentList.size() != 0){
            for(AgentPo branchPo:branchAgentList){
                agentList.addAll(getAllBranchAgent(branchPo.getId()));
            }
        }
        agentList.add(agentId);

        return agentList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}