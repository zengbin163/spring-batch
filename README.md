# spring-batch
基于spring生态圈的spring-batch框架实现串行读并行写的批次数据同步服务
1. 数据库端基于MySql
创建源库food，创建表food
CREATE TABLE `food` (
   `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `category_id` int(11) DEFAULT NULL COMMENT '分类id',
   `food_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '美食名称',
   `food_pic` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '美食主图链接',
   `food_info` text COLLATE utf8mb4_bin COMMENT '美食介绍',
   `price` decimal(10,2) DEFAULT NULL,
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`uid`) USING BTREE,
   KEY `category_id` (`category_id`) USING BTREE
   ) ENGINE=InnoDB AUTO_INCREMENT=3652506 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='美食介绍（介绍各地美食，按照地区来区分美食）';
创建目标库food2，创建表food
   CREATE TABLE `food` (
   `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `category_id` int(11) DEFAULT NULL COMMENT '分类id',
   `food_name` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '美食名称',
   `food_pic` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '美食主图链接',
   `food_info` text COLLATE utf8mb4_bin COMMENT '美食介绍',
   `price` decimal(10,2) DEFAULT NULL,
   `create_time` datetime DEFAULT NULL COMMENT '创建时间',
   `update_time` datetime DEFAULT NULL COMMENT '更新时间',
   PRIMARY KEY (`uid`) USING BTREE,
   KEY `category_id` (`category_id`) USING BTREE
   ) ENGINE=InnoDB AUTO_INCREMENT=3652506 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='美食介绍（介绍各地美食，按照地区来区分美食）';
2. 源代码参考

