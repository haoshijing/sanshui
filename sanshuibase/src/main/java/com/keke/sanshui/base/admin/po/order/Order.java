package com.keke.sanshui.base.admin.po.order;

import lombok.Data;

@Data
public class Order {
    private Integer id;
    /**
     * 平台传过来来的订单号
     */
    private String orderNo;
    /**
     * 自己系统生成的订单号
     */
    private String selfOrderNo;
    /**
     * 客户的guid号
     */
    private Integer clientGuid;
    /**
     * 客户的支付方式
     */
    private String payType;
    /**
     * 商品名称
     */
    private String title;
    /**
     * 商品价格
     */
    private String money;
    /**
     * 商家的人民币价格
     */
    private String price;
    /**
     * 支付状态
     */
    private Integer payState;
    /**
     * 平台支付时间
     */
    private String payTime;
    /**
     * 数据写入时间
     */
    private Long insertTime;
    /**
     * 发送给游戏服务器的状态
     */
    private Integer sendStatus;
    /**
     *最后修改时间
     */
    private Long lastUpdateTime;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 发送给游戏服务器的时间
     */
    private Long sendTime;
}
