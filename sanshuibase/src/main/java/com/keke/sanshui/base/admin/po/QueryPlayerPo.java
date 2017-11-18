package com.keke.sanshui.base.admin.po;

import lombok.Data;

@Data
public class QueryPlayerPo  extends  PlayerPo{
    private Integer offset;
    private Integer limit;
}
