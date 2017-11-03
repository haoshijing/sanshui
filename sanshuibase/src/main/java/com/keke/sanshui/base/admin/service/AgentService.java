package com.keke.sanshui.base.admin.service;

import com.google.common.collect.Sets;
import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.event.OperLogEvent;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.po.OperLogPo;
import com.keke.sanshui.base.enums.AgentLevelEnums;
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

    public AgentPo selectById(Integer id){
        return agentDAO.selectById(id);
    }

   public List<AgentPo> selectList(AgentPo agentPo){

        return agentDAO.selectList(agentPo);
    }

    public int insertAgent(AgentPo agentPo,Integer adminId){
        agentDAO.insert(agentPo);
        OperLogPo operLogPo = new OperLogPo();
        operLogPo.setInsertTime(System.currentTimeMillis());
        operLogPo.setOperTarget(agentPo.getId());
        StringBuilder mark = new StringBuilder("管理员").append("把")
                .append("[玩家").
                        append(agentPo.getPlayerId()).
                        append("] 设置成[").append(AgentLevelEnums.getByType(agentPo.getLevel()).getMark())
                .append("]");
        operLogPo.setMark(mark.toString());
        operLogPo.setOperType(1);
        applicationContext.publishEvent(new OperLogEvent(agentPo,operLogPo));
        return agentPo.getId();
    }

    /**
     * 获得它的下属playerId列表
     * @param agentId
     * @return
     */
    public Set<Integer> getAllBranchAgent(Integer agentId,boolean first){
        AgentPo queryAgentPo = new AgentPo();
        queryAgentPo.setParentId(agentId);
        Set<Integer> agentList = Sets.newHashSet();
        List<AgentPo> branchAgentList = agentDAO.selectList(queryAgentPo);

        for(AgentPo agentPo:branchAgentList){
          Set<Integer> ids =   getAllBranchAgent(agentPo.getId(),false);
          agentList.addAll(ids);
        }
        if(!first) {
            agentList.add(agentId);
        }
        return agentList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<AgentPo> selectAll() {
        return  agentDAO.selectAll();
    }
}
