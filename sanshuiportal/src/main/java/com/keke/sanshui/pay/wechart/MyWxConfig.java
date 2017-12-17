package com.keke.sanshui.pay.wechart;

import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.InputStream;

@Repository
public class MyWxConfig implements WXPayConfig {

    @Value("${wxAppId}")
    private String appId;

    @Value("${wxPayBizId}")
    private String mchId;

    @Value("${wxPaySecret}")
    private String signKey;

    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return signKey;
    }

    @Override
    public InputStream getCertStream() {
        return null;
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 3000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 4000;
    }
}
