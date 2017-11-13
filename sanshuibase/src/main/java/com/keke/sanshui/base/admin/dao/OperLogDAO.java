package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.log.OperLogPo;
import com.keke.sanshui.base.admin.po.log.QueryOperLogPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OperLogDAO {

    int insertLog(@Param("log") OperLogPo operLogPo);

    List<OperLogPo> selectList(@Param("param")QueryOperLogPo queryOperLogPo);

    Long selectCount(@Param("param")QueryOperLogPo queryOperLogPo);

}
