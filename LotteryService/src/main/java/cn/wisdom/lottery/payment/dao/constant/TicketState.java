package cn.wisdom.lottery.payment.dao.constant;

public enum TicketState {

	//未支付
	NotPrint, 
	
	//已支付，待分配出票
	CanPrint, 
	
	//已分配，待出票
	Distributed,
	
	//已出票
	Printed, 
	
	//已兑奖
	Prized
}
