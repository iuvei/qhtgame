DROP TABLE IF EXISTS `qht_system`;
CREATE TABLE `qht_system` (
  `id` int(11) NOT NULL COMMENT 'id',
  `data` varchar(128) NOT NULL COMMENT 'data',
  `comment` varchar(128) NOT NULL COMMENT '说明',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局系统表';

INSERT INTO `qht_system`(`id`,`data`,`comment`) VALUES (1,'1490354950160','报表生成截止日期时间戮');
INSERT INTO `qht_system` (`id`, `data`, `comment`) VALUES (2, '0', 'pk10在线机器人人数');
INSERT INTO `qht_system` (`id`, `data`, `comment`) VALUES (3, 'http://localhost:8180/handicap/main/handicap.do', '盘口URL');


DROP TABLE IF EXISTS `qht_admin`;
CREATE TABLE `qht_admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `type` int(11) NOT NULL COMMENT '类型  1_超级账号 2_代理账号 3_App账号',
  `status` int(11) NOT NULL COMMENT '状态 1_正常 2_禁用',
  `jurisdiction` varchar(255) NOT NULL COMMENT '权限栏',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='后台管理员信息表';

INSERT INTO `qht_admin` (`id`, `username`, `password`, `nickname`, `type`, `status`, `jurisdiction`, `createtime`, `updatetime`) VALUES ('1', 'admin', '123456', 'admin', '1', '1', '0', '1490513317079', '1490592769766');



DROP TABLE IF EXISTS `qht_player`;
CREATE TABLE `qht_player` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `loginname` varchar(32) NOT NULL COMMENT '登入名',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `password` varchar(128) NOT NULL COMMENT '密码',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `status` int(11) NOT NULL COMMENT '状态 1_正常 2_禁用',
  `typeid` int(11) NOT NULL COMMENT '类别 1_真实 2_虚拟',
  `balance` Decimal(11,2) NOT NULL COMMENT '余额',
  `frozen_bal` Decimal(11,2) NOT NULL COMMENT '冻结资金',
  `integral` Decimal(11,2) NOT NULL COMMENT '积分',
  `qq` char(16) NOT NULL COMMENT 'qq号',
  `weixin` varchar(32) NOT NULL COMMENT '微信号',
  `telephone` char(16) NOT NULL COMMENT '手机号',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`loginname`),
  UNIQUE KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='玩家信息表';



DROP TABLE IF EXISTS `qht_lottery`;
CREATE TABLE `qht_lottery` (
  `id` int(11) NOT NULL COMMENT '自增id',
  `name` varchar(32) NOT NULL COMMENT '彩种名称',
  `shutname` varchar(32) NOT NULL COMMENT '彩种简称',
  `opentime` bigint(20) NOT NULL COMMENT '开盘间隔时间',
  `spacetime` bigint(20) NOT NULL COMMENT '封盘间隔时间',
  `notbettime` bigint(20) NOT NULL COMMENT '停止撤单间隔时间',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  `rule` blob NOT NULL COMMENT '游戏规则',
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='彩种信息表';

INSERT INTO `qht_lottery` (`id`, `name`, `shutname`, `opentime`, `spacetime`, `notbettime`, `createtime`, `updatetime`, `rule`) VALUES ('1', 'pk10', '北京赛车', '270000', '50000', '120000', '1491012767000', '1495592637292', 'sbG+qXBrMTDTzs+3uebU8qO6CgkJCcqxyrGyygoJCQkxMdGhNQoJCQkzRC9QMy/KscqxwNYKCQkJUEsxMAoJCQm/7DgKCQkJzuW31rLKCrnYsdUKssK52r78ICAgICBb17TMrKO6v6rG9F0KssK52r78CdfuuN+9sb3wo7oyMC4wMAnX7rXNvbG98KO6MTcuMDAJ1+6439eiyv2jujEwCcXF0PKjujEJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKudix1Qqywrna0ce+/CAgICAgW9e0zKyjur+qxvRdCrLCudrRx778CdfuuN+9sb3wo7oxMDIuMDAJ1+61zb2xvfCjujg4LjAwCdfuuN/Xosr9o7o5MAnFxdDyo7oyCb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCrnYsdUKssLHsMj9w/sgICAgIFvXtMyso7q/qsb0XQqywsewyP3D+wnX7rjfvbG98KO6MzA4LjAwCdfutc29sb3wo7oyNTYuMDAJ1+6439eiyv2jujcyMAnFxdDyo7ozCb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCrnYsdUKtqjOu7Wo0aEgICAgIFvXtMyso7q/qsb0XQq2qM67tajRoQnX7rjfvbG98KO6MjAuMDAJ1+61zb2xvfCjujE3LjAwCdfuuN/Xosr9o7o1MAnFxdDyo7o0Cb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCrnYsdUKwb3D5iAgICAgW9e0zKyjur+qxvRdCrnavvwJ1+64372xvfCjujMuMDAJ1+61zb2xvfCjujMuMDAJ1+6439eiyv2jujAJxcXQ8qO6MQm/qsb0CbnYsdUgfCCxo7Tm0N64xCB8INDeuMTQxc+iCgrRx778CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujIJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKvL6+/AnX7rjfvbG98KO6MC4wMAnX7rXNvbG98KO6MC4wMAnX7rjf16LK/aO6MAnFxdDyo7ozCb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCsvEw/sJ1+64372xvfCjujAuMDAJ1+61zb2xvfCjujAuMDAJ1+6439eiyv2jujAJxcXQ8qO6NAm/qsb0CbnYsdUgfCCxo7Tm0N64xCB8INDeuMTQxc+iCgrO5cP7CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujUJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKwfnD+wnX7rjfvbG98KO6MC4wMAnX7rXNvbG98KO6MC4wMAnX7rjf16LK/aO6MAnFxdDyo7o2Cb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCsbfw/sJ1+64372xvfCjujAuMDAJ1+61zb2xvfCjujAuMDAJ1+6439eiyv2jujAJxcXQ8qO6Nwm/qsb0CbnYsdUgfCCxo7Tm0N64xCB8INDeuMTQxc+iCgqwy8P7CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujgJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKvsXD+wnX7rjfvbG98KO6MC4wMAnX7rXNvbG98KO6MC4wMAnX7rjf16LK/aO6MAnFxdDyo7o5Cb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCsquw/sJ1+64372xvfCjujAuMDAJ1+61zb2xvfCjujAuMDAJ1+6439eiyv2jujAJxcXQ8qO6MTAJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKudrRxwnX7rjfvbG98KO6MC4wMAnX7rXNvbG98KO6MC4wMAnX7rjf16LK/aO6MAnFxdDyo7oxMQm/qsb0CbnYsdUgfCCxo7Tm0N64xCB8INDeuMTQxc+iCgq52tHHvL4J1+64372xvfCjujAuMDAJ1+61zb2xvfCjujAuMDAJ1+6439eiyv2jujAJxcXQ8qO6MTIJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKudix1QrB+ruiICAgICBb17TMrKO6v6rG9F0Kudq+/AnX7rjfvbG98KO6MC4wMAnX7rXNvbG98KO6MC4wMAnX7rjf16LK/aO6MAnFxdDyo7oxCb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCtHHvvwJ1+64372xvfCjujAuMDAJ1+61zb2xvfCjujAuMDAJ1+6439eiyv2jujAJxcXQ8qO6Mgm/qsb0CbnYsdUgfCCxo7Tm0N64xCB8INDeuMTQxc+iCgq8vr78CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujMJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKtdrLxMP7CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujQJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKtdrO5cP7CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujUJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKudrRx778CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujYJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKudrRx7y+vvwJ1+64372xvfCjujAuMDAJ1+61zb2xvfCjujAuMDAJ1+6439eiyv2jujAJxcXQ8qO6Nwm/qsb0CbnYsdUgfCCxo7Tm0N64xCB8INDeuMTQxc+iCgq52LHVCrna0ce8vtGh0rsgICAgIFvXtMyso7q/qsb0XQq52tHHvL7RodK7CdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujEJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKudix1Qq52tHH1+m6zyAgICAgW9e0zKyjur+qxvRdCrna0cfX6brPCdfuuN+9sb3wo7oxMDAuMDAJ1+61zb2xvfCjujEwMC4wMAnX7rjf16LK/aO6MAnFxdDyo7oxCb+qxvQJudix1SB8ILGjtObQ3rjEIHwg0N64xNDFz6IKCrna0cfX3LrNCdfuuN+9sb3wo7owLjAwCdfutc29sb3wo7owLjAwCdfuuN/Xosr9o7owCcXF0PKjujIJv6rG9Am52LHVIHwgsaO05tDeuMQgfCDQ3rjE0MXPogoKCg==');


DROP TABLE IF EXISTS `qht_appinfo`;
CREATE TABLE `qht_appinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appcode` varchar(32) NOT NULL COMMENT 'app编号',
  `agent` varchar(32) NOT NULL COMMENT '代理',
  `appname` varchar(32) NOT NULL COMMENT 'app名称',
  `appcompany` varchar(64) NOT NULL COMMENT 'app属于哪个公司',
  `actiontime` bigint(20) NOT NULL COMMENT '有效时间',
  `wechat_code` varchar(64) DEFAULT NULL COMMENT '微信账号',
  `wechat_p` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `wechat_img` varchar(255) DEFAULT NULL COMMENT '微信图片',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `appcode` (`appcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='APP信息表';

-- insert  into `qht_appinfo`(`id`,`appcode`,`agent`,`appname`,`appcompany`,`actiontime`,`createtime`,`updatetime`,`wechat_code`,`wechat_p`,`wechat_img`) values (1,'GS1001','stt','GS1001','qht',1514735999000,1491374487859,1493695297280,'333333','C:\\fakepath\\WeChat 圖片_20170422152314.bmp','C:\\fakepath\\WeChat 圖片_20170422152314.bmp');
insert  into `qht_appinfo`(`id`,`appcode`,`agent`,`appname`,`appcompany`,`actiontime`,`createtime`,`updatetime`,`wechat_code`,`wechat_p`,`wechat_img`) values (1,'GS1001','stt','GS1001','qht',1514735999000,1491374487859,1493695297280,'333333','C:\\fakepath\\WeChat 圖片_20170422152314.bmp','C:\\fakepath\\WeChat 圖片_20170422152314.bmp'),(2,'GS1002','qht','嗯嗯','来来来',1493567999000,1493371059989,1493371059989,'222','C:\\fakepath\\WeChat 圖片_20170422152314.bmp','C:\\fakepath\\WeChat 圖片_20170422152314.bmp'),(3,'GS1003','stt','GS1003','qht',1496246399000,1493692449850,1493692449850,'36666668','C:\\fakepath\\005Rwhyegw1f94kpvz0v7j30c808w753.jpg','C:\\fakepath\\005Rwhyegw1f94kpvz0v7j30c808w753.jpg'),(4,'GS1004','aaa','aaa','qqq',1496246399000,1493694541843,1493694541843,'6666','C:\\fakepath\\005Rwhyegw1f94kpshfz9j30c809j758.jpg','C:\\fakepath\\122.jpg'),(5,'GS1005','55','66','77',1499961599000,1493695345267,1493695449812,'2222','C:\\fakepath\\dd.jpg','C:\\fakepath\\122.jpg'),(6,'GS1006','DD','QHT','GS1006',1513353599000,1493695550475,1493695550475,'232232','C:\\fakepath\\122.jpg','C:\\fakepath\\dd.jpg'),(7,'GS1007','qw','qw','qht',1513267199000,1493696618251,1493696618251,'22321','C:\\fakepath\\WeChat 圖片_20170422152314.bmp','C:\\fakepath\\122.jpg'),(8,'GS1008','555','gs1008','qht',1496246399000,1493696761368,1493696761368,'888888','C:\\fakepath\\122.jpg','C:\\fakepath\\dd.jpg'),(9,'2','2','2','2',1494777599000,1493706738528,1493706738528,'2','C:\\fakepath\\WeChat 圖片_20170422152314.bmp','C:\\fakepath\\dd.jpg'),(10,'3','3','3','3',1496246399000,1493706899625,1493706899625,'33','C:\\fakepath\\dd.jpg','C:\\fakepath\\122.jpg'),(11,'9','9','9','9',1495468799000,1493708446760,1493708446760,'9','C:\\fakepath\\dd.jpg','C:\\fakepath\\dd.jpg'),(13,'11','11','11','11',1496159999000,1493708766898,1493708766898,'11','C:\\fakepath\\dd.jpg','C:\\fakepath\\dd.jpg'),(14,'12','12','12','12',1495900799000,1493708866837,1493708866837,'12','C:\\fakepath\\dd.jpg','C:\\fakepath\\122.jpg');


DROP TABLE IF EXISTS `qht_gamerecord`;
CREATE TABLE `qht_gamerecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `lotteryid` int(11) NOT NULL COMMENT '彩种',
  `period` varchar(32) NOT NULL COMMENT '期号',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `typeid` int(11) NOT NULL COMMENT '身份类别 1_真实 2_虚拟',
  `betamount` Decimal(11,2) NOT NULL COMMENT '投注额',
  `paidamount` Decimal(11,2) NOT NULL COMMENT '赔付额',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='游戏记录表';

DROP TABLE IF EXISTS `qht_financerecord`;
CREATE TABLE `qht_financerecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `type` int(11) NOT NULL COMMENT '类型 1_充值 2_提现',
  `typeid` int(11) NOT NULL COMMENT '身份类别 1_真实 2_虚拟',
  `amount` Decimal(11,2) NOT NULL COMMENT '金额',
  `requestname` varchar(32) NOT NULL COMMENT '请求者',
  `oprname` varchar(32) NOT NULL COMMENT '操作者',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='财务记录表';

DROP TABLE IF EXISTS `qht_financeorder`;
CREATE TABLE `qht_financeorder` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `odd` char(32) NOT NULL COMMENT '订单号',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `type` int(11) NOT NULL COMMENT '类型 1_充值 2_提现',
  `amount` Decimal(11,2) NOT NULL COMMENT '金额',
  `requesttime` bigint(20) NOT NULL COMMENT '请求时间',
  `requestname` varchar(32) NOT NULL COMMENT '请求者',
  `tag` int(11) NOT NULL COMMENT '标志 1_已处理 2_未处理 3_忽略',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单受理表';

DROP TABLE IF EXISTS `qht_amountrecord`;
CREATE TABLE `qht_amountrecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `type` int(11) NOT NULL COMMENT '类型',
  `amount` Decimal(11,2) NOT NULL COMMENT '金额',
  `bef_bal` Decimal(11,2) NOT NULL COMMENT '操作前余额',
  `aft_bal` Decimal(11,2) NOT NULL COMMENT '操作后余额',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='金额变动表';

DROP TABLE IF EXISTS `qht_periodrecord`;
CREATE TABLE `qht_periodrecord` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `period` varchar(32) NOT NULL COMMENT '期号',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `lorreyid` int(11) NOT NULL COMMENT '彩种编号',
  `number` int(11) NOT NULL COMMENT '参与人数',
  `bet` Decimal(11,2) NOT NULL COMMENT '压注额',
  `paid` Decimal(11,2) NOT NULL COMMENT '赔付额',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='期数记录表';


DROP TABLE IF EXISTS `qht_report_day`;
CREATE TABLE `qht_report_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `date` int(11) NOT NULL COMMENT '日期 YYYYMMDD',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `typeid` int(11) NOT NULL COMMENT '身份类别 1_真实 2_虚拟',
  `recharge_count` int(11) NOT NULL COMMENT '充值笔数',
  `recharge_amount` Decimal(11,2) NOT NULL COMMENT '充值金额',
  `withdrawals_count` int(11) NOT NULL COMMENT '提现笔数',
  `withdrawals_amount` Decimal(11,2) NOT NULL COMMENT '提现金额',
  `game_count` int(11) NOT NULL COMMENT '游戏局数',
  `bet_amount` Decimal(11,2) NOT NULL COMMENT '压注金额',
  `paid_amount` Decimal(11,2) NOT NULL COMMENT '赔付金额',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日报表';





DROP TABLE IF EXISTS `qht_pk10_period_data`;
CREATE TABLE `qht_pk10_period_data` (
  `period` varchar(32) NOT NULL COMMENT '期号',
  `opencode` char(32) NOT NULL COMMENT '开奖数据',
  `opentime` bigint(20) NOT NULL COMMENT '开奖时间',
  PRIMARY KEY (`period`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10期数信息表';

DROP TABLE IF EXISTS `qht_pk10_paid_ini`;
CREATE TABLE `qht_pk10_paid_ini` (
  `id` int(11) NOT NULL COMMENT '1d',
  `n_number` decimal(11,5) NOT NULL COMMENT '特码赔付',
  `n_big` decimal(11,5) NOT NULL COMMENT '大赔付',
  `n_small` decimal(11,5) NOT NULL COMMENT '小赔付',
  `n_single` decimal(11,5) NOT NULL COMMENT '单赔付',
  `n_double` decimal(11,5) NOT NULL COMMENT '双赔付',
  `n_dragon` decimal(11,5) NOT NULL COMMENT '龙赔付',
  `n_tiger` decimal(11,5) NOT NULL COMMENT '虎赔付',
  `s_big` decimal(11,5) NOT NULL COMMENT '冠亚和大赔付',
  `s_small` decimal(11,5) NOT NULL COMMENT '冠亚和小赔付',
  `s_single` decimal(11,5) NOT NULL COMMENT '冠亚和单赔付',
  `s_double` decimal(11,5) NOT NULL COMMENT '冠亚和双赔付',
  `s_number_341819` decimal(11,5) NOT NULL COMMENT '冠亚和数字341819赔付',
  `s_number_561617` decimal(11,5) NOT NULL COMMENT '冠亚和数字561617赔付',
  `s_number_781415` decimal(11,5) NOT NULL COMMENT '冠亚和数字781415赔付',
  `s_number_9101213` decimal(11,5) NOT NULL COMMENT '冠亚和数字9101213赔付',
  `s_number_11` decimal(11,5) NOT NULL COMMENT '冠亚和数字11赔付',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10赔率表';

INSERT INTO `qht_pk10_paid_ini` VALUES ('1', '9.50000', '1.90000', '1.90000', '1.90000', '1.98000', '1.90000', '1.90000', '2.00000', '2.00000', '2.00000', '2.00000', '39.00000', '20.00000', '10.00000', '5.00000', '3.00000', 1490513317079, 1490513317079);


DROP TABLE IF EXISTS `qht_pk10_symbol`;
CREATE TABLE `qht_pk10_symbol` (
  `id` int(11) NOT NULL COMMENT 'id',
  `symbol` char(8) NOT NULL COMMENT '符号',
  `tag` int(11) NOT NULL COMMENT '标志 1_有效 2_失效',
  `comment` varchar(128) NOT NULL COMMENT '说明',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10分隔符号符表';

INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('1', '', '1', '空格', '1491447079920');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('2', ',', '1', '逗号', '1491447121121');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('3', '.', '1', '点号', '1491446512348');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('4', ';', '2', '分号', '1491447251884');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('6', '/', '1', '斜号', '1491447135314');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('7', '&gt;', '1', '大于号', '1491447142886');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('8', '&lt;', '1', '小于号', '1491447139588');
INSERT INTO `qht_pk10_symbol` (`id`, `symbol`, `tag`, `comment`, `updatetime`) VALUES ('9', '&', '1', '和号', '1491447131666');


DROP TABLE IF EXISTS `qht_pk10_pointout`;
CREATE TABLE `qht_pk10_pointout` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `lottery_id` int(11) NOT NULL COMMENT '彩种',
  `text_id` int(11) NOT NULL COMMENT '内容编号',
  `spacetime` int(11) NOT NULL COMMENT '离封盘时间',
  `text` blob NOT NULL COMMENT '内容',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10群提示表';

INSERT INTO `qht_pk10_pointout` (`id`, `lottery_id`, `text_id`, `spacetime`, `text`, `createtime`, `updatetime`) VALUES ('2', '1', '1', '285', '���ڿ�ʼ��ע', '1494569399698', '1494569399698');


DROP TABLE IF EXISTS `qht_pk10_bet_set`;
CREATE TABLE `qht_pk10_bet_set` (
  `id` int(11) NOT NULL COMMENT 'id(pk10=1)',
  `big_small_low` decimal(11,5) NOT NULL COMMENT '大小最低',
  `big_small_high` decimal(11,5) NOT NULL COMMENT '大小最高',
  `single_double_low` decimal(11,5) NOT NULL COMMENT '单双最低',
  `single_double_high` decimal(11,5) NOT NULL COMMENT '单双最高',
  `dragon_tiger_low` decimal(11,5) NOT NULL COMMENT '龙虎最低',
  `dragon_tiger_high` decimal(11,5) NOT NULL COMMENT '龙虎最高',
  `n_number_low` decimal(11,5) NOT NULL COMMENT '特码最低',
  `n_number_high` decimal(11,5) NOT NULL COMMENT '特码最高',
  `s_number_low` decimal(11,5) NOT NULL COMMENT '和值最低',
  `s_number_high` decimal(11,5) NOT NULL COMMENT '和值最高',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10投注限制表';

INSERT INTO `qht_pk10_bet_set` (`id`, `big_small_low`, `big_small_high`, `single_double_low`, `single_double_high`, `dragon_tiger_low`, `dragon_tiger_high`, `n_number_low`, `n_number_high`, `s_number_low`, `s_number_high`, `createtime`, `updatetime`) VALUES ('1', '5.00000', '5.00000', '5.00000', '5.00000', '5.00000', '5.00000', '5.00000', '5.00000', '5.00000', '5.00000', '1492414271522', '1492414271522');


DROP TABLE IF EXISTS `qht_pk10_info`;
CREATE TABLE `qht_pk10_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `odd` varchar(32) NOT NULL COMMENT '单号',
  `period` varchar(32) NOT NULL COMMENT '期号',
  `appcode` varchar(32) NOT NULL COMMENT 'app编号 ',
  `username` varchar(32) NOT NULL COMMENT '玩家 ',
  `runway` int(11) NOT NULL COMMENT '车道(1~10表示冠军道~第10道 11表示冠亚和)',
  `bettype` char(3) NOT NULL COMMENT '投注类型(大小单双龙虎1~19中的一个)',
  `betamount` decimal(11,2) NOT NULL COMMENT '投注金额',
  `paidamount` decimal(11,2) NOT NULL COMMENT '赔付金额',
  `bettime` bigint(20) NOT NULL COMMENT '投注时间',
  `paidtime` bigint(20) NOT NULL COMMENT '赔付时间',
  `status` int(11) NOT NULL COMMENT '状态 1_投注 2_结算 3_取消 4_飞盘中',
  `updatetime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`odd`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10明细表';

DROP TABLE IF EXISTS `qht_customer`;
CREATE TABLE `qht_customer` (
  `id` int(11) NOT NULL COMMENT '客服编号',
  `cus_num` varchar(32) NOT NULL COMMENT '客服账号',
  `cus_pwd` varchar(128) NOT NULL COMMENT '客服密码',
  `cus_name` varchar(32) NOT NULL COMMENT '客服名称',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`cus_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='IM账号表';

insert  into `qht_customer`(`id`,`cus_num`,`cus_pwd`,`cus_name`,`createtime`,`updatetime`) values (2,'app_admin','12345678','APP群组客服01',1492414271522,1493295038936),(1,'pk10_custom','12345678','PK10群组客服01',1492414271522,1492414271522);


DROP TABLE IF EXISTS `qht_returnwater`;
CREATE TABLE `qht_returnwater` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `date` int(11) NOT NULL COMMENT '日期 YYYYMMDD',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `typeid` int(11) NOT NULL COMMENT '身份类别 1_真实 2_虚拟',
  `water_amount` decimal(11,2) NOT NULL COMMENT '流水总额',
  `profit_amount` decimal(11,2) NOT NULL COMMENT '利润总额',
  `up_amount` decimal(11,2) NOT NULL COMMENT '上分总额',
  `down_amount` decimal(11,2) NOT NULL COMMENT '下分总额',
  `status` int(11) NOT NULL COMMENT '状态 0_未返水 1_当日盈亏 2_当日流水 3_当日上分 4_当日下分',
  `return_amount` decimal(11,2) NOT NULL COMMENT '返水额',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='返水信息表';






DROP TABLE IF EXISTS `qht_pk10_player`;
CREATE TABLE `qht_pk10_player` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `appcode` varchar(32) NOT NULL COMMENT 'App',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `status` int(11) NOT NULL COMMENT '状态 1_有效  2_无效',
  `balance` Decimal(11,2) NOT NULL COMMENT '余额',
  `schemes` blob  NOT NULL COMMENT '方案',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10机器人信息表';

DROP TABLE IF EXISTS `qht_robot_scheme`;
CREATE TABLE `qht_robot_scheme` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `name` varchar(32) NOT NULL COMMENT '名称',
  `low_amount` decimal(11,2) NOT NULL COMMENT '剩余金额低于此值',
  `sendup_amount` decimal(11,2) NOT NULL COMMENT '发送上分金额',
  `up_amount` decimal(11,2) NOT NULL COMMENT '剩余金额高于此值',
  `senddown_amount` decimal(11,2) NOT NULL COMMENT '发送下分金额',
  `stop_amount` decimal(11,2) NOT NULL COMMENT '金额小于此值，停止投注',
  `send_text` blob NOT NULL COMMENT '投注内容',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10机器人方案表';

DROP TABLE IF EXISTS `qht_robot_record`;
CREATE TABLE `qht_robot_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `username` varchar(32) NOT NULL COMMENT '玩家',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `period` varchar(32) NOT NULL COMMENT '期号',
  `text` varchar(255) NOT NULL COMMENT '内容',
  `status` int(11) NOT NULL COMMENT '状态 1_投注成功 2_投注失败',
  `updatetime` bigint(20) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='pk10机器人投注记录表';
