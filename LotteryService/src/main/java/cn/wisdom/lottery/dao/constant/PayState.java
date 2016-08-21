package cn.wisdom.lottery.dao.constant;

public enum PayState {

	UnPaid("未支付"), 
	
	PaidApproving("待确认支付"), 
	
	Paid("已支付"),
	
	PaidFail("支付失败"); 
	
	private String name;
	
	private PayState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
