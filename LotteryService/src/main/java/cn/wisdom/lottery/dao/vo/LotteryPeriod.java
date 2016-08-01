package cn.wisdom.lottery.dao.vo;

import cn.wisdom.lottery.dao.annotation.Column;

public class LotteryPeriod extends BaseEntity {
	
	@Column("lottery_id")
	private long lotteryId;

	@Column("period")
	private int period;

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
}
