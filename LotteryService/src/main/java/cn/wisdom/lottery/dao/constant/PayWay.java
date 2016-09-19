package cn.wisdom.lottery.dao.constant;

public enum PayWay {

	WX_QR_PAY("扫码支付"),
	
	WX_PAY("微信支付"),
	
	BALANCE_PAY("余额支付");
	
	private String name;
	
	private PayWay(String name)
	{
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
