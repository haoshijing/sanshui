package com.keke.sanshui;

import com.keke.sanshui.base.util.MD5Tool;
import com.keke.sanshui.pay.paywap.RequestBean;

/**
 * @author haoshijing
 * @version 2018年06月02日 20:17
 **/
public class Main {
    public static void main(String[] args) {
        RequestBean requestBean = new RequestBean();
        requestBean.setP1_usercode("5010207509");
        requestBean.setP2_order("12");
        requestBean.setP3_money("100");
        requestBean.setP4_returnurl("12");
        requestBean.setP5_notifyurl("12");
        requestBean.setP6_ordertime("12");

        String rawString = requestBean.p1_usercode + "&" + requestBean.p2_order + "&"
                + requestBean.p3_money + "&" + requestBean.p4_returnurl + "&"
                + requestBean.p5_notifyurl + "&" + requestBean.p6_ordertime + "EECA1497F7C97D47DB4FDFAC51F9B190";
        // return
        // FormsAuthentication.HashPasswordForStoringInConfigFile(rawString,
        // "MD5");
        String data = MD5Tool.encoding(rawString);
        System.out.println("args = [" + data + "]");

    }
}
