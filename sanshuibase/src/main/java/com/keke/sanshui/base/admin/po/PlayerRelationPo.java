package com.keke.sanshui.base.admin.po;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:27
 **/
@Data
public class PlayerRelationPo {
    private Integer id;
    private Integer parentPlayerId;
    private Integer playerId;
    private Long lastUpdateTime;

    public boolean equals(PlayerRelationPo other){
        boolean match = false;
        if(this == other){
            return  true;
        }
        if(this.getPlayerId() != null){
            match =   this.getPlayerId().equals(other.playerId);
        }
        if(this.getParentPlayerId() != null){
            match = this.getParentPlayerId().equals(other.getParentPlayerId());
        }
        return match;
    }
}
