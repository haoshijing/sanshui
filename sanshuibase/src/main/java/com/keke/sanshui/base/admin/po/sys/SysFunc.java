package com.keke.sanshui.base.admin.po.sys;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月14日 12:43
 **/
@Data
public class SysFunc {
    private Integer id;
    private String funcName;
    private Integer status;
    private String funcUrl;
    private Integer parentId;
    private Long insertTime;
    private Long lastUpdateTime;
}
