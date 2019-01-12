--数据库初始化脚本

--创建数据库

create database seckill;

use seckill;

create table seckill(
seckill_id bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存Id',
name varchar(120) NOT NULL COMMENT '商品名称',
number int NOT NULL COMMENT '商品数量',
start_time timestamp NOT NULL COMMENT '秒杀开始时间',
end_time timestamp NOT NULL COMMENT '秒杀结束时间',
create_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
key idx_start_time(start_time),
key idx_end_time(end_time),
key idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='商品库存表';


--初始化数据
 insert into seckill(name,number,start_time,end_time) values
 ('1000元秒杀iphoneX',100,'2018-1-01 00:00:00','2018-1-02 00:00:00'),
  ('500元秒杀华为荣耀',200,'2018-1-01 00:00:00','2018-1-02 00:00:00'),
  ('300元秒杀小米6',300,'2018-1-01 00:00:00','2018-1-02 00:00:00'),
  ('200元秒杀vivo x21',400,'2018-1-01 00:00:00','2018-1-02 00:00:00');
 
 
 --秒杀成功明细表
 --用户登陆认证相关的信息
 create table success_killed(
 `seckill_id` bigint NOT NULL COMMENT '秒杀成功id',
 `user_phone` bigint NOT NULL COMMENT '用户手机号',
 `state`   tinyint NOT NULL DEFAULT -1 COMMENT '状态表示码:-1:无效  0:成功  1:已付款',
 `create_time` timestamp NOT NULL COMMENT '创建时间',
 PRIMARY KEY (seckill_id,user_phone),/* 联合主键*/
 key idx_create_time(create_time)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
 
 --登陆数据库
 mysql -uroot -proot