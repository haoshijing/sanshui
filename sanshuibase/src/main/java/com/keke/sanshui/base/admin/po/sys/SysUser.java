package com.keke.sanshui.base.admin.po.sys;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2017年11月14日 12:40
 **/
@Data
public class SysUser {
    private Integer id;
    private String username;
    private String password;
    private Integer status;
    private String roleIds;
    private Long insertTime;
    private Long lastUpdateTime;
}
