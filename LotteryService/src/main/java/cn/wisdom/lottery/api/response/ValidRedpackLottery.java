package cn.wisdom.lottery.api.response;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cn.wisdom.lottery.common.utils.DataConvertUtils;
import cn.wisdom.lottery.dao.constant.LotteryType;
import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.dao.vo.LotteryNumber;
import cn.wisdom.lottery.service.remote.response.LotteryOpenData;

public class ValidRedpackLottery {

	private long id;
	
	private LotteryType lotteryType;
	
	private int period;
	
	private Timestamp openTime;
	
	private List<String> numbers;
	
	public ValidRedpackLottery(Lottery lottery, LotteryOpenData periodInfo)
	{
		this.id = lottery.getId();
		this.lotteryType = lottery.getLotteryType();
		this.period = DataConvertUtils.toInt(periodInfo.getExpect());
		this.openTime = new Timestamp(DataConvertUtils.toLong(periodInfo.getOpentimestamp()));
		this.numbers = new ArrayList<String>();
		for (LotteryNumber lotteryNumber : lottery.getNumbers()) {
			this.numbers.add(lotteryNumber.getNumber());
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LotteryType getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(LotteryType lotteryType) {
		this.lotteryType = lotteryType;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public Timestamp getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Timestamp openTime) {
		this.openTime = openTime;
	}

	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}

	
}
