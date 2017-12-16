package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:08
 **/
public interface AgentPickTotalDAO {

    AgentPickTotalPo selectByAgentId(@Param("agentId") Integer agentId,@Param("week") Integer week);

    int insertTotalPo(@Param("param") AgentPickTotalPo playerPickTotalPo);

    int updateTotalPo(@Param("param") AgentPickTotalPo playerPickTotalPo);

    /*
     * 某一周内的所有总和
     * @param playerIds
     * @param week
     * @return
     */
    Long sumPickUp(@Param("agentIds") List<Integer> agentIds, @Param("week") Integer week);

    Long sumUnderPickUp(@Param("agentId") Integer parentAgentId, @Param("week") Integer week);

    List<AgentPickTotalPo> exportAgent(@Param("week") Integer week);

    int deleteAgentTotal(Integer week);
}
