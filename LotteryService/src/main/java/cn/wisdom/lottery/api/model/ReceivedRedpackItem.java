package cn.wisdom.lottery.api.model;

import java.sql.Timestamp;

import cn.wisdom.lottery.dao.constant.PrizeState;

public class ReceivedRedpackItem {

	private String senderName;
	
	private Timestamp acquireTime;
	
	private int rate;
	
    private float bonus;
    
    private PrizeState prizeState;

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public Timestamp getAcquireTime() {
		return acquireTime;
	}

	public void setAcquireTime(Timestamp acquireTime) {
		this.acquireTime = acquireTime;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}

	public PrizeState getPrizeState() {
		return prizeState;
	}

	public void setPrizeState(PrizeState prizeState) {
		this.prizeState = prizeState;
	}
}
