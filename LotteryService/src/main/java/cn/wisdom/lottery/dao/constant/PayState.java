package cn.wisdom.lottery.dao.constant;

public enum PayState {

	UnPaid("未支付"), 
	
	Paid("已支付"); 
	
	private String name;
	
	private PayState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
