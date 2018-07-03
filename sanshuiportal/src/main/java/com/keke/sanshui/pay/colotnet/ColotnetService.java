package com.keke.sanshui.pay.colotnet;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.coltentutil.SignUtils;
import com.keke.sanshui.pay.fuqianla.FuqianResponseVo;
import com.keke.sanshui.pay.fuqianla.FuqianlaRequestVo;
import com.keke.sanshui.util.SignUtil;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.net.URLEncoder;
import java.util.Map;

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

    private String createSign(ColotnetRequestVo colotnetRequestVo){
        Map<String,String> paramMap = Maps.newConcurrentMap();
        paramMap.put("requestNo", colotnetRequestVo.getRequestNo());
        paramMap.put("version", "V1.0");
        paramMap.put("productId", colotnetRequestVo.getProductId());
        paramMap.put("merNo", colotnetRequestVo.getMerNo());
        paramMap.put("orderNo",colotnetRequestVo.getOrderNo());
        paramMap.put("transId", colotnetRequestVo.getTransId());
        paramMap.put("orderDate", colotnetRequestVo.getOrderDate());
        paramMap.put("transAmt", colotnetRequestVo.getTransAmt());
        paramMap.put("isCompany", "0");
        paramMap.put("acctNo", "622609757106909090");
        paramMap.put("bankNo", "");
        paramMap.put("bankLocalProvinceName", "");
        paramMap.put("bankLocalCityName", "");
        paramMap.put("phoneNo", "");
        paramMap.put("remark", "");
        try {
            String signStr = SignUtils.signData(paramMap);
            colotnetRequestVo.setSignature(signStr);
            return signStr;
        }catch (Exception e){
        }
        return "";
    }
}
