package com.keke.sanshui.pay;

/**
 * @author haoshijing签名生成器
 * @version 2017年11月01日 17:52
 **/
public interface SignGenerator {

    /**
     * 生成签名
     * @param payVo
     * @return
     */
    String genSign(Object payVo);
}
