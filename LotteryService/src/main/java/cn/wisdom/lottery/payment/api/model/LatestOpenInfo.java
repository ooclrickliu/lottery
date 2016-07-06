package cn.wisdom.lottery.payment.api.model;

import java.sql.Timestamp;

import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

public class LatestOpenInfo {

	private String period;
	
	private Timestamp openTime;
	
	private String number;
	
	public LatestOpenInfo(LotteryOpenData lotteryOpenData) {
		 this.period = lotteryOpenData.getExpect();
		 this.openTime = DateTimeUtils.toTimestamp(DateTimeUtils.parseDate(
					lotteryOpenData.getOpentime(),
					DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
		 this.number = lotteryOpenData.getOpencode();
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}
