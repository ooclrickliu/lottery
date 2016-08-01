package cn.wisdom.lottery.dao.vo;

import java.sql.Timestamp;

import cn.wisdom.lottery.dao.annotation.Column;

public abstract class PrizeLottery extends BaseEntity {

	@Column("period")
	protected int period;

	@Column("open_time")
	protected Timestamp openTime;
	
	public abstract String getNumber();

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

}
