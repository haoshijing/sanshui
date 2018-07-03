package com.keke.pay.impl.colotnet;

import com.google.common.collect.Maps;
import com.keke.pay.PaySignCreator;
import com.keke.pay.impl.colotnet.pojo.ColotnetRequestVo;
import com.keke.sanshui.base.coltentutil.SignUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * @author haoshijing
 * @version 2018年07月03日 12:32
 **/
@Repository
public class ColtnetSignImpl implements PaySignCreator<ColotnetRequestVo> {
    @Override
    public String genertorSign(ColotnetRequestVo colotnetRequestVo) {
        return createSign(colotnetRequestVo);
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
            return signStr;
        }catch (Exception e){
        }
        return "";
    }
}
