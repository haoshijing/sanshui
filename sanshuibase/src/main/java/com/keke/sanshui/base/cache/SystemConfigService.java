package com.keke.sanshui.base.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.keke.sanshui.base.admin.dao.ConfigDAO;
import com.keke.sanshui.base.admin.po.SystemConfigPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author haoshijing
 * @version 2018年06月08日 13:41
 **/

@Repository
public class SystemConfigService {

    @Autowired
    private ConfigDAO configMapper;

    private LoadingCache<String,String> cache = CacheBuilder.newBuilder().
            expireAfterWrite(60, TimeUnit.SECONDS).build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            SystemConfigPo configPo =  configMapper.selectByKey(key);
            if(configPo != null){
                return configPo.getConfigValue();
            }
            return "";
        }
    });
    public String getConfigValue(String key){
       return getConfigValue(key,"");
    }
    public String getConfigValue(String key,String defaultValue){
        String data =  cache.getIfPresent(key);
        if(StringUtils.isNotEmpty(data)){
            return data;
        }
        SystemConfigPo po = configMapper.selectByKey(key);
        if(null != po){
            cache.put(key,po.getConfigValue());
            return po.getConfigValue();
        }
        return defaultValue;
    }

    /**
     * 根据key值获取po
     * @param configKeys key值数组
     * @return
     */
    public List<SystemConfigPo> getConfigs(String[] configKeys){
        List<SystemConfigPo> list = new ArrayList<>();
        for(String key : configKeys){
            list.add(configMapper.selectByKey(key));
        }
        return  list;
    }

    public List<SystemConfigPo> selectByTypeAndStatus(Integer type,Integer status){
        SystemConfigPo po = new SystemConfigPo();
        po.setStatus(status);
        po.setType(type);
        List<SystemConfigPo> list = configMapper.selectByTypeAndStatus(po);
        return  list;
    }

    public SystemConfigPo selectByKey(String key){
            return configMapper.selectByKey(key);
    }
}
