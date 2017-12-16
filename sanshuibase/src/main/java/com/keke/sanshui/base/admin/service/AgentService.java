package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.event.OperLogEvent;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.po.log.OperLogPo;
import com.keke.sanshui.base.enums.AgentLevelEnums;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AgentService implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Autowired
    AgentDAO agentDAO;

    public AgentPo selectById(Integer id){
        return agentDAO.selectById(id);
    }

   public List<AgentPo> selectList(AgentQueryPo agentPo){
        return agentDAO.selectList(agentPo);
    }
    public boolean onlyUpdateAgent(AgentPo updateAgentPo){
        agentDAO.updateAgent(updateAgentPo) ;
        return true;
    }

    public AgentPo findByGuid(Integer playerId){
        return agentDAO.selectByPlayerId(playerId);
    }

    public  int updateAgent(AgentPo agentPo){
        return updateAgent(agentPo,true);
    }
    public int updateAgent(AgentPo agentPo, boolean needLog){

        OperLogPo operLogPo = new OperLogPo();
       if(agentPo.getId() == null) {
           agentDAO.updateAgent(agentPo);
           if(needLog) {
               operLogPo.setInsertTime(System.currentTimeMillis());
               operLogPo.setOperTarget(agentPo.getPlayerId());
               StringBuilder mark = new StringBuilder("管理员").append("把")
                       .append("[玩家").
                               append(agentPo.getPlayerId()).
                               append("] 设置成[").append(AgentLevelEnums.getByType(agentPo.getLevel()).getMark())
                       .append("]");
               operLogPo.setMark(mark.toString());
               operLogPo.setOperType(1);
           }

       }else{
           AgentPo dbAgentPo = agentDAO.selectById(agentPo.getId());
            agentDAO.updateAgent(agentPo);
           if(needLog) {
               operLogPo.setInsertTime(System.currentTimeMillis());
               operLogPo.setOperTarget(dbAgentPo.getPlayerId());
               boolean changeLevel = false;
               boolean changeParentId = false;
               if (dbAgentPo != null) {
                   changeLevel = !dbAgentPo.getLevel().equals(agentPo.getLevel());
                   changeParentId = !dbAgentPo.getParentId().equals(agentPo.getParentId());
               }
               StringBuilder mark = new StringBuilder("管理员").append("修改了")
                       .append("[玩家").append(dbAgentPo.getPlayerId()).append("资料");
               if (changeLevel) {
                   mark.append("等级由(").append(AgentLevelEnums.getByType(dbAgentPo.getLevel()).getMark())
                           .append(")改为(").append(AgentLevelEnums.getByType(agentPo.getLevel()).getMark()).append(")");
               }
               if (changeParentId) {
                   mark.append("上级代理id由(").append(dbAgentPo.getParentId())
                           .append(")改为(").append(agentPo.getParentId()).append(")");
               }

               mark.append("]");

               operLogPo.setMark(mark.toString());
               operLogPo.setOperType(2);
           }
       }
       if(needLog) {
           applicationContext.publishEvent(new OperLogEvent(agentPo, operLogPo));
       }
        return 1;
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
    public List<Integer> getAllBranchAgent(Integer agentId,Integer isNeedAreaCal){
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setParentId(agentId);
        queryAgentPo.setLimit(10000);
        queryAgentPo.setOffset(0);
        queryAgentPo.setIsNeedAreaCal(isNeedAreaCal);
        List<AgentPo> branchAgentList = agentDAO.selectList(queryAgentPo);
        return  branchAgentList.stream().map(agentPo -> {
            return agentPo.getPlayerId();
        }).collect(Collectors.toList());
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public List<AgentPo> selectAll() {
        return  agentDAO.selectAll();
    }

    public Long selectCount(AgentQueryPo queryAgentPo) {
        return agentDAO.selectCount(queryAgentPo);
    }

}
