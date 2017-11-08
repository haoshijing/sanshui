package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:36
 **/
public interface PlayerRelationDAO {

     void insertRelation(@Param("param") PlayerRelationPo playerRelationPo);

     PlayerRelationPo selectByPlayerId(Integer playerId);

     List<PlayerRelationPo> selectUnderByPlayerId(Integer playerId);

     List<PlayerRelationPo> selectAll();

     Integer queryByAgentAndPlayerGuid(@Param("agentPlayerId") Integer agentIdGuid,@Param("playerId") Integer playerGuid);
}
