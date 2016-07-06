package cn.wisdom.lottery.payment.dao.vo;

import java.sql.Timestamp;

public class PrizeLottery extends BaseEntity {

	protected int period;
	
	protected Timestamp openTime;
	
	public String getNumber() {
		
		return "";
	}

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
