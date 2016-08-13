package cn.wisdom.lottery.dao.vo;

import java.sql.Timestamp;

import cn.wisdom.lottery.dao.annotation.Column;

public class LotteryRedpack extends BaseEntity {
	
	@Column("lottery_id")
	private long lotteryId;

	@Column("user_id")
	private long user_id;

    @Column("rate")
    private int rate;

    @Column("acquire_time")
    private Timestamp acquire_time;

    @Column("prize_bonus")
    private int prize_bonus;

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Timestamp getAcquire_time() {
		return acquire_time;
	}

	public void setAcquire_time(Timestamp acquire_time) {
		this.acquire_time = acquire_time;
	}

	public int getPrize_bonus() {
		return prize_bonus;
	}

	public void setPrize_bonus(int prize_bonus) {
		this.prize_bonus = prize_bonus;
	}

}
