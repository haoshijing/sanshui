package com.keke.sanshui.base.admin.dao;


import com.keke.sanshui.base.admin.po.AgentPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AgentDAO {

    List<AgentPo> selectList(@Param("agent") AgentPo agentPo);

    int insert(@Param("agent") AgentPo agentPo);
}
