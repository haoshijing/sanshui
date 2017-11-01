package com.keke.sanshui.pay.impl;

import com.keke.sanshui.pay.AbstractSignGenerator;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author haoshijing
 * @version 2017年11月01日 18:23
 **/
public class WeChartSignCreator extends AbstractSignGenerator {

    @Value("${weChartKey}")
    private String weChartKey;

    @Override
    protected String getConcatKey() {
        return "key="+weChartKey;
    }
}
