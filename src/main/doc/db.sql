DROP TABLE IF EXISTS pay_link;
create table pay_link (
id int primary key auto_increment comment '主键id',
cIdNo varchar(500) comment '二维支付链接地址',
pickCouponVal int comment '支付豆',
pickRmb int comment '支付人民币值,单位分'
);

drop TABLE IF EXISTS t_order;
create table t_order(
  id int primary key auto_increment comment '自动生成的主键id',
  orderNo varchar(255) comment '订单号',
  selfOrderNo varchar(255) comment '自己系统的订单号',
  clientGuid int comment '客户的guid号',
  payType varchar(15) comment '支付方式',
  title varchar(200) comment '商品名称',
  money varchar(200) comment '成交金额',
  price varchar(20) comment '商品单价',
  payState int comment '成交状态',
  payTime varchar(30) comment '支付时间',
  insertTime bigint comment '数据写入时间',
  sendStatus int comment '发送给服务器的状态',
  orderStatus int comment '订单状态',
  sendTime bigint comment '发送给服务器成功的时间',
  lastUpdateTime bigint comment '发送给服务器成功的时间',
   unique (selfOrderNo)
);
