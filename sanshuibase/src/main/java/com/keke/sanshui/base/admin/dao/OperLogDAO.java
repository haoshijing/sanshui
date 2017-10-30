package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.OperLogPo;
import org.apache.ibatis.annotations.Param;

public interface OperLogDAO {

    int insertLog(@Param("log") OperLogPo operLogPo);
}
