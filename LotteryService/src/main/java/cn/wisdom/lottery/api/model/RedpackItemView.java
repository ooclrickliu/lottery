package cn.wisdom.lottery.api.model;

import java.sql.Timestamp;

public class RedpackItemView {

	private String userName;
	
	private String headImgUrl;
	
	private int rate;
	
    private float bonus;
	
	private Timestamp acquireTime;
	
	private boolean best;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
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

	public boolean isBest() {
		return best;
	}

	public void setBest(boolean best) {
		this.best = best;
	}

	public float getBonus() {
		return bonus;
	}

	public void setBonus(float bonus) {
		this.bonus = bonus;
	}
}
