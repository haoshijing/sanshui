package com.keke.sanshui.base.admin.po;

import lombok.Data;

@Data
public class PlayerCouponPo {
    private Integer id;
    private Integer playerId;
    private Integer goldCount;
    private Integer silverCount;
    private Long lastUpdateTime;
}
