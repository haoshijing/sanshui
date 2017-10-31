package com.keke.sanshui.portal.controller;


import com.keke.sanshui.base.admin.po.PayLink;
import com.keke.sanshui.base.admin.service.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
public class PayLinkController {

    @Autowired
    private PayService payService;

    @RequestMapping("/add")
    @ResponseBody
    public int addLink(@RequestBody List<PayLink> payLink){
        log.info("payLink = {}",payLink);
        return payService.batchInsert(payLink);
    }
}
