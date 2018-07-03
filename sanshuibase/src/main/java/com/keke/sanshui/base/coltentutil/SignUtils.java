package com.keke.sanshui.base.coltentutil;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;

public class SignUtils {

    public static String signData(List<BasicNameValuePair> nvps) throws Exception {
        TreeMap<String, String> tempMap = new TreeMap<String, String>();
        for (BasicNameValuePair pair : nvps) {
            if (StringUtils.isNotBlank(pair.getValue())) {
                tempMap.put(pair.getName(), pair.getValue());
            }
        }
        StringBuffer buf = new StringBuffer();
        for (String key : tempMap.keySet()) {
            buf.append(key).append("=").append((String) tempMap.get(key)).append("&");
        }
        String signatureStr = buf.substring(0, buf.length() - 1);
        String signData = RSAUtil.signByPrivate(signatureStr, RSAUtil.readFile(ConfigUtils.getProperty("private_key_path"), "UTF-8"), "UTF-8");
        System.out.println("请求数据：" + signatureStr + "&signature=" + signData);
        return signData;
    }
    
    /**
     * 签名
     * @param paramMap
     * @return
     * @throws Exception
     */
    public static String signData(Map<String,String> paramMap) throws Exception {
    	String signatureStr = SignatureUtil.getSignatureStr(paramMap);
    	String signData = RSAUtil.signByPrivate(signatureStr, RSAUtil.readFile(ConfigUtils.getProperty("private_key_path"), "UTF-8"), "UTF-8");
    	System.out.println("请求数据：" + signatureStr + "&signature=" + signData);
    	return signData;
    }
    /**
     * 验签
     * @param str
     * @return
     */
    public static boolean verferSignData(String str) {
        TreeMap<String, String> tempMap = new TreeMap<String, String>();
        Map mapTypes = JSON.parseObject(str);  
        for (Object obj : mapTypes.keySet()){  
            if((!obj.toString().equals("signature")) && !mapTypes.get(obj).toString().equals("")){
            	tempMap.put(obj.toString(), mapTypes.get(obj).toString());
            }
        }  
        StringBuffer buf = new StringBuffer();
        for (String key : tempMap.keySet()) {
            buf.append(key).append("=").append((String) tempMap.get(key)).append("&");
        }
        String signatureStr = buf.substring(0, buf.length() - 1);
        String signature=mapTypes.get("signature").toString();
        return RSAUtil.verifyByKeyPath(signatureStr, signature, ConfigUtils.getProperty("public_key_path"), "UTF-8");
    }
    
}
