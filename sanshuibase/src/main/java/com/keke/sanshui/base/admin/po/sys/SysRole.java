package com.keke.sanshui.base.admin.po.sys;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月14日 12:42
 **/
@Data
public class SysRole {
    private Integer id;
    private String roleName;
    private Integer status;
    private String funcList;
    private Long insertTime;
    private Long lastUpdateTime;
}
