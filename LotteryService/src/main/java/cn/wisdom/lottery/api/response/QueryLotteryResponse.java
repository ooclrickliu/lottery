package cn.wisdom.lottery.api.response;

import java.util.List;
import java.util.Map;

import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

public class QueryLotteryResponse {

	private List<Lottery> lotteries;
	
	private LotteryOpenData openInfo;
	
	private float totalFee;
	
	private float totalPrize;
	
	private Map<String, Integer> prizeInfo;

	public List<Lottery> getLotteries() {
		return lotteries;
	}

	public void setLotteries(List<Lottery> lotteries) {
		this.lotteries = lotteries;
	}

	public LotteryOpenData getOpenInfo() {
		return openInfo;
	}

	public void setOpenInfo(LotteryOpenData openInfo) {
		this.openInfo = openInfo;
	}

	public float getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(float totalFee) {
		this.totalFee = totalFee;
	}

	public float getTotalPrize() {
		return totalPrize;
	}

	public void setTotalPrize(float totalPrize) {
		this.totalPrize = totalPrize;
	}

	public Map<String, Integer> getPrizeInfo() {
		return prizeInfo;
	}

	public void setPrizeInfo(Map<String, Integer> prizeInfo) {
		this.prizeInfo = prizeInfo;
	}
}
