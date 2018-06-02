package com.keke.sanshui.pay.paywap;

import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.util.MD5Tool;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.servlet.http.HttpServletRequest;

@Repository
@Slf4j
public class PayWapService {
    @Value("${userCode}")
    private String userCode;

    @Value("${paywapSecret}")
    private String payWapSecret;

    @Value("${callbackHost}")
    private String callbackHost;

    private static final String requestUrl = "http://pay.paywap.cn/form/pay";// 提交地址

    private static final String WX = "3";
    private static final String ALIPAY = "4";


    public String submitOrder(HttpServletRequest request,String selfOrderId, PayLink payLink, Integer guid, String payType){
       String returnUrl = requestUrl;
        RequestBean rbean = new RequestBean();
        rbean.setP1_usercode(userCode);
        rbean.setP2_order(selfOrderId);
        rbean.setP3_money(String.valueOf(payLink.getPickRmb()));
        rbean.setP4_returnurl(callbackHost+"/paywap/return/"+selfOrderId);//
        rbean.setP5_notifyurl(callbackHost+"/paywap/callback");//
        rbean.setP6_ordertime(String.valueOf(System.currentTimeMillis()));
        rbean.setP7_sign("");
        rbean.setP8_signtype("1");
        if(StringUtils.endsWithIgnoreCase(payType,"1")) {
            rbean.setP9_paymethod(WX);
        }else if (StringUtils.endsWithIgnoreCase(payType,"2")){
            rbean.setP9_paymethod(ALIPAY);
        }
        rbean.setP10_paychannelnum("");
        rbean.setP14_customname(String.valueOf(guid));
        rbean.setP17_customip(IpUtils.getIpAddr(request));
        rbean.setP18_product("钻石");
        rbean.setP20_productnum("1");
        rbean.setP25_terminal("2");
        rbean.setP26_iswappay("3");

        // 生成
        rbean.setP7_sign(getRequestSign(rbean));
        // form1.Action = requestUrl;
        // response.sendRedirect(requestUrl);
        returnUrl = returnUrl + getUrlCS(rbean);
        return returnUrl;
    }
    // 获取签名
    public String getRequestSign(RequestBean bean) {
        String rawString = bean.p1_usercode + "&" + bean.p2_order + "&"
                + bean.p3_money + "&" + bean.p4_returnurl + "&"
                + bean.p5_notifyurl + "&" + bean.p6_ordertime + payWapSecret;
        log.info("payWapSecret = {}",payWapSecret);
        // return
        // FormsAuthentication.HashPasswordForStoringInConfigFile(rawString,
        // "MD5");
        String data = MD5Tool.encoding(rawString);
        return data;
    }
    public String getResponseSign(ResponseBean bean) {
        // String rawString = bean.p1_usercode + "&" + bean.p2_order + "&" +
        // bean.p3_money + "&" + bean.p4_status +"&" + bean.p5_jtpayorder + "&"
        // + bean.p6_paymethod
        // +"&"+bean.p7_paychannelnum+"&"+bean.p8_charset+"&"+bean.p9_signtype+"&"+
        // compKey;
        String rawString = "";
        if (bean.p1_usercode == null || bean.p1_usercode.equals("")
                || bean.p1_usercode.equals("null")) {
            rawString = userCode;
        } else {
            rawString = bean.p1_usercode;
        }
        if (bean.p2_order != null && !bean.p2_order.equals("")
                && !bean.p2_order.equals("null")) {
            rawString = rawString + "&" + bean.p2_order;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p3_money != null && !bean.p3_money.equals("")
                && !bean.p3_money.equals("null")) {
            rawString = rawString + "&" + bean.p3_money;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p4_status != null && !bean.p4_status.equals("")
                && !bean.p4_status.equals("null")) {
            rawString = rawString + "&" + bean.p4_status;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p5_jtpayorder != null && !bean.p5_jtpayorder.equals("")
                && !bean.p5_jtpayorder.equals("null")) {
            rawString = rawString + "&" + bean.p5_jtpayorder;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p6_paymethod != null && !bean.p6_paymethod.equals("")
                && !bean.p6_paymethod.equals("null")) {
            rawString = rawString + "&" + bean.p6_paymethod;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p7_paychannelnum != null && !bean.p7_paychannelnum.equals("")
                && !bean.p7_paychannelnum.equals("null")) {
            rawString = rawString + "&" + bean.p7_paychannelnum;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p8_charset != null && !bean.p8_charset.equals("")
                && !bean.p8_charset.equals("null")) {
            rawString = rawString + "&" + bean.p8_charset;
        } else {
            rawString = rawString + "&";
        }
        if (bean.p9_signtype != null && !bean.p9_signtype.equals("")
                && !bean.p9_signtype.equals("null")) {
            rawString = rawString + "&" + bean.p9_signtype;
        } else {
            rawString = rawString + "&";
        }
        rawString = rawString + "&" + payWapSecret;
        return MD5Util.md5(rawString);
    }
    private String getUrlCS(RequestBean bean) {
        String khsbm = "100101";// 客户识别码 【快捷支付的时候需要传递此内容】 【 在您系统里对应用户的唯一标示】
        // String rawString = bean.p1_usercode + "&" + bean.p2_order + "&" +
        // bean.p3_money + "&" + bean.p4_returnurl + "&" + bean.p5_notifyurl +
        // "&" + bean.p6_ordertime + compKey;
        String rawString = "?p1_usercode=" + bean.p1_usercode + "&p2_order="
                + bean.p2_order + "&p3_money=" + bean.p3_money
                + "&p4_returnurl=" + bean.p4_returnurl + "&p5_notifyurl="
                + bean.p5_notifyurl + "&p6_ordertime=" + bean.p6_ordertime
                + "&p9_paymethod=" + bean.p9_paymethod + "&p14_customname="
                + bean.p14_customname + "&p10_paychannelnum="
                + bean.p10_paychannelnum + "&p17_customip=" + bean.p17_customip
                + "&p25_terminal=" + bean.p25_terminal + "&p26_iswappay="
                + bean.p26_iswappay + "&p7_sign=" + bean.getP7_sign();
		/*
		 * "&p19_productcat" + bean.p19_productcat + "&p20_productnum" +
		 * bean.p20_productnum + 卡类
		 */
        // return
        // FormsAuthentication.HashPasswordForStoringInConfigFile(rawString,
        // "MD5");
        return rawString;
    }
}
