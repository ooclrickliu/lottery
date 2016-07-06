CREATE DATABASE IF NOT EXISTS `ovpaydb` DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
USE `ovpaydb`;

GRANT ALL PRIVILEGES ON ovpaydb.* TO 'ovpay'@'%' IDENTIFIED BY 'ovpay';
GRANT ALL PRIVILEGES ON ovpaydb.* TO 'ovpay'@'localhost' IDENTIFIED BY 'ovpay';

CREATE TABLE IF NOT EXISTS `order`(
	id BIGINT    NOT NULL AUTO_INCREMENT ,
	order_no VARCHAR(50)    NOT NULL DEFAULT '' ,
	order_state VARCHAR(20)    NOT NULL DEFAULT '' ,
	order_total_fee FLOAT    NOT NULL DEFAULT 0 ,
	refunded_fee FLOAT    NOT NULL DEFAULT 0 ,
	user_id VARCHAR(20)    NOT NULL DEFAULT '' ,
	create_by VARCHAR(20)    NOT NULL DEFAULT '' ,
	create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	update_time TIMESTAMP    NULL ,	
	order_remark VARCHAR(200)     NULL ,
	is_delete SMALLINT    NOT NULL DEFAULT 0 ,
	extra_1 INT             NULL ,
	extra_2 VARCHAR(100)    NULL ,
	extra_3 TIMESTAMP       NULL ,
	CONSTRAINT order_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create index idx_order_no on `order`(order_no);
create index idx_order_state on `order`(order_state);
create index idx_create_by on `order`(create_by);
create index idx_user_create_state on `order`(user_id, create_by, order_state);

CREATE TABLE IF NOT EXISTS order_item(
	id BIGINT NOT NULL AUTO_INCREMENT,
	order_id bigint NOT NULL DEFAULT 0,
	item_no VARCHAR(50) NOT NULL DEFAULT '',
	item_name VARCHAR(100) NOT NULL DEFAULT '',
	item_price FLOAT NOT NULL DEFAULT 0,
	item_num SMALLINT NOT NULL DEFAULT 0,
	CONSTRAINT order_item_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create index idx_order_id on order_item(order_id);

CREATE TABLE IF NOT EXISTS refund_request(
	id BIGINT NOT NULL AUTO_INCREMENT,
	order_no VARCHAR(50) NOT NULL DEFAULT '',
	create_by VARCHAR(20)    NOT NULL DEFAULT '' ,
	refund_reason VARCHAR(200) NULL,
	refund_fee FLOAT NOT NULL DEFAULT 0,
	refund_desc VARCHAR(200) NULL,
	auditor_id BIGINT NOT NULL DEFAULT 0,
	feedback VARCHAR(200) NULL,
	create_time TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ,
	refund_state VARCHAR(20) NOT NULL DEFAULT '',
	batch_no VARCHAR(50) NOT NULL DEFAULT '',
	refund_log VARCHAR(100) NULL,
	CONSTRAINT refund_request_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

create index idx_order_no_state on refund_request(order_no, refund_state);

CREATE TABLE IF NOT EXISTS discount_strategy(
	id BIGINT NOT NULL AUTO_INCREMENT,
	ds_name VARCHAR(50) NOT NULL DEFAULT '',
	ds_desc VARCHAR(50) NULL,
	ds_class VARCHAR(50) NOT NULL DEFAULT '',
	CONSTRAINT discount_strategy_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS discount_log(
	id BIGINT NOT NULL AUTO_INCREMENT,
	ds_id BIGINT NOT NULL DEFAULT 0,
	order_no VARCHAR(50) NOT NULL DEFAULT '',
	product_no VARCHAR(50) NULL,
	disc_result VARCHAR(100) NULL,
	CONSTRAINT discount_log_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS payment(
	id BIGINT NOT NULL AUTO_INCREMENT,
	order_no VARCHAR(50) NOT NULL DEFAULT '',
	pay_no VARCHAR(100) NOT NULL DEFAULT '',
	pay_type VARCHAR(20) NOT NULL DEFAULT '',
	pay_source VARCHAR(20) NOT NULL DEFAULT '',
	pay_fee FLOAT NOT NULL DEFAULT 0,
	pay_state VARCHAR(20) NOT NULL DEFAULT 0,
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	pay_time TIMESTAMP NULL,
	pay_log_id BIGINT NOT NULL DEFAULT 0,
	is_delete SMALLINT    NOT NULL DEFAULT 0 ,
	CONSTRAINT payment_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS alipay_payment_log(
	id BIGINT NOT NULL AUTO_INCREMENT,
	notify_time TIMESTAMP NULL,
	notify_type VARCHAR(50) NOT NULL DEFAULT '',
	notify_id VARCHAR(50) NOT NULL DEFAULT '',
	sign_type VARCHAR(20) NOT NULL DEFAULT '',
	`sign` VARCHAR(200) NOT NULL DEFAULT '',
	out_trade_no VARCHAR(50) NOT NULL DEFAULT '',
	SUBJECT VARCHAR(200) NULL,
	trade_no VARCHAR(64) NOT NULL DEFAULT '',
	trade_status VARCHAR(20) NOT NULL DEFAULT '',
	buyer_id VARCHAR(30) NOT NULL DEFAULT '',
	buyer_email VARCHAR(100) NOT NULL DEFAULT '',
	total_fee FLOAT NOT NULL DEFAULT 0,
	gmt_create TIMESTAMP NULL,
	gmt_payment TIMESTAMP NULL,
	is_total_fee_adjust VARCHAR(5) NOT NULL DEFAULT "N",
	use_coupon VARCHAR(5) NOT NULL DEFAULT "N",
	discount FLOAT NOT NULL DEFAULT 0,
	refund_status VARCHAR(50) NULL,
	gmt_refund TIMESTAMP NULL,
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT alipay_payment_log_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS app_alipay_payment_log(
	id BIGINT NOT NULL AUTO_INCREMENT,
	resultStatus VARCHAR(10) NULL,
	memo VARCHAR(100) NULL,
	partner VARCHAR(50) NULL,
	seller_id VARCHAR(50) NULL,
	out_trade_no VARCHAR(100) NULL,
	subject VARCHAR(100) NULL,
	body VARCHAR(100) NULL,
	total_fee VARCHAR(20) NULL,
	notify_url VARCHAR(100) NULL,
	service VARCHAR(100) NULL,
	payment_type VARCHAR(10) NULL,
	_input_charset VARCHAR(20) NULL,
	it_b_pay VARCHAR(10) NULL,
	success VARCHAR(10) NULL,
	sign_type VARCHAR(20) NULL,
	`sign` VARCHAR(200) NULL,
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT alipay_payment_log_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS refund(
	id BIGINT NOT NULL AUTO_INCREMENT,
	refund_request_id BIGINT NOT NULL DEFAULT 0,
	batch_no VARCHAR(50) NOT NULL DEFAULT '',
	trade_no VARCHAR(64) NOT NULL DEFAULT '',
	refund_fee FLOAT NOT NULL DEFAULT 0,
	refund_state VARCHAR(100) NOT NULL DEFAULT 0,
	refund_tax FLOAT NULL,
	refund_tax_state VARCHAR(100) NULL,
	refund_time TIMESTAMP NULL ,
	refund_log_id BIGINT  NOT NULL DEFAULT 0,
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT refund_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS alipay_refund_log(
	id BIGINT NOT NULL AUTO_INCREMENT,
	batch_no VARCHAR(50) NOT NULL DEFAULT '',
	notify_time TIMESTAMP NULL,
	notify_type VARCHAR(50) NOT NULL DEFAULT '',
	notify_id VARCHAR(50) NOT NULL DEFAULT '',
	sign_type VARCHAR(20) NOT NULL DEFAULT '',
	`sign` VARCHAR(200)  NOT NULL DEFAULT '',
	success_num VARCHAR(50) NOT NULL DEFAULT '',
	result_details VARCHAR(1000) NOT NULL DEFAULT '',
	CONSTRAINT alipay_refund_log_pk PRIMARY KEY (id)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS app_properties(
	id           INT PRIMARY KEY AUTO_INCREMENT,
	prop_name	 VARCHAR(100) NOT NULL DEFAULT '' ,
	prop_value    VARCHAR(1000) NOT NULL DEFAULT '' ,
	create_time  TIMESTAMP NOT NULL,
	`desc`	 	 VARCHAR(100) NOT NULL DEFAULT '' 
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;

CREATE TABLE IF NOT EXISTS account_check_error(
	id           BIGINT PRIMARY KEY AUTO_INCREMENT,
	order_no	 VARCHAR(50) NOT NULL DEFAULT '' ,
	detail    	VARCHAR(100) NOT NULL DEFAULT '' ,
	is_read    	BOOLEAN NOT NULL DEFAULT 0 ,
	create_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
)ENGINE=INNODB DEFAULT CHARSET=UTF8 AUTO_INCREMENT=1;