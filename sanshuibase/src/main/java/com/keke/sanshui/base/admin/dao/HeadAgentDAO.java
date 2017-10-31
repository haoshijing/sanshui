package com.keke.sanshui.base.admin.dao;


import com.keke.sanshui.base.admin.po.HeadAgentPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HeadAgentDAO {

    List<HeadAgentPo> selectList();

    int insert(@Param("headAgent") HeadAgentPo headAgentPo);
}
