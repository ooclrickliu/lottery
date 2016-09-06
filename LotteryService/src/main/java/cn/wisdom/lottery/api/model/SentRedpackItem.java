package cn.wisdom.lottery.api.model;

import java.sql.Timestamp;

import cn.wisdom.lottery.dao.constant.PrizeState;

public class SentRedpackItem {

	private String type;
	
	private Timestamp sendTime;
	
	private int fee;
	
	private int count;
	
	private int qCount;
    
    private PrizeState prizeState;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Timestamp getSendTime() {
		return sendTime;
	}

	public void setSendTime(Timestamp sendTime) {
		this.sendTime = sendTime;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getqCount() {
		return qCount;
	}

	public void setqCount(int qCount) {
		this.qCount = qCount;
	}

	public PrizeState getPrizeState() {
		return prizeState;
	}

	public void setPrizeState(PrizeState prizeState) {
		this.prizeState = prizeState;
	}
}
