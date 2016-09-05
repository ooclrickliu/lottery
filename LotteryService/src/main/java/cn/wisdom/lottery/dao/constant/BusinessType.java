package cn.wisdom.lottery.dao.constant;

public enum BusinessType {

	// by for self
	Private, 
	
	// by for sharing to others
	// 1. Mode[bonus]: people get part of bonus of the lottery redpack
	RedPack_Bonus,
	
	// 2. Mode[Number]: people get a number of the lottery redpack
	RedPack_Number;
	
	public static boolean isRedpack(BusinessType businessType)
	{
		return businessType == RedPack_Bonus || businessType == RedPack_Number;
	}
}
