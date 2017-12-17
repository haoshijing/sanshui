package com.keke.sanshui.wechart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.wechart.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Repository
@Slf4j
public class WeChartService {
    @Resource
    private HttpClient httpClient;

    @Value("${wxAppId}")
    private String appId;

    @Value("${wxAppSecret}")
    private String appSecret;

    private String accessToken;


    @PostConstruct
    public void doGetAcessToken(){
        String requesUrl = String.format("%s&appid=%s&secret=%s",Constants.ACCESS_TOKEN_URL,appId,appSecret);
        try {
            ContentResponse contentResponse = httpClient.newRequest(requesUrl).send();
            String response =   contentResponse.getContentAsString();
            JSONObject jsonObject = JSON.parseObject(response);
            if(jsonObject != null){
                if(jsonObject.containsKey("access_token")){
                    accessToken =jsonObject.getString("access_token");
                }
            }
        }catch (Exception e){

        }
    }

    public String getAccessToken(){
        return accessToken;
    }

    public static void main(String[] args) {
        String requesUrl = String.format("%s&appid=%s&secret=%s",Constants.ACCESS_TOKEN_URL,"wx26c5daba5571b131","01a8d87641c35657689d3fbb2108329f");
        try {
            SslContextFactory sslContextFactory = new SslContextFactory();
            HttpClient httpClient = new HttpClient(sslContextFactory);
            httpClient.start();
            ContentResponse contentResponse = httpClient.newRequest(requesUrl).send();
            String response =   contentResponse.getContentAsString();
            JSONObject jsonObject = JSON.parseObject(response);
            if(jsonObject != null){
                if(jsonObject.containsKey("ac")){

                }
            }
        }catch (Exception e){
                e.printStackTrace();
        }
    }

    public String getOpenId(String code) {
        String openId = "";
        String accessTokenUrl = new StringBuilder(Constants.AUTHOR_URL).append("?appId=")
                .append(appId).append("&secret=").append(appSecret).append("&code=").append(code)
                .toString();
        try {
            ContentResponse contentResponse = httpClient.newRequest(accessTokenUrl).send();
            JSONObject jsonObject = JSON.parseObject(contentResponse.getContentAsString());
            if(jsonObject != null){
                if(jsonObject.containsKey("openid")){
                    return jsonObject.getString("openid");
                }
            }
        }catch (Exception e){
            log.error("",e);
        }
        return openId;
    }
}
