package com.keke.sanshui.admin.request.agent;

import lombok.Data;

@Data
public class AgentQueryVo {
    private String wechartNo;
    private String nickName;
    private String userName;
    private Integer guid;
    private Integer week;
    private Integer level;
    private Integer parentGuid;
    private Integer page = 1;
    private Integer limit = 20;
}
