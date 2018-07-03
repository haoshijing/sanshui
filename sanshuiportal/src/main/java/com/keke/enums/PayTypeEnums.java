package com.keke.enums;

import lombok.Getter;
import lombok.Setter;

/**
 * @author haoshijing
 * @version 2018年07月03日 12:35
 **/
public enum PayTypeEnums {

    PAYWAP(1,"旺实富"),COLOTET(2,"");
    PayTypeEnums(Integer type,String desc){
        this.type = type;
        this.desc = desc;
    }
    @Getter @Setter
    private Integer type;
    @Getter @Setter
    private String desc;
}
