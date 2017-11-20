package com.keke.sanshui.admin.response.player;

import lombok.Data;

import java.util.List;

@Data
public class AgentPlayerResponseVo {
    private List<PlayerResponseVo> playerResponseVoList;
    private Long underMoney;
}
