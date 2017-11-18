package com.keke.sanshui.base.admin.po;

import lombok.Data;

@Data
public class PlayerPo {
   private Integer id;
    private Integer playerId;
    private String openId;
    private Long insertTime;
    private Long  lastUpdateTime;
    private Integer status;
    private String name;
    private Long money;
    private String otherName;
    /**
     * 游戏服务器创建时间
   */
    private Long gameInsertTime;
}
