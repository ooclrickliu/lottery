package cn.wisdom.lottery.dao;

import cn.wisdom.lottery.dao.vo.WxPayLog;

public interface WxPayLogDao 
{
	public long saveWxPayLog(WxPayLog wxPayLog);
	
	public WxPayLog getWxPayLogByTradeNo(String outTradeNo);
}
