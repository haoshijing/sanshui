package com.keke.sanshui.admin.agent.controller.response;

import lombok.Data;

@Data
public class AgentResponseVo {
    private String guid;
    private String account;
    private String nickName;
    private String balance;
    private String registerTime;
}
