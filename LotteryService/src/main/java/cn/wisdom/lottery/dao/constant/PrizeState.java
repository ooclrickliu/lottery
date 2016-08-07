package cn.wisdom.lottery.dao.constant;

public enum PrizeState {

	NotOpen("未开奖"),
	
	Win("中奖"),
	
	Lose("未中奖"),
	;
	
	private String name;
	
	private PrizeState(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
}
