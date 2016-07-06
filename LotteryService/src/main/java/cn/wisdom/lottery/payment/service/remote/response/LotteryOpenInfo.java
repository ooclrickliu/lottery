package cn.wisdom.lottery.payment.service.remote.response;

import java.util.List;

public class LotteryOpenInfo {

	private int rows;
	
	private String code;
	
	private List<LotteryOpenData> data;

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<LotteryOpenData> getData() {
		return data;
	}

	public void setData(List<LotteryOpenData> data) {
		this.data = data;
	}
}
