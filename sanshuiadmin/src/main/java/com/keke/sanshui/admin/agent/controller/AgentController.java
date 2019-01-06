package com.keke.sanshui.admin.agent.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.agent.controller.response.AgentResponseVo;
import com.keke.sanshui.admin.agent.response.UnderAgentResponseVo;
import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/agentapi/")
public class AgentController extends AbstractController{

    @PostMapping("/queryAgent")
    public ApiResponse<List<AgentResponseVo>> queryUnderAgentList(HttpServletRequest request, @RequestBody PlayerQueryVo playerQueryVo){
        try{
            ContentResponse response = httpClient.POST(queryServerHost + "/agentinfolist").send();
            String data = response.getContentAsString();
            JSONObject jsonObject = JSON.parseObject(data);
            if(jsonObject != null){
                JSONArray jsonArray = jsonObject.getJSONArray("Data");
                List<AgentResponseVo> agentResponseVos = jsonArray.stream().map(
                        jObject -> {
                            AgentResponseVo agentResponseVo = new AgentResponseVo();
                            JSONObject jData = (JSONObject)jObject;
                            agentResponseVo.setAccount(jData.getString(""));
                            agentResponseVo.setGuid(jData.getString("Guid"));
                            agentResponseVo.setNickName(jData.getString("Nick"));
                            agentResponseVo.setBalance(jData.getString("Money"));

                            Long createTime = jData.getLongValue("CreateTime");
                            if(createTime != null){
                                agentResponseVo.setRegisterTime(new DateTime(createTime).toString("yyyy-MM-dd HH:mm:ss"));
                            }
                            return agentResponseVo;
                        }
                ).collect(Collectors.toList());
                return new ApiResponse<>(agentResponseVos);
            }
            return new ApiResponse<>(Lists.newArrayList());
        }catch (Exception e){
            log.error("",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),null);
        }
    }

}
