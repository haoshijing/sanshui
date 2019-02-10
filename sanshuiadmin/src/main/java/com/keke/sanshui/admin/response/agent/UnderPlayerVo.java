package com.keke.sanshui.admin.response.agent;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月13日 19:02
 **/
@Data
public class UnderPlayerVo {
    /**
     * 玩家guid
     */
    private Integer playerGuid;

    /**
     * 玩家name
     */
    private String name;
    /**
     * 玩家总充值
     */
    private Long playerPickUp;

    private String createTime;
}
