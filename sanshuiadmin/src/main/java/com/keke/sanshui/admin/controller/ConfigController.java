package com.keke.sanshui.admin.controller;


import com.alibaba.fastjson.JSON;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.config.ConfigResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private HttpClient httpClient;

    @Value("${queryServerHost}")
    private String queryServerHost;


    @GetMapping("/obtainData")
    public ApiResponse<ConfigResponseVo> obtainConfig() throws Exception {
        ConfigResponseVo configResponseVo = new ConfigResponseVo();
        ContentResponse response = httpClient.POST(queryServerHost + "/query_serverdata").send();
        if (response != null) {
            configResponseVo = JSON.parseObject(response.getContentAsString(), ConfigResponseVo.class);
        }
        return new ApiResponse<>(configResponseVo);
    }


    @PostMapping("/updateData")
    public ApiResponse<Boolean> updateConfig(@RequestBody ConfigResponseVo vo) throws Exception {
        httpClient.POST(queryServerHost + "/modify_serverdata" + toRequestData(vo)).send();
        return new ApiResponse<>(true);
    }

    private String toRequestData(ConfigResponseVo vo) {
        StringBuilder stringBuilder = new StringBuilder("?1=1");
        if (StringUtils.isNotEmpty(vo.getVersion())) {
            stringBuilder.append("&Version=").append(vo.getVersion());
        }
        if (StringUtils.isNotEmpty(vo.getAddress())) {
            stringBuilder.append("&Address=").append(vo.getAddress());
        }
        if (StringUtils.isNotEmpty(vo.getAndroidUpdateUrl())) {
            stringBuilder.append("&AndroidUpdateUrl=").append(vo.getAndroidUpdateUrl());
        }
        if (StringUtils.isNotEmpty(vo.getIOSUpdateUrl())) {
            stringBuilder.append("&IOSUpdateUrl=").append(vo.getIOSUpdateUrl());
        }
        if (StringUtils.isNotEmpty(vo.getWXShareUrl())) {
            stringBuilder.append("&WXShareUrl=").append(vo.getWXShareUrl());
        }
        if (StringUtils.isNotEmpty(vo.getResourceDownLoadUrl())) {
            stringBuilder.append("&ResourceDownLoadUrl=").append(vo.getResourceDownLoadUrl());
        }
        if (StringUtils.isNotEmpty(vo.getScrollMessage())) {
            stringBuilder.append("&ScrollMessage=").append(vo.getScrollMessage());
        }
        if (StringUtils.isNotEmpty(vo.getSoundDownLoadUrl())) {
            stringBuilder.append("&SoundDownLoadUrl=").append(vo.getSoundDownLoadUrl());
        }
        if (StringUtils.isNotEmpty(vo.getSoundUpLoadUrl())) {
            stringBuilder.append("&SoundUpLoadUrl=").append(vo.getSoundUpLoadUrl());
        }
        if (StringUtils.isNotEmpty(vo.getNotice())) {
            stringBuilder.append("&Notice=").append(vo.getNotice());
        }
        if (StringUtils.isNotEmpty(vo.getCreateDefaultMoney())) {
            stringBuilder.append("&CreateDefaultMoney=").append(vo.getCreateDefaultMoney());
        }
        log.info("requestData={}",stringBuilder.toString());
        return stringBuilder.toString();
    }


}
