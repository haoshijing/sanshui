package com.keke.sanshui.pay.huayue.order;

import com.keke.sanshui.pay.easyjh.BaseWithMapVo;
import lombok.Data;

import java.util.Map;

@Data
public class EastYOrderResponseVo extends BaseWithMapVo {
    private String url;
    private String merchant;
    private String orderId;
    private Integer code;

}
