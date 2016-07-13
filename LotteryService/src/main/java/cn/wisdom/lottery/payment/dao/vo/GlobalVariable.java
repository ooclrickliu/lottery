package cn.wisdom.lottery.payment.dao.vo;

import cn.wisdom.lottery.payment.dao.annotation.Column;

public class GlobalVariable extends BaseEntity {

	@Column("key")
	private String key;

	@Column("value")
	private String value;
}
