package com.keke.sanshui.admin.service;

import com.google.common.collect.Lists;
import com.keke.sanshui.admin.vo.HeadAgentVo;
import com.keke.sanshui.base.admin.po.HeadAgentPo;
import com.keke.sanshui.base.admin.service.HeadAgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminHeadAgentReadService {

    @Autowired
    HeadAgentService headAgentService;
    public List<HeadAgentVo> selectAgentVoList(){
        List<HeadAgentPo> agentPos = headAgentService.selectList();
        return Lists.newArrayList();
    }
}
