package com.keke.sanshui.base.admin.service;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
