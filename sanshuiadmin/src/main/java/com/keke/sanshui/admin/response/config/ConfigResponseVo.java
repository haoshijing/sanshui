package com.keke.sanshui.admin.response.config;


import lombok.Data;

@Data
public class ConfigResponseVo {
    private String Address = "";
    private String Version = "";
    private String UpdateMessage = "";
    private String Notice = "";
    private String ScrollMessage = "";
    private String AndroidUpdateUrl = "";
    private String IOSUpdateUrl = "";
    private String SoundUpLoadUrl;
    private String SoundDownLoadUrl = "";
    private String WXShareUrl = "";
    private String ResourceDownLoadUrl = "";
    private String CreateDefaultMoney = "0";
    private Integer OnlineCount = 0;
    private Integer RegisterCount = 0;

}
