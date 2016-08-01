package cn.wisdom.lottery.api.request;

import java.util.List;

public class LotteryOrderRequest {

	private List<String> numbers;
	
	private int periods;
	
	private int times;

	public List<String> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<String> numbers) {
		this.numbers = numbers;
	}

	public int getPeriods() {
		return periods;
	}

	public void setPeriods(int periods) {
		this.periods = periods;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}
}
