package com.keke.sanshui.pay.paywap;

import com.keke.sanshui.base.util.MD5Tool;

public class Test {
    public static void main(String[] args) {
        String str = "5010207509&1001527957033724&800&http://allin.hjgame.vip:8080/allin/paywap/return/1001527957033724&http://allin.hjgame.vip:8080/allin/paywap/callback&1527957033724EECA1497F7C97D47DB4FDFAC51F9B190";
        System.out.println("args = [" + MD5Tool.encoding(str) + "]");
    }
}
