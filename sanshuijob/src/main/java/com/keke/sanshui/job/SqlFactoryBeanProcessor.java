package com.keke.sanshui.job;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;

/**
 * @author haoshijing
 * @version 2017年11月14日 14:48
 **/

@Repository
public class SqlFactoryBeanProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof SqlSessionFactoryBean){
            SqlSessionFactoryBean factoryBean = (SqlSessionFactoryBean)bean;
            factoryBean.setVfs(SpringBootVFS.class);

        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
}
