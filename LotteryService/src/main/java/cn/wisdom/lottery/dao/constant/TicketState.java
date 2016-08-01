package cn.wisdom.lottery.dao.constant;

public enum TicketState {

	//未支付
	UnPaid("未支付"), 
	
	//已支付，待分配出票
	Paid("已支付"), 
	
	//已分配，待出票
	Distributed("已分配"), 
	
	//已出票
	Printed("已出票"), 
	
	//已兑奖
	Prized("已出奖");
	
	private String name;
	
	private TicketState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
