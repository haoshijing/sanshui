package com.keke.sanshui.base.admin.po;

import lombok.Data;

/**
 * 管理员信息表
 */
@Data
public class AdminPo {
    private Integer id;
    private String userName;
    private String password;
    private Long insertTime;
    private Long lastUpdateTime;
    private Integer status;
}
