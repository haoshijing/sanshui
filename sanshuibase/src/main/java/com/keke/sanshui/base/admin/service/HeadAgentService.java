package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.HeadAgentDAO;
import com.keke.sanshui.base.admin.event.OperLogEvent;
import com.keke.sanshui.base.admin.po.HeadAgentPo;
import com.keke.sanshui.base.admin.po.OperLogPo;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HeadAgentService implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Autowired
    HeadAgentDAO headAgentDAO;

   public List<HeadAgentPo> selectList(){

        return headAgentDAO.selectList();
    }

    public int insertHeadAgent(HeadAgentPo headAgentPo,Long adminId){
        int insertHeadAgent = headAgentDAO.insert(headAgentPo);
        OperLogPo operLogPo = new OperLogPo();

        applicationContext.publishEvent(new OperLogEvent(headAgentPo,operLogPo));
        return insertHeadAgent;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
