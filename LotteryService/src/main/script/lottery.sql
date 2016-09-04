-- phpMyAdmin SQL Dump
-- version 3.4.9
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2016 年 07 月 12 日 16:53
-- 服务器版本: 5.5.20
-- PHP 版本: 5.3.9

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `lottery`
--

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `openid` varchar(50) NOT NULL,
  `role` varchar(20) NOT NULL DEFAULT '',
  `real_name` varchar(50) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `card_no` varchar(20) DEFAULT NULL,`nick_name` varchar(50) NOT NULL,

  `nick_name` varchar(200) DEFAULT NULL,
  `sex` varchar(20) NULL,
  `province` varchar(50) NULL,
  `city` varchar(50) NULL,
  `country` varchar(50) NULL,
  `head_img_url` varchar(200) NULL,
  `unionid` varchar(100) NULL,
  `subscribe_time` int(11) NULL,

  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `openid` (`openid`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=2 ;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `openid`, `role`, `real_name`, `phone`, `card_no`, `create_time`, `update_time`) VALUES
(1, 'test', 'CUSTOMER', '刘志', '15629913656', '422301198239289909', '2016-07-12 12:50:07', '2016-07-12 12:50:07');


-- --------------------------------------------------------

--
-- 表的结构 `lottery`
--

CREATE TABLE IF NOT EXISTS `lottery` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_no` varchar(50) NOT NULL,
  `total_fee` float NOT NULL,
  `lottery_type` varchar(10) NOT NULL,
  `business_type` varchar(10) NOT NULL DEFAULT '',
  `times` int(11) NOT NULL DEFAULT '1',
  `pay_state` varchar(20) NULL,
  `owner` bigint(20) DEFAULT NULL,
  `merchant` bigint(20) DEFAULT NULL,
  `distribute_time` timestamp NULL DEFAULT NULL,
  `remark` varchar(1000) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL,
  `create_by` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_l_order_no` (`order_no`),
  KEY `idx_l_type` (`lottery_type`),
  KEY `idx_l_merchant` (`merchant`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1;

-- --------------------------------------------------------

--
-- 表的结构 `lottery_period`
--

CREATE TABLE IF NOT EXISTS `lottery_period` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lottery_id` bigint(20) NOT NULL,
  `period` int(6) NOT NULL,
  `ticket_print_time` timestamp NULL,  --
  `ticket_fetch_time` timestamp NULL,  --
  `prize_state` varchar(20) NULL,  --
  `prize_info` varchar(200) NULL,   --
  `prize_bonus` int(11) NULL,   --
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;


-- --------------------------------------------------------

--
-- 表的结构 `lottery_number`
--

CREATE TABLE IF NOT EXISTS `lottery_number` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lottery_id` bigint(20) NOT NULL,
  `number` varchar(100) NOT NULL DEFAULT '''''',
  PRIMARY KEY (`id`),
  KEY `lottery_id` (`lottery_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- 表的结构 `prize_no_ssq`
--

CREATE TABLE IF NOT EXISTS `prize_no_ssq` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `period` int(6) NOT NULL,
  `open_time` timestamp NULL DEFAULT NULL,
  `number` varchar(100) DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_period` (`period`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=155 ;

--
-- 转存表中的数据 `prize_no_ssq`
--

INSERT INTO `prize_no_ssq` (`id`, `period`, `open_time`, `number`, `create_time`, `update_time`) VALUES
(2, 2016001, '2016-01-03 11:00:00', NULL, '2016-07-11 14:07:44', '2016-07-11 14:07:44'),
(3, 2016002, '2016-01-05 11:00:00', NULL, '2016-07-11 14:07:44', '2016-07-11 14:07:44'),
(4, 2016003, '2016-01-07 11:00:00', NULL, '2016-07-11 14:07:44', '2016-07-11 14:07:44'),
(5, 2016004, '2016-01-10 11:00:00', NULL, '2016-07-11 14:07:44', '2016-07-11 14:07:44'),
(6, 2016005, '2016-01-12 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(7, 2016006, '2016-01-14 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(8, 2016007, '2016-01-17 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(9, 2016008, '2016-01-19 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(10, 2016009, '2016-01-21 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(11, 2016010, '2016-01-24 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(12, 2016011, '2016-01-26 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(13, 2016012, '2016-01-28 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(14, 2016013, '2016-01-31 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(15, 2016014, '2016-02-02 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(16, 2016015, '2016-02-04 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(17, 2016016, '2016-02-14 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(18, 2016017, '2016-02-16 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(19, 2016018, '2016-02-18 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(20, 2016019, '2016-02-21 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(21, 2016020, '2016-02-23 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(22, 2016021, '2016-02-25 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(23, 2016022, '2016-02-28 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(24, 2016023, '2016-03-01 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(25, 2016024, '2016-03-03 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(26, 2016025, '2016-03-06 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(27, 2016026, '2016-03-08 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(28, 2016027, '2016-03-10 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(29, 2016028, '2016-03-13 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(30, 2016029, '2016-03-15 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(31, 2016030, '2016-03-17 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(32, 2016031, '2016-03-20 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(33, 2016032, '2016-03-22 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(34, 2016033, '2016-03-24 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(35, 2016034, '2016-03-27 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(36, 2016035, '2016-03-29 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(37, 2016036, '2016-03-31 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(38, 2016037, '2016-04-03 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(39, 2016038, '2016-04-05 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(40, 2016039, '2016-04-07 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(41, 2016040, '2016-04-10 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(42, 2016041, '2016-04-12 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(43, 2016042, '2016-04-14 11:00:00', NULL, '2016-07-11 14:07:45', '2016-07-11 14:07:45'),
(44, 2016043, '2016-04-17 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(45, 2016044, '2016-04-19 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(46, 2016045, '2016-04-21 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(47, 2016046, '2016-04-24 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(48, 2016047, '2016-04-26 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(49, 2016048, '2016-04-28 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(50, 2016049, '2016-05-01 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(51, 2016050, '2016-05-03 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(52, 2016051, '2016-05-05 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(53, 2016052, '2016-05-08 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(54, 2016053, '2016-05-10 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(55, 2016054, '2016-05-12 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(56, 2016055, '2016-05-15 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(57, 2016056, '2016-05-17 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(58, 2016057, '2016-05-19 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(59, 2016058, '2016-05-22 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(60, 2016059, '2016-05-24 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(61, 2016060, '2016-05-26 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(62, 2016061, '2016-05-29 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(63, 2016062, '2016-05-31 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(64, 2016063, '2016-06-02 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(65, 2016064, '2016-06-05 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(66, 2016065, '2016-06-07 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(67, 2016066, '2016-06-09 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(68, 2016067, '2016-06-12 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(69, 2016068, '2016-06-14 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(70, 2016069, '2016-06-16 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(71, 2016070, '2016-06-19 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(72, 2016071, '2016-06-21 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(73, 2016072, '2016-06-23 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(74, 2016073, '2016-06-26 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(75, 2016074, '2016-06-28 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(76, 2016075, '2016-06-30 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(77, 2016076, '2016-07-03 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(78, 2016077, '2016-07-05 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(79, 2016078, '2016-07-07 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(80, 2016079, '2016-07-10 11:00:00', '01,03,10,12,24,28+02', '2016-07-11 14:07:46', '2016-07-12 13:30:01'),
(81, 2016080, '2016-07-12 11:00:00', '01,16,17,24,25,32+14', '2016-07-11 14:07:46', '2016-07-12 13:36:01'),
(82, 2016081, '2016-07-14 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(83, 2016082, '2016-07-17 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(84, 2016083, '2016-07-19 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(85, 2016084, '2016-07-21 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(86, 2016085, '2016-07-24 11:00:00', NULL, '2016-07-11 14:07:46', '2016-07-11 14:07:46'),
(87, 2016086, '2016-07-26 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(88, 2016087, '2016-07-28 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(89, 2016088, '2016-07-31 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(90, 2016089, '2016-08-02 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(91, 2016090, '2016-08-04 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(92, 2016091, '2016-08-07 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(93, 2016092, '2016-08-09 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(94, 2016093, '2016-08-11 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(95, 2016094, '2016-08-14 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(96, 2016095, '2016-08-16 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(97, 2016096, '2016-08-18 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(98, 2016097, '2016-08-21 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(99, 2016098, '2016-08-23 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(100, 2016099, '2016-08-25 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(101, 2016100, '2016-08-28 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(102, 2016101, '2016-08-30 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(103, 2016102, '2016-09-01 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(104, 2016103, '2016-09-04 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(105, 2016104, '2016-09-06 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(106, 2016105, '2016-09-08 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(107, 2016106, '2016-09-11 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(108, 2016107, '2016-09-13 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(109, 2016108, '2016-09-15 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(110, 2016109, '2016-09-18 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(111, 2016110, '2016-09-20 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(112, 2016111, '2016-09-22 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(113, 2016112, '2016-09-25 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(114, 2016113, '2016-09-27 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(115, 2016114, '2016-09-29 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(116, 2016115, '2016-10-02 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(117, 2016116, '2016-10-04 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(118, 2016117, '2016-10-06 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(119, 2016118, '2016-10-09 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(120, 2016119, '2016-10-11 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(121, 2016120, '2016-10-13 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(122, 2016121, '2016-10-16 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(123, 2016122, '2016-10-18 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(124, 2016123, '2016-10-20 11:00:00', NULL, '2016-07-11 14:07:47', '2016-07-11 14:07:47'),
(125, 2016124, '2016-10-23 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(126, 2016125, '2016-10-25 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(127, 2016126, '2016-10-27 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(128, 2016127, '2016-10-30 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(129, 2016128, '2016-11-01 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(130, 2016129, '2016-11-03 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(131, 2016130, '2016-11-06 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(132, 2016131, '2016-11-08 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(133, 2016132, '2016-11-10 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(134, 2016133, '2016-11-13 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(135, 2016134, '2016-11-15 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(136, 2016135, '2016-11-17 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(137, 2016136, '2016-11-20 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(138, 2016137, '2016-11-22 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(139, 2016138, '2016-11-24 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(140, 2016139, '2016-11-27 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(141, 2016140, '2016-11-29 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(142, 2016141, '2016-12-01 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(143, 2016142, '2016-12-04 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(144, 2016143, '2016-12-06 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(145, 2016144, '2016-12-08 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(146, 2016145, '2016-12-11 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(147, 2016146, '2016-12-13 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(148, 2016147, '2016-12-15 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(149, 2016148, '2016-12-18 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(150, 2016149, '2016-12-20 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(151, 2016150, '2016-12-22 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(152, 2016151, '2016-12-25 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(153, 2016152, '2016-12-27 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48'),
(154, 2016153, '2016-12-29 11:00:00', NULL, '2016-07-11 14:07:48', '2016-07-11 14:07:48');

-- --------------------------------------------------------

CREATE TABLE IF NOT EXISTS `wx_pay_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `appid` varchar(32) NOT NULL,
  `bank_type` varchar(16) NOT NULL,
  `cash_fee` int NOT NULL,
  `fee_type` varchar(16) NOT NULL,
  `is_subscribe` varchar(1) NOT NULL,
  `mch_id` varchar(32) NOT NULL,
  `nonce_str` varchar(32) NOT NULL,
  `openid` varchar(128) NOT NULL,
  `out_trade_no` varchar(32) NOT NULL,
  `result_code` varchar(16) NOT NULL,
  `return_code` varchar(16) NOT NULL,
  `sign` varchar(32) NOT NULL,
  `time_end` varchar(14) NOT NULL,
  `total_fee` int NOT NULL,
  `trade_type` varchar(16) NOT NULL,
  `transaction_id` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;


alter table `user` add password varchar(50) DEFAULT '' after openid;
create unique index idx_user_phone on user(phone);

-- Table user_access_token
CREATE TABLE IF NOT EXISTS access_token (
    id bigint    NOT NULL  AUTO_INCREMENT,
    user_id bigint    NOT NULL DEFAULT 0 ,
    access_token varchar(50)    NOT NULL DEFAULT '' ,
    client_type SMALLINT    NOT NULL DEFAULT 0 ,
    expire_time timestamp    NULL ,
    create_time timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    update_time timestamp    NOT NULL ,
    is_delete SMALLINT    NOT NULL DEFAULT 0 ,
    CONSTRAINT user_access_token_pk PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
-- --------------------------------------------------------

--
-- 表的结构 `lottery_redpack`
--

CREATE TABLE IF NOT EXISTS `lottery_redpack` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `lottery_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `rate` int NULL,
  `acquire_time` timestamp NULL,
  `prize_bonus` int(11) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

alter table `lottery_period` add prize_open_time timestamp NULL after ticket_print_time;

alter table `lottery` add redpack_count int NULL after owner;
alter table `lottery` add snatched_num int NULL after redpack_count;

alter table `lottery` add `pay_img_url` varchar(500) NULL;

alter table `lottery_period` add ticket_img_url varchar(500) NULL after ticket_print_time;

alter table `lottery` add `period_num` int NULL after business_type;

alter table `lottery` modify `business_type` varchar(20) NULL;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
