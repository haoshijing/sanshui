package com.keke.sanshui.base.admin.po;

import lombok.Data;

@Data
public class PayLink {
    /**
     * 主键id
     */
    private Integer id;
    /**
     * 二维码编号
     */
    private String cIdNo;
    /**
     * 充值金币
     */
    private Integer pickCouponVal;

    /**
     * 额外赠送金币
     */
    private Integer moreCouponVal;

    /**
     * 充值人民币值
     */
    private Integer pickRmb;
}
