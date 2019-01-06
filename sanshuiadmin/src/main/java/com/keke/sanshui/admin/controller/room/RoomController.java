package com.keke.sanshui.admin.controller.room;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.room.RoomResponseVo;
import com.keke.sanshui.admin.response.room.RoomTotalResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/room")
@RestController
@Slf4j
public class RoomController extends AbstractController {

    @PostMapping("/queryTotal")
    public ApiResponse<RoomTotalResponseVo> queryTotal() {
        RoomTotalResponseVo roomTotalResponseVo = new RoomTotalResponseVo();
        try {
            ContentResponse response = httpClient.POST(queryServerHost + "/agentinfolist").send();
            String data = response.getContentAsString();
            JSONObject jsonObject = JSON.parseObject(data);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("Data");
                jsonArray.forEach(jData -> {

                });
            }
        } catch (Exception e) {
            log.error("", e);
            return new ApiResponse<>(RetCode.SERVER_ERROR, e.getMessage(), null);
        }

        return new ApiResponse<>(roomTotalResponseVo);
    }


    @PostMapping("/queryList")
    public ApiResponse<List<RoomResponseVo>> queryList() {
        List<RoomResponseVo> responseVos = Lists.newArrayList();
        try {
            ContentResponse response = httpClient.POST(queryServerHost + "/agentinfolist").send();
            String data = response.getContentAsString();
            JSONObject jsonObject = JSON.parseObject(data);
            if (jsonObject != null) {
                JSONArray jsonArray = jsonObject.getJSONArray("Data");

                responseVos = jsonArray.stream().map(jData -> {
                    RoomResponseVo roomResponseVo = new RoomResponseVo();
                    return roomResponseVo;
                }).collect(Collectors.toList());
            }
        } catch (Exception e) {
            log.error("", e);
            return new ApiResponse<>(RetCode.SERVER_ERROR, e.getMessage(), null);
        }

        return new ApiResponse<>(responseVos);

    }

}
