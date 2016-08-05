package cn.wisdom.lottery.service;

import cn.wisdom.lottery.dao.vo.WxPayLog;
import cn.wisdom.lottery.service.exception.ServiceException;

public interface WxPayLogService {
	
	public void saveWxPayLog(WxPayLog wxPayLog) throws ServiceException;
	
	public WxPayLog getWxPayLogByTradeNo(String outTradeNo) throws ServiceException;
}
