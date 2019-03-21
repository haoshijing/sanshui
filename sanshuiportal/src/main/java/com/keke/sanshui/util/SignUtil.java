package com.keke.sanshui.util;

import com.google.common.collect.Maps;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.base.vo.PayVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhRequestVo;
import com.keke.sanshui.pay.easyjh.order.EasyJhResponseVo;
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

    public final static String createSign(String orderId, Integer gUid, String rechargeDiamond, String key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OrderId=").append(orderId).append("&");
        stringBuilder.append("Guid=").append(gUid);
        stringBuilder.append("&RechargeDiamond=").append(rechargeDiamond);
        stringBuilder.append(key);
        String data = MD5Util.md5(stringBuilder.toString()).toLowerCase();
        return data;
    }

    public static void main(String[] args) {

        String sign = SignUtil.createSign("761524837249474",
                76,
                "1",
                "fjsoafasdfj;asdfas;dafjasdafsafjasd");
        System.out.println("sign = [" + sign + "]");

    }

    public final static String createEasyJhSign(EasyJhRequestVo requestVo, String signKey) {
        Field[] fields = EasyJhRequestVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                String data = (String) ReflectionUtils.getField(field, requestVo);
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
        stringBuilder.append("&key=").append(signKey);
        String md5Sign = MD5Util.md5(stringBuilder.toString()).toUpperCase();
        return md5Sign;
    }

    public final static String createEasyJhResponseSign(EasyJhResponseVo responseVo, String signKey) {
        Field[] fields = EasyJhResponseVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!field.getName().equals("sign")) {
                field.setAccessible(true);
                String data = (String) ReflectionUtils.getField(field, responseVo);
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
        stringBuilder.append("&key=").append(signKey);
        String md5Sign = MD5Util.md5(stringBuilder.toString());
        return md5Sign;
    }
}

