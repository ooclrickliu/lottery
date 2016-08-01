package cn.wisdom.lottery.dao.vo;

import cn.wisdom.lottery.dao.annotation.Column;

public class GlobalVariable extends BaseEntity {

	@Column("key")
	private String key;

	@Column("value")
	private String value;
}
