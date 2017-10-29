package com.keke.sanshui.dao;

import com.keke.sanshui.po.PayLink;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PayLinkDAO {
    int insert(@Param("link")PayLink payLink);

    int batchInsert(List<PayLink> payLinks);

    PayLink getById(Integer id);

    List<PayLink> selectAll();
}
