package com.keke.sanshui.admin.service.log;

import com.google.common.collect.Collections2;
import com.keke.sanshui.admin.request.log.LogQueryVo;
import com.keke.sanshui.admin.response.log.LogVo;
import com.keke.sanshui.base.admin.dao.OperLogDAO;
import com.keke.sanshui.base.admin.po.log.OperLogPo;
import com.keke.sanshui.base.admin.po.log.QueryOperLogPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdminLogReadService {

    @Autowired
    private OperLogDAO operLogDAO;
    public List<LogVo> queryList(LogQueryVo logQueryVo) {
        QueryOperLogPo queryOperLogPo = new QueryOperLogPo();
        queryOperLogPo.setLimit(logQueryVo.getLimit());
        queryOperLogPo.setOffset((logQueryVo.getPage() -1) *logQueryVo.getLimit());
        List<OperLogPo> operLogPos =  operLogDAO.selectList(queryOperLogPo);
        return operLogPos.stream(
        ).map(operLogPo -> {
            LogVo logVo = new LogVo();
            logVo.setLastUpdateTime(operLogPo.getInsertTime());
            logVo.setLogId(operLogPo.getId());
            logVo.setMark(operLogPo.getMark());
            logVo.setType(operLogPo.getOperType());
            return logVo;
        }).collect(Collectors.toList());
    }

    public Long queryCount(LogQueryVo logQueryVo) {
        QueryOperLogPo queryOperLogPo = new QueryOperLogPo();
        return operLogDAO.selectCount(queryOperLogPo);
    }
}
