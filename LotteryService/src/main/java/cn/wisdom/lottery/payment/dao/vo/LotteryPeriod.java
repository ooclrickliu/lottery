package cn.wisdom.lottery.payment.dao.vo;

public class LotteryPeriod extends BaseEntity {
	
	private long lotteryId;
	
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
