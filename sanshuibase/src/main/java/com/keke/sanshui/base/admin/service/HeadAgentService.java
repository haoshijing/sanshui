package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.HeadAgentDAO;
import com.keke.sanshui.base.admin.po.HeadAgentPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HeadAgentService {

    @Autowired
    HeadAgentDAO headAgentDAO;

   public List<HeadAgentPo> selectList(){

        return headAgentDAO.selectList();
    }
}
