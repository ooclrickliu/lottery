package cn.wisdom.lottery.api.model;

import cn.wisdom.lottery.common.model.JsonDocument;

public class LotteryJsonDocument extends JsonDocument {

	private static final String SERVICE_LOTTERY = "Lottery";
	
	public static final LotteryJsonDocument SUCCESS = new LotteryJsonDocument();

	public LotteryJsonDocument() {
		super();
		
		this.setServiceCode(SERVICE_LOTTERY);
		this.setStateCode(STATE_SUCCESS);
	}

	public LotteryJsonDocument(Object data) {
		super(SERVICE_LOTTERY, data);
	}

	public LotteryJsonDocument(String errCode) {
		super(SERVICE_LOTTERY, errCode);
	}

}
