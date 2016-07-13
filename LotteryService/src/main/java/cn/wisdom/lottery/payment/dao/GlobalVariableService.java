package cn.wisdom.lottery.payment.dao;

public interface GlobalVariableService {

	String getValue(String key);
	
	void setValue(String key, String value);
	
	void delete(String key);
}
