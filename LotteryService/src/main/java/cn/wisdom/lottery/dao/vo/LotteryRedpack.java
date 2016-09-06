package cn.wisdom.lottery.dao.vo;

import java.sql.Timestamp;

import cn.wisdom.lottery.dao.annotation.Column;

public class LotteryRedpack extends BaseEntity {
	
	@Column("lottery_id")
	private long lotteryId;

	@Column("user_id")
	private long userId;

    @Column("rate")
    private int rate;

    @Column("acquire_time")
    private Timestamp acquireTime;

    @Column("prize_bonus")
    private float prizeBonus;
    
    private User user;

	public long getLotteryId() {
		return lotteryId;
	}

	public void setLotteryId(long lotteryId) {
		this.lotteryId = lotteryId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public Timestamp getAcquireTime() {
		return acquireTime;
	}

	public void setAcquireTime(Timestamp acquireTime) {
		this.acquireTime = acquireTime;
	}

	public float getPrizeBonus() {
		return prizeBonus;
	}

	public void setPrizeBonus(float prizeBonus) {
		this.prizeBonus = prizeBonus;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
