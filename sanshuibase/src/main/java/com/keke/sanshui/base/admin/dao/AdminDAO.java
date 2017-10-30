package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.AdminPo;

public interface AdminDAO {
    AdminPo selectByUsername(String userName);
}
