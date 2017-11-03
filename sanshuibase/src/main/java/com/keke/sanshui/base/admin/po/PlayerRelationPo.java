package com.keke.sanshui.base.admin.po;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:27
 **/
@Data
public class PlayerRelationPo {
    private Integer id;
    private Integer playerId;
    private Integer agentPlayerId;
    private Long lastUpdateTime;
}
