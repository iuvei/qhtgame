DROP TABLE IF EXISTS `qht_pk10_handicap_bet`;
CREATE TABLE `qht_pk10_handicap_bet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `odd` varchar(32) NOT NULL COMMENT '单号',
  `period` varchar(32) NOT NULL COMMENT '期号',
  `runway` int(11) NOT NULL COMMENT '车道(1~10表示冠军道~第10道 11表示冠亚和)',
  `bettype` int(11) NOT NULL COMMENT '投注值(大/20 小/21 单/22 双/23 龙/24 虎/25 1~19中的一个)',
  `betamount` Decimal(11,2) NOT NULL COMMENT '投注金额',
  `status` int(11) NOT NULL COMMENT '状态 1_投注 2_结算 3_失败',
  `eventuate` blob NOT NULL COMMENT '返回结果',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`odd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10盘口压注表';


DROP TABLE IF EXISTS `qht_pk10_webinfo`;
CREATE TABLE `qht_pk10_webinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(32) NOT NULL COMMENT '站点名称',
  `url` varchar(255) NOT NULL COMMENT '网站url',
  `type` int(11) NOT NULL COMMENT '网站分类  1_皇冠  2_银河 3_永利',
  `username` varchar(64) NOT NULL COMMENT '用户名',
  `password` varchar(64) NOT NULL COMMENT '密码',
  `status` int(11) NOT NULL COMMENT '状态 1_使用 2_停用 3_登入',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10网站信息表';

INSERT INTO `qht_pk10_webinfo` (`id`, `name`, `url`, `type`, `username`, `password`, `status`, `updatetime`, `createtime`) VALUES ('1', 'hongguang_1', 'http://pkk7777.com', '1', 'chi900', 'as147258', '1', '1495006305099', '1490354950160');
INSERT INTO `qht_pk10_webinfo` (`id`, `name`, `url`, `type`, `username`, `password`, `status`, `updatetime`, `createtime`) VALUES ('2', 'yinhe_1', 'http://85771.tb66188.com/member/Main', '2', 'cs110', 'as258369', '1', '1495006305099', '1490354950160');



