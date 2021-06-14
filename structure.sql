/*
SQLyog Ultimate v11.24 (32 bit)
MySQL - 5.6.22 : Database - mybase
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
/*Table structure for table `email` */

DROP TABLE IF EXISTS `email`;

CREATE TABLE `email` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `content` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `email_record` */

DROP TABLE IF EXISTS `email_record`;

CREATE TABLE `email_record` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email_id` int(5) DEFAULT NULL,
  `user_id` int(5) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `email_user` */

DROP TABLE IF EXISTS `email_user`;

CREATE TABLE `email_user` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `email_id` int(5) DEFAULT NULL,
  `user_id` int(5) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `job_info` */

DROP TABLE IF EXISTS `job_info`;

CREATE TABLE `job_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `company_name` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `company_addr` varchar(200) DEFAULT NULL COMMENT '公司地址',
  `company_info` mediumtext COMMENT '公司信息',
  `job_name` varchar(100) DEFAULT NULL COMMENT '职位名称',
  `job_addr` varchar(150) DEFAULT NULL COMMENT '工作地点',
  `job_info` mediumtext COMMENT '职位信息',
  `salary_min` int(10) DEFAULT NULL COMMENT '最低薪资',
  `salary_max` int(10) DEFAULT NULL COMMENT '最高薪资',
  `salary_unit` varchar(10) DEFAULT NULL COMMENT '薪资单位（年，月）',
  `url` varchar(150) DEFAULT NULL COMMENT '招聘详情页',
  `time` varchar(10) DEFAULT NULL COMMENT '发布时间',
  `create_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `token` */

DROP TABLE IF EXISTS `token`;

CREATE TABLE `token` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `token` varchar(200) DEFAULT NULL,
  `remark` varbinary(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(5) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `tel` varchar(20) DEFAULT NULL,
  `pass` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
