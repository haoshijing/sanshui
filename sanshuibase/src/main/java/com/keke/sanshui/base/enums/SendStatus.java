package com.keke.sanshui.base.enums;


import lombok.Getter;

@Getter
public enum SendStatus {
    Not_Send(1, "未发送"),
    Alread_Send(2,"已发送");

    private SendStatus(int code,String mark){
        this.code = code;
        this.mark = mark;
    }
    private int code;
    private String mark;
}
