package cn.wisdom.lottery.service;

import cn.wisdom.lottery.dao.vo.Lottery;
import cn.wisdom.lottery.service.exception.ServiceException;

public interface LotteryDistributeService {

	void distribute(Lottery lottery) throws ServiceException;
	
	void distribute(long lotteryId, long merchantId) throws ServiceException;
}
