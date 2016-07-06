package cn.wisdom.lottery.payment.dao.vo;


public class LotteryNumber extends BaseEntity {

	private long lotteryId;
	
	private String number;
	
	public LotteryNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

}
