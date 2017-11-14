package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.AdminPo;
import org.apache.ibatis.annotations.Param;

public interface AdminDAO {
    AdminPo selectByUsername(String userName);

    void updatePwd(@Param("newPwd") String newPwd,@Param("userName") String userName);
}
