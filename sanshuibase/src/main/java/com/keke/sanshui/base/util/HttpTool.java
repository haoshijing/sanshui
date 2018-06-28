package com.keke.sanshui.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.SimpleHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author albert
 * @package com.xianduankeji.ktv.base.util
 * @email cn.lu.duke@gmail.com
 * @date January 10, 2018
 */

@Slf4j
public class HttpTool {

    /**
     * url 过滤
     * @param url
     * @return
     */
    public static String post(String url){
        return post(url, new HashMap<>(), "");
    }

    /**
     *
     * @param baseurl
     * @param params
     * @param ua
     * @return
     */
    public static String post(String baseurl, Map<String, Object> params, String ua) {
        return post(baseurl, params, ua, 2000, 2000);
    }

    /**
     * 发起请求
     * @param baseurl
     * @param params
     * @param ua
     * @param connTimeout
     * @param dataTimeout
     * @return resposeBody
     */
    public static String post(String baseurl, Map<String, Object> params, String ua,
                              int connTimeout, int dataTimeout) {
        HttpClient client = new HttpClient(new SimpleHttpConnectionManager(true));
        //ua 不为空的时候设置ua
        if (!StringUtils.isEmpty(ua)) {
            client.getParams().setParameter("http.useragent", ua);
        }
        //设置timeout
        client.setConnectionTimeout(connTimeout);
        client.setTimeout(dataTimeout);
        PostMethod post = new PostMethod(baseurl);
        List pairs = null;

        String execCode;
        try {
            pairs = getNameValuePairs(params);
            NameValuePair[] requestParams = new NameValuePair[pairs.size()];
            pairs.toArray(requestParams);
            post.addRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
            post.setRequestHeader("Connection", "close");
            post.setRequestBody(requestParams);
            int code = client.executeMethod(post);
            if (code != 200) {
                execCode = null;
                return execCode;
            }

            execCode = post.getResponseBodyAsString();
        } catch (Exception e) {
            log.error("", e);
            return null;
        } finally {
            closeClient(client, post);
        }

        return execCode;
    }


    /**
     * 关闭HTTP连接
     * @param client
     * @param post
     */
    private static void closeClient(HttpClient client, PostMethod post) {
        try {
            post.releaseConnection();
        } catch (Exception varClose) {
            log.error("", varClose);
        }
    }

    /**
     * 键值对list
     * @param paramsMap
     * @return
     * @throws UnsupportedEncodingException
     */
    private static List<NameValuePair> getNameValuePairs(Map<String, Object> paramsMap){
        List<NameValuePair> pairs = new ArrayList();
        Iterator i$ = paramsMap.keySet().iterator();

        while(i$.hasNext()) {
            String key = (String)i$.next();
            Object value = paramsMap.get(key);
            if (null != value) {
                NameValuePair nameValuePair = new NameValuePair();
                nameValuePair.setName(key);
                nameValuePair.setValue(value.toString());
                pairs.add(nameValuePair);
            }
        }
        return pairs;
    }

}
