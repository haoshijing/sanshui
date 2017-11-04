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
drop TABLE IF EXISTS t_admin;
create table t_admin
(
id int primary key auto_increment comment '主键id',
userName varchar(200) comment '用户名',
password varchar(200) comment '密码',
saltPassword varchar(200) comment '加密盐值',
insertTime bigint comment '写入时间',
lastUpdateTime bigint comment '最后修改时间',
status int comment '账号状态'
) comment '管理员表';
drop TABLE IF EXISTS t_oper_log;
create table t_oper_log
(
id int primary key auto_increment comment '主键id',
operType int comment '操作类型',
operTarget int comment '操作对象',
mark varchar(200) comment '操作信息',
operUserId int comment '操作人',
insertTime bigint comment '写入时间'
) comment '操作日志表';
drop TABLE IF EXISTS t_agent;
create table t_agent(
id int primary key auto_increment comment '主键id',
playerId int comment '游戏账号id',
agentName varchar(100) comment '代理名称',
password varchar(64) comment '登陆密码',
agentWeChartNo varchar(100) comment '代理微信号',
agentNickName varchar(100) comment '昵称',
level int comment '代理级别 1 - 地区 2-群主',
parentId int comment '上级代理',
status int comment '账号状态1-激活2-封禁',
memo varchar(512) comment '说明',
insertTime bigint comment '写入时间',
lastUpdateTime bigint comment '最后修改时间'
 ) comment '代理表';

drop TABLE IF EXISTS t_player_coupon;
create table t_player_coupon(
id int primary key auto_increment comment '主键id',
playerId int comment '游戏id',
goldCount int comment '金豆数量',
 silverCount int comment '银豆数量',
 lastUpdateTime bigint comment '最后同步时间'
) comment '豆值剩余表,该表数据通过cannal进行同步';

drop TABLE IF EXISTS t_player_pick_total;
create table t_player_pick_total(
 id  int primary key auto_increment comment '主键id',
 playerId int  comment '玩家id',
 week int comment '所在周',
 totalMoney bigint comment '充值总额',
lastUpdateTime bigint comment'最后更新时间'
) ;
alter table t_player_pick_total add unique index player_week_idx(playerId,week);

drop TABLE IF EXISTS t_agent_total;
create table t_agent_total(
id  int primary key auto_increment comment '主键id',
agentId int comment '代理id',
 week int comment '所在周',
 playerTotal bigint comment '下属总充值'
);
alter table t_agent_total add unique index agentId_week_idx(agentId,week);
/**
会员关系表,由游戏服务器同步
 */
drop TABLE IF EXISTS  t_player_relation;
CREATE  TABLE  t_player_relation(
id  int primary key auto_increment comment '主键id',
playerId int comment '玩家游戏id',
 agentPlayerId int comment '代理id',
 lastUpdateTime bigint comment '最后的更新时间'
);
alter table t_player_relation add  index pid_idx(playerId);

drop TABLE IF EXISTS  t_player;
create table t_player(
id  int primary key auto_increment comment '主键id',
playerId int comment '玩家游戏id',
openId varchar(255) comment '玩家游戏id',
insertTime bigint comment '写入到数据库的时间',
lastUpdateTime bigint comment '最后修改时间',
status int comment '玩家状态'
);
alter table t_player add  index pid_idx(playerId);
alter table t_player add unique index player_player_openid(playerId,openId);