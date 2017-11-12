package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.AdminDAO;
import com.keke.sanshui.base.admin.event.OperLogEvent;
import com.keke.sanshui.base.admin.po.AdminPo;
import com.keke.sanshui.base.admin.po.OperLogPo;
import com.keke.sanshui.base.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Repository;

@Repository
public class AdminService implements ApplicationContextAware {

    @Value("${saltEncrypt}")
    private String saltEncrypt;
    @Autowired
    private AdminDAO adminDAO;

    private ApplicationContext ctx;

    public boolean checkUser(String userName,String password,String clientIp){
        OperLogPo operLogPo = new OperLogPo();
        operLogPo.setInsertTime(System.currentTimeMillis());
        operLogPo.setOperType(4);
        operLogPo.setOperTarget(1);
        Boolean checkRet = false;
        AdminPo adminPo = adminDAO.selectByUsername(userName);
        if(adminPo != null){
            String dbPassword = adminPo.getPassword();
            String userPassword = MD5Util.md5(MD5Util.md5(password)+saltEncrypt);
            checkRet =  StringUtils.equals(dbPassword,userPassword);
        }
        String loginRet =  checkRet?"成功":"失败";
        operLogPo.setMark("管理员进行登录,登录ip:"+clientIp+"登陆结果:"+loginRet);
        ctx.publishEvent(new OperLogEvent(ctx,operLogPo));
        return checkRet;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ctx = applicationContext;
    }
}
