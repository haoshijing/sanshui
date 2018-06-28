package com.keke.sanshui.pay.paywap.v3;

import lombok.Data;

@Data
public class RequestBean {
    String p1_yingyongnum = "";

    //商户订单号
    String p2_ordernumber = "";
    String p3_money = "";
    String p14_customname = "";
    String p25_terminal = "";
    String p7_productcode = "";

    //商户订单时间
    String p6_ordertime = "";
    String p10_bank_card_code = "";


    //订单签名

    String p8_sign="";
    //签名方式
    String p9_signtype = "1";

    //商户支付银行卡类型id
    String p11_cardtype;
    //商户支付银行卡类型长度
    String p12_channel;
    //订单失效时间
    String p13_orderfailertime;

    //商户联系内容
    String p15_customcontact;
    //付款ip地址
    String p16_customip = "192_168_0_253";
    //商户名称
    String p17_product = "xxx";
    //商品种类
    String p18_productcat;
    //商品数量
    String p19_productnum;
    //商品描述
    String p20_pdesc;
    //对接版本
    String p21_version;
    //sdk版本
    String p22_sdkversion;
    //编码格式
    String p23_charset = "UTF-8";
    //备注
    String p24_remark;

}
