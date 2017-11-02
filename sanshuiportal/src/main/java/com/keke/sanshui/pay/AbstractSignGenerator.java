package com.keke.sanshui.pay;

import com.google.common.collect.Maps;
import com.keke.sanshui.base.util.MD5Util;
import com.keke.sanshui.base.vo.PayVo;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.SortedMap;

/**
 * @author haoshijing
 * @version 2017年11月01日 18:14
 **/
public abstract class AbstractSignGenerator implements SignGenerator {

    @Override
    public String genSign(Object payVo) {
        return getSign(payVo, true);
    }

    public String getSign(Object payVo, boolean notEmptyExclude) {
        Field[] fields = PayVo.class.getDeclaredFields();
        SortedMap<String, String> sortedMap = Maps.newTreeMap();
        for (Field field : fields) {
            if (!"sign".equals(field.getName())) {
                field.setAccessible(true);
                String data = (String) ReflectionUtils.getField(field, payVo);
                boolean needAdd = true;
                if (StringUtils.isEmpty(data) && notEmptyExclude) {
                    needAdd = false;
                }
                if (needAdd) {
                    sortedMap.put(field.getName(), data);
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
            if (stringBuilder.toString().length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        stringBuilder.append(getConcatKey());
        String md5Sign = MD5Util.md5(stringBuilder.toString());
        return md5Sign;
    }

    /**
     * 获得支付秘钥
     *
     * @return
     */
    protected abstract String getConcatKey();
}
