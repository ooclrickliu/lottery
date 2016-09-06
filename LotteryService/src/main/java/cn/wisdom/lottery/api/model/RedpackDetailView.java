package cn.wisdom.lottery.api.model;

import java.util.ArrayList;
import java.util.List;

import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.dao.vo.LotteryPeriod;

public class RedpackDetailView {

	private String senderName;
	
	private String headImgUrl;
	
	private String wish;
	
	private int redpackNum;
	
	private float totalFee;
	
	private int currentUserRate;
	
	private List<LotteryNumber> numbers = new ArrayList<LotteryNumber>();
	
	private LotteryPeriod period;
	
	private List<RedpackItemView> redpackItems = new ArrayList<RedpackItemView>();

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getHeadImgUrl() {
		return headImgUrl;
	}

	public void setHeadImgUrl(String headImgUrl) {
		this.headImgUrl = headImgUrl;
	}

	public String getWish() {
		return wish;
	}

	public void setWish(String wish) {
		this.wish = wish;
	}

	public int getRedpackNum() {
		return redpackNum;
	}

	public void setRedpackNum(int redpackNum) {
		this.redpackNum = redpackNum;
	}

	public float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(float totalFee) {
		this.totalFee = totalFee;
	}

	public int getCurrentUserRate() {
		return currentUserRate;
	}

	public void setCurrentUserRate(int currentUserRate) {
		this.currentUserRate = currentUserRate;
	}

	public List<RedpackItemView> getRedpackItems() {
		return redpackItems;
	}

	public void setRedpackItems(List<RedpackItemView> redpackItems) {
		this.redpackItems = redpackItems;
	}

	public List<LotteryNumber> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<LotteryNumber> numbers) {
		this.numbers = numbers;
	}

	public LotteryPeriod getPeriod() {
		return period;
	}

	public void setPeriod(LotteryPeriod period) {
		this.period = period;
	}
}
