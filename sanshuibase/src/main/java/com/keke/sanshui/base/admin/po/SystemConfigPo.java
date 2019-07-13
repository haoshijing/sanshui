package com.keke.sanshui.base.admin.po;

import lombok.Data;

/**
 * @author haoshijing
 * @version 2018年06月06日 13:07
 **/
@Data
public class SystemConfigPo {
    private Integer id;
    private String configKey;
    private String configValue;
    private String memo;
    private Long createTime;
    /**
     * 1-有效 2-无效
     */
    private Integer status;

    /**
     * 验证正则表达式
     */
    private String regExp;

    /**
     * 验证错误提示语
     */
    private String errorMsg;

    /**
     *1:代理者风险管理配置  2:消费者风险管理配置  3:佣金管理配置  4:运营管理配置
     */
    private Integer type;

}
