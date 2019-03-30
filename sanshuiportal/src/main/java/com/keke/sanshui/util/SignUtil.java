package com.keke.sanshui.util;

import com.google.common.collect.Maps;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.base.vo.PayVo;
import com.keke.sanshui.pay.fuqianla.FuqianResponseVo;
import com.keke.sanshui.pay.zpay.ZPayQueryRequestVO;
import com.keke.sanshui.pay.zpay.ZPayRequestVo;
import com.keke.sanshui.pay.zpay.ZPayResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

@Slf4j
public class SignUtil {

    public final static boolean match(PayVo payVo, String signKey) {
        boolean match = false;
        String md5Sign = createPaySign(payVo, signKey);
        match = md5Sign.equals(payVo.getSign());
        return match;
    }

    public final static Pair<String, String> createZPayRequestSign(ZPayRequestVo zPayRequestVo, String signKey) {
        Field[] fields = ZPayRequestVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                Class<?> c = field.getType();
                String data = String.valueOf(ReflectionUtils.getField(field, zPayRequestVo));
                if (StringUtils.isEmpty(data)) {
                    continue;
                }
                sortedMap.put(field.getName(), data);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        String first = stringBuilder.toString();
        stringBuilder.append("&key=").append(signKey);
        String md5Sign = MD5Util.md5(stringBuilder.toString()).toUpperCase();
        String paramUrl = new StringBuilder(first).append("&sign=").append(md5Sign).toString();
        return Pair.of(md5Sign, paramUrl);
    }

    public final static String createZPayResponseSign(ZPayResponseVo zPayResponseVo, String signKey) {
        Field[] fields = ZPayResponseVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                String data = String.valueOf(ReflectionUtils.getField(field, zPayResponseVo));
                if (StringUtils.isEmpty(data)) {
                    continue;
                }
                sortedMap.put(field.getName(), data);
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

    public final static String createFullqianResponseSign(FuqianResponseVo fuqianResponseVo, String signKey) {
        Field[] fields = ZPayResponseVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!(field.getName().equals("sign_info") || field.getName().equals("sign_type"))) {
                field.setAccessible(true);
                String data = String.valueOf(ReflectionUtils.getField(field, fuqianResponseVo));
                if (StringUtils.isEmpty(data)) {
                    continue;
                }
                sortedMap.put(field.getName(), data);
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

    public final static String createZPayQuerySign(ZPayQueryRequestVO zPayQueryRequestVO, String signKey) {
        Field[] fields = ZPayQueryRequestVO.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                String data = String.valueOf(ReflectionUtils.getField(field, zPayQueryRequestVO));
                if (StringUtils.isEmpty(data)) {
                    continue;
                }
                sortedMap.put(field.getName(), data);
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


    public final static String createPaySign(PayVo payVo, String signKey) {
        Field[] fields = PayVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                String data = (String) ReflectionUtils.getField(field, payVo);
                if (StringUtils.isEmpty(data)) {
                    data = "";
                }
                sortedMap.put(field.getName(), data);
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        stringBuilder.append("&pkey=").append(signKey);
        String md5Sign = MD5Util.md5(stringBuilder.toString());
        return md5Sign;
    }

    public final static String createSign(String orderId, Integer gUid, String payMoney,String card,String more, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OrderId=").append(orderId).append("&");
        stringBuilder.append("Guid=").append(gUid);
        stringBuilder.append("&Money=").append(payMoney);
        stringBuilder.append("&Card=").append(card);
        stringBuilder.append("&More=").append(more);
        stringBuilder.append(key);
        String md5Str = stringBuilder.toString();
        log.info("md5Str = {}", md5Str);
        String data = MD5Util.md5(md5Str).toUpperCase();
        return data;
    }


    public final static String createSign(Map<String, String> data, String signKey) {
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (!StringUtils.endsWithIgnoreCase("sign", entry.getKey())) {
                sortedMap.put(entry.getKey(), entry.getValue());
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

