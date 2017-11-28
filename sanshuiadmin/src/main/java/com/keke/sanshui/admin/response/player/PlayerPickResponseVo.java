package com.keke.sanshui.admin.response.player;

import lombok.Data;

@Data
public class PlayerPickResponseVo {
    private Integer guid;
    private Integer money;
    private String otherName;
    private String orderTime;
    private String orderStatusStr;
    private Integer orderStatus;
}
