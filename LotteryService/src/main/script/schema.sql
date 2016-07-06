
CREATE DATABASE IF NOT EXISTS `ovdoorbellpaydb` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE `ovdoorbellpaydb`;

GRANT ALL PRIVILEGES ON ovdoorbellpaydb.* to 'ovdoorbellpay'@'%' IDENTIFIED BY 'ovdoorbellpay';
GRANT ALL PRIVILEGES ON ovdoorbellpaydb.* to 'ovdoorbellpay'@'localhost' IDENTIFIED BY 'ovdoorbellpay';

USE ovdoorbellpaydb;

CREATE TABLE IF NOT EXISTS admin(
	id           INT PRIMARY KEY AUTO_INCREMENT,
	admin_name   VARCHAR(20) NOT NULL,
	admin_pwd    VARCHAR(50) NOT NULL,
	create_time  TIMESTAMP NOT NULL,
	update_time  TIMESTAMP NOT NULL,
	update_by    INT NOT NULL
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS permission(
	id           INT PRIMARY KEY AUTO_INCREMENT,
	perm_code    VARCHAR(20) NOT NULL,
	perm_name    VARCHAR(20) NOT NULL,
	perm_desc    VARCHAR(100) NOT NULL,
	create_time  TIMESTAMP NOT NULL,
	is_sa		 tinyint(1)  NOT NULL
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS admin_permission(
	id           INT PRIMARY KEY AUTO_INCREMENT,
	ap_admin_id  INT NOT NULL,
	ap_perm_id   INT NOT NULL,
	create_time  TIMESTAMP NOT NULL,
	update_time  TIMESTAMP NOT NULL,
	update_by    INT NOT NULL
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS admin_token(
	id               INT PRIMARY KEY AUTO_INCREMENT,
	at_admin_token   VARCHAR(50) NOT NULL,
	at_admin_id      INT NOT NULL,
	at_expire_time   TIMESTAMP NOT NULL, 
	create_time      TIMESTAMP NOT NULL,
	update_time      TIMESTAMP NOT NULL
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS member_profile ( 
    id              bigint       PRIMARY KEY AUTO_INCREMENT,
    device_id       varchar(50)  NOT NULL DEFAULT '' ,
    mp_start_time   timestamp    NULL,
    mp_expire_time  timestamp    NULL,
    mp_end_time     timestamp    NULL ,
    mp_month        INT NOT NULL DEFAULT 0,
    mp_status       varchar(20)  NOT NULL DEFAULT '' ,
    mp_purchase_way varchar(20)  NOT NULL DEFAULT '' ,
    order_no        varchar(50)  NOT NULL DEFAULT '' ,
	create_by       varchar(50)  NOT NULL DEFAULT '' ,
	create_time     timestamp    NOT NULL,
    update_time     timestamp    NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index idx_device_state_time on member_profile(device_id, mp_status, mp_start_time, mp_expire_time);
create index idx_order_no on member_profile(order_no);

CREATE TABLE IF NOT EXISTS member_resource ( 
    id          bigint      PRIMARY KEY  AUTO_INCREMENT,
    mp_id       bigint      NOT NULL DEFAULT 0 ,
    res_code    varchar(20) NOT NULL DEFAULT ''
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index idx_res_code on member_resource(res_code);
create index idx_mp_id on member_resource(mp_id);

CREATE TABLE IF NOT EXISTS member_action ( 
    id           bigint        NOT NULL  AUTO_INCREMENT,
    u_id         varchar(20)   NOT NULL DEFAULT '' ,
    device_id    varchar(20)   NOT NULL DEFAULT '' ,
    mp_id        bigint        NOT NULL DEFAULT 0 ,
    action_type  varchar(20)   NOT NULL DEFAULT 0,
    total_fee    FLOAT(8,2)    NOT NULL DEFAULT 0,
    reference_id varchar(100)  NOT NULL DEFAULT '' ,
    remark       varchar(500)  NOT NULL DEFAULT '' ,
    create_time  timestamp     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CONSTRAINT member_action PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index idx_ma_device_id on member_action(device_id);
create index idx_ma_mp_id on member_action(mp_id);

CREATE TABLE IF NOT EXISTS resource (
    id bigint    NOT NULL  AUTO_INCREMENT,
    res_code varchar(20)    NOT NULL DEFAULT '' ,
    res_name varchar(20)    NOT NULL DEFAULT 0,
    res_type varchar(20)    NOT NULL DEFAULT '' ,
    res_size BIGINT    NOT NULL DEFAULT 0 ,
    res_price FLOAT(8,2)    NOT NULL DEFAULT 0 ,
    res_unit  varchar(10)    NOT NULL,
    CONSTRAINT resource PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

create index idx_res_code on resource(res_code);

CREATE TABLE IF NOT EXISTS notify (
    id           bigint      NOT NULL AUTO_INCREMENT,
    device_id    varchar(50) NOT NULL DEFAULT '' ,
    space        bigint      NOT NULL,
    flow         bigint      NOT NULL,
    expire_time  varchar(30) NULL,
    CONSTRAINT notify PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS permission_url(
	id           INT PRIMARY KEY AUTO_INCREMENT,
	url_name	 VARCHAR(50) NOT NULL DEFAULT '' ,
	perm_code    VARCHAR(20) NOT NULL DEFAULT '' ,
	url_type	 VARCHAR(10) NOT NULL DEFAULT '' ,
	create_time  TIMESTAMP NOT NULL
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS app_properties(
	id           INT PRIMARY KEY AUTO_INCREMENT,
	prop_name	 VARCHAR(100) NOT NULL DEFAULT '' ,
	prop_value   VARCHAR(100) NOT NULL DEFAULT '' ,
	create_time  TIMESTAMP NOT NULL,
	`desc`	 	 VARCHAR(100) NOT NULL DEFAULT '' 
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS `commodity_sales_statistics_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `res_code` varchar(50) NOT NULL,
  `amount` int(11) DEFAULT '0',
  `count` int(11) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `commodity_sales_statistics_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `res_code` varchar(50) NOT NULL,
  `amount` int(11) DEFAULT '0',
  `count` int(11) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `commodity_sales_statistics_quarter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `res_code` varchar(50) NOT NULL,
  `amount` int(11) DEFAULT '0',
  `count` int(11) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `commodity_sales_statistics_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `res_code` varchar(50) NOT NULL,
  `amount` int(11) DEFAULT '0',
  `count` int(11) DEFAULT '0',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customer_consumption_statistics_day` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `device_id` varchar(20) NOT NULL,
  `spend_fees` float(10,2) DEFAULT '0.00',
  `refund_fees` float(10,2) DEFAULT '0.00',
  `retained_fees` float(10,2) DEFAULT '0.00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customer_consumption_statistics_month` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `device_id` varchar(20) NOT NULL,
  `spend_fees` float(10,2) DEFAULT '0.00',
  `refund_fees` float(10,2) DEFAULT '0.00',
  `retained_fees` float(10,2) DEFAULT '0.00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customer_consumption_statistics_quarter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `device_id` varchar(20) NOT NULL,
  `spend_fees` float(10,2) DEFAULT '0.00',
  `refund_fees` float(10,2) DEFAULT '0.00',
  `retained_fees` float(10,2) DEFAULT '0.00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customer_consumption_statistics_year` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `device_id` varchar(20) NOT NULL,
  `spend_fees` float(10,2) DEFAULT '0.00',
  `refund_fees` float(10,2) DEFAULT '0.00',
  `retained_fees` float(10,2) DEFAULT '0.00',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `in_out_statistics_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `incoming` float(10,2) NOT NULL,
  `outgoing` float(10,2) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `in_out_statistics_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `incoming` float(10,2) NOT NULL,
  `outgoing` float(10,2) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `in_out_statistics_quarter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `incoming` float(10,2) NOT NULL,
  `outgoing` float(10,2) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `in_out_statistics_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `incoming` float(10,2) NOT NULL,
  `outgoing` float(10,2) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `order_statistics_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date_time` datetime NOT NULL,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `day` int(11) NOT NULL,
  `purchase_amount` int(11) NOT NULL,
  `refund_amount` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `order_statistics_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `month` int(11) NOT NULL,
  `purchase_amount` int(11) NOT NULL,
  `refund_amount` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `order_statistics_quarter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `quarter` int(11) NOT NULL,
  `purchase_amount` int(11) NOT NULL,
  `refund_amount` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `order_statistics_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `year` int(11) NOT NULL,
  `purchase_amount` int(11) NOT NULL,
  `refund_amount` int(11) NOT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS notify_clear(
	id           BIGINT PRIMARY KEY AUTO_INCREMENT,
	device_id	 VARCHAR(100) NOT NULL DEFAULT ''
)ENGINE=INNODB DEFAULT CHARSET=UTF8;
create unique index idx_notify_clear_device on notify_clear(device_id);




