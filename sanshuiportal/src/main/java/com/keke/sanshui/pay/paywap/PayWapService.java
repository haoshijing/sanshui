package com.keke.sanshui.pay.paywap;

import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.util.MD5Tool;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.pay.paywap.v3.RequestBean;
import com.keke.sanshui.pay.paywap.v3.ResponseBean;
import com.keke.sanshui.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Repository
@Slf4j
public class PayWapService {
    @Value("${payWapAppId}")
    private String payWapAppId;

    @Value("${paywapSecret}")
    private String payWapSecret;


    private static final String WX = "3";
    private static final String ALIPAY = "4";


    public RequestBean submitOrder(HttpServletRequest request, String selfOrderId, PayLink payLink, Integer guid, String payType) {
        Map<String,Object> params = Maps.newHashMap();
        RequestBean rbean = new RequestBean();
        rbean.setP1_yingyongnum(payWapAppId);
        rbean.setP2_ordernumber(selfOrderId);
        rbean.setP3_money(String.valueOf(payLink.getPickRmb()/100));
        rbean.setP6_ordertime(String.valueOf(System.currentTimeMillis()));
        rbean.setP7_productcode("ZFBZFWAP");

        String sign = MD5Tool.encoding(rbean.getP1_yingyongnum() + "&" + rbean.getP2_ordernumber() + "&" + rbean.getP3_money() + "&" + rbean.getP6_ordertime() + "&" + rbean.getP7_productcode() + "&" + payWapSecret);
        rbean.setP8_sign("");
        rbean.setP9_signtype("1");
        rbean.setP14_customname(String.valueOf(guid));
        rbean.setP16_customip(IpUtils.getIpAddr(request));
        rbean.setP17_product("钻石");
        rbean.setP23_charset("utf-8");
        rbean.setP25_terminal("2");
        rbean.setP8_sign(sign);
      //  params.put("p1_yingyongnum",rbean.getP1_yingyongnum());
//        params.put("p2_ordernumber",rbean.getP2_ordernumber());
//        params.put("p3_money",rbean.getP3_money());
//        params.put("p6_ordertime",rbean.getP6_ordertime());
//        params.put("p7_productcode",rbean.getP7_productcode());
//        params.put("p8_sign",rbean.getP8_sign());
//        params.put("p9_signtype",rbean.getP9_signtype());
//        params.put("p10_bank_card_code",rbean.getP10_bank_card_code());
//        params.put("p14_customname",rbean.getP14_customname());
//        params.put("p16_customip",rbean.getP16_customip());
//        params.put("p23_charset",rbean.getP23_charset());
//        params.put("p25_terminal",rbean.getP25_terminal());
        return rbean;
    }

    // 获取签名
    public String getResponseSign(ResponseBean bean) {
        // String rawString = bean.p1_usercode + "&" + bean.p2_order + "&" +
        // bean.p3_money + "&" + bean.p4_status +"&" + bean.p5_jtpayorder + "&"
        // + bean.p6_paymethod
        // +"&"+bean.p7_paychannelnum+"&"+bean.p8_charset+"&"+bean.p9_signtype+"&"+
        // compKey;

        String str = MD5Tool.encoding(bean.getP1_yingyongnum() + "&" + bean.getP2_ordernumber() + "&" + bean.getP3_money() + "&" + bean.getP4_zfstate() + "&" + bean.getP5_orderid() + "&" + bean.getP6_productcode() + "&" + bean.getP7_bank_card_code() + "&" + bean.getP8_charset() + "&" + bean.getP9_signtype() + "&" + bean.getP11_pdesc() + "&" + payWapSecret);

        return str;
    }


}
