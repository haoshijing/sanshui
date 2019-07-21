package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.SystemConfigPo;

import java.util.List;

/**
 * @author haoshijing
 * @version 2018年06月08日 13:21
 **/
public interface ConfigDAO {
    SystemConfigPo selectByKey(String configKey);

    List<SystemConfigPo> selectByTypeAndStatus(SystemConfigPo po);
}
