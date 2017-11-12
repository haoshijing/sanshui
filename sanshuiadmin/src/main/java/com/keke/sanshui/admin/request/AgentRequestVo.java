package com.keke.sanshui.admin.request;

import lombok.Data;

@Data
public class AgentRequestVo {
    private Integer id;
    private Integer guid;
    private String  userName;
    private String wechartNo;
    private String nickName;
    private Integer level;
    private Integer parentAgentId;
    private String memo;
}
