-- --------------------------------------------------------
-- Host:                         10.10.94.81
-- Server version:               5.7.23 - MySQL Community Server (GPL)
-- Server OS:                    linux-glibc2.12
-- HeidiSQL Version:             12.4.0.6665
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for driverhealth
CREATE DATABASE IF NOT EXISTS `driverhealth` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `driverhealth`;

-- Dumping structure for table driverhealth.xx_alarm_push_log
CREATE TABLE IF NOT EXISTS `xx_alarm_push_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '用户手机号码',
  `content` varchar(1024) COLLATE utf8mb4_bin NOT NULL,
  `read_status` enum('read','unread') COLLATE utf8mb4_bin NOT NULL DEFAULT 'unread' COMMENT '阅读状态',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  `type` enum('疲劳驾驶预警','家用设备数据异常预警','体征异常预警','自动接管车辆','睡眠监测','运动推荐','心率异常','保险','呼吸预警','心率预警') COLLATE utf8mb4_bin NOT NULL COMMENT '预警类型:疲劳驾驶预警=疲劳驾驶预警,家用设备数据异常预警=家用设备数据异常预警,体征异常预警=体征异常预警',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1043 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT COMMENT='模拟预警推送表';

-- Dumping data for table driverhealth.xx_alarm_push_log: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_articles
CREATE TABLE IF NOT EXISTS `xx_articles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('健康菜谱','疾病防治','健康助手') COLLATE utf8mb4_bin DEFAULT NULL COMMENT '类型:健康菜谱=健康菜谱,疾病防治=疾病防治,健康助手=健康助手',
  `cover_image` varchar(256) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '封皮图片',
  `title` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `content` longtext COLLATE utf8mb4_bin COMMENT '内容',
  `createtime` bigint(14) NOT NULL COMMENT '创建时间',
  `updatetime` bigint(14) NOT NULL COMMENT '创建时间',
  `source` tinyint(1) NOT NULL,
  `tags_ids` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=176 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT COMMENT='健康贴士';

-- Dumping data for table driverhealth.xx_articles: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_assistant_push_log
CREATE TABLE IF NOT EXISTS `xx_assistant_push_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '用户手机号码',
  `title` varchar(128) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '标题',
  `content` varchar(1024) COLLATE utf8mb4_bin NOT NULL,
  `read_status` enum('read','unread') COLLATE utf8mb4_bin NOT NULL DEFAULT 'unread' COMMENT '阅读状态',
  `createtime` bigint(20) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=203 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT COMMENT='模拟推送表';

-- Dumping data for table driverhealth.xx_assistant_push_log: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_health_report
CREATE TABLE IF NOT EXISTS `xx_health_report` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '报告标题',
  `username` varchar(50) COLLATE utf8mb4_bin NOT NULL,
  `type` enum('month','quarter','year') COLLATE utf8mb4_bin NOT NULL DEFAULT 'month' COMMENT '报告周期类型',
  `createtime` bigint(20) NOT NULL,
  `updatetime` bigint(20) NOT NULL,
  `proportion` int(11) unsigned DEFAULT '0' COMMENT '击败占比',
  `tired_total_times` int(11) unsigned DEFAULT '0' COMMENT '疲劳总测量次数',
  `tired_abnormal_times` int(11) unsigned DEFAULT '0' COMMENT '疲劳次数',
  `heart_total_times` int(11) unsigned DEFAULT '0' COMMENT '心率总测量次数',
  `heart_abnormal_times` int(11) unsigned DEFAULT '0' COMMENT '心率异常次数',
  `respiratory_total_times` int(10) unsigned DEFAULT '0' COMMENT '呼吸测量总数',
  `respiratory_abnormal_times` int(10) unsigned DEFAULT '0' COMMENT '呼吸异常次数',
  `respiratory_rate_max` int(10) unsigned DEFAULT '0' COMMENT '最高呼吸率',
  `read_status` enum('read','unread') COLLATE utf8mb4_bin NOT NULL DEFAULT 'unread' COMMENT '阅读状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=395 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT COMMENT='健康分析报告';

-- Dumping data for table driverhealth.xx_health_report: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_ivi_history
CREATE TABLE IF NOT EXISTS `xx_ivi_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) COLLATE utf8mb4_bin NOT NULL,
  `createtime` bigint(20) NOT NULL,
  `hr` int(11) NOT NULL COMMENT '心率',
  `br` int(11) NOT NULL,
  `tired` enum('正常','疲劳') COLLATE utf8mb4_bin NOT NULL DEFAULT '正常' COMMENT '疲劳状态',
  `conclusion` enum('正常','轻微异常','严重异常') COLLATE utf8mb4_bin NOT NULL DEFAULT '正常' COMMENT '测量结论',
  `description` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '结论描述',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT COMMENT='IVI数据采集记录表';

-- Dumping data for table driverhealth.xx_ivi_history: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_sleep_push
CREATE TABLE IF NOT EXISTS `xx_sleep_push` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phone_num` varchar(20) NOT NULL COMMENT '手机号',
  `sleep_state` tinyint(1) NOT NULL COMMENT '睡眠状态',
  `state` int(1) NOT NULL COMMENT '状态 0 未读 1 已读',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2096 DEFAULT CHARSET=utf8mb4 COMMENT='睡眠状态推送表';

-- Dumping data for table driverhealth.xx_sleep_push: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_type_push
CREATE TABLE IF NOT EXISTS `xx_type_push` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `phone_num` varchar(20) NOT NULL COMMENT '电话',
  `push_type` int(2) NOT NULL COMMENT '推送类型 ',
  `state` int(1) NOT NULL COMMENT '状态 0 未读 1 已读',
  `createtime` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3179 DEFAULT CHARSET=utf8mb4 COMMENT='众类型信息推送表';

-- Dumping data for table driverhealth.xx_type_push: ~0 rows (approximately)

-- Dumping structure for table driverhealth.xx_user
CREATE TABLE IF NOT EXISTS `xx_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(15) COLLATE utf8mb4_bin NOT NULL COMMENT '用户手机号',
  `nickname` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  `password` varchar(120) COLLATE utf8mb4_bin NOT NULL COMMENT 'default 000000',
  `createtime` bigint(20) NOT NULL,
  `lockbits` tinyint(6) NOT NULL DEFAULT '0',
  `avatar` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT '头像文件名',
  `ima` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT 'reserved',
  `imb` varchar(50) COLLATE utf8mb4_bin NOT NULL DEFAULT 'reserved',
  PRIMARY KEY (`id`,`username`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=COMPACT COMMENT='用户';

-- Dumping data for table driverhealth.xx_user: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
