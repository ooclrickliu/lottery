package cn.wisdom.lottery.dao.constant;

public enum LotteryType {

	SSQ("双色球");
	
	private String typeName;
	
	private LotteryType(String name)
	{
		this.typeName = name;
	}

	public String getTypeName() {
		return typeName;
	}
}
