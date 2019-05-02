/*
SQLyog v10.2 
MySQL - 5.5.49 : Database - chatroom
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`chatroom` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `chatroom`;

/*Table structure for table `message` */

DROP TABLE IF EXISTS `message`;

CREATE TABLE `message` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `from_id` varchar(64) DEFAULT NULL COMMENT '发送者ID',
  `to_id` text COMMENT '接受者ID',
  `group_id` varchar(64) DEFAULT NULL COMMENT '群聊ID',
  `content` text COMMENT '消息内容',
  `type` varchar(4) DEFAULT NULL COMMENT '消息类型(群聊/单聊)',
  `status` varchar(4) DEFAULT NULL COMMENT '消息状态(0未读,1,已读)',
  `create_time` datetime DEFAULT NULL,
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `del_flag` varchar(2) DEFAULT NULL COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `message` */

insert  into `message`(`id`,`from_id`,`to_id`,`group_id`,`content`,`type`,`status`,`create_time`,`sort`,`create_by`,`del_flag`) values ('04dfd151f1fc4b22a8098e1464d6f757','d1d687fbdbfe4a22818389a6350c242b','4e2cf4a39e1245ab936f3defb563b8ee,','12312312312312','初始化..','2','0','2019-04-16 21:36:16',NULL,NULL,'0'),('0d1f8568490a428abaafc0bb3860be44','5ccd84eb827642038441137b97cf4e67','d1d687fbdbfe4a22818389a6350c242b',NULL,'123456','1','1','2019-04-16 02:02:33',NULL,NULL,'0'),('0e89392b63f54216bc47b5ce9aaf07d6','5ccd84eb827642038441137b97cf4e67','4e2cf4a39e1245ab936f3defb563b8ee,','12312312312312','fasdf','2','0','2019-04-16 21:25:02',NULL,NULL,'0'),('0e9c9c419e3348ecbd3050ffbd78f41c','d1d687fbdbfe4a22818389a6350c242b','4e2cf4a39e1245ab936f3defb563b8ee,d1d687fbdbfe4a22818389a6350c242b,','12312312312312','我是小兵','2','0','2019-04-16 21:45:50',NULL,NULL,'0'),('0f50c800529e44a2aa6ed95c7a561c7e','d1d687fbdbfe4a22818389a6350c242b','4e2cf4a39e1245ab936f3defb563b8ee,','12312312312312','FADSFA','2','0','2019-04-16 21:39:32',NULL,NULL,'0'),('0fedd2df671e4ce99218fcbc53a0e792','5ccd84eb827642038441137b97cf4e67','4e2cf4a39e1245ab936f3defb563b8ee',NULL,'123','1','0','2019-04-16 01:44:08',NULL,NULL,'0'),('192abbfdcfd6487f96fdc44a8e665f8e','d1d687fbdbfe4a22818389a6350c242b','4e2cf4a39e1245ab936f3defb563b8ee,','12312312312312','123123','2','0','2019-04-16 21:22:16',NULL,NULL,'0'),('4cb5d3989f274c289e264237002eea26','5ccd84eb827642038441137b97cf4e67','4e2cf4a39e1245ab936f3defb563b8ee,','12312312312312','SDF','2','0','2019-04-16 21:40:53',NULL,NULL,'0'),('580f95c347a54afdaac9d4164579698a','5ccd84eb827642038441137b97cf4e67','d1d687fbdbfe4a22818389a6350c242b',NULL,'123456','1','1','2019-04-16 02:00:03',NULL,NULL,'0'),('74b06e3b5a4e46298e7a73c07ae64e84','5ccd84eb827642038441137b97cf4e67','4e2cf4a39e1245ab936f3defb563b8ee,','12312312312312','FSAD','2','0','2019-04-16 21:40:43',NULL,NULL,'0');

/*Table structure for table `user_group` */

DROP TABLE IF EXISTS `user_group`;

CREATE TABLE `user_group` (
  `id` varchar(64) DEFAULT NULL COMMENT '群ID',
  `group_name` varchar(128) DEFAULT NULL COMMENT '群名称',
  `group_introduction` varchar(200) DEFAULT NULL COMMENT '群简介',
  `group_user_count` int(11) DEFAULT NULL COMMENT '群人员数量',
  `group_members` text COMMENT '群成员的ID集合',
  `create_by` varchar(64) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建者',
  `udate_by` varchar(64) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL COMMENT '修改者',
  `del_flag` varchar(2) DEFAULT NULL COMMENT '是否有效'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_group` */

insert  into `user_group`(`id`,`group_name`,`group_introduction`,`group_user_count`,`group_members`,`create_by`,`create_time`,`udate_by`,`update_time`,`del_flag`) values ('12312312312312','大家嗨','我们一起聊',3,'4e2cf4a39e1245ab936f3defb563b8ee,5ccd84eb827642038441137b97cf4e67,d1d687fbdbfe4a22818389a6350c242b,\r\n',NULL,'2019-04-16 19:03:11',NULL,NULL,'0');

/*Table structure for table `user_info` */

DROP TABLE IF EXISTS `user_info`;

CREATE TABLE `user_info` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `username` varchar(128) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(128) DEFAULT NULL COMMENT '用户密码',
  `nickname` varchar(128) DEFAULT NULL COMMENT '用户昵称',
  `sex` varchar(128) DEFAULT NULL COMMENT '用户性别',
  `age` varchar(128) DEFAULT NULL COMMENT '年龄',
  `profilehead` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `profile` varchar(400) DEFAULT NULL COMMENT '简介',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改者',
  `is_online` varchar(4) DEFAULT NULL COMMENT '是否在线(0,在线,1,不在线)',
  `status` varchar(4) DEFAULT NULL COMMENT '状态',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '是否有效(0,有效,1无效)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_info` */

insert  into `user_info`(`id`,`username`,`password`,`nickname`,`sex`,`age`,`profilehead`,`profile`,`create_time`,`update_time`,`create_by`,`update_by`,`is_online`,`status`,`del_flag`) values ('4e2cf4a39e1245ab936f3defb563b8ee','html','123456','小王',NULL,NULL,NULL,NULL,NULL,'2019-04-16 18:39:43',NULL,NULL,'0',NULL,'0'),('5ccd84eb827642038441137b97cf4e67','xie','123456','谢',NULL,NULL,NULL,NULL,NULL,'2019-04-16 21:46:03',NULL,NULL,'0',NULL,'0'),('94583fe68c64477e9da1c3e02e5594f3','wanglong','12345','网龙',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL,'0'),('d1d687fbdbfe4a22818389a6350c242b','bing','12345','冰bing','0','24','upload/冰bing/冰bing.jpg','我是一个粉刷家',NULL,'2019-04-16 21:47:19',NULL,NULL,'1',NULL,'0'),('e56f9ba081954d909724d2c716a2e62a','xzb','123456','xzb',NULL,NULL,NULL,NULL,NULL,'2019-04-16 05:21:08',NULL,NULL,'0',NULL,'0'),('ffd9a7ad363a41e8a1e9035b4f04b4cb','xiaokai','12345','小凯',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,'1',NULL,'0');

/*Table structure for table `user_relation` */

DROP TABLE IF EXISTS `user_relation`;

CREATE TABLE `user_relation` (
  `id` varchar(64) NOT NULL COMMENT '主键ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户ID',
  `user_friend_id` varchar(64) DEFAULT NULL COMMENT '用户好友ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `update_by` varchar(64) DEFAULT NULL COMMENT '修改人',
  `del_flag` varchar(4) DEFAULT NULL COMMENT '是否有效(0,有效,1,无效)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_relation` */

insert  into `user_relation`(`id`,`user_id`,`user_friend_id`,`create_time`,`update_time`,`create_by`,`update_by`,`del_flag`) values ('a9b3cd644cf64c62aeee627895539d58','4e2cf4a39e1245ab936f3defb563b8ee','d1d687fbdbfe4a22818389a6350c242b','2019-04-16 17:28:45',NULL,'4e2cf4a39e1245ab936f3defb563b8ee',NULL,'1'),('f2758506f0de492f8f70723aaa33393d','d1d687fbdbfe4a22818389a6350c242b','5ccd84eb827642038441137b97cf4e67','2019-04-16 18:33:48',NULL,'d1d687fbdbfe4a22818389a6350c242b',NULL,'0');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
