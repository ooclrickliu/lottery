package cn.wisdom.lottery.payment.dao.vo;

import cn.wisdom.lottery.payment.dao.annotation.Column;


public class LotteryNumber extends BaseEntity {

	@Column("lottery_id")
	private long lotteryId;

	@Column("number")
	private String number;
	
	public LotteryNumber()
	{
		
	}
	
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
