package com.keke.sanshui.base.admin.dao.sys;

import com.keke.sanshui.base.admin.po.sys.query.QuerySysRole;
import com.keke.sanshui.base.admin.po.sys.query.QuerySysUser;
import com.keke.sanshui.base.admin.po.sys.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月14日 12:47
 **/
public interface SysRoleDAO {
    int insertSysRole(@Param("param")SysRole sysRole);

    List<SysRole> selectList(@Param("param")QuerySysRole querySysUser);

    Long selectCount(@Param("param")QuerySysUser querySysUser);
}
