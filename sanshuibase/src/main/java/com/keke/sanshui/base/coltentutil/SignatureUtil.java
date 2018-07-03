package com.keke.sanshui.base.coltentutil;

import java.util.Map;
import java.util.TreeMap;

public class SignatureUtil {

	/**
	 * 商户请求数据签名<br>
	 * <br>
	 * 
	 * <font>参数按照字母顺序升顺排列</font>
	 * 
	 * @param respMap
	 *            请求参数
	 * @return
	 */
	public static String getSignatureStr(Map<String, String> respMap) {
		Map<String, String> map = new TreeMap<String, String>();
		map.putAll(respMap);
		StringBuffer signature = new StringBuffer();
		for (String key : map.keySet()) {
			if (!StringUtil.isEmpty(map.get(key)) && !"signature".equals(key)) {
				signature.append(key + "=" + map.get(key) + "&");
			}
		}
		return signature.toString().substring(0, signature.length() - 1);
	}

}
