package com.keke.sanshui.base.admin.dao;


import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentDAO {

    List<AgentPo> selectList(@Param("param") AgentQueryPo agentPo);

    int insert(@Param("agent") AgentPo agentPo);

    AgentPo selectById(Integer id);

    AgentPo selectByPlayerId(Integer playerId);

    List<AgentPo> selectAll();

    Long selectCount(@Param("param") AgentQueryPo queryAgentPo);

    int updateAgent(@Param("param") AgentPo agentPo);
}
