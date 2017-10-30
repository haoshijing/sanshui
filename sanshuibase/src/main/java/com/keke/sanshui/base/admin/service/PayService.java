package com.keke.sanshui.base.admin.service;

import com.google.common.collect.Lists;

import com.keke.sanshui.base.admin.dao.PayLinkDAO;
import com.keke.sanshui.base.admin.po.PayLink;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayService {

    @Autowired
    private PayLinkDAO payLinkDAO;

    public PayLink getCid(Integer id){
        return payLinkDAO.getById(id);
    }


    public int insertLink(PayLink payLink){
        return payLinkDAO.batchInsert(Lists.newArrayList(payLink));
    }

    public List<PayLink> queryAllLink(){
        return payLinkDAO.selectAll();
    }

    public int batchInsert(List<PayLink> payLink) {
        return payLinkDAO.batchInsert(payLink);
    }
}
