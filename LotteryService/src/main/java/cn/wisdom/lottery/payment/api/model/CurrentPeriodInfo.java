package cn.wisdom.lottery.payment.api.model;


import java.sql.Timestamp;

import cn.wisdom.lottery.payment.common.utils.DateTimeUtils;
import cn.wisdom.lottery.payment.service.remote.response.LotteryOpenData;

public class CurrentPeriodInfo {

	private String period;

	private Timestamp openTime;

	public CurrentPeriodInfo(LotteryOpenData lotteryOpenData) {
		this.period = lotteryOpenData.getExpect();
		this.openTime = DateTimeUtils.toTimestamp(DateTimeUtils.parseDate(
				lotteryOpenData.getOpentime(),
				DateTimeUtils.PATTERN_SQL_DATETIME_FULL));
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
}
