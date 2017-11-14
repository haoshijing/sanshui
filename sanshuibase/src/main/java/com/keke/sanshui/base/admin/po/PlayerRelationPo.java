package com.keke.sanshui.base.admin.po;

import lombok.Data;
import org.springframework.core.annotation.Order;

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

    @Override
    public int hashCode(){
        if(this.parentPlayerId == null){
            this.parentPlayerId = 0;
        }
        if(this.playerId == null){
            this.playerId = 0;
        }
        return new StringBuilder(this.playerId).append(":").append(this.parentPlayerId).toString().hashCode();
    }

    @Override
    public boolean equals(Object obj){
        PlayerRelationPo other = (PlayerRelationPo)obj;
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
