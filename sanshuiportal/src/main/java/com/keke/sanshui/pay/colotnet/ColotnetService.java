package com.keke.sanshui.pay.colotnet;

import com.alibaba.fastjson.JSONObject;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.pay.fuqianla.FuqianResponseVo;
import com.keke.sanshui.pay.fuqianla.FuqianlaRequestVo;
import com.keke.sanshui.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;

@Repository
public class ColotnetService {
    @Value("${merNo}")
    private String merNo;

    @Value("${coltnetSign}")
    private String coltnetSign;

    private static final String WX_PAY = "1";
    private static final String ALI_PAY = "2";

    public ColotnetRequestVo createRequestVo(PayLink payLink, String payType, String selfOrderId) {
        ColotnetRequestVo requestVo = new ColotnetRequestVo();
        requestVo.setTransId("75");
        requestVo.setMerNo(merNo);
        if(StringUtils.equals(payType,WX_PAY)){
            requestVo.setProductId("1205");
        }else if(StringUtils.equals(payType,ALI_PAY)){
            requestVo.setProductId("1210");
        }
        requestVo.setOrderNo(new DateTime(System.currentTimeMillis()).toString("yyyyMMdd"));
        requestVo.setOrderNo(selfOrderId);
        requestVo.setTransAmt(String.valueOf(payLink.getPickRmb()));
        requestVo.setCommodityName(payLink.getPickCouponVal()+"钻石");
        return requestVo;
    }

    public boolean checkSign(FuqianResponseVo fuqianResponseVo) {
        String sign = SignUtil.createFullqianResponseSign(fuqianResponseVo,fuqianlaSign);
        return StringUtils.equals(fuqianResponseVo.getSign_info().toUpperCase(),sign);
    }

}
