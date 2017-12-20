package com.keke.sanshui.pay.wechart;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.pay.zpay.ZPayResponseVo;
import com.keke.sanshui.util.IpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.UUID;

@Repository
public class WechartPayService {

    @Value("${wxAppId}")
    private String appId;

    @Value("${wxPayBizId}")
    private String mchId;

    @Value("${wxPaySecret}")
    private String signKey;

    @Value("${callbackHost}")
    private String callbackHost;

    public Map<String,String> createPreOrderVo(HttpServletRequest httpServletRequest ,PayLink payLink, String selfOrderId){
        WeChartPreOrderVo weChartPreOrderVo = new WeChartPreOrderVo();
        weChartPreOrderVo.setAppId(appId);
        weChartPreOrderVo.setMch_id(mchId);
        weChartPreOrderVo.setDevice_info("WEB");
        weChartPreOrderVo.setNonce_str(UUID.randomUUID().toString().replace("-",""));
        weChartPreOrderVo.setSign_type("MD5");
        weChartPreOrderVo.setBody("虚拟-"+payLink.getPickCouponVal()+"豆");
        weChartPreOrderVo.setOut_trade_no(selfOrderId);
        weChartPreOrderVo.setTotal_fee(payLink.getPickRmb());
        weChartPreOrderVo.setSpbill_create_ip(IpUtils.getIpAddr(httpServletRequest));
        try{
            weChartPreOrderVo.setNotify_url(new StringBuilder(callbackHost).append("/wechart/callback").toString());
        }catch (Exception e){

        }
        weChartPreOrderVo.setTrade_type("MWEB");
        Map<String,String> info = Maps.newHashMap();
        Map<String,String> data = Maps.newHashMap();
        data.put("type","wap");
        data.put("wap_name","十三水");
        info.put("h5_info", JSON.toJSONString(data));
        weChartPreOrderVo.setScene_info(JSON.toJSONString(info));

        Map<String,String> datas = Maps.newHashMap();
        datas.put("body",weChartPreOrderVo.getBody());
        datas.put("out_trade_no",weChartPreOrderVo.getOut_trade_no());
        datas.put("scene_info",weChartPreOrderVo.getScene_info());
        datas.put("trade_type",weChartPreOrderVo.getTrade_type());
        datas.put("total_fee",weChartPreOrderVo.getTotal_fee().toString());
        datas.put("spbill_create_ip",weChartPreOrderVo.getSpbill_create_ip());
        datas.put("sign_type",weChartPreOrderVo.getSign_type());
        datas.put("nonce_str", weChartPreOrderVo.getNonce_str());
        datas.put("notify_url", weChartPreOrderVo.getNotify_url());
        datas.put("device_info", weChartPreOrderVo.getDevice_info());
        return datas;
    }

    public String getReturnUrl(String orderId){
        try {
            String url =  new StringBuilder(callbackHost).append("/wxpay/user/").append(orderId).toString();
             url = URLEncoder.encode(url,"utf-8");
             return url;
        }catch (Exception e){
            return "";
        }
    }
    String createWxSign(WeChartPreOrderVo weChartPreOrderVo){
        Field[] fields = ZPayResponseVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!(field.getName().equals("sign_info")  || field.getName().equals("sign_type") )) {
                field.setAccessible(true);
                String data = String.valueOf(ReflectionUtils.getField(field,weChartPreOrderVo));
                if(StringUtils.isEmpty(data)) {
                    continue;
                }
                sortedMap.put(field.getName(),data);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        stringBuilder.append("&key=").append(signKey);
        String md5Sign = MD5Util.md5(stringBuilder.toString()).toUpperCase();
        return md5Sign;
    }
}
