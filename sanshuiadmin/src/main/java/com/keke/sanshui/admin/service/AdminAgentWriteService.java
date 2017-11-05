package com.keke.sanshui.admin.service;

import com.keke.sanshui.admin.request.AgentRequestVo;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 代理后台写业务实现类
 */
@Service
public class AdminAgentWriteService {
    @Autowired
    AgentService agentService;


    @Value("${saltEncrypt}")
    private String saltEncrypt;

    public Boolean createOrUpdateAgent(AgentRequestVo agentRequestVo, Integer adminId) {
        Boolean ret = false;
        if (agentRequestVo.getId() == null) {
            AgentPo agentPo = new AgentPo();
            String password = agentRequestVo.getPassword();
            String encryptPwd = MD5Util.md5(MD5Util.md5(password) + saltEncrypt);
            agentPo.setPassword(encryptPwd);
            agentPo.setParentId(agentRequestVo.getParentAgentId());
            agentPo.setStatus(agentRequestVo.getStatus());
            agentPo.setPlayerId(agentRequestVo.getPlayerId());
            agentPo.setLevel(agentRequestVo.getLevel());
            agentPo.setInsertTime(System.currentTimeMillis());
            agentPo.setAgentWeChartNo(agentRequestVo.getAgentWechartNo());
            agentPo.setLastUpdateTime(System.currentTimeMillis());
            agentService.insertAgent(agentPo, adminId);
        }
        return ret;
    }
}
