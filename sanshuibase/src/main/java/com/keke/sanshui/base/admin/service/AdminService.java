package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.AdminDAO;
import com.keke.sanshui.base.admin.po.AdminPo;
import com.keke.sanshui.base.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

@Repository
public class AdminService {

    @Value("${saltEncrypt}")
    private String saltEncrypt;
    @Autowired
    private AdminDAO adminDAO;

    public boolean checkUser(String userName,String password){
        AdminPo adminPo = adminDAO.selectByUsername(userName);
        if(adminPo != null){
            String dbPassword = adminPo.getPassword();
            String userPassword = MD5Util.md5(MD5Util.md5(password)+saltEncrypt);
            return StringUtils.equals(dbPassword,userPassword);
        }
        return false;
    }
}
