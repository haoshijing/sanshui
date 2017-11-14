package com.keke.sanshui.base.admin.dao.sys;

import com.keke.sanshui.base.admin.po.sys.query.QuerySysUser;
import com.keke.sanshui.base.admin.po.sys.SysUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月14日 12:44
 **/
public interface SysUserDAO {
    int insertSysUser(@Param("param")SysUser sysUser);

    List<SysUser> selectList(@Param("param")QuerySysUser querySysUser);

    Long selectCount(@Param("param")QuerySysUser querySysUser);
}
