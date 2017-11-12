package com.keke.sanshui.base.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * 代理级别
 */
public enum  AgentLevelEnums {

    Area(2,"地区代理"),
    Group(3,"群代理");

    private AgentLevelEnums(Integer type,String mark){
        this.mark = mark;
        this.type = type;
    }

    public static AgentLevelEnums getByType(Integer type){
        for(AgentLevelEnums agentLevelEnums : AgentLevelEnums.values()){
            if(agentLevelEnums.getType() == type){
                return agentLevelEnums;
            }

        }
        return  null;
    }
    @Getter
    @Setter
    private Integer type;
    @Getter
    @Setter
    private String mark;
}
