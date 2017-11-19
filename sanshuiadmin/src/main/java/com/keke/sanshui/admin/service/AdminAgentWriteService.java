package com.keke.sanshui.admin.service;

import com.keke.sanshui.admin.request.AgentRequestVo;
import com.keke.sanshui.admin.response.agent.AreaAgentVo;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 代理后台写业务实现类
 */
@Service
public class AdminAgentWriteService {
    @Autowired
    AgentService agentService;
    private final static String PROXY_PWD = "13shui";

    @Value("${saltEncrypt}")
    private String saltEncrypt;

    public Pair<Boolean,String> createOrUpdateAgent(AgentRequestVo agentRequestVo) {
        if (agentRequestVo.getId() == null) {
            AgentPo updateAgentPo = new AgentPo();
            String encryptPwd = MD5Util.md5(MD5Util.md5(PROXY_PWD) + saltEncrypt);
            updateAgentPo.setPassword(encryptPwd);
            updateAgentPo.setParentId(agentRequestVo.getParentAgentId());
            updateAgentPo.setLevel(agentRequestVo.getLevel());
            updateAgentPo.setLastUpdateTime(System.currentTimeMillis());
            updateAgentPo.setAgentWeChartNo(agentRequestVo.getWechartNo());
            updateAgentPo.setAgentNickName(agentRequestVo.getNickName());
            updateAgentPo.setAgentName(agentRequestVo.getUserName());
            updateAgentPo.setPlayerId(agentRequestVo.getGuid());
            updateAgentPo.setMemo(agentRequestVo.getMemo());
            updateAgentPo.setStatus(1);
            if(updateAgentPo.getLevel() == 2) {
                updateAgentPo.setParentId(null);
            }
            boolean checkIsValidName = checkIsValidUserName(agentRequestVo.getUserName());
            if(!checkIsValidName){
                return Pair.of(false,"该用户名已存在");
            }
            boolean checkGuid = checkIsCanSetGuid(agentRequestVo.getGuid());
            if(!checkGuid){
                return Pair.of(false,"无效的guid,可能为已设置代理或者改guid无资格成为代理");
            }
            boolean checkParentAgentId = checkParentAgentId(agentRequestVo.getParentAgentId(),agentRequestVo.getLevel());
            if(!checkParentAgentId){
                return Pair.of(false,"无效的上级代理id");
            }
            int updateRet = agentService.updateAgent(updateAgentPo);
            return Pair.of(true,"");
        }else{
            AgentPo updateAgentPo = new AgentPo();
            updateAgentPo.setMemo(agentRequestVo.getMemo());
            updateAgentPo.setParentId(agentRequestVo.getParentAgentId());
            updateAgentPo.setLevel(agentRequestVo.getLevel());
            updateAgentPo.setLastUpdateTime(System.currentTimeMillis());
            updateAgentPo.setAgentWeChartNo(agentRequestVo.getWechartNo());
            updateAgentPo.setAgentNickName(agentRequestVo.getNickName());
            updateAgentPo.setMemo(agentRequestVo.getMemo());
            updateAgentPo.setId(agentRequestVo.getId());
            AgentPo agentPo = agentService.selectById(agentRequestVo.getId());
            if(agentPo != null){
                agentService.updateAgent(updateAgentPo);
            }
            return Pair.of(true,"");
        }
    }

    private boolean checkIsValidUserName(String userName){
        AgentQueryPo agentPo = new AgentQueryPo();
        agentPo.setAgentName(userName);
        Long count  = agentService.selectCount(agentPo);
        return count== null || count == 0;
    }
    private boolean checkIsCanSetGuid(Integer guid){
        AgentQueryPo queryPo = new AgentQueryPo();
        queryPo.setPlayerId(guid);
        List<AgentPo> agentPoList = agentService.selectList(queryPo);
        if(agentPoList.size() == 1){
            return agentPoList.get(0).getStatus() ==2;
        }
        return false;
    }

    private boolean checkParentAgentId(Integer parentAgentId,Integer level){
        if(parentAgentId != null) {
            if(level == 3) {
                AgentPo agentPo = agentService.selectById(parentAgentId);
                if (agentPo == null || agentPo.getStatus() == 2 || agentPo.getLevel() == 3) {
                    return false;
                }
            }else if(level == 2){
                return false;
            }
        }
        return true;
    }

    public List<Integer> getCanChooseAgentList() {
        AgentQueryPo agentQueryPo = new AgentQueryPo();
        agentQueryPo.setLevel(3);
        agentQueryPo.setStatus(2);
        agentQueryPo.setLimit(200);
        List<AgentPo> agentPos= agentService.selectList(agentQueryPo);
        return agentPos.stream().map(agentPo -> {
            return agentPo.getPlayerId();
        }).collect(Collectors.toList());
    }

    public List<AreaAgentVo> getCanChooseAreaAgent(Integer currentAgentId) {
        AgentQueryPo agentQueryPo = new AgentQueryPo();

        agentQueryPo.setLevel(2);
        agentQueryPo.setStatus(1);
        List<AgentPo> agentPos= agentService.selectList(agentQueryPo);
        return agentPos.stream().filter(agentPo -> {
            if(currentAgentId != null) {
                return !agentPo.getId().equals(currentAgentId);
            }
            return true;
        }).map(agentPo -> {
            AreaAgentVo agentVo = new AreaAgentVo();
            agentVo.setGuid(agentPo.getPlayerId());
            agentVo.setAgentId(agentPo.getId());
            agentVo.setWechartNo(agentPo.getAgentWeChartNo());
            return agentVo;
        }).collect(Collectors.toList());
    }

    public Boolean resetPwd(Integer agentId) {
        AgentPo updateAgentPo = new AgentPo();
        updateAgentPo.setId(agentId);
        String encryptPwd = MD5Util.md5(MD5Util.md5(PROXY_PWD) + saltEncrypt);
        updateAgentPo.setPassword(encryptPwd);
        return agentService.updateAgent(updateAgentPo,false) > 0;
    }

    public Boolean updatePwd(String oldPwd, String newPwd,String guid) {
        AgentPo agentPo = agentService.findByGuid(Integer.valueOf(guid));
        String dbPassword = agentPo.getPassword();
        String userPassword = MD5Util.md5(MD5Util.md5(oldPwd)+saltEncrypt);
        Boolean checkRet =  StringUtils.equals(dbPassword,userPassword);
        if(!checkRet){
            return false;
        }
        AgentPo updateAgentPo = new AgentPo();
        updateAgentPo.setPassword(MD5Util.md5(MD5Util.md5(newPwd)+saltEncrypt));
        updateAgentPo.setId(agentPo.getId());
        agentService.updateAgent(updateAgentPo,false);
        return  true;
    }
}
